package com.chen.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
/**
   * 测试：多的sheet实体类
 * @author 曦月女孩
 *
 */
public class API {
	@Excel(name = "接口地址A")
	public String apiUrl;
	@Excel(name = "接口参数B")
	public String apiParams;

	public API() {
		super();
		
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiParams() {
		return apiParams;
	}

	public void setApiParams(String apiParams) {
		this.apiParams = apiParams;
	}

	@Override
	public String toString() {
		return "API [apiUrl=" + apiUrl + ", apiParams=" + apiParams + "]";
	}

}
