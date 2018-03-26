package com.allroot.tool;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



public class MyHttpRequest {
	private String response;//正确的返回信息
	private String ErrorStr;//返回的错误信息
	private Boolean SuccessFlag;//返回是否正确
	private Integer Status;//返回Http状态码
	public String getResponse() {
		return response;
	}
	public MyHttpRequest(String response, String errorStr, Boolean successFlag) {
		super();
		this.response = response;
		ErrorStr = errorStr;
		SuccessFlag = successFlag;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getErrorStr() {
		return ErrorStr;
	}
	public void setErrorStr(String errorStr) {
		ErrorStr = errorStr;
	}
	public Boolean getSuccessFlag() {
		return SuccessFlag;
	}
	public void setSuccessFlag(Boolean successFlag) {
		SuccessFlag = successFlag;
	}
	
	public Integer getStatus() {
		return Status;
	}
	public void setStatus(Integer status) {
		Status = status;
	}
	
	public MyHttpRequest() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）

	 * @param outputStr 提交的数据

	 * @return MyHttpRequest 对象
	 */
	public static MyHttpRequest httpRequest(String requestUrl, String requestMethod, String outputStr,String charset) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				//if(requestUrl.contains(s))
				httpUrlConn.connect();

			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}

	
	public static MyHttpRequest EubOutLinehttpRequest(String requestUrl, Map<String,String> requestHead,String requestMethod, String outputStr,String charset) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			httpUrlConn.setRequestProperty("Content-Type","application/xml");
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			Set<String> keys=requestHead.keySet();
			for(String key:keys) {
				httpUrlConn.setRequestProperty(key,requestHead.get(key));
			}

			if ("GET".equalsIgnoreCase(requestMethod)) {
				//if(requestUrl.contains(s))
				httpUrlConn.connect();

			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}
	public static MyHttpRequest httpRequest(String requestUrl, Map<String,String> requestHead,String requestMethod, String outputStr,String charset) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			Set<String> keys=requestHead.keySet();
			for(String key:keys) {
				httpUrlConn.setRequestProperty(key,requestHead.get(key));
			}

			if ("GET".equalsIgnoreCase(requestMethod)) {
				//if(requestUrl.contains(s))
				httpUrlConn.connect();

			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}
	
	
	/**
	 * 发起https请求并获取结果   json 格式的
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）

	 * @param outputStr 提交的数据

	 * @return MyHttpRequest 对象
	 */
	public static MyHttpRequest httpRequestJson(String requestUrl, Map<String,String> requestHead,String requestMethod, String outputStr,String charset) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type","application/json; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			Set<String> keys=requestHead.keySet();
			for(String key:keys) {
				httpUrlConn.setRequestProperty(key,requestHead.get(key));
			}

			if ("GET".equalsIgnoreCase(requestMethod)) {
				//if(requestUrl.contains(s))
				httpUrlConn.connect();

			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			httpobj.Status=httpUrlConn.getResponseCode();
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}

	public static MyHttpRequest httpRequestJson(String requestUrl, Map<String,String> requestHead,String requestMethod, String outputStr,String charset,String ContentType) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type",ContentType+"; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			Set<String> keys=requestHead.keySet();
			for(String key:keys) {
				httpUrlConn.setRequestProperty(key,requestHead.get(key));
			}

			if ("GET".equalsIgnoreCase(requestMethod)) {
				//if(requestUrl.contains(s))
				httpUrlConn.connect();

			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}


