package joomSignSend;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.allroot.db.ConnDB;
import com.allroot.entity.JoomUser;
import com.allroot.tool.Log;
import com.allroot.tool.MyHttpRequest;
import com.allroot.tool.Tools;

public class JoomSignSend {

	public void signSendGoods(JoomUser user) {
	
		String access_token=user.getAccessToken();
		String url="https://www.merchant.wish.com/api/v2/order/fulfill-one";
		String suffix = user.getAliasName();//店铺简称
		String UID = user.getUID();
		String sql = " select * from S_SyncSet where  Platform = 'joom' and uid = '" 
				+ Tools.SetDBValue(UID) + "'";
		String logs="";
		String LPostData="";
		
		try {
			int signminute = 20;	//标记间隔时间
			int Shortage = 0;	//等待处理订单-缺货订单
			int delivery = 0;	//等待处理订单-自发货订单
			int overseas = 0;	//等待处理订单-海外仓订单
			int WarehouseBag = 0;	//--等待仓库打包
			int LogisticsBag = 0; 	//--等待物流收包
			int Shipped = 0;	//已发货及已归档
			int usedsign = 0; //是否打开了标记发货
			ArrayList<HashMap<String, Object>> list = null;
			System.out.println(sql);
			list = ConnDB.DBSelect(sql, true);
			HashMap<String, Object> dataMap = null ;
			if (list != null && list.size() > 0) {
				for (int i=0; i < list.size(); i ++) {
					dataMap = list.get(i);
					
					
					// 是否打开了标记发货
					if ("usedsign".equals(Tools.toString(dataMap.get("Code"))) && 
							Tools.toString(user.getNID()).equals(Tools.toString(dataMap.get("ShopID"))) ) {
						usedsign = Tools.toInt(dataMap.get("Value")); 
					}
					
					// 自动标记发货时间间隔
					if ("signminute".equals(Tools.toString(dataMap.get("Code")))) {
						signminute = Tools.toInt(dataMap.get("Value")); 
					}
					
					//等待处理订单-缺货订单
					if ("Shortage".equals(Tools.toString(dataMap.get("Code")))) {
						Shortage = Tools.toInt(dataMap.get("Value")); 
					}
					
					//等待处理订单-自发货订单
					if ("delivery".equals(Tools.toString(dataMap.get("Code")))) {
						delivery = Tools.toInt(dataMap.get("Value")); 
					}
					
					//等待处理订单-海外仓订单
					if ("overseas".equals(Tools.toString(dataMap.get("Code")))) {
						overseas = Tools.toInt(dataMap.get("Value")); 
					}
					
					//--等待仓库打包
					if ("WarehouseBag".equals(Tools.toString(dataMap.get("Code")))) {
						WarehouseBag = Tools.toInt(dataMap.get("Value")); 
					}
					
					//--等待物流收包
					if ("LogisticsBag".equals(Tools.toString(dataMap.get("Code")))) {
						LogisticsBag = Tools.toInt(dataMap.get("Value")); 
					}
					
					//已发货及已归档
					if ("Shipped".equals(Tools.toString(dataMap.get("Code")))) {
						Shipped = Tools.toInt(dataMap.get("Value")); 
					}
				}
			}
			
			if (usedsign == 1) {
				String TrackNo = "";//跟踪号 tracking_number 
				String carrierEN = "";//承运商英文  tracking_provider
				String ACK = "";//order_id
				String NID = "";
				sql="exec P_XS_GetUnShipedOrdersJoom '"+Tools.SetDBValue(Shortage)+"','"+Tools.SetDBValue(delivery)+"','"+Tools.SetDBValue(WarehouseBag)+"','"+Tools.SetDBValue(LogisticsBag)+"','"+Tools.SetDBValue(Shipped)+"'";
				ArrayList<HashMap<String, Object>> pjList = null;
				HashMap<String, String> trackNoMap = new HashMap<String, String>();
				System.out.println(sql);
				pjList = ConnDB.DBSelect(sql, true);
				HashMap<String, Object> pjMap = null;
				if (pjList != null && pjList.size() > 0) {
					for(int i=0;i<pjList.size();i++){
						pjMap = pjList.get(i);
						// 分包上传的跟踪号中间有分号，要处理下
						carrierEN = Tools.toString(pjMap.get("carrierEN")).trim() ;
						TrackNo = Tools.toString(pjMap.get("TrackNo")).trim() ;
						ACK = Tools.toString(pjMap.get("ACK"));
						NID = Tools.toString(pjMap.get("NID"));
						if (TrackNo.indexOf(";") > 0) {
							TrackNo = TrackNo.substring(0,TrackNo.indexOf(";"));
						}
						if(TrackNo.isEmpty()){
							logs = "下载用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败：跟踪号为空";
							Log.printLog(logs);
							Log.errLog(logs);
							continue;
						}
						if(carrierEN.isEmpty()){
							logs = "下载用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败：承运商英文为空";
							Log.printLog(logs);
							Log.errLog(logs);
							continue;
						}
						if(ACK.isEmpty()){
							logs = "下载用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败：wish order_id为空";
							Log.printLog(logs);
							Log.errLog(logs);
							continue;
						}
						
					  LPostData="access_token="+access_token
								+"&id="+ACK
								+"&tracking_provider="+carrierEN
								+"&tracking_number="+TrackNo;
						
					  fulfill_one(url,LPostData,user,NID);	
						
					}
				}else{
					logs = "标记发货用户："+UID+"店铺[" + suffix + "]：没有需要标记的单子";
					Log.printLog(logs);
					Log.errLog(logs);
					return;
				}
				
				
				
				
				
				
			}else{
				logs = "标记发货用户："+UID+"店铺[" + suffix + "]：未开启标记,如需标记,请到软件中设置";
				Log.printLog(logs);
				Log.errLog(logs);
				return;
			}
			
			
			
		} catch (Exception e) {
			logs = "标记发货用户："+UID+"店铺[" + suffix + "]：标记失败："+e;
			Log.printLog(logs);
			Log.errLog(logs);
			return;
		}

		
		
	}

	
	private void fulfill_one(String url, String LPostData,JoomUser user,String NID) throws Exception {
		String UID = user.getUID();
		String suffix = user.getAliasName();//店铺简称
		String logs="";
		String sql="";
		MyHttpRequest httpobj = MyHttpRequest.httpRequest(url, "POST", LPostData, "UTF-8");
		
		Log.printLog("正在获取标记结果,请等待一分钟...");
		Thread.sleep(60000);
		
		if(!httpobj.getSuccessFlag()){
			logs = "标记发货用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败：httpobj的返回值为false";
			Log.printLog(logs);
			Log.errLog(logs);
			return;
		}
		String response=httpobj.getResponse();
		if (response.indexOf("code") == -1) {
			logs = "标记发货用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败：code不存在";
			Log.printLog(logs);
			Log.errLog(logs);
			return;
		}
		JSONObject jsoob = new JSONObject(response);
		int code=jsoob.optInt("code",1);
		if(code==0){
			//成功
			if (jsoob.getJSONObject("data").optBoolean("success", false)) {
				sql="update P_trade  set SHIPPINGMETHOD = '1' " +
		                  " where nid=" + NID +
		                  " update P_tradeun  set SHIPPINGMETHOD = '1' " +
		                  " where nid=" + NID ;
				System.out.println(sql);
				ConnDB.DBExcuteSQL(sql);
				logs = "标记发货用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货成功";
				Log.printLog(logs);
				Log.errLog(logs);
			} else {
				// 失败
				logs = "标记发货用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败："+response;
				Log.printLog(logs);
				Log.errLog(logs);
				return;
			}
		}else{
			//失败
			logs = "标记发货用户："+UID+"店铺[" + suffix + "],订单编号："+NID+"标记发货失败："+response;
			Log.printLog(logs);
			Log.errLog(logs);
			return;
			
		}

	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
