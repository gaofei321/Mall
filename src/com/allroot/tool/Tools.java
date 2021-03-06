package com.allroot.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



//import com.auto.db.ConnDB;

public class Tools {
	public static SimpleDateFormat dateTimeFormate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateFormate = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static java.text.DecimalFormat priceFormat = new java.text.DecimalFormat(
			"0.00");
	public static SimpleDateFormat timestampFormate = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static SimpleDateFormat timeFormate = new SimpleDateFormat(
			"HH:mm:ss");
	public static String SetDBValue(Object Avalue) {
		return Avalue == null ? "" : Avalue.toString().replaceAll("'", "''");

	}
	public static String printRequestParam(HttpServletRequest request) {
		Enumeration<String> eParam = request.getParameterNames();
		StringBuilder sb = new StringBuilder();
		while (eParam.hasMoreElements()) {
			String paramName = eParam.nextElement();
			sb.append(paramName + "=" + request.getParameter(paramName));
			if (eParam.hasMoreElements())
				sb.append("&");
		}
		return sb.toString();
	}

	public static String printRequestHead(HttpServletRequest request) {
		Enumeration<String> eHead = request.getHeaderNames();
		StringBuilder sb = new StringBuilder();
		while (eHead.hasMoreElements()) {
			String headName = eHead.nextElement();
			sb.append(headName + "=" + request.getHeader(headName));
			if (eHead.hasMoreElements())
				sb.append("&");
		}
		return sb.toString();
	}

	public static String rs2Json(ResultSet rs) {
		if (rs == null)
			return "[]";
		StringBuffer sbJson = new StringBuffer();
		sbJson.append("[");
		try {
			rs.beforeFirst();
			while (rs.next()) {
				sbJson.append("{ID:\"" + rs.getString(1).trim() + "\",");
				sbJson.append("NAME:\"" + rs.getString(2).trim() + "\"},");
			}
			if (sbJson.length() > 1)
				sbJson.deleteCharAt(sbJson.length() - 1);
			sbJson.append("]");
			return sbJson.toString();
		} catch (SQLException e) {
			return "[]";
		}
	}

