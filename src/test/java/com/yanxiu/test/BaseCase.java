package com.yanxiu.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.yanxiu.common.HttpHelper;
import com.yanxiu.common.ResponseResult;
import com.yanxiu.result.Report;

public class BaseCase {
	
	private String fileName;
	protected static List<Report> result = new ArrayList<Report>();
	private static VelocityEngine ve;
	private static Template t;
	private static VelocityContext context;
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@BeforeTest
	public void setUp() throws Exception{
		System.out.println("this is in before test");
	    ve = new VelocityEngine();
		ve.init();
		t = ve.getTemplate("report.vm"); 
		context = new VelocityContext();
	}
	
	@DataProvider(name = "paramInfo")
	public Object[][] getLgoinInfo() throws ClientProtocolException, IOException {
//		final String fileName = "/test.yaml";
		
		Yaml yaml = new Yaml();
		List<Map<String, Object>> testcases = new ArrayList<Map<String, Object>>();

		try {
			System.out.println(fileName);
//			InputStream ios = new FileInputStream(new File(fileName));
			InputStream ios = TestLogin.class.getResourceAsStream(fileName);
			// Parse the YAML file and return the output as a series of Maps and
			// Lists

			testcases = (List<Map<String, Object>>) yaml.load(ios);
			// System.out.println(testcases.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Object> infos = new ArrayList<Object>();
		Object[][] loginInfo = new Object[testcases.size()][5];

		for (int i = 0; i < testcases.size(); i++) {

			String name = (String) testcases.get(i).get("name");
			infos.add(name);
			String url = (String) testcases.get(i).get("uri");
			infos.add(url);
			String method = (String) testcases.get(i).get("method");
			infos.add(method);
			Map<String, String> param = (Map<String, String>) testcases.get(i)
					.get("param");

			StringBuilder params = new StringBuilder();
			for (String key : param.keySet()) {
				params.append(key + "=" + String.valueOf(param.get(key)) + "&");
			}
			
			String token = "";
			Map<String, String> tokenInfo = (Map<String, String>) testcases.get(i)
					.get("token");
			if(tokenInfo!=null){
				String loginName = String.valueOf(tokenInfo.get("loginName"));
				String password = String.valueOf(tokenInfo.get("password"));
				token = getToken(loginName,password);
			}
			params.append("token="+token);
			infos.add(params.toString());

			// System.out.println(testcases.get(i).get("expected"));
			Map<String, Object> exp = (Map<String, Object>) testcases.get(i)
					.get("expected");
			JSONObject expected = new JSONObject(exp);
			infos.add(expected);
			

			
			
			for (int j = 0; j < infos.size(); j++) {

				loginInfo[i][j] = infos.get(j);
			}
			infos.clear();

		}
		return loginInfo;
	}
	
	public String getToken(String loginName,String password) throws ClientProtocolException, IOException{
		String token = "";
		String url = "http://u.yanxiu.com/login.json";
		ResponseResult responseResult = HttpHelper.doGet(url + "?loginName=" + loginName+"&password="+password);
		JSONObject body = responseResult.getBody();
		System.out.println(body);
		token = body.getString("token");
		return token;
	}
	
	
	@AfterTest
	public void tearDown() throws ResourceNotFoundException, ParseErrorException, Exception{
		
        System.out.println("this is after test");
		context.put("objs", result); 


		StringWriter writer = new StringWriter();
		//转换输出 
		t.merge(context, writer); 
		  File resultFile =new File("TestResult.html");
		  if(!resultFile.exists()){
			  resultFile.createNewFile();
		  }
		  FileWriter fileWritter = new FileWriter(resultFile.getName(),true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(writer.toString());
        bufferWritter.close();
		System.out.println(writer.toString());

	}
}
