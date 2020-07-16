package com.chen.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.pojo.WriteBackData;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;

public class AddCases extends BaseCase {

	/**
	   * 新增项目测试类
	 * @author 曦月女孩
	 */
     public int sheetIndex = 3;
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
		//添加token鉴权头
		Map<String, String> headers = getAuthenticationHeaders();
		HttpResponse response = HttpUtils.call(type, contentType, url, params, headers);
		// 打印响应
		String body = HttpUtils.getResponseBody(response);
		System.out.println(caseInfo + "===============================================================");
//		4、断言响应结果
		boolean assertFlag = assertResponse(body, caseInfo.getExpectedData());
		System.out.println("断言结果 ：" + assertFlag);
//		5、添加接口响应回写内容
		WriteBackData wbd = new WriteBackData(sheetIndex, caseInfo.getCaseId(), 7, body);
		ExcelUtils.wbdList.add(wbd);
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
