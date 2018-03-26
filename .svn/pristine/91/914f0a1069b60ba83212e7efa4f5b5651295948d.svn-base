package com.allroot.tool;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {

	public static final String key = "19730217";
	

	public static void main(String[] args) {
        String base64=com.allroot.base64.Base64Util.getBASE64("AAAAAAAA-DD17-450C-BCEC-02F69D756250", "utf-8");
		Log.printLog(base64);
		Log.printLog(convertFromBase64(base64));
		

		// Log.printLog(getTeacherList("��ʦ10(0010) ��ʦ11(0011) ��ʦ9(009) ��ʦ12(0012) ��ʦ13(0013) ��ʦ14(0014)").toString());
		// TODO Auto-generated method stub
		// System.out.println(strToDate("2015-10-19"));
		// try {
		// System.out.println(jsonTest());
		// System.out.println(jsonTest2());
		// } catch (JSONException e) {
		// e.printStackTrace();

		// }
		// System.out.println(getCurentDate());
		// System.out
		// .println(downPage(
		// "http://www.kikowireless.com/image/data/category/Hybrid%20Case/Apple/iPhone%206/design%20candy%20shell/Apple-iPhone-6-47-camouflage-design-candy-shell-case-orange.jpg",
		// "1.jpg"));
		// System.out.println(getLocalIP());
		// System.out.println(getServerIPByName("www.sohu.com"));

	}

	public static Integer strToInteger(String strInt) {
		try {
			return Integer.valueOf(strInt);
		} catch (NumberFormatException e) {
			return Integer.valueOf(0);
		}
	}

	public static Short strToShort(String strShort) {
		try {
			return Short.valueOf(strShort);
		} catch (NumberFormatException e) {
			return Short.valueOf("0");
		}
	}

	public static Long strToLong(String strLong) {
		try {
			return Long.valueOf(strLong);
		} catch (NumberFormatException e) {
			return Long.valueOf("0");
		}
	}

	public static String addDateByDays(Date startDate, int amount) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		String sDate = sdf.format(calendar.getTime());

		return sDate;
	}

	public static Float strToFloat(String strFloat) {
		try {
			return Float.valueOf(strFloat);
		} catch (NumberFormatException e) {
			return Float.valueOf("0.00f");
		}
	}

	public static Double strToDouble(String strDouble) {
		try {
			return Double.valueOf(strDouble);
		} catch (NumberFormatException e) {
			return Double.valueOf("0.00");
		}
	}

	public static Date strToDate(String strDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return Date.valueOf(strDate);

		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String jsonTest() throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray jsonMembers = new JSONArray();
		JSONObject member1 = new JSONObject();
		member1.put("loginname", "zhangfan");
		member1.put("password", "userpass");
		member1.put("email", "10371443@qq.com");
		member1.put("sign_date", "2007-06-12");
		jsonMembers.put(member1);

		JSONObject member2 = new JSONObject();
		member2.put("loginname", "zf");
		member2.put("password", "userpass");
		member2.put("email", "8223939@qq.com");
		member2.put("sign_date", "2008-07-16");
		jsonMembers.put(member2);
		json.put("users", jsonMembers);

		return json.toString();
	}

	public static String jsonTest2() throws JSONException {
		String jsonString = "{\"users\":[{\"loginname\":\"zhangfan\",\"password\":\"userpass\",\"email\":\"10371443@qq.com\"},{\"loginname\":\"zf\",\"password\":\"userpass\",\"email\":\"822393@qq.com\"}]}";
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonArray = json.getJSONArray("users");
		String loginNames = "loginname list:";
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject user = (JSONObject) jsonArray.get(i);
			String userName = (String) user.get("loginname");
			if (i == jsonArray.length() - 1) {
				loginNames += userName;
			} else {
				loginNames += userName + ",";
			}
		}
		return loginNames;
	}

	public static String getLocalIP() {
		InetAddress localIp = null;
		String sLocalIp = "";
		try {
			localIp = InetAddress.getLocalHost();
			sLocalIp = localIp.getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return sLocalIp;
	}

	public static String getServerIPByName(String serverName) {
		InetAddress serverIp = null;
		String sServerIp = "";
		try {
			serverIp = InetAddress.getByName(serverName);
			sServerIp = serverIp.getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return sServerIp;
	}

	public static boolean downPage(String sUrl, String fileName) {

		URL url = null;
		URLConnection conn = null;
		try {
			url = new URL(sUrl);

			conn = url.openConnection();

			System.out.println("content length:" + conn.getContentLengthLong());
			System.out.println("content type:" + conn.getContentType());

			try {
				InputStream in = conn.getInputStream();
				BufferedInputStream bin = new BufferedInputStream(in);
				FileOutputStream fout = new FileOutputStream(fileName);
				int len = 0;
				byte b[] = new byte[1024];
				while ((len = bin.read(b, 0, 1024)) != -1) {
					fout.write(b, 0, len);

				}
				fout.close();
				bin.close();
				in.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public static String getCurentDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	public static void trigAuthTimer() {

		Timer timer = new Timer();

	
	}

	public static String addMonthDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.MONTH, 1);
		String sDate = sdf.format(calendar.getTime());

		return sDate;
	}

	public static String gb2312ToUtf8(String str) {
		String urlEncode = "";
		try {
			urlEncode = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlEncode;
	}

	public static String utf8Togb2312(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(
							str.substring(i + 1, i + 3), 16));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				i += 2;
				break;
			default:

				sb.append(c);
				break;
			}
		}
		String result = sb.toString();
		String res = null;
		try {
			byte[] inputBytes = result.getBytes("8859_1");
			res = new String(inputBytes, "UTF-8");
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * Filter the specified message string for characters that are sensitive in
	 * HTML. This avoids potential attacks caused by including JavaScript codes
	 * in the request URL that is often reported in error messages.
	 * 
	 * @param message
	 *            The message string to be filtered
	 */
	public static String filter(String message) {

		if (message == null)
			return (null);

		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuilder result = new StringBuilder(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());

	}

	/** Normalizes the given string. */
	protected String normalize(String s) {
		if (s == null) {
			return "";
		}

		StringBuilder str = new StringBuilder();

		int len = s.length();
		for (int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '<':
				str.append("&lt;");
				break;
			case '>':
				str.append("&gt;");
				break;
			case '&':
				str.append("&amp;");
				break;
			case '"':
				str.append("&quot;");
				break;
			case '\r':
			case '\n':
				// if (canonical) {
				str.append("&#");
				str.append(Integer.toString(ch));
				str.append(';');
				break;
			// }
			// else, default append char
			//$FALL-THROUGH$
			default:
				str.append(ch);
			}
		}

		return (str.toString());
	}

	public static void getClassInfo(String className)
			throws InstantiationException, IllegalAccessException {
		Class<?> f[] = null;
		Object o = null;
		Class c = null;
		try {
			c = Class.forName(className);
			o = c.newInstance();
			Field fld[] = c.getDeclaredFields();
			for (int i = 0; i < fld.length; i++) {
				fld[i].setAccessible(true);
				printMsg(fld[i].getModifiers() + "  " + fld[i].getType() + " "
						+ fld[i].getName());
			}
			Method[] method = c.getDeclaredMethods();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < method.length; i++) {
				method[i].setAccessible(true);
				sb.append(Modifier.toString(method[i].getModifiers()) + "  "
						+ method[i].getReturnType().getName() + " "
						+ method[i].getName() + " ");
				Class<?> cP[] = method[i].getParameterTypes();
				for (int j = 0; j < cP.length; j++) {
					sb.append(Modifier.toString(cP[j].getModifiers()) + " "
							+ cP[j].getName() + ",");
				}
				printMsg(sb.toString());
				sb.delete(0, sb.length());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printMsg(String msg) {
		System.out.println(msg);
	}

	public static List<String> getTeacherList(String managers) {
		List<String> ls = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
		Matcher matcher = pattern.matcher(managers);
		while (matcher.find())
			ls.add(matcher.group());
		return ls;
	}

	public static String convertFromBase64(String s) {
		String result = "";
		
		if (s.length() > 0 && s.contains("-") == false) {
			result = com.allroot.base64.Base64Util.getFromBASE64(s, "utf-8");
			Log.printLog(result);
		} else {
			result = s;
		}
		return result;
	}

}
