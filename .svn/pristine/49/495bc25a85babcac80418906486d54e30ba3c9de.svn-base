package com.allroot.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

//启用加密,需增加相应配置
//java -cp druid-1.0.2.jar com.alibaba.druid.filter.config.ConfigTools you_password
//connectionProperties=config.decrypt=true
//filters:stat,wall,config

public class DruidPool {
	private static  DataSource dbcpSource;
	private final static String dbConfigFile = "db.properties";
	
	static{
		InputStream in=null;
		in = DruidPool.class.getClassLoader().getResourceAsStream(dbConfigFile);
		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			////Log.printLog("加载数据库连接配置出错:"+Tools.toString(e.getMessage()));
		}finally{
			in = null;
		}
		try {
			dbcpSource = DruidDataSourceFactory.createDataSource(p);
		} catch (Exception e) {
			////Log.printLog("初始连接池出错:"+Tools.toString(e.getMessage()));
		}finally{
			p = null;
		}
		//初始和测试连接
		Connection conn = null;
		try {
			conn = dbcpSource.getConnection();			
		} catch (SQLException e) {
			e.printStackTrace();
			////Log.printLog("建立数据库连接出错:"+Tools.toString(e.getMessage()));
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}				
	}
	public synchronized static Connection getConnFromPool() throws Exception {
		return dbcpSource.getConnection();
	}
}