	public static String rsRow2Json(ResultSet rs, int rowNum) {
		if (rs == null)
			return "[]";
		StringBuffer sbJson = new StringBuffer();
		sbJson.append("[");
		String cName = "";
		String value = "";
		try {
			if (rs.absolute(rowNum)) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int fCount = rsmd.getColumnCount();
				for (int i = 1; i <= fCount; i++) {
					sbJson.append("{Field:\"" + cName.trim() + "\",");
					value = rs.getString(cName);
					if (value == null)
						value = "";
					value = value.trim();
					sbJson.append("Value:\"" + value.trim() + "\"},");
				}

				if (sbJson.length() > 1)
					sbJson.deleteCharAt(sbJson.length() - 1);
				sbJson.append("]");
				return sbJson.toString();
			} else {
				return "[]";
			}
		} catch (SQLException e) {
			return "[]";
		}
	}

	public static String rsAll2Json(ResultSet rs) {
		if (rs == null)
			return "[]";
		StringBuffer sbJson = new StringBuffer();
		sbJson.append("[");
		String cName = "";
		String v = "";
		try {
			rs.beforeFirst();
			ResultSetMetaData rsmd = rs.getMetaData();
			int fCount = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= fCount; i++) {
					sbJson.append("{");
					cName = rsmd.getColumnName(i);
					v = rs.getString(cName);
					if (v == null) {
						v = "";
					}
					sbJson.append(cName.toLowerCase().trim() + ":\"" + v.trim()
							+ "\"");
					if (i < fCount)
						sbJson.append(",");
					else
						sbJson.append("},");
				}
			}

			if (!cName.equals(""))
				sbJson.deleteCharAt(sbJson.length() - 1);
			sbJson.append("]");
			return sbJson.toString();
		} catch (SQLException e) {
			return "[]";
		}
	}

	public static String getSelectStr(ResultSet rs, String name)
			throws SQLException {
		String str = "<select name=\"" + name + "\" id=\"" + name + "\" >";
		while (rs.next()) {
			str += "<option value=\"" + rs.getString("CodeNbr").trim() + "\">"
					+ rs.getString("Name").trim() + "</option>";
		}
		str += "</select>";
		return str;
	}

	public static String getcheckBoxStr(ResultSet rs, String name)
			throws SQLException {
		String str = "";
		while (rs.next()) {
			str += "<input type = \"checkbox\" name = \"" + name
					+ "\" value = \"" + rs.getString("CodeNbr").trim()
					+ "\" id=\"" + name + "_" + rs.getString("CodeNbr").trim()
					+ "\" />" + rs.getString("Name").trim() + "<br />";
		}
		return str;
	}

	public static String getCNStr(String str) {
		String s = str;
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
			// return new String(str.getBytes("GBK"), "UTF-8");
		} catch (Exception e) {
			return s;
		}
	}

	public static Document getXMLDoc(String fileName) {
		try {
			InputStream iss = null;// /
									// ConnDB.class.getClassLoader().getResourceAsStream(fileName);
			DocumentBuilderFactory domfac = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			Document doc = (Document) dombuilder.parse(iss);
			return doc;
		} catch (Exception e) {
			return null;
		}
	}

	public static Document getXMLDoc(File xmlFile) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(xmlFile);
	}

	public static String getXMLDOMValue(Document doc, String nodeName) {
		try {
			NodeList paramNode = doc.getElementsByTagName(nodeName);
			String nodeValue = paramNode.item(0).getFirstChild().getNodeValue()
					.trim();
			return nodeValue;
		} catch (Exception e) {
			return null;
		}
	}

	public static NodeList getXMLNodeList(Document doc, String nodeName) {
		if (doc == null) {
			return null;
		}
		try {
			NodeList paramNode = doc.getElementsByTagName(nodeName);
			return paramNode;
		} catch (Exception e) {
			return null;
		}
	}

	private static final byte[] encodingTable = { (byte) 'B', (byte) 'A',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
			(byte) 'R', (byte) 'S', (byte) 't', (byte) 'U', (byte) 'V',
			(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 'T', (byte) 'u',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) '+', (byte) '/' };

	/*
	 * private static final byte[] encodingTable1 = {//test (byte) 'A', (byte)
	 * 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte)
	 * 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte)
	 * 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte)
	 * 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte)
	 * 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte)
	 * 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte)
	 * 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte)
	 * 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte)
	 * 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte)
	 * '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte)
	 * '9', (byte) '+', (byte) '/' };
	 */
	private static final byte[] decodingTable;
	static {
		decodingTable = new byte[128];
		for (int i = 0; i < encodingTable.length; i++) {
			decodingTable[encodingTable[i]] = (byte) i;
		}
	}

	public static byte[] encode(byte[] data) {
		byte[] bytes;
		int modulus = data.length % 3;
		if (modulus == 0) {
			bytes = new byte[(4 * data.length) / 3];
		} else {
			bytes = new byte[4 * ((data.length / 3) + 1)];
		}
		int dataLength = (data.length - modulus);
		int a1;
		int a2;
		int a3;
		for (int i = 0, j = 0; i < dataLength; i += 3, j += 4) {
			a1 = data[i] & 0xff;
			a2 = data[i + 1] & 0xff;
			a3 = data[i + 2] & 0xff;
			bytes[j] = encodingTable[(a1 >>> 2) & 0x3f];
			bytes[j + 1] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f];
			bytes[j + 2] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f];
			bytes[j + 3] = encodingTable[a3 & 0x3f];
		}
		int b1;
		int b2;
		int b3;
		int d1;
		int d2;
		switch (modulus) {
		case 0: /* nothing left to do */
			break;
		case 1:
			d1 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = (d1 << 4) & 0x3f;
			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = (byte) '=';
			bytes[bytes.length - 1] = (byte) '=';
			break;
		case 2:
			d1 = data[data.length - 2] & 0xff;
			d2 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
			b3 = (d2 << 2) & 0x3f;
			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = encodingTable[b3];
			bytes[bytes.length - 1] = (byte) '=';
			break;
		}
		return bytes;
	}

	public static byte[] decode(byte[] data) {
		byte[] bytes;
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		data = discardNonBase64Bytes(data);
		if (data[data.length - 2] == '=') {
			bytes = new byte[(((data.length / 4) - 1) * 3) + 1];
		} else if (data[data.length - 1] == '=') {
			bytes = new byte[(((data.length / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((data.length / 4) * 3)];
		}
		for (int i = 0, j = 0; i < (data.length - 4); i += 4, j += 3) {
			b1 = decodingTable[data[i]];
			b2 = decodingTable[data[i + 1]];
			b3 = decodingTable[data[i + 2]];
			b4 = decodingTable[data[i + 3]];
			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | b4);
		}
		if (data[data.length - 2] == '=') {
			b1 = decodingTable[data[data.length - 4]];
			b2 = decodingTable[data[data.length - 3]];
			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data[data.length - 1] == '=') {
			b1 = decodingTable[data[data.length - 4]];
			b2 = decodingTable[data[data.length - 3]];
			b3 = decodingTable[data[data.length - 2]];
			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data[data.length - 4]];
			b2 = decodingTable[data[data.length - 3]];
			b3 = decodingTable[data[data.length - 2]];
			b4 = decodingTable[data[data.length - 1]];
			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
		}
		return bytes;
	}

	public static byte[] decode(String data) {
		byte[] bytes;
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		data = discardNonBase64Chars(data);
		if (data.charAt(data.length() - 2) == '=') {
			bytes = new byte[(((data.length() / 4) - 1) * 3) + 1];
		} else if (data.charAt(data.length() - 1) == '=') {
			bytes = new byte[(((data.length() / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((data.length() / 4) * 3)];
		}
		for (int i = 0, j = 0; i < (data.length() - 4); i += 4, j += 3) {
			b1 = decodingTable[data.charAt(i)];
			b2 = decodingTable[data.charAt(i + 1)];
			b3 = decodingTable[data.charAt(i + 2)];
			b4 = decodingTable[data.charAt(i + 3)];
			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | b4);
		}
		if (data.charAt(data.length() - 2) == '=') {
			b1 = decodingTable[data.charAt(data.length() - 4)];
			b2 = decodingTable[data.charAt(data.length() - 3)];
			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data.charAt(data.length() - 1) == '=') {
			b1 = decodingTable[data.charAt(data.length() - 4)];
			b2 = decodingTable[data.charAt(data.length() - 3)];
			b3 = decodingTable[data.charAt(data.length() - 2)];
			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data.charAt(data.length() - 4)];
			b2 = decodingTable[data.charAt(data.length() - 3)];
			b3 = decodingTable[data.charAt(data.length() - 2)];
			b4 = decodingTable[data.charAt(data.length() - 1)];
			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
		}
		return bytes;
	}

	private static byte[] discardNonBase64Bytes(byte[] data) {
		byte[] temp = new byte[data.length];
		int bytesCopied = 0;
		for (int i = 0; i < data.length; i++) {
			if (isValidBase64Byte(data[i])) {
				temp[bytesCopied++] = data[i];
			}
		}
		byte[] newData = new byte[bytesCopied];
		System.arraycopy(temp, 0, newData, 0, bytesCopied);
		return newData;
	}

	private static String discardNonBase64Chars(String data) {
		StringBuffer sb = new StringBuffer();
		int length = data.length();
		for (int i = 0; i < length; i++) {
			if (isValidBase64Byte((byte) (data.charAt(i)))) {
				sb.append(data.charAt(i));
			}
		}
		return sb.toString();
	}

	private static boolean isValidBase64Byte(byte b) {
		if (b == '=') {
			return true;
		} else if ((b < 0) || (b >= 128)) {
			return false;
		} else if (decodingTable[b] == -1) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {

	}

	public static String getTimeStr(Date d) {
		return timeFormate.format(d);
	}

	public static String getDateStr() {
		return dateTimeFormate.format(new Date());
	}

	public static String getDateStr2() {
		return dateFormate.format(new Date());
	}

	public static String getDateStr2(Date d) {
		return dateFormate.format(d);
	}

	public static String getDateTimeStr(Date d) {
		return dateTimeFormate.format(d);
	}

	public static String getBeforeDateStr(int num) {
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, num);
		Date date = theCa.getTime();
		return Tools.getDateStr2(date);
	}

	public static String getBeforeDateStr2(int num) {
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, num);
		Date date = theCa.getTime();
		return Tools.getDateTimeStr(date);
	}

	public static List<String> getDateList(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>();
		try {
			Date date_start = sdf.parse(start);
			Date date_end = sdf.parse(end);
			Date date = date_start;
			Calendar cd = Calendar.getInstance();

			while (date.getTime() < date_end.getTime()) {
				list.add(sdf.format(date));
				cd.setTime(date);
				cd.add(Calendar.DATE, 1);
				date = cd.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static String getDateStr(Date d) {
		return dateTimeFormate.format(d);
	}

	public static String getTimeStampStr(String timeStr) {
		if (Tools.isNullOrEmpty(timeStr)) {
			return null;
		}
		String dateStr = null;
		try {
			Date date = timestampFormate.parse(timeStr);
			dateStr = getDateStr(date);
		} catch (ParseException e) {
			return null;
		}

		return dateStr;

	}

	private final static String[] hex = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
			"1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
			"27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
			"3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
			"5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
			"69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
			"74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
			"7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
			"8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
			"95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
			"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
			"AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
			"B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
			"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
			"CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
			"D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
			"E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
			"ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
			"F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

	private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01,
			0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	public static String escape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z') {
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') {
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') {
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
					|| ch == '~' || ch == '*' || ch == '\'' || ch == '('
					|| ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007F) {
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else {
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0x00FF & ch)]);
			}
		}
		return sbuf.toString();
	}

	public static String unescape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z') {
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') {
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') {
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
					|| ch == '~' || ch == '*' || ch == '\'' || ch == '('
					|| ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) {
					cint = (cint << 4) | val[s.charAt(i + 1)];
					cint = (cint << 4) | val[s.charAt(i + 2)];
					i += 2;
				} else {
					cint = (cint << 4) | val[s.charAt(i + 2)];
					cint = (cint << 4) | val[s.charAt(i + 3)];
					cint = (cint << 4) | val[s.charAt(i + 4)];
					cint = (cint << 4) | val[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			} else {
				sbuf.append((char) ch);
			}
			i++;
		}
		return sbuf.toString();
	}

	public static int toInt(Object o) {
		if (o == null)
			return 0;
		int i = 0;
		try {
			double d = Double.parseDouble(o.toString());
			i -= d;
		} catch (Exception e) {
			return 0;
		}
		return -i;
	}

	public static String toString(Object o) {
		if (o == null)
			return "";
		return o.toString().trim();
	}

	public static Timestamp toDate(Object o) {
		return o != null ? new java.sql.Timestamp(((Date) o).getTime()) : null;
	}

	public static int convertBoolToInt(Object b) {
		if (b == null) {
			return 0;
		}

		String check = b.toString().toUpperCase().trim();

		if (check.equals("TRUE") || check.equals("1")) {
			return 1;
		} else {
			return 0;
		}
	}

	public static String toDBString(Object o) {
		String s = "";
		if (o == null) {
			s = "";
		} else {
			s = o.toString().replace("'", "''");
		}
		return s;
	}

	public static String toDBDateString(Object o) {
		if (o instanceof Calendar) {
			o = ((Calendar) o).getTime();
		}
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = "";
		if (o == null) {
			s = "null";
		} else {
			s = "'" + dFormat.format(o) + "'";
		}
		dFormat = null;
		return s;
	}

	public static Boolean verifyRequest(HttpServletRequest request) {
		@SuppressWarnings("rawtypes")
		Enumeration enu = request.getParameterNames();
		String paramName = "";
		String paramValue = "";
		String unEscapeValue = "";
		String mType = request.getParameter("auto");
		mType = mType == null ? "" : mType;
		HttpSession session = request.getSession();
		String uID = (String) session.getAttribute("uid");
		String sourceUrl = request.getHeader("Referer");
		uID = uID == null ? "null" : uID;
		sourceUrl = sourceUrl == null ? "" : sourceUrl;
		String SQL[];
		/*
		 * SQL = new String[9]; SQL[0] = "drop "; SQL[1] = "delete "; SQL[2] =
		 * "update "; SQL[3] = "insert "; SQL[4] = "alter "; SQL[5] =
		 * "truncate "; SQL[6] = "execute "; SQL[7] = " or "; SQL[8] = " and ";
		 * SQL[9] = "exec "; SQL[10] = "master "; SQL[11] = "select ";
		 */
		String s = "drop ,delete ,update ,insert ,alter ,truncate ,execute ";
		SQL = s.split(",");
		try {
			while (enu.hasMoreElements()) {
				paramName = (String) enu.nextElement();
				paramValue = request.getParameter(paramName);
				paramValue = paramValue == null ? "" : paramValue;
				if (paramValue.equals("")) {
					continue;
				}
				try {
					unEscapeValue = Escape.unescape(paramValue);
				} catch (Exception e2) {
					unEscapeValue = "";
				}
				unEscapeValue = unEscapeValue.trim().toLowerCase();
				paramValue = paramValue.trim().toLowerCase();
				// unEscapeValue
				for (int i = 0; i < SQL.length; i++) {
					// System.out.println(paramName+"="+paramValue+","+SQL[i]);
					if (paramValue.indexOf(SQL[i]) != -1) {
						return false;
					}
					if (unEscapeValue.indexOf(SQL[i]) != -1) {
						return false;
					}
				}
			}
		} catch (Exception e) {

		}
		return true;
	}

	public static boolean deleteFolder(String sPath) {
		Boolean flag = false;
		File file = new File(sPath);

		if (!file.exists()) {
			return flag;
		} else {
			if (file.isFile()) {
				return deleteFile(sPath);
			} else {
				return deleteDirectory(sPath);
			}
		}
	}

	public static boolean deleteFile(String sPath) {
		Boolean flag = false;
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static boolean deleteDirectory(String sPath) {
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		Boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {

			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;

		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public static String getJarPath(Class clazz, Integer getType) {
		String path = clazz.getProtectionDomain().getCodeSource().getLocation()
				.getFile();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (java.io.UnsupportedEncodingException ex) {
			return "";
		}
		java.io.File jarFile = new java.io.File(path);
		String jarName = jarFile.getName();
		String jarPath = "";
		java.io.File parent = jarFile.getParentFile();
		if (parent != null) {
			jarPath = parent.getAbsolutePath();
		}
		if (getType == 1) {
			return jarName;
		} else if (getType == 2) {
			return jarPath + "\\";
		} else {
			return jarPath + "\\" + jarName;
		}
	}

	public static String getJarPath() {
		String p = getJarPath(Log.class, 1).toLowerCase();
		if (p.indexOf(".jar") != -1) {
			return getJarPath(Log.class, 2);
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public static String getAppPath(Class cls) {
		if (cls == null)
			throw new java.lang.IllegalArgumentException("");
		ClassLoader loader = cls.getClassLoader();
		String clsName = cls.getName() + ".class";
		Package pack = cls.getPackage();
		String path = "";
		if (pack != null) {
			String packName = pack.getName();
			if (packName.startsWith("java.") || packName.startsWith("javax."))
				throw new java.lang.IllegalArgumentException("");
			clsName = clsName.substring(packName.length() + 1);
			if (packName.indexOf(".") < 0)
				path = packName + "/";
			else {
				int start = 0, end = 0;
				end = packName.indexOf(".");
				while (end != -1) {
					path = path + packName.substring(start, end) + "/";
					start = end + 1;
					end = packName.indexOf(".", start);
				}
				path = path + packName.substring(start) + "/";
			}
		}
		java.net.URL url = loader.getResource(path + clsName);
		String realPath = url.getPath();
		int pos = realPath.indexOf("file:");
		if (pos > -1)
			realPath = realPath.substring(pos + 5);
		realPath = realPath.substring(0, pos - 1);
		if (realPath.endsWith("!"))
			realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		try {
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return realPath;
	}

	public static boolean isNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj.toString());
	}

	@SuppressWarnings("rawtypes")
	public static String join2Str(Collection s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static Boolean isExtNameFile(String fileName, String extName) {
		if (fileName == null || extName == null) {
			return false;
		}
		fileName = fileName.toLowerCase().trim();
		extName = extName.toLowerCase().trim();
		if (fileName.equals("") || fileName.indexOf(".") == -1
				|| extName.equals("")) {
			return false;
		}
		extName = "," + extName + ",";
		String fExt = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();
		if (extName.toLowerCase().indexOf("," + fExt + ",") == -1) {
			return false;
		}
		return true;
	}

	public static String getWebRootPath() {
		String result = Tools.class.getResource("Tools.class").toString();
		int index = result.indexOf("WEB-INF");
		if (index == -1) {
			index = result.indexOf("bin");
		}
		result = result.substring(0, index);
		if (result.startsWith("jar")) {
			result = result.substring(10);
		} else if (result.startsWith("file")) {
			result = result.substring(6);
		}
		if (result.endsWith("/"))
			result = result.substring(0, result.length() - 1);
		return result;
	}

	public static String getFilePath(String path) {
		String p = getWebRootPath();
		Date d = new Date();
		p += "/" + path + "/" + Log.dateFormat2.format(d);
		d = null;
		File fp = new File(p);
		if (!fp.exists()) {
			fp.mkdirs();
		}
		fp = null;
		return p;
	}

	public static String getFileTempPath(String path) {
		String p = getWebRootPath();
		Date d = new Date(System.currentTimeMillis());
		p += "/" + path + "/temp/" + Log.dateFormat3.format(d);
		d = null;
		File fp = new File(p);
		if (!fp.exists()) {
			fp.mkdirs();
		}
		fp = null;
		return p;
	}

	public static String getCurrencyDriver() {
		String result = Tools.class.getResource("Tools.class").toString();
		if (result.startsWith("jar")) {
			result = result.substring(10, 12);
		} else if (result.startsWith("file")) {
			result = result.substring(6, 8);
		}
		return result;
	}

	public static String getImportCallBack(String data) {
		// data = data==null?"":Escape.escape1(data);
		return "<script>importCallback('" + data + "')</script>";
	}

	public static String getAlertCallBack(String data) {
		data = data == null ? "" : Escape.escape1(data);
		return "<script>alert(unescape('" + data + "'))</script>";
	}

	public static String getUpLoadTempPath() {
		String p = getWebRootPath();
		Date d = new Date(System.currentTimeMillis());
		p += "/upload/temp/" + Log.dateFormat3.format(d);
		d = null;
		File fp = new File(p);

		if (!fp.exists()) {
			fp.mkdirs();
		}
		fp = null;
		return p;
	}

	public static String getUpLoadPath() {
		String p = getWebRootPath();
		Date d = new Date();
		p += "/upload/" + Log.dateFormat2.format(d);
		d = null;
		File fp = new File(p);
		if (!fp.exists()) {
			fp.mkdirs();
		}
		fp = null;
		return p;
	}

	public static String getDownLoadPath() {
		String p = getWebRootPath();
		Date d = new Date();
		p += "/download/" + Log.dateFormat2.format(d);
		d = null;
		File fp = new File(p);
		if (!fp.exists()) {
			fp.mkdirs();
		}
		fp = null;
		return p;
	}

	public static Boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	

	public static HashMap<String, Object> decodeUrl(String url) {
		HashMap<String, Object> infoMap = new HashMap<String, Object>();
		String tmp = url;
		String[] arr = url.split("&");
		for (int i = 0; i < arr.length; i++) {
			tmp = arr[i];
			// Log.printLog(tmp);
			infoMap.put(tmp.substring(0, tmp.indexOf("=")),
					tmp.substring(tmp.indexOf("=") + 1, tmp.length()));
		}
		return infoMap;
	}

	public static boolean downPage(String sUrl, String fileName) {

		URL url = null;
		URLConnection conn = null;
		boolean bReadTimedOut = false;
		try {
			File f = new File(fileName);

			url = new URL(sUrl);

			conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			System.out.println("content length:" + conn.getContentLengthLong());
			System.out.println("content type:" + conn.getContentType());

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
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			bReadTimedOut = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}
	public static String XMLEscape(String xml){
		xml=xml.replace("&", "&amp;");
		xml=xml.replace("<", "&lt;");
		xml=xml.replace(">", "&gt;");
		xml=xml.replace("\"", "&quot;");
		xml=xml.replace("'", "&apos;");
		return xml;
	}
	public static double toDouble(Object o) {
		if (o == null)
			return 0;
		double d;
		try {
			d = Double.parseDouble(o.toString());

		} catch (Exception e) {
			return 0;
		}
		return d;
	}
}
