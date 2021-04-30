package com.yanxiu.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.yanxiu.common.HttpHelper;

import com.yanxiu.common.ResponseResult;
import com.yanxiu.common.TestCaseUtils;
import com.yanxiu.result.Report;
import com.yanxiu.result.ReportUtils;

public class TestCase {
	private ReportUtils utils = new ReportUtils();

	
	@BeforeTest
	public void setUp() throws Exception {
		utils.init();
	}

	@DataProvider(name = "paramsInfo")
	public Object[][] getParams() throws ClientProtocolException, IOException {

		List<String> configFileList = TestCaseUtils.getAllConfigFileList();

		List<Map<String, Object>> testcases = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < configFileList.size(); i++) {
			testcases.addAll(TestCaseUtils.loadYaml(configFileList.get(i)));
		}

		List<Object> infos = new ArrayList<Object>();
		Object[][] paramInfo = new Object[testcases.size()][5];

		for (int i = 0; i < testcases.size(); i++) {

			String name = (String) testcases.get(i).get("name");
			infos.add(name);
			String url = (String) testcases.get(i).get("uri");
			infos.add(url);
			String method = (String) testcases.get(i).get("method");
			infos.add(method);
			Map<String, String> param = (Map<String, String>) testcases.get(i).get("param");

			StringBuilder params = new StringBuilder();
			for (String key : param.keySet()) {
				params.append(key + "=" + String.valueOf(param.get(key)) + "&");
			}

			String token = "";
			Map<String, String> tokenInfo = (Map<String, String>) testcases.get(i).get("token");
			if (tokenInfo != null) {
				String loginName = String.valueOf(tokenInfo.get("loginName"));
				String password = String.valueOf(tokenInfo.get("password"));
				token = TestCaseUtils.getToken(loginName, password);
			}
			params.append("token=" + token);
			infos.add(params.toString());

			// System.out.println(testcases.get(i).get("expected"));
			Map<String, Object> exp = (Map<String, Object>) testcases.get(i).get("expected");
			JSONObject expected = new JSONObject(exp);
			infos.add(expected);

			for (int j = 0; j < infos.size(); j++) {

				paramInfo[i][j] = infos.get(j);
			}
			infos.clear();

		}

		return paramInfo;
	}

	// @DataProvider(name = "param")
	// public Object[][] getParam(){
	// List<String> configFileList = TestCaseUtils.getAllConfigFileList();
	//
	// List<Params> testcases = new ArrayList<Params>();
	// for (int i = 0; i < configFileList.size(); i++) {
	// testcases.addAll(TestCaseUtils.loadYamlFile(configFileList.get(i)));
	// }
	//
	// return new Object[][] {
	// new Object[] { testcases.toArray()}};
	//
	// }
	//
	// @Test(dataProvider = "param")
	// public void test(Params params){
	// String name = params.getName();
	// System.out.println("name:"+name);
	//
	// }

	@Test(dataProvider = "paramsInfo")
	public void testCase(String name, String url, String method, String param, JSONObject expected)
			throws JsonProcessingException, IOException, ProcessingException {

		if (method.equals("GET")) {
			ResponseResult responseResult = HttpHelper.doGet(url + "?" + param);
			if (responseResult.getStatus_code() == 200) {
				JSONObject actual = responseResult.getBody();

				ObjectMapper mapper = new ObjectMapper();
				JsonNode exp = mapper.readTree(expected.toString());
				JsonNode act = mapper.readTree(actual.toString());

				JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
				JsonSchema schema = factory.getJsonSchema(exp);
				ProcessingReport report;

				report = schema.validate(act);
				String testResult = report.isSuccess() ? "PASS" : "FAIL";

				utils.addResultToResultList(new Report(url, name, method, param, 200, testResult,
						report.isSuccess() ? "N/A" : report.toString()));
//				Assert.assertTrue(report.isSuccess(), report.toString());
			} else {
				utils.addResultToResultList(new Report(url, name, method, param, responseResult.getStatus_code(), "FAIL",
						"status code is not 200"));
			}
		}
	}

	@AfterTest
	public void tearDown() throws ResourceNotFoundException, ParseErrorException, Exception {
		utils.generateReport();
	}
}
