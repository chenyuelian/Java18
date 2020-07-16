package com.chen.cases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
     //父类
public class BaseCase {
	@BeforeSuite // 它在整个套件执行之前执行
	public void init() throws Exception {
		Contants.HEADERS.put("X-Lemonban-Media-Type", "lemonban.v2");
		Contants.HEADERS.put("Content-Type", "application/json");
	System.out.println("===============项目初始化==========================================================================");
	//加载参数化配置文件
	Properties prop = new Properties();
	FileInputStream fis = new FileInputStream("src\\test\\resources\\paramsProperties");
	prop.load(fis);
	//添加到环境变量中
	AuthenticationUtils.ENV.putAll((Map)prop);
	fis.close();
	}

	@AfterSuite
	public void finish() throws Exception {
		ExcelUtils.batchWrite();
	System.out.println("===============项目执行结束=============================================================================");;
	}
	/**
	 * 参数化
	 * 
	 * @param caseInfo
	 */
	public void replaceParams(CaseInfo caseInfo) {
		String sql = caseInfo.getSql();
		String params = caseInfo.getParams();
		 
	//把{"mobile_phone":"18888334561","pwd":"12345678"}
	//替换成ENV${register_mb}=18888334561，${register_pwd}=12345678，${login_pwd}=12345678	
		// 循环env环境变量，和sql、params进行替换操作
		Set<String> keySet = AuthenticationUtils.ENV.keySet();
		for (String oldKey : keySet) {
			String newValue = AuthenticationUtils.ENV.get(oldKey).toString();
			if (StringUtils.isNotBlank(sql) && sql.contains(oldKey) ) {//判断sql是否包含oldKey，节省性能
				sql = sql.replace(oldKey, newValue);
				caseInfo.setSql(sql);
			}
			if (StringUtils.isNotBlank(params) && params.contains(oldKey)) {
				params = params.replace(oldKey, newValue);
				caseInfo.setParams(params);
			}
		}
	}
	/**
	 * 从环境变量中获取token添加到请求头中
	 * @return
	 */
	public Map<String, String> getAuthenticationHeaders() {
		Map<String, String> headers = new HashMap<>();// 自己new一个出来
		// 从环境变量中获取token
		Object token = AuthenticationUtils.ENV.get("token");
		// 把token添加到请求头中
		headers.put("Authorization", "Bearer " + token);
		headers.putAll(Contants.HEADERS);
		return headers;
	}
	/**
	  * 对响应体进行断言
	 * @param body           响应体
	 * @param expectedData   断言期望值
	 * @return               断言结果 true/false
	 */
		public boolean assertResponse(String body, String expectedData) {
			// 定义一个断言标志
			boolean assertFlag = true;
			Map<String, Object> expectedMap = JSONObject.parseObject(expectedData, Map.class);
			// 遍历map{"$.code":0,"$.msg":"OK"}
			Set<String> keySet = expectedMap.keySet();
			for (String jsonpathKey : keySet) {
				// 具体的期望值：0或者“ok”
				Object expectedValue = expectedMap.get(jsonpathKey);// 真正的期望值
				// 使用jsonpathKey从body里面去找实际值
				Object actualObject = JSONPath.read(body, jsonpathKey);
				// 期望值和实际值比较
				if (!expectedValue.equals(actualObject)) {
					assertFlag = false;
					break;
				}
			}
			return assertFlag;
		}
}
