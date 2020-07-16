package com.chen.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class CaseInfo {
	//CaseId(用例编号)	Name(接口名称)	Url(接口地址)	Type(接口提交类型)	Desc(用例描述)	Params(参数)	Content-Type
		//映射mapping，实体类属性和excel列（cell）对应
		//设置CaseId属性映射CaseId(用例编号)
	@Excel(name = "用例编号")	
	private int caseId ;
	@Excel(name = "接口名称")
	private String name ;
	@Excel(name = "url")
	private String url;
	@Excel(name = "请求方式")
	private String type;
	@Excel(name = "用例描述")
	private String desc;	
	@Excel(name = "参数")
	private String params;	
	@Excel(name = "参数类型")
	private String contentType ;
	@Excel(name = "期望结果")
	private String expectedData ;
	@Excel(name = "SQL")
	private String sql ;
	public CaseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CaseInfo(int caseId, String name, String url, String type, String desc, String params, String contentType,
			String expectedData, String sql) {
		super();
		this.caseId = caseId;
		this.name = name;
		this.url = url;
		this.type = type;
		this.desc = desc;
		this.params = params;
		this.contentType = contentType;
		this.expectedData = expectedData;
		this.sql = sql;
	}
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getExpectedData() {
		return expectedData;
	}
	public void setExpectedData(String expectedData) {
		this.expectedData = expectedData;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	@Override
	public String toString() {
		return "CaseInfo [caseId=" + caseId + ", name=" + name + ", url=" + url + ", type=" + type + ", desc=" + desc
				+ ", params=" + params + ", contentType=" + contentType + ", expectedData=" + expectedData + ", sql="
				+ sql + "]";
	}
	
}
