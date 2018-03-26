package com.allroot.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.allroot.db.ConnDB;
import com.allroot.tool.JSON;
import com.allroot.tool.Tools;

public class AtWeblogImp {

	private String sortField;
	private String sortOrder;
	private Integer pageIndex;
	private Integer pageSize;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static boolean create(String uid, String uname, String modname,
			String logtype, String logs, String ip) {
		ArrayList<HashMap<String, Object>> arrayList = null;

		Object[] params = null;// 查询参数
		HashMap<String, Object> hashMap = null;
		try {
			params = new Object[] { uid, uname, modname, logtype, logs, ip };
			hashMap = createWeblog(params);
			if (hashMap != null) {

			} else {

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	public static HashMap<String, Object> createWeblog(Object[] params)
			throws Exception {
		try {
			return ConnDB.getSQLMapValue("AT_Web_P_createWeblog", params, true);
		} catch (Exception e) {
			throw e;
		}
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}