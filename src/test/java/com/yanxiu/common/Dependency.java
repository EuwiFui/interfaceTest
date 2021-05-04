package com.yanxiu.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Dependency {
	private List<String> projectID = new ArrayList<String>();
	private final String trainListUrl = "http://mobile.yanxiu.com/v20/api/guopei/trainlist";
	
	public List<String> getProjectID(String token) throws ClientProtocolException, IOException{
		ResponseResult result = HttpHelper.doGet(trainListUrl+"?token="+token);
		
		JSONObject responseBody = result.getBody();
		JSONObject body = responseBody.getJSONObject("body");
		JSONArray trains = body.getJSONArray("trains");
		for(int i=0;i<trains.length();i++){
			JSONObject train = trains.getJSONObject(i);
			String pid = train.getString("pid");
			projectID.add(pid);
		}
		return projectID;
	}	
}
