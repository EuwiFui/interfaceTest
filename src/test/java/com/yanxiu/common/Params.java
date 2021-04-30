package com.yanxiu.common;

import org.json.JSONObject;

public class Params {
	private String name;
	private String url;
	private String method;
	private String param;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public JSONObject getExpected() {
		return expected;
	}

	public void setExpected(JSONObject expected) {
		this.expected = expected;
	}

	private JSONObject expected;
	
	public Params(String name,String url,String method,String param,JSONObject expected){
		this.name = name;
		this.url = url;
		this.method = method;
		this.param = param;
		this.expected = expected;
	}
}
