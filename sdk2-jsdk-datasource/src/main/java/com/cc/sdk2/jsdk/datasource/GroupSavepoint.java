package com.cc.sdk2.jsdk.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

/**
 * 组数据源的保存点
 *
 */
public class GroupSavepoint implements Savepoint {

    private final boolean named;
    private final int savepointId;
    private final String savepointName;
    private final Map<Connection, Savepoint> savepointMap;

    public GroupSavepoint(int savepointId) {
        this.named = false;
        this.savepointId = savepointId;
        this.savepointName = null;
        this.savepointMap = new HashMap<>();
    }

    public GroupSavepoint(String savepointName) {
        this.named = true;
        this.savepointId = 0;
        this.savepointName = savepointName;
        this.savepointMap = new HashMap<>();
    }

    @Override
    public int getSavepointId() throws SQLException {
        if (named) {
            throw new SQLException("This is a named savepoint");
        }
        return savepointId;
    }

    @Override
    public String getSavepointName() throws SQLException {
        if (!named) {
            throw new SQLException("This is an un-named savepoint");
        }
        return savepointName;
    }

    public void addSavepoint(Connection connection, Savepoint savepoint) {
        this.savepointMap.put(connection, savepoint);
    }

    public Map<Connection, Savepoint> getSavepointMap() {
        return savepointMap;
    }

    public boolean isNamed() {
        return named;
    }
}
