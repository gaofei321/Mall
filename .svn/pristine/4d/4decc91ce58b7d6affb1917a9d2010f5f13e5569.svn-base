package com.allroot.tool;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpParameters {
	//获取所有请求属性
 public static HashMap<String, String> GetAllHttpParameters(HttpServletRequest request,Boolean IsNew) throws UnsupportedEncodingException{
	HashMap<String, String> list=new HashMap<String, String>();
	request.setCharacterEncoding("UTF-8");
	if (IsNew) {
 	Iterator<Map.Entry<String, String[]>>  it = request.getParameterMap().entrySet().iterator();  
     while(it.hasNext()) {  
     	Map.Entry<String, String[]> entry = it.next(); 
        String ParameterName = entry.getKey(); 
        String[] ParameterValues=entry.getValue();
        for (int i = 0; i < ParameterValues.length; i++) {
			String ParameterValue=ParameterValues[i];
			list.put(ParameterName, ParameterValue);	
		} 
	}
	}
	else{
		Enumeration ParameterNames=request.getParameterNames();
		while (ParameterNames.hasMoreElements()) {
			String ParameterName = (String) ParameterNames.nextElement();
			String[] ParameterValues=request.getParameterValues(ParameterName);
			for (int i = 0; i < ParameterValues.length; i++) {
				String ParameterValue=ParameterValues[i];
				list.put(ParameterName, ParameterValue);	
			}
			
		}	
		
	}
	 return list; 
 }
 public static HashMap<String, String> GetAllHttpParameters(HttpServletRequest request) throws UnsupportedEncodingException{
   return GetAllHttpParameters(request, true);	 
	 
 }
}

