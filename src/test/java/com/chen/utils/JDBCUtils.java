package com.chen.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class JDBCUtils {
	// 协议：数据库名://IP:PORT/数据库名称?
	// jdbc:oracle:thin:@localhost:1521:orcl
	public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
	public static final String USERNAME = "future";
	public static final String PASSWORD = "123456";

	public static void main(String[] args) throws Exception {
		// DButils
//		QueryRunner 
//		XXXXHandler
     QueryRunner runner = new QueryRunner();
     Connection  conn = getConnection();
     String sql = "select count(*) from member a where a.mobile_phone = 13253458811;";
	 Long result = runner.query(conn, sql, new ScalarHandler<Long>());
      System.out.println(result);
      close(conn);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		// 定义数据库连接对象
		Connection conn = null;
		try {
			// 你导入的数据库驱动包， mysql。
			conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}