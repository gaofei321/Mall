package com.allroot.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.allroot.db.ConnDB;
import com.allroot.entity.MyMallUser;
import com.allroot.tool.Log;
import com.allroot.tool.MyHttpRequest;
import com.allroot.tool.Tools;

public class MyMallAbout {
	
	private final static String jobName = "[同步myMall]";
	private final static String appKey="d5c936d9587fa466";
	private final static String appSecret="fc0f75e8eb48a81e7427685324b77333"; 
	public static String SetDBValue(Object Avalue) {
		return Avalue == null ? "" : Avalue.toString().replaceAll("'", "''");
	}
	// 获取所有监测账号
	public static ArrayList<MyMallUser> GetAllOtherMyMallUser() {
		ArrayList<MyMallUser> users = new ArrayList<MyMallUser>();
		Map<String,Object> outpd=new HashMap<String,Object>();
		String SqlStr = "select NID,AliasName,AccessToken,SyncInvertal,resource_owner,SiteSC,LocalFlag,merchant_user_id,RefreshToken,Appkey,AppSecret,UID,PlatformTransaction,SyncSuccessTime,LoginName,Password,DATEDIFF(HOUR,LastSyncTime,GETDATE()) as diffhour from S_SyncInfoMall";
		ArrayList<HashMap<String, Object>> list = null;
		try {
			list = ConnDB.DBSelect(SqlStr, true);
			
			
			for (int i = 0; i < list.size(); i++) {	
				
				String RefreshToken = Tools.toString(list.get(i).get("RefreshToken"));
				int diffhour = Tools.toInt(list.get(i).get("diffhour"));
				String Appkey=Tools.toString(list.get(i).get("Appkey"));
				String AppSecret=Tools.toString(list.get(i).get("AppSecret"));
				if(Appkey.equals("")){
					Appkey=appKey;
					AppSecret=appSecret;
				}
				// 判断最后同步时间与现在是否超过1小时
				if (diffhour >= 1) {
					// 大于1小时 重新授权
					outpd = GetMallTokenByRefreshToken(Appkey,AppSecret,RefreshToken);
					list.get(i).put("AccessToken", Tools.toString(outpd.get("access_token")));
					list.get(i).put("RefreshToken", Tools.toString(outpd.get("RefreshToken")));
				}
				MyMallUser user = new MyMallUser();
				user.setUID(Tools.toString(list.get(i).get("UID")));
				user.setAliasName(Tools.toString(list.get(i).get("AliasName")));
				//user.setShopName(Tools.toString(list.get(i).get("ShopName")));
				user.setAccessToken(Tools.toString(list.get(i).get("AccessToken")));
				user.setRefreshToken(Tools.toString(list.get(i).get("RefreshToken")));
				user.setAppkey(Tools.toString(list.get(i).get("Appkey")));
				user.setAppSecret(Tools.toString(list.get(i).get("AppSecret")));

				
				users.add(user);
			}
			//System.out.println(list);
		} catch (Exception e) {
			Log.errLog("[GetAllOtherMallUser[" + SqlStr + "] 出错:" + Tools.toString(e.getMessage()));
			Log.printLog("[GetAllOtherMallUser]执行数据库出错: " + Tools.toString(e.getMessage()));
		}
		return users;
	}
	
	
	public static Map<String,Object> GetMallTokenByRefreshToken(String Appkey, String AppSecret, String RefreshToken){
		Map<String,Object> outpd=new HashMap<String,Object>();
		String RequestURL = "https://mall.my.com/oauth/v2/token";
		String PostData = "client_id=" + Appkey 
						+ "&client_secret=" + AppSecret
						+ "&grant_type=refresh_token"
				        + "&refresh_token=" + RefreshToken;
		MyHttpRequest httpobj = MyHttpRequest.httpRequest(RequestURL, "POST", PostData, "UTF-8");
		if (httpobj == null) {
			Log.printLog(jobName + "获取授权失败!");
		} else {
			if (httpobj.getSuccessFlag()) {
				String jsonstr = httpobj.getResponse();
				if (jsonstr.indexOf("access_token") == -1) {
					Log.printLog(jobName + "获取待同步店的AccessToken出错:" + Tools.toString(jsonstr));	
				} else {
					try {
						JSONObject jsonReq = new JSONObject(jsonstr);
						String access_token = jsonReq.optString("access_token", "");
						RefreshToken = jsonReq.optString("refresh_token", "");
						updatetoken(access_token,RefreshToken);
						outpd.put("access_token", access_token);
						outpd.put("RefreshToken", RefreshToken);
					} catch (JSONException e) {
						Log.printLog(jobName + "获取待同步店的AccessToken出错:" + Tools.toString(e.getMessage()));	
					} catch (Exception e) {
						Log.printLog(jobName + "更新待同步店的AccessToken出错:" + Tools.toString(e.getMessage()));	
					}
					
				}
	
			} else {
				Log.printLog(jobName + "获取待同步店的AccessToken出错:" + Tools.toString(httpobj.getErrorStr()));
			}
	
		}
		return outpd;
	}
	
	
	public static void updatetoken(String access_token,String RefreshToken) throws Exception {
		String sql="update S_SyncInfoMall set AccessToken='"+SetDBValue(access_token)+"',LastSyncTime=GETDATE() where RefreshToken='"+SetDBValue(RefreshToken)+"'";
		ConnDB.executeUpdate(sql,false);
	}


	
	
	
	
	
	
	
	
	
	
	
}
