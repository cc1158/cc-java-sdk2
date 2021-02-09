package com.cc.sdk2.jsdk.commons.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/30 14:58
 **/
public class DbHelper {
    private ThreadLocal<Connection> LOCAL_CONN = new ThreadLocal<Connection>();

    private String driver;
    private String url;
    private String user;
    private String password;

    public DbHelper(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if (LOCAL_CONN.get() == null) {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            LOCAL_CONN.set(connection);
            return connection;
        }
        return LOCAL_CONN.get();
    }

    public void close() throws SQLException {
        Connection conn = LOCAL_CONN.get();
        conn.close();
    }
}
