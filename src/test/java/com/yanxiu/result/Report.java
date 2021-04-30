package com.yanxiu.result;

public class Report {
	private String url;
	private String name;
	private String params;
	private String method;
	private int statusCode;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	private String result;
	private String reason;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Report(String url,String name,String method,String param,int statusCode,String result,String reason){
		this.url = url;
		this.name = name;
		this.result = result;
		this.reason = reason;
		this.method = method;
		this.params = param;
		this.statusCode = statusCode;
	}

}
