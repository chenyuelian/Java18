package com.chen.cases;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.apache.http.HttpResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;

public class LoginCases extends BaseCase {
	/**
	 * 登录测试类
	 * 
	 * @author 曦月女孩
	 */
	public int sheetIndex = 1;

	public static void main(String[] args) {
		String json = "{\"code\":0,\"msg\":\"OK\",\"data\":{\"id\":8884216,\"leave_amount\":63000.0,\"mobile_phone\":\"13253458811\",\"reg_name\":\"檬檬\",\"reg_time\":\"2020-05-12 21:34:44.0\",\"type\":0,\"token_info\":{\"token_type\":\"Bearer\",\"expires_in\":\"2020-05-21 15:30:06\",\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjg4ODQyMTYsImV4cCI6MTU5MDA0NjIwNn0.boCfso06M9nesnFMCnmdQ16VijebE89lbwiaOlzXyHMIoP1hMAOHj_0ZPivsQapu8hbh4_1cDkN_mTHuUM_Ylg\"}},\"copyright\":\"Copyright 柠檬班 © 2017-2019 湖南省零檬信息技术有限公司 All Rights Reserved\"}";
//绝对路径：从跟路径一直找到最后一个路径
		Object object = JSONPath.read(json, "$.data.token_info.token");
//相对路径：只要符合路径都找出来.      输出结果会多了一对大括号[],需要自己来处理
//	Object object = JSONPath.read(json, "$..token");	
		System.out.println(object);
	}

	@Test(dataProvider = "datas")
	public void test(CaseInfo caseInfo) throws Exception {
//		1、参数化替换
		replaceParams(caseInfo);
//		2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
//		3、调用接口

		String type = caseInfo.getType();
		String contentType = caseInfo.getContentType();
		String url = caseInfo.getUrl();
		String params = caseInfo.getParams();
		HttpResponse response = HttpUtils.call(type, contentType, url, params, Contants.HEADERS);
		// 打印响应
		String body = HttpUtils.getResponseBody(response);
		// 3.1获取token
		AuthenticationUtils.getValue2ENV(body, "$.data.token_info.token", "token");
		// 3.2 获取menber_id
		AuthenticationUtils.getValue2ENV(body, "$.data.id", "${member_id}");
		System.out.println("===============================================================");
//		4、断言响应结果
		// 获取期望值
		//String expectedData = caseInfo.getExpectedData();
		boolean assertFlag = assertResponse(body, caseInfo.getExpectedData());
		System.out.println("断言结果 ：" + assertFlag);
//		5、添加接口响应回写内容
//		6、数据库后置查询结果
//		7、据库断言
//		8、添加断言回写内容
//		9、添加日志
//		10、报表断言	

	}


	@DataProvider(name = "datas")
	public Object[][] datas() throws Exception {
		Object[][] datas2 = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
		return datas2;
	}

}
