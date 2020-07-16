package com.chen.cases;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;
import com.chen.utils.SQLUtis;

import ognl.SetPropertyAccessor;

public class ResgisterCases extends BaseCase {
	private static final Object[][] datas = null;

	/**
	 * 注册测试类
	 * 
	 * @author 曦月女孩
	 */
	public int sheetIndex = 0;

	@Test(dataProvider = "datas")
	public void test(CaseInfo caseInfo) throws Exception {
//		1、参数化替换(字符串的替换，把带有$符号的替换成正常值)
		replaceParams(caseInfo);
		String type = caseInfo.getType();
		String contentType = caseInfo.getContentType();
		String url = caseInfo.getUrl();
		String sql = caseInfo.getSql();
		String params = caseInfo.getParams(); 
		// 2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
		Object beforeSqlResult = null;
		if (StringUtils.isNoneBlank(sql)) {
			beforeSqlResult = SQLUtis.getSingleResult(sql);
		}
//		3、调用接口
		HttpResponse response = HttpUtils.call(type, contentType, url, params, Contants.HEADERS);
		// 打印响应
		String body = HttpUtils.getResponseBody(response);
		System.out.println(caseInfo + "===============================================================");
//		4、断言响应结果
		boolean assertFlag = assertResponse(body, caseInfo.getExpectedData());
		System.out.println("断言结果 ：" + assertFlag);
//		5、添加接口响应回写内容
//		6、数据库后置查询结果
		Object afterSqlResult = null;
		boolean sqlAssertFlag = true;
		if (StringUtils.isNoneBlank(sql)) {
			afterSqlResult = SQLUtis.getSingleResult(sql);
			System.out.println("beforeSqlResult :" + beforeSqlResult);
			System.out.println("afterSqlResult :" + afterSqlResult);
//			7、据库断言
			sqlAssertFlag = sqlAssert(beforeSqlResult, afterSqlResult);
			System.out.println("数据库断言：" + sqlAssertFlag);
		}

//		8、添加断言回写内容
//		9、添加日志
//		10、报表断
	}

	

	/**
	 * 数据库断言
	 * 
	 * @param beforeSqlResult
	 * @param afterSqlResult
	 * @return
	 */
	public boolean sqlAssert(Object beforeSqlResult, Object afterSqlResult) {
		// beforeSqlResult == 0 并且 afterSqlResult ==1
		if ("0".equals(beforeSqlResult.toString()) && "1".equals(afterSqlResult.toString())) {
			return true;
		} else {
			return false;
		}
	}

	@DataProvider(name = "datas")
	public Object[][] datas() throws Exception {

		Object[][] datas2 = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
		return datas2;
	}
}
