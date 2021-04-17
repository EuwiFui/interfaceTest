package com.yanxiu.test;

import java.io.File;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.examples.Utils;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import com.yanxiu.common.HttpHelper;
import com.yanxiu.common.ResponseResult;



//import org.skyscreamer.jsonassert.FieldComparisonFailure;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.skyscreamer.jsonassert.JSONCompare;
//import org.skyscreamer.jsonassert.JSONCompareMode;
//import org.skyscreamer.jsonassert.JSONCompareResult;

public class TestLogin {

	/*
	 * @Test public void testLoginWithCorrectInfo() throws
	 * ClientProtocolException, IOException, JSONException{ //String url =
	 * "http://u.yanxiu.com/test/login.json?password=123456&loginName=XY00273788%40yanxiu.com"
	 * ;
	 * 
	 * final String fileName = "test.yaml";
	 * 
	 * Yaml yaml = new Yaml(); List <Map<String,Object>> testcases = new
	 * ArrayList<Map<String,Object>>();
	 * 
	 * try { InputStream ios = new FileInputStream(new File(fileName));
	 * 
	 * // Parse the YAML file and return the output as a series of Maps and
	 * Lists
	 * 
	 * testcases = (List<Map<String, Object>>) yaml.load(ios);
	 * System.out.println(testcases.toString());
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * for(int i=0;i<testcases.size();i++){ String name = (String)
	 * testcases.get(i).get("name"); String url = (String)
	 * testcases.get(i).get("uri"); Map<String,String> param = (Map<String,
	 * String>) testcases.get(i).get("param"); StringBuilder params = new
	 * StringBuilder(); for (String key : param.keySet()) {
	 * params.append(key+"="+String.valueOf(param.get(key))+"&"); } String
	 * method = (String) testcases.get(i).get("method");
	 * System.out.println(testcases.get(i).get("expected")); Map<String,Object>
	 * exp = (Map<String, Object>) testcases.get(i).get("expected"); // String e
	 * = exp.replace("=", "=\"").replace(",", "\",").replace("}", "\"}"); // //
	 * System.out.println(e);
	 * 
	 * JSONObject expected = new JSONObject(exp); System.out.println(expected);
	 * System.out.println(String.valueOf(param.get("password")));
	 * if(method.equals("GET")){ JSONObject actual =
	 * HttpHelper.doGet(url+"?"+params); System.out.println(url+"?"+params);
	 * 
	 * System.out.println(actual); System.out.println(expected);
	 * JSONCompareResult result = JSONCompare.compareJSON(expected, actual,
	 * JSONCompareMode.LENIENT);
	 * 
	 * List<FieldComparisonFailure> failedFields = result.getFieldFailures();
	 * List<String> unmatchFields = new ArrayList<String>(); for(int
	 * j=0;j<failedFields.size();j++){ String field =
	 * failedFields.get(j).getField(); System.out.println(field);
	 * if(!isinIgnoreFields(field)){ unmatchFields.add(field); }
	 * 
	 * }
	 * 
	 * System.out.println(unmatchFields);
	 * Assert.assertTrue(unmatchFields.isEmpty(),
	 * "unmatch fields: "+unmatchFields.size()); }
	 * 
	 * }
	 */
	// String url =
	// "http://mobile.yanxiu.com/test/api/guopei/homeworkList?token=7a5a23273d46c18670fd63369b043b9c&pid=1789";
	// JSONObject actual = HttpHelper.doGet(url);
	// System.out.println(actual);
	// String str =
	// "{\"BaseBeanCreateTime\":1473305339967,\"code\":\"0\",\"debugDesc\":\"[debug_token:  http-nio-45901-exec-8-1473305339913-12_7_45901 ] �������ݳɹ� backen code[0]\",\"desc\":\"�������ݳɹ�\",\"body\":{\"BaseBeanCreateTime\":1473305339967,\"endDate\":\"2017-06-06\",\"stages\":[{\"id\":2227,\"name\":\"���㣡\",\"subject\":\"��һ�׶�\",\"homeworks\":[{\"id\":19164,\"title\":\"������ҵ1\",\"description\":\"������ҵ1\",\"createtime\":\"2016-07-04\",\"templateid\":\"132\",\"ismyrec\":0,\"homeworkid\":189058,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":1,\"detail\":null,\"type\":1,\"group\":0},{\"id\":19165,\"title\":\"������ҵ2\",\"description\":\"������ҵ2\",\"createtime\":\"2016-07-04\",\"templateid\":\"132\",\"ismyrec\":1,\"homeworkid\":188983,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":1,\"detail\":null,\"type\":1,\"group\":0},{\"id\":19230,\"title\":\"��ͨ��Ƶ\",\"description\":\"���Ǹ���ͨ��Ƶ\",\"createtime\":\"2016-08-05\",\"templateid\":\"324\",\"ismyrec\":0,\"homeworkid\":189066,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":1,\"detail\":null,\"type\":2,\"group\":0},{\"id\":19231,\"title\":\"��ʱ��Ҫ�����Ƶ\",\"description\":\"�������Ƶ\",\"createtime\":\"2016-08-05\",\"templateid\":\"379\",\"ismyrec\":0,\"homeworkid\":0,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":0,\"detail\":null,\"type\":3,\"group\":0}]},{\"id\":2228,\"name\":\"�룡\",\"subject\":\"�ڶ��׶�\",\"homeworks\":null},{\"id\":2229,\"name\":\"����\",\"subject\":\"�����׶�\",\"homeworks\":[{\"id\":19166,\"title\":\"������ҵ3\",\"description\":\"������ҵ3\",\"createtime\":\"2016-07-04\",\"templateid\":\"132\",\"ismyrec\":0,\"homeworkid\":188984,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":1,\"detail\":null,\"type\":1,\"group\":0},{\"id\":19167,\"title\":\"������ҵ4\",\"description\":\"������ҵ4\",\"createtime\":\"2016-07-04\",\"templateid\":\"132\",\"ismyrec\":0,\"homeworkid\":188985,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":1,\"detail\":null,\"type\":1,\"group\":0}]},{\"id\":2230,\"name\":\"�����ܽ�\",\"subject\":\"�����ܽ�\",\"homeworks\":[{\"id\":19168,\"title\":\"�����ܽ�\",\"description\":\"�ϴ�����\",\"createtime\":\"2016-07-04\",\"templateid\":\"165\",\"ismyrec\":0,\"homeworkid\":189057,\"recommend\":0,\"score\":\"0\",\"endDate\":\"2017-06-06\",\"isFinished\":1,\"detail\":null,\"type\":1,\"group\":0}]}]}}";
	// JSONObject expected = new JSONObject(str);
	//
	// JSONCompareResult result = JSONCompare.compareJSON(expected, actual,
	// JSONCompareMode.LENIENT);
	//
	// List<FieldComparisonFailure> failedFields = result.getFieldFailures();
	// List<String> unmatchFields = new ArrayList<String>();
	// for(int i=0;i<failedFields.size();i++){
	// String field = failedFields.get(i).getField();
	// System.out.println(field);
	// if(!isinIgnoreFields(field)){
	// unmatchFields.add(field);
	// }
	//
	// }

