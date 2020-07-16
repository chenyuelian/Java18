package com.chen.pojo;

public class WriteBackData {
	// 回写的sheet下标
	private int sheetIndex;
   //回写的行号
	private int rowNum;
   //回写的列号
	private int cellNum;
   //回写的内容
	private String content;

	public WriteBackData() {
		super();
		
	}

	public WriteBackData(int sheetIndex, int rowNum, int cellNum, String content) {
		super();
		this.sheetIndex = sheetIndex;
		this.rowNum = rowNum;
		this.cellNum = cellNum;
		this.content = content;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getCellNum() {
		return cellNum;
	}

	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "WriteBackData [sheetIndex=" + sheetIndex + ", rowNum=" + rowNum + ", cellNum=" + cellNum + ", content="
				+ content + "]";
	}

}
