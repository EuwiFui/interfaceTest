package com.yanxiu.common;


	import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

	import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

	public class HttpHelper {
//		private static CloseableHttpClient httpclient = null;
//		private HttpPost post = null;
//		private HttpGet get = null;

//		public static CloseableHttpClient getInstance(){
//			if(httpclient==null){
//				httpclient = HttpClients.createDefault();
//			}
//			return httpclient;
//		}
		public static ResponseResult doGet(String url) throws ClientProtocolException, IOException{
			List<BasicNameValuePair> headers = new ArrayList<>();
			return doGet(url,headers);
		}
		public static ResponseResult doGet(String url,List<BasicNameValuePair> headers) throws ClientProtocolException, IOException{
			HttpGet get = new HttpGet(url);
			ResponseResult responseResult = new ResponseResult();
			for(BasicNameValuePair header:headers){
				get.setHeader(header.getName(), header.getValue());
			}
			CloseableHttpClient client = HttpClients.createDefault();
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(2000).build();
//			get.setConfig(requestConfig);
//			
			CloseableHttpResponse response = client.execute(get);
			JSONObject result = null;
			responseResult.setStatus_code(response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String ret = EntityUtils.toString(response.getEntity());
				
				try {
					result = new JSONObject(ret);
					responseResult.setBody(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.close();
				client.close();
				
			}

			
					
			return responseResult;
		}
//		public HttpHelper(String url, String method) {
//			if (method.equals("GET")) {
//				get = new HttpGet(url);
//
//			} else if (method.equals("POST")) {
//				post = new HttpPost(url);
//			}
//
////			httpclient = HttpClients.createDefault();
//
//		}

//		public void setHeaders(Map<String, String> headers) {
//			if (get != null) {
//				for (String key : headers.keySet()) {
//					get.setHeader(key, headers.get(key));
//				}
//			} else if (post != null) {
//				for (String key : headers.keySet()) {
//					post.setHeader(key, headers.get(key));
//				}
//			}
//
//		}

//		public String execute() throws ClientProtocolException, IOException {
//			HttpResponse response = null;
//			if (post != null)
//				response = httpclient.execute(post);
//			else if (get != null)
//				response = httpclient.execute(get);
//	        return EntityUtils.toString(response.getEntity(), "utf-8");
//		}
		
//		public static  void closeConnection() throws IOException{
//			httpclient.close();
//		}

	}

