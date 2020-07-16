package com.chen.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * HTTP协议调度方法
 * 
 * @param type        请求方式（post、get、patch...）
 * @param contentType 接口参数类型(json、form、xml...)
 * @param url         接口请求地址（如果get，url必须加上参数）
 * @param params      接口请求参数（必须是json格式）
 * @param  headers    接口请求头
 * @return
 */

public class HttpUtils {
	//抽取call方法
	public static HttpResponse call(String type, String contentType,
			String url, String params, Map<String,String> headers) throws Exception {
		HttpResponse body = null;
		   // 如果是post请求
		if ("post".equalsIgnoreCase(type) ) { //equalsIgnoreCase这个可以忽略大小写
			// 如果是json格式的参数
			if ("json".equalsIgnoreCase(contentType)) {
				body = HttpUtils.post(url, params,headers);
				// 如果是form格式的参数
			} else if ("form".equalsIgnoreCase(contentType)) {
				params = HttpUtils.json2KeyValue(params,headers);// 调用json2KeyValue方法
				body = HttpUtils.formPost(url, params,headers);
			}
			// 如果是get请求
		} else if ("get".equalsIgnoreCase(type)) {
			// get请求不分json和form
			// get请求需要url拼接params的2种方式： url/xxx/xxx 或 url？key=value&key=value

			body = HttpUtils.get(url,headers);
			// 如果是patch请求
		} else if ("patch".equalsIgnoreCase(type)) {
			body = HttpUtils.patch(url, params,headers);
		}
		return body;
	}

//	public static void main(String[] args) {
//		String json = "{\"mobilephone\":\"13877783311\",\"pwd\":\"12345678\"}";
//
//		System.out.println(json2KeyValue(json));
//	}
	/**
	 * json格式参数转成form格式参数
	 * @param json json格式参数
	 * @param  headers    接口请求头
	 * @return form格式参数
	 */
	public static String json2KeyValue(String json, Map<String,String> headers) {
		// 判断传入字符串是否为空
		if (StringUtils.isEmpty(json)) {
			return "";
		}
		// json转成map
		// Map<String,Object>:键已经确定是字符串类型（json是字符串类型），value不知道是什么类型就给Object类型
		Map<String, Object> map = JSONObject.parseObject(json, Map.class);
		String result = "";// 定义一个返回值
		// 获取map中所有key，并循环遍历（map用keyset来循环）
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			// key1=value1&key2=value2
			Object value = map.get(key);
			// 结果的拼接
			result += key + "=" + value + "&";
		}
		// 去除最后一个多余的&符号
		result = result.substring(0, result.length() - 1);
		return result;
	}

	public static HttpResponse formPost(String url, String params, Map<String,String> headers) throws Exception {
		/**
		 * 发送post请求
		 * 
		 * @param url    请求地址
		 * @param params form类型请求参数
		 * @param  headers    接口请求头
		 * @return 响应体
		 * @throws Exception
		 */
		// 1+2+3 HttpPost是一个请求对象
		HttpPost post = new HttpPost(url);// "http://api.lemonban.com/futureloan/member/login"
		// 4、如果有请求头填写请求头
		//post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		setHeaders(headers, post);
		// 5、如果有参数填写参数
		// String json = "{\"mobile_phone\": \"13762312765\",\"pwd\": \"123123123\"}";
		// StringEntity requestBody = new StringEntity(json, "utf-8");
		// 匿名对象
		post.setEntity(new StringEntity(params, "UTF-8"));
		// 6、创建客户端
		CloseableHttpClient client = HttpClients.createDefault();
		// 点击发送按钮,获取响应
		CloseableHttpResponse response = client.execute(post);
		return response;
		// 8、格式化响应,并返回
		//return getResponseBody(response);
	}


	public static HttpResponse get(String url, Map<String,String> headers) throws Exception {
		// 静态方法的作用：直接可以用类名调用，省去创建对象的步骤
		/**
		 * 发送get请求
		 * 
		 * @param url 请求地址+参数
		 * @param  headers    接口请求头
		 * @return 响应体
		 * @throws Exception
		 */
		// 1+2+3 创建请求 、选择请求方法和填写url HttpGet是一个请求对象
		HttpGet get = new HttpGet(url);// "http://api.lemonban.com/futureloan/loans"
		// 4如果有请求头填写请求头 5、省略
		//get.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
		setHeaders(headers, get);
		// 6、创建客户端
		
		CloseableHttpClient client = HttpClients.createDefault();
		// 点击发送按钮,获取响应
		CloseableHttpResponse response = client.execute(get);
		return response;
		// 8、格式化响应 并返回 allHeaders是一个工具类
		// 封装方法的快捷键：alt + shift + m
		//return getResponseBody(response);
	}

	public static HttpResponse post(String url, String params, Map<String,String> headers) throws Exception {
		/**
		 * 发送post请求
		 * 
		 * @param url    请求地址
		 * @param params json类型请求参数
		 * @param  headers    接口请求头
		 * @return 响应体
		 * @throws Exception
		 */
		// 1+2+3 HttpPost是一个请求对象
		HttpPost post = new HttpPost(url);// "http://api.lemonban.com/futureloan/member/login"
		// 4、如果有请求头填写请求头
//		post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		post.setHeader("Content-Type", "application/json");
		setHeaders(headers, post);
		// 5、如果有参数填写参数
		// String json = "{\"mobile_phone\": \"13762312765\",\"pwd\": \"123123123\"}";
		// StringEntity requestBody = new StringEntity(json, "utf-8");
		// 匿名对象
		post.setEntity(new StringEntity(params, "UTF-8"));
		// 6、创建客户端
		CloseableHttpClient client = HttpClients.createDefault();
		// 点击发送按钮,获取响应
		CloseableHttpResponse response = client.execute(post);
		return response;
		// 8、格式化响应,并返回
		//return getResponseBody(response);
	}

	public static HttpResponse patch(String url, String params1, Map<String,String> headers) throws Exception {
		// "http://api.lemonban.com/futureloan/member/update"
		/**
		    * 发送patch请求* 
		 * @param url     请求地址
		 * @param params1 json 类型请求参数
		 * @param  headers    接口请求头
		 * @return 响应体
		 * @throws Exception
		 */
		// 1+2+3 创建请求 、选择请求方法和填写url HttpGet是一个请求对象
		HttpPatch patch = new HttpPatch(url);// "http://api.lemonban.com/futureloan/member/update"
		// 4如果有请求头填写请求头 5、省略
//		patch.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		patch.setHeader("Content-Type", "application/json");
		setHeaders(headers, patch);
		// 5、如果有参数填写参数
		// String json = "{\"member_id\": 187, \"reg_name\": \"檬檬\"}";
		// StringEntity requestBody = new StringEntity(json, "utf-8");
		// 匿名对象
		patch.setEntity(new StringEntity(params1, "UTF-8"));
		// 6、创建客户端(postman本来就是一个客户端，所以不用创建) 发送请求就必须有个客户端
		CloseableHttpClient client = HttpClients.createDefault();
		// 点击发送按钮,获取响应
		CloseableHttpResponse response = client.execute(patch);
		return response;
		//return getResponseBody(response);
	}

	/**
	  * 打印响应头、响应体、状态码，并且返回响应体。 
	 * @param response 响应对象
	 * @param  headers    接口请求头
	 * @return 响应体
	 * @throws IOException
	 */
	public static String getResponseBody(HttpResponse response) throws IOException {
		// 获取所有头
		Header[] allHeaders = response.getAllHeaders();
		// 获取响应体
		HttpEntity entity = response.getEntity();
		// 获取状态码//链式编程 偷懒
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(Arrays.toString(allHeaders));
		String body = EntityUtils.toString(entity);
		// 格式化json字符串
		body = jsonFormat(body);
		System.out.println(body);
		System.out.println(statusCode);
		return body;

	}

	/**
	  * 格式化json字符串* 
	 * @param   jsonString 需要被格式化的字符串
	 * @return
	 */
	public static String jsonFormat(String jsonString) {
		JSONObject object = JSONObject.parseObject(jsonString);
		jsonString = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteDateUseDateFormat);
		return jsonString;
	}
	/**
	   * 添加请求头
	 * @param headers  请求头
	 * @param request  请求对象
	 */
		public static void setHeaders(Map<String, String> headers, HttpRequest request) {
			Set<String> keySet = headers.keySet();
			//循环headers
			for (String key : keySet) {
				String value = headers.get(key);
		      //设置请求头
				request.setHeader(key, value);
			}
		}
}
