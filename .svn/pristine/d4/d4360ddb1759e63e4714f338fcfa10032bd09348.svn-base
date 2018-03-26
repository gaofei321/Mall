package com.allroot.db;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sourceforge.jtds.jdbc.ClobImpl;

import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.allroot.db.C3p0Pool;
import com.allroot.tool.Tools;
import com.allroot.tool.Log;

public class ConnDB {
	public ConnDB() {

	}

	// 从连接池中获取连接
	public static Connection getConn() throws Exception {
		return DruidPool.getConnFromPool();
	}

	public static PreparedStatement getPreSt(String sql) throws Exception {
		return getConn().prepareStatement(sql);
	}

	// 自定义连接参数
	public static Connection getConn(String ip, String port, String dbName,
			String uName, String pwd) throws Exception {
		Connection conn = null;
		String drvName = null;
		String sqlUrl = null;
		try {
			// Class.forName(strDriverClassName);
			drvName = "net.sourceforge.jtds.jdbc.Driver";
			sqlUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port
					+ ";DatabaseName=" + dbName;
			DriverManager.registerDriver((Driver) Class.forName(drvName)
					.newInstance());
			try {
				conn = DriverManager.getConnection(sqlUrl, uName, pwd);
			} catch (Exception ex) {
				Log.printLog("连接数据库出错: " + Tools.toString(ex.getMessage()));
				// ex.printStackTrace();
				// throw ex;
			}
		} catch (Exception e) {
			Log.printLog("加载数据库驱动出错: " + Tools.toString(e.getMessage()));
			// e.printStackTrace();
			throw e;
		} finally {
			drvName = null;
			sqlUrl = null;
		}
		return conn;
	}

	public static Connection getConn(HashMap<String, String> dbMap)
			throws Exception {
		Connection conn = null;
		String sqlUrl = null;
		String ip = dbMap.get("ip");
		String port = dbMap.get("port");
		String dbName = dbMap.get("dbname");
		String uName = dbMap.get("uname");
		String pwd = dbMap.get("pwd");
		String drvName = dbMap.get("drvname");
		try {
			drvName = "net.sourceforge.jtds.jdbc.Driver";
			sqlUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port
					+ ";DatabaseName=" + dbName;
			DriverManager.registerDriver((Driver) Class.forName(drvName)
					.newInstance());
			try {
				conn = DriverManager.getConnection(sqlUrl, uName, pwd);
			} catch (Exception ex) {
				Log.printLog("连接数据库出错: " + Tools.toString(ex.getMessage()));
				// ex.printStackTrace();
				throw ex;
			}
		} catch (Exception e) {
			Log.printLog("加载数据库驱动出错: " + Tools.toString(e.getMessage()));
			// e.printStackTrace();
			throw e;
		} finally {
			drvName = null;
			sqlUrl = null;
		}
		return conn;
	}

