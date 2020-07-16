package com.chen.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.chen.constants.Contants;
import com.chen.pojo.API;
import com.chen.pojo.WriteBackData;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;


public class ExcelUtils {
//存储excel回写对象集合	
public static List<WriteBackData> wbdList = new ArrayList<>();
 
	
	public static void main(String[] args) throws Exception {
//		Object[][] datas = read2(2,1,API.class);
//		for(Object[] datas2 : datas ) {
//			System.out.println(Arrays.toString(datas2));
//		}
		List<API> list2 = read(2, 1, API.class);
	}

	/**获取对应sheet二维数组
	 * @param startSheetIndex  读取sheet开始索引
	 * @param sheetNum         读取sheet长度
	 * @param clazz            封装实体类型
	 * @return
	 * @throws Exception
	 */
	public static Object[][] getDatas(int startSheetIndex, int sheetNum, Class clazz) throws Exception {

		List list = read(startSheetIndex, sheetNum, clazz);
		Object[][] datas = new Object[list.size()][1];
		for (int i = 0; i < list.size(); i++) {
			datas[i][0] = list.get(i);

		}

		return datas;
	}

	
	/**
	 * alt+shift+j
	 * 
	 * @param <T>
	 * @param startSheetIndex
	 * @param sheetNum
	 * @param clazz           当传入字节码对象时，就会确定范型T,比如：CaseInfo.class，那么T=CaseInfo
	 * @return
	 * @throws Exception
	 */// 使用范型支持多实体类   Class<T>:字节码对象是支持范型的
	public static <T> List<T> read(int startSheetIndex, int sheetNum, Class<T> clazz) throws Exception {
		Object[][] datas = null;
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(Contants.EXCEL_PATH);
			ImportParams params = new ImportParams();
			params.setStartSheetIndex(startSheetIndex);
			params.setSheetNum(sheetNum);// 这个变成参数也可以
			List<T> list = ExcelImportUtil.importExcel(fis, clazz, params);
			return list;

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} finally {
			// 关流
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static  void batchWrite () throws Exception  {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(Contants.EXCEL_PATH);
			Workbook excel = WorkbookFactory.create(fis);
			//循环wbdList
			for (WriteBackData wbd : wbdList) {
				int sheetIndex = wbd.getSheetIndex();
				int rowNum = wbd.getRowNum();
				int cellNum = wbd.getCellNum();
				String content = wbd.getContent();
				//获取sheet
				Sheet sheet = excel.getSheetAt(sheetIndex);
			//获取row
				Row row = sheet.getRow(rowNum);
				//获取cell
				Cell cell = row.getCell(cellNum,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			  //修改cell值
				cell.setCellValue(content);
			}
			//回写write
			fos = new FileOutputStream(Contants.EXCEL_PATH);
			excel.write(fos);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}finally {// 关流
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 关流
						try {
							if (fos != null) {
								fos.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
		}
	}
	 
	
}
	
