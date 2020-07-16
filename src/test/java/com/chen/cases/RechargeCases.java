package com.chen.cases;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.poi.hpsf.Decimal;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONPath;
import com.chen.constants.Contants;
import com.chen.pojo.CaseInfo;
import com.chen.pojo.WriteBackData;
import com.chen.utils.AuthenticationUtils;
import com.chen.utils.ExcelUtils;
import com.chen.utils.HttpUtils;
import com.chen.utils.SQLUtis;

public class RechargeCases extends BaseCase {

	/**
	 * 充值测试类
	 * 
	 * @author 曦月女孩
	 */
	
	 public int sheetIndex = 2;
	@Test(dataProvider = "datas")
	public void test(CaseInfo caseInfo) throws Exception {
//		1、参数化替换
		replaceParams(caseInfo);//这个参数化一定要放在第一行，不能放在后面，要先做参数化再取值
		String type = caseInfo.getType();
		String contentType = caseInfo.getContentType();
		String url = caseInfo.getUrl();
		String sql = caseInfo.getSql();
		String params = caseInfo.getParams();
//		2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
		Object beforeSqlResult = null;
		if (StringUtils.isNoneBlank(sql)) {
			beforeSqlResult = SQLUtis.getSingleResult(sql);
		}
//		3、调用接口
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
		Object afterSqlResult = null;
		boolean sqlAssertFlag = true;
		if (StringUtils.isNoneBlank(sql)) {
			afterSqlResult = SQLUtis.getSingleResult(sql);
			System.out.println("beforeSqlResult :" + beforeSqlResult);
			System.out.println("afterSqlResult :" + afterSqlResult);
//			7、据库断言
			sqlAssertFlag = sqlAssert(beforeSqlResult,afterSqlResult,params);
        System.out.println("数据库断言：" + sqlAssertFlag);
		}

//		8、添加断言回写内容
//		9、添加日志
//		10、报表断言	

	}
	/**
	 * 数据库断言的方法
	 * @param beforeSqlResult
	 * @param afterSqlResult
	 * @param params
	 * @return
	 */
	public boolean sqlAssert(Object beforeSqlResult, Object afterSqlResult,String params) {
		//取出参数params的值:amount的值
		Object expectedResult = JSONPath.read(params, "$.amount");
	//afterSqlResult - beforeSqlResult ==expectedResult
	//	BigInteger 超大小数    BigDecimal 超大整数
		 BigDecimal beforeSqlRValue = (BigDecimal)beforeSqlResult;
		 BigDecimal afterSqlValue = (BigDecimal)afterSqlResult;
		 BigDecimal expectedValue = new BigDecimal(expectedResult.toString());
		 System.out.println("beforeSqlRValue:"+ beforeSqlRValue);
		 System.out.println("afterSqlValue:" + afterSqlValue);
		 System.out.println("afterSqlValue.subtract(beforeSqlRValue)"+ afterSqlValue.subtract(beforeSqlRValue));
	//afterSqlResult - beforeSqlResult和expectedResult 比较       比较的结果只有3种结果：（-1 0 1）
		 if(afterSqlValue.subtract(beforeSqlRValue).compareTo(expectedValue) == 0) {
		return true;
	}else {
		return false;
	}
	
	
	}
	@DataProvider(name = "datas")
	public Object[][] datas() throws Exception {
		Object[][] datas2 = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
		return datas2;
	}
}
