package com.yanxiu.result;

public class Report {
	private String name;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Report(String name,String result,String reason){
		this.name = name;
		this.result = result;
		this.reason = reason;
	}

}
