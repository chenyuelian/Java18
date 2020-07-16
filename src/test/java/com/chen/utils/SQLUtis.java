package com.chen.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class SQLUtis {
	/**
	 * 执行sql语句，返回单个结果集
	 * @param sql   sql语句
	 * @return
	 */
	public static Object getSingleResult(String sql) {
		//创建 QueryRunner对象
		QueryRunner runner = new QueryRunner();
	    //获取数据库连接
		Connection  conn = JDBCUtils.getConnection();
	   //执行查询语句
		try {
			Object result = runner.query(conn, sql, new ScalarHandler<>());
			//关闭数据库连接 
		      JDBCUtils.close(conn);
		      return  result;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
}
