package com.allroot.db;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.allroot.tool.Tools;
import com.allroot.tool.Log;
import com.allroot.tool.Util;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0Pool {	
	private static String uName = "";
	private static String pwd = "";
	private static String drvName = "";
	private static String sqlUrl = "";
	public static ComboPooledDataSource ds = null;
	
	static {
		InputStream in = C3p0Pool.class.getResourceAsStream("db.properties");
		Properties p = new Properties();
		Connection conn = null;
		try {
			ds = new ComboPooledDataSource();
			p.load(in);
			uName = p.getProperty("username");
			pwd = p.getProperty("password");
			drvName =p.getProperty("driverClassName");
			sqlUrl = p.getProperty("url");
			ds.setJdbcUrl(sqlUrl);
			ds.setUser(uName);
			ds.setPassword(pwd);
			ds.setDriverClass(drvName);
			//ds.setInitialPoolSize(Util.strToInteger(p.getProperty("initialPoolSize")).intValue());
			//ds.setMaxPoolSize(Util.strToInteger(p.getProperty("maxPoolSize")).intValue());
			//ds.setAcquireIncrement(Util.strToInteger(p.getProperty("acquireIncrement")).intValue());
			//ds.setCheckoutTimeout(Util.strToInteger(p.getProperty("checkoutTimeout")).intValue());
//			ds.setMaxStatements(100);
			conn = ds.getConnection();
		    
		} catch (Exception e) {
			Log.printLog(Tools.toString(e.getMessage()));
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized static Connection getConnFromPool() throws Exception {
		return ds.getConnection();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println(getConnFromPool());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
