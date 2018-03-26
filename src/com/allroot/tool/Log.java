package com.allroot.tool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Log {
	private static String msgLogFileName = "../autologs/auto_msg";
	private static String sqlLogFileName = "../autologs/auto_sql";
	private static String errLogFileName = "../autologs/auto_err";
	private static int fileSize = 2048000;
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public final static SimpleDateFormat dateFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat dateFormat3 = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");
	private static int debug = 0;
	static {
		InputStream in = Util.class.getResourceAsStream("config.properties");
		Properties p = new Properties();

		try {
			p.load(in);
			debug = Integer.valueOf(p.getProperty("debug", "0")).intValue();
		} catch (IOException e) {

		}
	}

	public synchronized static void msgLog(String msg) {
		File f = null;
		FileWriter writer = null;
		try {
			f = new File(msgLogFileName + ".log");
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdir();
			}
			if (!f.exists()) {
				f.createNewFile();
			}

			writer = new FileWriter(f, true);
			writer.write("[" + dateFormat.format(new Date()) + "] " + msg
					+ "\r\n");
			writer.close();
			if (f.length() >= fileSize) {
				f.renameTo(new File(msgLogFileName + dFormat.format(new Date())
						+ ".log"));
			}
		} catch (IOException e) {

			System.out.println("MSG[" + msg + "]: " + e.getMessage());
			try {
				if (f.exists()) {
					f.renameTo(new File(msgLogFileName
							+ dFormat.format(new Date()) + ".log"));
				}
			} catch (Exception e1) {
			}
			// e.printStackTrace();
		} finally {
			writer = null;
			f = null;
			msg = null;
		}
	}

	public synchronized static void sqlLog(String sql) {
		File f = null;
		FileWriter writer = null;
		try {
			f = new File(sqlLogFileName + ".log");
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdir();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			writer = new FileWriter(f, true);
			writer.write("[" + dateFormat.format(new Date()) + "] " + sql
					+ "\r\n");
			writer.close();
			if (f.length() >= fileSize) {
				f.renameTo(new File(sqlLogFileName + dFormat.format(new Date())
						+ ".log"));
			}
		} catch (IOException e) {
			System.out.println("SQL[" + sql + "]: " + e.getMessage());
			try {
				if (f.exists()) {
					f.renameTo(new File(sqlLogFileName
							+ dFormat.format(new Date()) + ".log"));
				}
			} catch (Exception e1) {
			}

		} finally {
			writer = null;
			f = null;
			sql = null;
		}
	}

	public synchronized static void errLog(String msg) {
		File f = null;
		FileWriter writer = null;
		try {
			f = new File(errLogFileName + ".log");
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdir();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			writer = new FileWriter(f, true);

			writer.write("[" + dateFormat.format(new Date()) + "][ERROR] "
					+ msg + "\r\n");
			writer.close();
			if (f.length() >= fileSize) {
				f.renameTo(new File(errLogFileName + dFormat.format(new Date())
						+ ".log"));
			}
		} catch (IOException e) {
			System.out.println("ERROR[" + msg + "]: " + e.getMessage());
			try {
				if (f.exists()) {
					f.renameTo(new File(errLogFileName
							+ dFormat.format(new Date()) + ".log"));
				}
			} catch (Exception e1) {
			}

		} finally {
			writer = null;
			f = null;
			msg = null;
		}
	}

	public static void printLog(String msg) {
		if (debug == 1) {
			Log.msgLog(msg);
			System.out
					.println("[" + dateFormat.format(new Date()) + "] " + msg);
		}
		;
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

	// public static void webLog(AtWeblog log) {
	//
	// try {
	// AtWeblogImp awlIimp = new AtWeblogImp();
	// if (log != null) {
	// log.setLogtime(Timestamp.valueOf(Util.getCurentDate()));
	// awlIimp.create(log);
	// }
	// } catch (Exception e) {
	// Log.errLog("��־д�����:" + e.getMessage());
	// }
	// }

}
