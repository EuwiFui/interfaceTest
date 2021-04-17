package com.yanxiu.test;

import java.io.IOException;
import java.io.StringWriter;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.yanxiu.common.HttpHelper;
import com.yanxiu.common.ResponseResult;
import com.yanxiu.result.Report;

public class TestTrainList extends BaseCase {

	@BeforeClass
	public void setUp(){
		System.out.println("set file name");
		super.setFileName("/trainlist.yaml");
	}
	
	@Test(dataProvider = "paramInfo")
	public void testTrainList(String name, String url, String method, String param,
			JSONObject expected) throws Exception {
		
		if (method.equals("GET")) {
			
			ResponseResult responseResult = HttpHelper.doGet(url + "?" + param);
			JSONObject actual = responseResult.getBody();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode exp = mapper.readTree(expected.toString());
			JsonNode act = mapper.readTree(actual.toString());
			
			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			// ProcessingReport re =
			// factory.getSyntaxValidator().validateSchema(exp);
			// System.out.println(re);
			JsonSchema schema = factory.getJsonSchema(exp);
			ProcessingReport report;
			
			
			report = schema.validate(act);
			String testResult = report.isSuccess()?"PASS":"FAIL";
			 result.add(new Report(name,testResult,report.isSuccess()?"":report.toString()));
			Assert.assertTrue(report.isSuccess(), report.toString());
		   
		   
		}
		

	}
	
}
