package com.allroot.tool;

import java.util.ArrayList;
import java.util.HashMap;

import com.allroot.db.ConnDB;
import com.allroot.tool.Tools;



public class SysConfig {
	
	public static String getSysConfig(String configID,String defaultValue){
		String sql = "select id,name,value from at_sysconfig where enable = 1 and id='" + Tools.toDBString(configID) + "'";
		ArrayList<HashMap<String, Object>> list = null;
		try {
			list = ConnDB.DBSelect(sql,false);
			if (list == null || list.size() == 0) {
				return defaultValue;
			} else {
				HashMap<String, Object> m = list.get(0);
				return Tools.toString(m.get("value"));
			}
		} catch (Exception e) {
			return defaultValue;
		} finally {
			sql = null;
			list = null;
		}
	}
}
