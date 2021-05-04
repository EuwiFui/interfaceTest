package com.yanxiu.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;



public class TestCaseUtils {

	public static List<String> getAllConfigFileList(){
		
		List<String> configFileList = new ArrayList<String>();

		String configFilePath = TestCaseUtils.class.getClassLoader().getResource("").getPath();
		File configFileDir = new File(configFilePath);
		String[] allFileNames = configFileDir.list();
		
	
		for(int i=0;i<allFileNames.length;i++){
			String prefix=allFileNames[i].substring(allFileNames[i].lastIndexOf(".")+1);
			if(prefix.equalsIgnoreCase("yaml")){
			configFileList.add(allFileNames[i]);
			}
		}
		System.out.println(configFileList);
		return configFileList;
	}
	
	
	public static List<Map<String, Object>> loadYaml(String filename){
		
		filename = "/"+filename;
			Yaml yaml = new Yaml();
		List<Map<String, Object>> testcases = new ArrayList<Map<String, Object>>();
		try{
			InputStream ios = TestCaseUtils.class.getResourceAsStream(filename);
			testcases = (List<Map<String, Object>>) yaml.load(ios);
		}catch(Exception e){
			e.printStackTrace();
		}
		return testcases;
	}
	
	
	public static List<Params> loadYamlFile(String filename){
		filename = "/"+filename;
		Yaml yaml = new Yaml();
		
		List<Params> testcases = new ArrayList<Params>();
		try{
			InputStream ios = TestCaseUtils.class.getResourceAsStream(filename);
			testcases = (List<Params>) yaml.load(ios);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return testcases;
	}
	
	public static String getToken(String loginName,String password) throws ClientProtocolException, IOException{
		String token = "";
		String url = "http://u.yanxiu.com/login.json";
		ResponseResult responseResult = HttpHelper.doGet(url + "?loginName=" + loginName+"&password="+password);
		JSONObject body = responseResult.getBody();
		
		token = body.getString("token");
		return token;
	}
	
	public static JSONObject modifyExpected(JSONObject expected,String dependency,String token) throws ClientProtocolException, IOException{
		JSONObject modifiedExpected = null;
		Dependency de = new Dependency();
		
		if(dependency.equals("projectId")){
			List<String> pids = de.getProjectID(token);
			String replacePid = "\"enum\": [";
			for(int i=0;i<pids.size();i++){
				replacePid+="\""+pids.get(i)+"\",";
			}
			replacePid+="null,\"0\"],";
			System.out.println("@@@@@@@@"+replacePid);
			String str = expected.toString().replace("\"projectId\":{", "\"projectId\":{"+replacePid);
			
			System.out.println("@@@@@@@@"+str);
			modifiedExpected = new JSONObject(str);
		}
		System.out.println("#######3"+modifiedExpected);
		return modifiedExpected;
	}
	
	
}