	// Assert.assertTrue(unmatchFields.isEmpty(),
	// "unmatch fields: "+unmatchFields.size());

	// JSONAssert.assertEquals(expected,result,false);

	// String code = result.getString("code");
	// JSONObject body = result.getJSONObject("body");
	// System.out.println("code:"+code);
	// String endDate = body.getString("endDate");
	// System.out.println("endDate:"+endDate);
	//
	// JSONArray stages = body.getJSONArray("stages");
	// // System.out.println(stages);
	//
	// for(int i=0;i<stages.length();i++){
	// JSONObject stage = new JSONObject(stages.getString(i));
	// System.out.println(stage);
	// String subject = stage.getString("subject");
	// String name = stage.getString("name");
	// String id = stage.getString("id");
	// System.out.println("subject:"+subject+"name:"+name+"id:"+id);
	// try{
	// JSONArray homeworks = stage.getJSONArray("homeworks");
	//
	// for(int j=0;j<homeworks.length();j++){
	// JSONObject homework = new JSONObject(homeworks.getString(j));
	// String createtime = homework.getString("createtime");
	// String enddate = homework.getString("endDate");
	// String description = homework.getString("description");
	// String recommend = homework.getString("recommend");
	// String title = homework.getString("title");
	// String type = homework.getString("type");
	// String score = homework.getString("score");
	//
	// System.out.println(createtime + enddate + description +
	// recommend+title+type+score);
	// }
	// }catch(JSONException e){
	// System.out.println(stage.getString("homeworks"));
	// }
	// }

	// }

	// public boolean isinIgnoreFields(String field) {
	// List<String> ignoreFields = new ArrayList<String>();
	// ignoreFields.add("BaseBeanCreateTime");
	// ignoreFields.add("body.BaseBeanCreateTime");
	// ignoreFields.add("debugDesc");
	// ignoreFields.add("debug");
	// for (int i = 0; i < ignoreFields.size(); i++) {
	// if (field.equals(ignoreFields.get(i)))
	// return true;
	// }
	// return false;
	// }

	@DataProvider(name = "loginInfo")
	public Object[][] getLgoinInfo() {
		final String fileName = "/test.yaml";
		
		Yaml yaml = new Yaml();
		List<Map<String, Object>> testcases = new ArrayList<Map<String, Object>>();

		try {
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

	@Test(dataProvider = "loginInfo")
	public void testLogin(String name, String url, String method, String param,
			JSONObject expected) throws ClientProtocolException, IOException,
			JSONException, InterruptedException, ProcessingException {
		
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
			Assert.assertTrue(report.isSuccess(), report.toString());

			// JSONCompareResult result = JSONCompare.compareJSON(expected,
			// actual, JSONCompareMode.LENIENT);
			//
			// List<FieldComparisonFailure> failedFields =
			// result.getFieldFailures();
			// List<String> u.nmatchFields = new ArrayList<String>();
			// for(int j=0;j<failedFields.size();j++){
			// String field = failedFields.get(j).getField();
			// // System.out.println(field);
			// if(!isinIgnoreFields(field)){
			// unmatchFields.add(field);
			// }
			//
			// }
			//
			// // System.out.println(unmatchFields);
			// Assert.assertEquals(responseResult.getStatus_code(), 200);
			// Assert.assertTrue(unmatchFields.isEmpty(),
			// "unmatch fields: "+unmatchFields.toString());

		}

	}

	// @AfterMethod
	// public void tearDown() throws IOException {
	//
	// }
}