	/**
	 * 发起https请求并获取结果   json 格式的
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）

	 * @param outputStr 提交的数据

	 * @return MyHttpRequest 对象
	 */
	public static MyHttpRequest httpRequestJson(String requestUrl, String requestMethod, String outputStr,String charset) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type","application/json; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				//if(requestUrl.contains(s))
				httpUrlConn.connect();

			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			httpobj.Status=httpUrlConn.getResponseCode();
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}
	@Override
	public String toString() {
		return "MyHttpRequest [response=" + response + ", ErrorStr=" + ErrorStr + ", SuccessFlag=" + SuccessFlag
				+ ", Status=" + Status + "]";
	}
	
	/**
	 * 淼信物流提交
	 * @return
	 * @throws Exception 
	 */
	public static HashMap<String,Object> miaoxinHttp(String json,String param,String url) throws Exception {
		HashMap<String,Object> pd=new HashMap<String,Object>();
		
		HttpClient objClient = new HttpClient();
		objClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		PostMethod objPostMethod = new PostMethod(url);
		
		try {
			json=URLEncoder.encode(json,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		NameValuePair objNameValuePair = new NameValuePair();
		objNameValuePair.setName(param);
		objNameValuePair.setValue(json);
		objPostMethod.addParameter(objNameValuePair);
		try {
			int statusCode = objClient.executeMethod(objPostMethod);//返回状态码

			String strResponseBody = new String(objPostMethod.getResponseBody());
			pd.put("statusCode", statusCode);
			pd.put("strResponseBody", strResponseBody);
			System.out.println("createOrder:statusCode~~~~~" + statusCode);
			System.out.println("createOrder:strResponseBody~~~~~" + strResponseBody);
			
		}catch(Exception e){
			pd.put("statusCode", 404);
		}
		return pd;
		
	
	}
	public static byte[] httpRequestByStream(String requestUrl, Map<String,String> requestHead
			,String requestMethod, String outputStr,String charset) {
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputstream=null;
		// 得到pdf的二进制数据，以二进制封装得到数据，具有通用性
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			if(requestHead!=null&&requestHead.size()>0){
				Set<String> keys=requestHead.keySet();
				for(String key:keys) {
					httpUrlConn.setRequestProperty(key,requestHead.get(key));
				}
			}
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}
			try {
				inputstream=httpUrlConn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputstream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
			} catch (IOException e) {
				inputstream=httpUrlConn.getErrorStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputstream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
			}
			
			return outStream.toByteArray();
		} catch (ConnectException ce) {
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			return null;
		}
		catch (Exception e) {
			System.out.println("httpRequest-->err2,"+e.getMessage());
			return null;
		}finally {
            try {
            	if(inputstream!=null)
            		inputstream.close();
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		
	}
	
	public static byte[] httpRequestBLS(String requestUrl, Map<String,String> requestHead,String requestMethod, String outputStr,String charset) {
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputstream=null;
		// 得到pdf的二进制数据，以二进制封装得到数据，具有通用性
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			//httpUrlConn.setRequestProperty("Content-Type","textml, */*;");
			httpUrlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			if(requestHead!=null&&requestHead.size()>0){
				Set<String> keys=requestHead.keySet();
				for(String key:keys) {
					httpUrlConn.setRequestProperty(key,requestHead.get(key));
				}
			}
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			// 当有数据需要提交时
			}else if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}
			inputstream=httpUrlConn.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputstream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			return outStream.toByteArray();
		} catch (ConnectException ce) {
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			return null;
		}
		catch (Exception e) {
			System.out.println("httpRequest-->err2,"+e.getMessage());
			return null;
		}finally {
            try {
            	if(inputstream!=null)
            		inputstream.close();
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
	}
	public static MyHttpRequest httpRequestUploadFile(String requestUrl, byte[] filebyte,String charset) {
		MyHttpRequest httpobj=new MyHttpRequest();
		StringBuffer buffer = new StringBuffer();
		URL url = null;
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream =null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);// 允许输出
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);// 不使用Cache
			httpUrlConn.setInstanceFollowRedirects(true);
			httpUrlConn.setRequestProperty("Content-Type","application/pdf");
			httpUrlConn.setConnectTimeout(1000*60*2); 
			// 设置请求方式（GET/POST）
			//httpUrlConn.setRequestMethod(requestMethod);
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 注意编码格式，防止中文乱码
			outputStream.write(filebyte);
			outputStream.close();
			// 将返回的输入流转换成字符串
			InputStreamReader inputStreamReader=null;
            try {
            	 //请求成功正常取
            	inputStream = httpUrlConn.getInputStream();
            	inputStreamReader = new InputStreamReader(inputStream, charset);
    			bufferedReader = new BufferedReader(inputStreamReader);
			} catch (IOException e) {
				//失败从错误信息里面取
				inputStream=httpUrlConn.getErrorStream();
				inputStreamReader = new InputStreamReader(inputStream, charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				
			}
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			httpobj.SuccessFlag=true;
			httpobj.response=buffer.toString();
			httpobj.ErrorStr="";
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=ce.getMessage();
			System.out.println("httpRequest-->err1,"+ce.getMessage());
			//log.error("Weixin server connection timed out.");
		}
		catch (Exception e) {
			httpobj.SuccessFlag=false;
			httpobj.response="";
			httpobj.ErrorStr=e.getMessage();
			System.out.println("httpRequest-->err2,"+e.getMessage());
			//log.error("https request error:{}", e);
		}finally {
            try {
                if (inputStream != null) {
                    System.out.println("关闭inputStream");
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    //System.out.println("关闭bufferedReader");
                    bufferedReader.close();
                }
                if(httpUrlConn!=null)
                	httpUrlConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return httpobj;
	}
}