	// executeUpdate方法用于进行add或者update记录的操作
	// 入口参数为sql语句，成功返回true，否则为false
	public static int executeUpdate(String sql, Boolean isLog) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		int rowCount = 0;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rowCount = stmt.executeUpdate(sql);
		} catch (Exception ex) {
			rowCount = 0;
			Log.errLog("[executeUpdate][" + sql + "] 出错:" + ex.getMessage());
			Log.printLog("[executeUpdate]执行数据库更新出错: " + ex.getMessage());
			throw ex;
		} finally {
			if (isLog)
				Log.sqlLog("[executeUpdate]用时["
						+ (System.currentTimeMillis() - startTime) + "] " + sql);
			stmt.close();
			conn.close();
			stmt = null;
		}
		return rowCount;
	}

	public static ArrayList<HashMap<String, Object>> DBSelect(String sql,
			Boolean isLog) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(sql);
			ArrayList<HashMap<String, Object>> list = ResultSetToList(rst);
			rst.close();
			rst = null;
			return list;
		} catch (Exception e) {
			Log.errLog("[DBSelect][" + sql + "] 出错:" + e.getMessage());
			Log.printLog("[DBSelect]执行数据库出错: " + Tools.toString(e.getMessage()));
			throw e;
		} finally {
			if (isLog)
				Log.sqlLog("[DBSelect]["
						+ (System.currentTimeMillis() - startTime) + "] " + sql);
			stmt.close();
			conn.close();
			stmt = null;
		}
	}

	public static ArrayList<HashMap<String, Object>> DBSelect(String procedure,
			Object[] params, Boolean isLog) throws Exception {

		Connection conn = null;
		CallableStatement cst = null;
		ResultSet rst = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			cst = getCst(procedure, params, conn);
			cst.execute();
			rst = cst.getResultSet();
			if (rst != null)
				return ConnDB.ResultSetToList(rst);
			else
				return null;

		} catch (Exception e) {
			Log.errLog("[Call DBSelect]出错:" + Tools.toString(e.getMessage()));
			throw e;
		} finally {
			if (isLog)
				Log.sqlLog("[Call DBSelect]用时["
						+ (System.currentTimeMillis() - startTime) + "]");
			if (rst != null) {
				rst.close();
			}
			if (cst != null) {
				cst.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static ArrayList<HashMap<String, Object>> ResultSetToList(
			ResultSet rs) throws Exception {
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> rowData = null;
		try {
			while (rs.next()) {
				rowData = new HashMap<String, Object>(columnCount);
				for (int i = 1; i <= columnCount; i++) {
					Object v = rs.getObject(i);
					if (v != null
							&& (v.getClass() == Date.class || v.getClass() == java.sql.Date.class)) {
						Timestamp ts = rs.getTimestamp(i);
						v = new java.util.Date(ts.getTime());
						ts = null;
					} else if (v != null && v.getClass() == Boolean.class) {
						v = v.toString().toLowerCase().equals("true") ? 1 : 0;
					} else if (v != null && v.getClass() == Clob.class) {
						v = clob2String((Clob) v);
					} else if (v != null && v.getClass() == ClobImpl.class) {// c3p0支持varchar(max)
						v = clob2String((Clob) v);
					} else if (v != null && v.getClass() == ClobProxyImpl.class) {// druid支持varchar(max)
						v = clob2String((Clob) v);
					}
					rowData.put(md.getColumnName(i), v);
					v = null;
				}
				list.add(rowData);
				rowData = null;
			}
		} catch (Exception e) {
			Log.printLog("[ResultSetToList]出错:"
					+ Tools.toString(e.getMessage()));
			Log.errLog("[ResultSetToList]出错:" + Tools.toString(e.getMessage()));
			// e.printStackTrace();
			throw e;
		} finally {
			md = null;
			rowData = null;
		}
		return list;
	}

	private static String clob2String(Clob clob) throws Exception {
		return (clob != null ? clob.getSubString(1, (int) clob.length()) : null);
	}

	public static HashMap<String, Object> getRSValue(String sql)
			throws Exception {
		ArrayList<HashMap<String, Object>> data = null;
		HashMap<String, Object> d = null;
		try {
			data = DBSelect(sql, false);
			if (data != null) {
				d = data.size() > 0 ? (HashMap<String, Object>) data.get(0)
						: null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			data = null;
		}
		return d;
	}

	public static HashMap<String, Object> getGridData(String sql, Boolean isLog)
			throws Exception {
		// 支持sql返回2个数据集,第一个为数据,第二个为总记录数量 2012-12-24
		sql = sql == null ? "" : sql;
		if (sql.equals("")) {
			return null;
		} else
			Log.printLog(sql);
		Connection conn = null;
		PreparedStatement stmt1 = null;
		ResultSet rst = null;
		HashMap<String, Object> result = null;

		ArrayList<HashMap<String, Object>> dataAll = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			stmt1 = conn.prepareStatement(sql);
			stmt1.execute();
			rst = stmt1.getResultSet();
			dataAll = ConnDB.ResultSetToList(rst);

			result = new HashMap<String, Object>();
			result.put("data", dataAll);

			if (stmt1.getMoreResults()) {
				rst = stmt1.getResultSet();
				try {
					if (rst.next()) {
						result.put("total", rst.getInt(1));
					} else {
						result.put("total", dataAll.size());
					}
				} catch (Exception e) {
					result.put("total", dataAll.size());
				}
			}

			if (result.get("total") == null) {
				result.put("total", dataAll.size());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			Log.errLog("[getGridData][" + sql + "] 出错:"
					+ Tools.toString(e.getMessage()));
			Log.printLog("[getGridData]取网格数据出错:"
					+ Tools.toString(e.getMessage()));
			throw e;
		} finally {
			if (isLog)
				Log.sqlLog("[GridData]用时["
						+ (System.currentTimeMillis() - startTime) + "] " + sql);
			dataAll = null;
			rst.close();
			stmt1.close();
			conn.close();
			rst = null;
			stmt1 = null;
		}
		return result;
	}

	public static HashMap<String, Object> getGridData(String procedure,
			Object[] params, Boolean isLog) throws Exception {
		Connection conn = null;
		CallableStatement cst = null;
		HashMap<String, Object> result = null;
		ArrayList<HashMap<String, Object>> dataAll = null;
		ResultSet rst = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			cst = getCst(procedure, params, conn);
			cst.execute();
			try {
				rst = cst.getResultSet();
				dataAll = ConnDB.ResultSetToList(rst);

				result = new HashMap<String, Object>();
				result.put("data", dataAll);
				if (cst.getMoreResults()) {
					rst = cst.getResultSet();
					try {
						if (rst.next()) {
							result.put("total", rst.getInt(1));
						} else {
							result.put("total", dataAll.size());
						}
					} catch (Exception e) {
						result.put("total", dataAll.size());
					}
				}
				if (result.get("total") == null) {
					result.put("total", dataAll.size());
				}
			} catch (Exception e) {
				// e.printStackTrace();
				Log.errLog("取网格数据出错:" + e.getMessage());
				throw e;
			} finally {
				if (isLog)
					Log.sqlLog("[ExecuteCallableStatement]用时["
							+ (System.currentTimeMillis() - startTime) + "] ");
				dataAll = null;
				try {
					rst.close();
				} catch (Exception e) {
				}
			}
			return result;

		} finally {
			if (cst != null) {
				cst.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static String getSQLValue(String sql, Boolean isLog)
			throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String v = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs != null && rs.next()) {
				v = rs.getString(1);
				rs.close();
			}
		} catch (Exception e) {
			Log.errLog("[getSQLValue][" + sql + "] 出错:"
					+ Tools.toString(e.getMessage()));
			Log.printLog("[getSQLValue]执行数据库出错: "
					+ Tools.toString(e.getMessage()));
			throw e;
		} finally {
			if (isLog)
				Log.sqlLog("[GetSQLValue]用时["
						+ (System.currentTimeMillis() - startTime) + "] " + sql);
			stmt.close();
			conn.close();
			rs = null;
		}
		return v;
	}

	public static HashMap<String, Object> getSQLMapValue(String procedure,
			Object[] params, Boolean isLog) throws Exception {
		ArrayList<HashMap<String, Object>> dataAll = null;
		try {
			dataAll = DBSelect(procedure, params, isLog);
			if (dataAll != null && dataAll.size() > 0) {
				return dataAll.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.errLog("[Call GetSQLValue]出错:" + Tools.toString(e.getMessage()));
			dataAll = null;
			throw e;
		}
	}

	public static String getSQLStrValue(String procedure, Object[] params,
			Boolean isLog) throws Exception {
		Connection conn = null;
		CallableStatement cst = null;
		ResultSet rst = null;
		long startTime = System.currentTimeMillis();
		try {
			conn = getConn();
			cst = getCst(procedure, params, conn);
			cst.execute();
			rst = cst.getResultSet();
			if (rst != null && rst.next()) {
				return rst.getString(1);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.errLog("[Call getSQLStrValue]出错:"
					+ Tools.toString(e.getMessage()));
			throw e;
		} finally {
			if (isLog)
				Log.sqlLog("[Call getSQLStrValue]用时["
						+ (System.currentTimeMillis() - startTime) + "]");
			if (rst != null) {
				rst.close();
			}
			if (cst != null) {
				cst.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static void executeProcedure(String procedure, Object[] params,
			Boolean isLog) throws Exception {
		PreparedStatement pst = null;
		long startTime = System.currentTimeMillis();
		Connection conn = ConnDB.getConn();
		try {
			pst = getPst(procedure, params, conn);
			pst.execute();
			pst.close();
		} catch (Exception e) {
			Log.errLog("[executeProcedure]出错:[" + procedure + "]");
			throw e;
		} finally {
			if (isLog)
				Log.sqlLog("[ExecutePreparedStatement]用时["
						+ (System.currentTimeMillis() - startTime) + "]");
			conn.close();
		}
	}

	// 返回PreparedStatement
	public static PreparedStatement getPst(String procedure, Object[] params,
			Connection conn) throws Exception {
		PreparedStatement pst = null;
		int len = 0;
		String sql = "execute " + Tools.toString(procedure) + " ";
		StringBuilder sb = new StringBuilder();
		sb.append("execute ");
		sb.append(procedure + " ");
		if (params == null) {
			pst = conn.prepareStatement(sb.toString());
		} else {
			len = params.length;
			for (int i = 0; i < len; i++) {
				if (i != len - 1) {
					sb.append(" ?,");
				} else {
					sb.append("?");
				}
			}
			pst = conn.prepareStatement(sb.toString());
			for (int i = 1; i <= params.length; i++) {
				pst.setObject(i, params[i - 1]);
				sql += "'" + Tools.toString(params[i - 1]) + "',";
			}
			if (sql.endsWith(",")) {
				sql = sql.substring(0, sql.length() - 1);
			}
		}
		Log.sqlLog("[prepareStatement] " + sql);
		sql = null;
		sb = null;
		return pst;
	}

	// 返回CallableStatement
	public static CallableStatement getCst(String procedure, Object[] params,
			Connection conn) throws Exception {
		CallableStatement cst = null;
		int len = 0;
		String sql = "call " + Tools.toString(procedure) + " ";
		StringBuilder sb = new StringBuilder();
		sb.append("{call ");
		sb.append(procedure + " ");
		if (params == null) {
			sb.append("}");
			cst = conn.prepareCall(sb.toString());
		} else {
			sb.append("(");
			len = params.length;
			for (int i = 0; i < len; i++) {
				if (i != len - 1) {
					sb.append("?,");
				} else {
					sb.append("?");
				}
			}
			sb.append(")}");
			cst = conn.prepareCall(sb.toString());
			for (int i = 1; i <= params.length; i++) {
				cst.setObject(i, params[i - 1]);
				sql += "'" + Tools.toString(params[i - 1]) + "',";
			}
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		//Log.printLog("[CallableStatement] " + sql);
		return cst;
	}

	public static int DBExcuteSQL(String sql) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		try {
			int r = -1;
			conn = getConn();
			stmt = conn.createStatement();
			r = stmt.executeUpdate(sql);
			return r;
		} catch (Exception e) {
			throw e;
		} finally {
			stmt.close();
			conn.close();
			stmt = null;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println(getConn());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
