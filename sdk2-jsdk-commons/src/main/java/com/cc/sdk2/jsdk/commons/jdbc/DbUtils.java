package com.cc.sdk2.jsdk.commons.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/30 14:58
 **/
public class DbUtils {
    public static String[][] getResultSetArray(Connection connection, String sql) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            int cols = rs.getMetaData().getColumnCount();
            int rowCount = 0;
            List<String[]> allRows = new ArrayList<>();
            while (rs.next()) {
                rowCount++;
                String row[] = new String[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getString(i + 1);
                }
                allRows.add(row);
            }
            String[][] ret = new String[rowCount][cols];
            for (int i = 0; i < rowCount; i++) {
                String[] oneRow = allRows.get(i);
                for (int j = 0; j < cols; j++) {
                    ret[i][j] = oneRow[j];
                }
            }
            return ret;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static String getValue(Connection conn, String sql) throws Exception {
        String ret = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                ret = rs.getString(1);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return ret;
    }

    public static int executeUpdate(Connection conn, String sql) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
