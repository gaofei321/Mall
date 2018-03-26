package com.allroot.tool;

public class MyErrors {
	private String SuccessStr;//正确的返回信息
	private String ErrorStr;//返回的错误信息
	private Boolean SuccessFlag;//返回是否正确
	public MyErrors() {
		this.ErrorStr="";
		this.SuccessStr="";
	}
	public String getSuccessStr() {
		return SuccessStr;
	}
	public MyErrors(String successStr, String errorStr, Boolean successFlag) {
		super();
		SuccessStr = successStr;
		ErrorStr = errorStr;
		SuccessFlag = successFlag;
	}
	public void setSuccessStr(String successStr) {
		SuccessStr = successStr;
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

}
