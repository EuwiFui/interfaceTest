package com.yanxiu.common;

import org.json.JSONObject;

public class ResponseResult {
	private int status_code;
	private JSONObject body;
	public int getStatus_code() {
		return status_code;
	}
	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}
	public JSONObject getBody() {
		return body;
	}
	public void setBody(JSONObject body) {
		this.body = body;
	}
	
	
}
