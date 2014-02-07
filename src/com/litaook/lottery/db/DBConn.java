package com.litaook.lottery.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 创建sqlite数据库连接，请勿频繁连接和断开数据库，尽量共享一个或多个连接。
 * 
 * @author litao
 * 
 */
public class DBConn {
	private Connection conn = null;
	private static DBConn dbc = null;
	String rootDir = System.getProperty("user.dir");

	private DBConn() {
		try {
			// 加载JDBC类库
			Class.forName("org.sqlite.JDBC");
			// 数据库文件路径
			String dbScheme = new StringBuffer("jdbc:sqlite:").append(rootDir)
					.append("/db/lucky.db").toString();
			// 建立数据库连接，如果数据库文件不存在就创建文件
			conn = DriverManager.getConnection(dbScheme);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单例调用
	 * 
	 * @return
	 */
	public static DBConn getInstance() {
		if (dbc == null) {
			dbc = new DBConn();
		}
		return dbc;
	}

	/**
	 * 返回数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getConnect() throws Exception {
		if (conn == null) {
			throw new Exception("请使用DBConn.getInstance().connect()");
		} else {
			return conn;
		}
	}
}
