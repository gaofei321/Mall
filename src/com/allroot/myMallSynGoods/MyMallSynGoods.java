package com.allroot.myMallSynGoods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.allroot.db.ConnDB;
import com.allroot.entity.MyMallUser;
import com.allroot.tool.JSON;
import com.allroot.tool.Log;
import com.allroot.tool.MyHttpRequest;
import com.allroot.tool.Tools;

public class MyMallSynGoods {

	public void synMyMallGoods(MyMallUser user) {
		 Calendar calendar1 = Calendar.getInstance();
		  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		  calendar1.add(Calendar.DATE, -1);
		  String since = sdf1.format(calendar1.getTime());//获取3天前的数据
		//  System.out.println("时间：-》》》》》》"+since);
		Map<String,String> Authorization = new HashMap<String,String>();
		String access_token=user.getAccessToken();
		String LPostData="start=0&limit=300";
		String url="https://mall.my.com/merchant/wish/api/v2/order/get-fulfill?"+LPostData;
		Authorization.put("Authorization", "Bearer " + access_token);
		
		synProcess(user,url,"",Authorization);
		
		
	}
//同步商品接口
	private void synProcess(MyMallUser user, String url, String LPostData,Map<String,String> Authorization) {
		String sql="";
		String logs="";
		String UID=user.getUID();
		String suffix = user.getAliasName();//店铺简称
		MyHttpRequest httpobj = MyHttpRequest.httpRequest(url,Authorization,"GET", LPostData, "UTF-8");
		if(httpobj==null){
			logs = "下载用户："+UID+"店铺[" + suffix + "]未发货订单失败，httpobj为null";
			Log.printLog(logs);
			Log.errLog(logs);
			return;
		}else{
			String jsonStr = httpobj.getResponse();
			Map<String, Object> jsonobj = (Map<String, Object>) JSON.decode(jsonStr);
			int code = Tools.toInt(jsonobj.get("code"));
			Map<String,Object> paging=(Map<String, Object>) jsonobj.get("paging");
			String next="";
			if(paging!=null && !paging.isEmpty()){
				next=Tools.toString(paging.get("next"));
			}
			if (code == 0) {
				//解析数据
				List<Map<String,Object>> data=(List<Map<String, Object>>) jsonobj.get("data");
				if(data.isEmpty()){
					logs = "下载用户："+UID+"店铺[" + suffix + "]未发货订单失败，返回数据data为空";
					Log.printLog(logs);
					Log.errLog(logs);
					return;
				}else{
					for(int j=0;j<data.size();j++){
						Map<String,Object> Order = (Map<String, Object>) data.get(j).get("Order");
						String color = Tools.toString(Order.get("color")); 
						double price = Tools.toDouble(Order.get("price"));
						int quantity = Tools.toInt(Order.get("quantity"));
						double shipping_cost = Tools.toDouble(Order.get("shipping_cost"))*quantity;
						double shipping = Tools.toDouble(Order.get("shipping"))*quantity;
						String sku = Tools.toString(Order.get("sku"));
						String order_id = Tools.toString(Order.get("order_id"));
						String orderstate = Tools.toString(Order.get("state"));
						String cost = Tools.toString(Order.get("cost"));
						String variant_id  = Tools.toString(Order.get("variant_id"));
						String days_to_fulfill = Tools.toString(Order.get("days_to_fulfill"));
						String product_name = Tools.toString(Order.get("product_name"));
						String transaction_id = Tools.toString(Order.get("transaction_id"));
						String size = Tools.toString(Order.get("size"));	
						String order_time = Tools.toString(Order.get("order_time"));
						SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
						Date date;
						try {
							date = sdf1.parse(order_time);
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							order_time=sdf.format(date);
					        System.out.println(order_time);
						} catch (ParseException e1) {
							logs = "下载用户："+UID+"店铺[" + suffix + "]未发货订单失败，失败原因："+e1.getMessage();
							Log.printLog(logs);
							Log.errLog(logs);
							return;
						}
				        
					
						
						String buyer_id = Tools.toString(Order.get("buyer_id"));
						String CUSTOM = Tools.toString(Order.get("is_wish_express"));
						String requires_delivery_confirmation = Tools.toString(Order.get("requires_delivery_confirmation"));
						String Memo = "";
						String product_id = Tools.toString(Order.get("product_id"));
						double order_total = Tools.toDouble(Order.get("order_total"));
						
						if(CUSTOM.equalsIgnoreCase("True")){
							CUSTOM="Mall Express order";
						}else{
							CUSTOM="";
						}
						if(requires_delivery_confirmation.equalsIgnoreCase("True")){
							Memo="需要确认妥投";
				          CUSTOM=CUSTOM+"妥投";
						}
						if(!size.isEmpty() || !color.isEmpty()){
							product_name = product_name + " [";
							if(!size.isEmpty() && !color.isEmpty()){
								product_name = product_name + "size:" +size+";color:"+color;
							}else if(!size.isEmpty() && color.isEmpty()){
								product_name = product_name + "size:" + size;
							}else if(size.isEmpty() && !color.isEmpty()){
								product_name = product_name + "color:" +color;
					          	product_name = product_name + "]";
							}
						}
						
						Map<String,Object> ShippingDetail = (Map<String, Object>) Order.get("ShippingDetail");
						String phone_number = Tools.toString(ShippingDetail.get("phone_number"));
						String city = Tools.toString(ShippingDetail.get("city"));
						String fstate = Tools.toString(ShippingDetail.get("state"));
						String fname = Tools.toString(ShippingDetail.get("name"));
						String country = Tools.toString(ShippingDetail.get("country"));
						String street_address2 = Tools.toString(ShippingDetail.get("street_address2"));
						String street_address1 = Tools.toString(ShippingDetail.get("street_address1"));
						String zipcode = Tools.toString(ShippingDetail.get("zipcode"));
						
						sql="delete from P_trade_ADT where TradeNID=(select top 1 nid from P_trade_A Where suffix='"+Tools.SetDBValue(suffix)+"' and UID='"+Tools.SetDBValue(UID)+"' and ack='"+Tools.SetDBValue(order_id)+"')"
								+ "delete from P_Trade_A where suffix='"+Tools.SetDBValue(suffix)+"'and UID='"+Tools.SetDBValue(UID)+"' and ack='"+Tools.SetDBValue(order_id)+"'";  //删除备份表记录
						ArrayList<HashMap<String, Object>> list = null;
						try {
							System.out.println("删除备份表记录sql:->>>>>"+sql);
							ConnDB.DBExcuteSQL(sql); //删除备份表记录
							sql=" select NID from P_Trade  where uid = '" 
									+ Tools.SetDBValue(UID) + "' and TRANSACTIONID = '" + Tools.SetDBValue(order_id)+"' and suffix='"+Tools.SetDBValue(suffix)+"'"
									+ " union all select NID from P_tradepack  where uid = '" 
									+ Tools.SetDBValue(UID) + "' and TRANSACTIONID = '" + Tools.SetDBValue(order_id)+"' and suffix='"+Tools.SetDBValue(suffix)+"'"
									+ " union all select NID from P_trade_b  where uid = '" 
									+ Tools.SetDBValue(UID) + "' and TRANSACTIONID = '" + Tools.SetDBValue(order_id)+"' and suffix='"+Tools.SetDBValue(suffix)+ "'"
									+ " union all select NID from P_TradeUn_His  where uid = '" 
									+ Tools.SetDBValue(UID) + "' and TRANSACTIONID = '" + Tools.SetDBValue(order_id)+"' and suffix='"+Tools.SetDBValue(suffix)+ "'"
									+ " union all select NID from P_tradeUn  where uid = '" 
									+ Tools.SetDBValue(UID) + "' and TRANSACTIONID = '" + Tools.SetDBValue(order_id)+"' and suffix='"+Tools.SetDBValue(suffix)+ "'"
									+ " union all select NID from P_trade_His where uid = '" 
									+ Tools.SetDBValue(UID) + "' and TRANSACTIONID = '" + Tools.SetDBValue(order_id)+"' and suffix='"+Tools.SetDBValue(suffix)+ "'";
							System.out.println("判断是否存在sql:->>>>>"+sql);
							list = ConnDB.DBSelect(sql, true);
							if(list.size()>0){
								logs = "下载用户："+UID+"店铺[" + suffix + "]未发货订单失败，TRANSACTIONID:"+order_id+"数据库中已存在，不能去下载";
								Log.printLog(logs);
								Log.errLog(logs);
								continue;
							}
							
							
							sql="if (select top 1 SHIPPINGMETHOD from p_trade where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+"'and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"')='1'"
									+"	begin"
									+"	update p_trade set SHIPPINGMETHOD=0,AdditionalCharge=0 where NID=(select top 1 NID from p_trade where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"' )"
									+"	end"
									+"	else"
									+"	if (select top 1 SHIPPINGMETHOD from p_trade_b where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"')='1'"
									+"	begin"
									+"	update p_trade_b set SHIPPINGMETHOD=0,AdditionalCharge=0 where NID=(select top 1 NID from p_trade_b where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"' )"
									+"	end"
									+"	else"
									+"	if (select top 1 SHIPPINGMETHOD from p_tradeun where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"')='1'"
									+"	begin"
									+"	update p_tradeun set SHIPPINGMETHOD=0,AdditionalCharge=0 where NID=(select top 1 NID from p_tradeun where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"' )"
									+"	end"
									+"	else"
									+"	if (select top 1 SHIPPINGMETHOD from p_trade_his where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"')='1'"
									+"	begin"
									+"	update p_trade_his set SHIPPINGMETHOD=0,AdditionalCharge=0 where NID=(select top 1 NID from p_trade_his where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"' )"
									+"	end"
									+"	else"
									+"	if (select top 1 SHIPPINGMETHOD from P_TradeUn_His where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"')='1'"
									+"	begin"
									+"	update P_TradeUn_His set SHIPPINGMETHOD=0,AdditionalCharge=0 where NID=(select top 1 NID from P_TradeUn_His where TRANSACTIONID ='"+Tools.SetDBValue(order_id)+ "' and UID='"+Tools.SetDBValue(UID)+"' and suffix='"+Tools.SetDBValue(suffix)+"' )"
									+"	end "// 检查附表是否存在,否则重复
								+" insert into p_trade_A (filterflag,ack,ORDERTIME,buyerid,[user],suffix,amt,SHIPPINGAMT,SHIPTONAME,SHIPTOSTREET,"
								+ "SHIPTOSTREET2,SHIPTOCITY,SHIPTOSTATE,SHIPTOZIP,SHIPTOCOUNTRYCODE,SHIPTOCOUNTRYNAME,SHIPTOPHONENUM,currencyCode,"
								+ "PAYMENTSTATUS,TRANSACTIONTYPE,PAYMENTTYPE,INVNUM,ADDRESSOWNER,CUSTOM,guid,TRANSACTIONID,Memo,SHIPDISCOUNT,UID) "
								+ "values"
								+ "(5,'"+Tools.SetDBValue(order_id)+"','"+Tools.SetDBValue(order_time)+"','"+Tools.SetDBValue(buyer_id)+"','"+Tools.SetDBValue(suffix)+"','"+Tools.SetDBValue(suffix)+"','"+Tools.SetDBValue(price*quantity+shipping)+"','"+Tools.SetDBValue(shipping)+"','"+Tools.SetDBValue(fname)+"','"+Tools.SetDBValue(street_address1)+"','"
								+Tools.SetDBValue(street_address2)+"','"+Tools.SetDBValue(city)+"','"+Tools.SetDBValue(fstate)+"','"+Tools.SetDBValue(zipcode)+"','"+Tools.SetDBValue(country)+"',"+"isnull((select CountryEnName from b_country where  UID='"+Tools.SetDBValue(UID)+"' and CountryCode='"+Tools.SetDBValue(country)+"' ),'')"+",'"+Tools.SetDBValue(phone_number)+"','USD',"
								+"'Completed','Mall Cart','instant','','mall'"+",'"+Tools.SetDBValue(CUSTOM)+"','"+Tools.SetDBValue(order_id)+"','"+Tools.SetDBValue(order_id)+"','"+Tools.SetDBValue(Memo)+"','"+Tools.SetDBValue(price*quantity*0.15+shipping*0.15)+"','"+Tools.SetDBValue(UID)+"')";
							
							System.out.println("主表sql:->>>>>"+sql);
							
							ConnDB.DBExcuteSQL(sql);//往主表插入数据
							sql="declare @fTradeNID varchar(50)=''"
									+ "set @fTradeNID=isnull((select top 1 nid from p_trade_A  where TRANSACTIONID='"+Tools.SetDBValue(order_id)+"' and UID='"+Tools.SetDBValue(UID)+"'),0)"
									+"insert into P_Trade_ADt (tradenid,l_name,BmpFileName,L_CURRENCYCODE,L_ShipFee,l_qty,L_OPTIONSNAME,l_amt,l_number,"
									+ "L_EBAYITEMTXNID,sku,ebaysku,L_TransFee,UID)"
									+ "values"
									+ "(@fTradeNID,'"+Tools.SetDBValue(product_name)+"','','USD','"+Tools.SetDBValue(shipping)+"','"+Tools.SetDBValue(quantity)+"','','"+Tools.SetDBValue(price*quantity)+"','"+Tools.SetDBValue(product_id)+"','"
									+Tools.SetDBValue(transaction_id)+"','"+Tools.SetDBValue(sku)+"','"+Tools.SetDBValue(sku)+"','"+Tools.SetDBValue(price*quantity*0.15+shipping*0.15)+"','"+Tools.SetDBValue(UID)+"')"
									+" exec P_XS_TradeFromMall '" + Tools.SetDBValue(UID) + "',@fTradeNID";  
							
							System.out.println("细表sql:->>>>>"+sql);
							
							ConnDB.DBExcuteSQL(sql);//往细表插入数据
						
						} catch (Exception e) {
							logs = "下载用户："+UID+"店铺[" + suffix + "]未发货订单失败，失败原因："+e.getMessage();
							Log.printLog(logs);
							Log.errLog(logs);
							return;
						}
						
					}	
				}
				String updateSql="update S_SyncInfoMall set SyncSuccessTime=GETDATE() where AliasName='"+Tools.SetDBValue(suffix)+"' and UID='"+Tools.SetDBValue(UID)+"'";
				try {
					ConnDB.executeUpdate(updateSql,false);
				} catch (Exception e) {
					logs = "下载用户："+UID+"店铺[" + suffix + "]更新最后同步时间失败";
					Log.printLog(logs);
					Log.errLog(logs);
				}
				
			}else{
				logs = "下载用户："+UID+"店铺[" + suffix + "]未发货订单失败，授权失败："+(String) jsonobj.get("message");
				Log.printLog(logs);
				Log.errLog(logs);
			}			
			if(!next.isEmpty()){
				/*url=next.substring(0, next.indexOf("?"));
				LPostData=next.substring(next.indexOf("?")+1)+LPostData.substring(LPostData.indexOf("&"));*/
				synProcess(user,next,"",Authorization);
			}
		}
		
		
		
		
		
	}

}
