package com.cc.sdk2.jsdk.datasource.connection;


import com.cc.sdk2.jsdk.datasource.GroupDataSource;
import com.cc.sdk2.jsdk.datasource.GroupSavepoint;
import com.cc.sdk2.jsdk.datasource.spring.TransactionDefinition;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class TransactionalConnection implements Connection {

    /**
     * 配合{@link #forEachConnection(ConnectionConsumer)}使用的方法接口
     */
    @FunctionalInterface
    private interface ConnectionConsumer {

        void accept(Connection connection) throws SQLException;
    }

    /**
     * 配合{@link #handleSavepoint(Savepoint, SavepointConsumer)}使用的方法接口
     */
    @FunctionalInterface
    private interface SavepointConsumer {

        void accept(Connection connection, Savepoint underlyingSavepoint) throws SQLException;
    }


    private GroupDataSource groupDataSource;

    private String userName;
    private String password;

    private boolean autoCommit = true;

    private boolean readOnly = false;

    private boolean closed = false;

    private Map<DataSource, Connection> connectionCache = new ConcurrentHashMap<>(2);


    private void forEachConnection(ConnectionConsumer function) throws SQLException {
        forEachConnection(function, (c) -> {
        });
    }

    private void forEachConnection(ConnectionConsumer function, ConnectionConsumer rollback) throws SQLException {
        SQLException e = null;
        for (Map.Entry<DataSource, Connection> entry : connectionCache.entrySet()) {
            if (null == e) {
                try {
                    function.accept(entry.getValue());
                    continue;
                } catch (SQLException e2) {
                    e = e2;
                }
            }
            try {
                rollback.accept(entry.getValue());
            } catch (SQLException e2) {
            }
        }
        if (null != e) {
            throw e;
        }
    }

    /**
     * 处理保存点
     *
     * @param savepoint 保存点
     * @param function  回调方法
     * @throws SQLException 异常
     */
    private void handleSavepoint(Savepoint savepoint, SavepointConsumer function) throws SQLException {
        Map<Connection, Savepoint> savepointMap = new HashMap<>();
        while (!savepointStack.isEmpty()) {
            GroupSavepoint groupSavepoint = savepointStack.pop();
            savepointMap.putAll(groupSavepoint.getSavepointMap());
            if (groupSavepoint == savepoint) {
                break;
            }
        }
        for (Map.Entry<Connection, Savepoint> entry : savepointMap.entrySet()) {
            function.accept(entry.getKey(), entry.getValue());
        }
    }

    private int transactionIsolation = TransactionDefinition.ISOLATION_DEFAULT;


    /**
     * 保存点缓存
     */
    private Deque<GroupSavepoint> savepointStack = new ArrayDeque<>();

    /**
     * 保存点计数器
     */
    private int savepointCounter = 0;


    public TransactionalConnection(GroupDataSource groupDataSource) {
        this.groupDataSource = groupDataSource;
    }


    public TransactionalConnection(GroupDataSource groupDataSource, String userName, String password) {
        this.groupDataSource = groupDataSource;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 获取数据源的链接
     */
    private Connection getConnection() throws SQLException {
        DataSource dataSource = getDataSource();
        Connection connection = connectionCache.get(dataSource);
        if (connection != null) {
            return connection;
        }
        connection = (this.userName == null || this.userName.trim().length() == 0) ? dataSource.getConnection() : dataSource.getConnection(userName, password);
        initConnection(connection);
        connectionCache.put(dataSource, connection);
        return connection;
    }

    private DataSource getDataSource() {
        //获取当前的datasource
        return this.groupDataSource.getCurrentDataSource();
    }

    private void initConnection(Connection connection) throws SQLException {
        if (this.readOnly) {
            try {
                connection.setReadOnly(this.readOnly);
            } catch (SQLException e) {
            }
        }
        if (this.autoCommit != connection.getAutoCommit()) {
            connection.setAutoCommit(this.autoCommit);
        }
        //初始化保存点栈
        if (!savepointStack.isEmpty()) {
            GroupSavepoint groupSavepoint = savepointStack.peek();
            Savepoint savepoint = groupSavepoint.isNamed() ? connection.setSavepoint(groupSavepoint.getSavepointName()) : connection.setSavepoint();
            groupSavepoint.addSavepoint(connection, savepoint);
        }

    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }

    @Override
    public String toString() {
        try {
            return "Transactional Connection proxy for DataSource [" + getDataSource() + "]";
        } catch (Exception ignore) {
            return "Transactional Connection proxy for DataSource [UNINITIALIZED]";
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return getConnection().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return getConnection().nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        //TODO 对每一个链接设置自动提交
        forEachConnection(connection -> connection.setAutoCommit(autoCommit));
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.autoCommit;
    }

    @Override
    public void commit() throws SQLException {
        //TODO 提交
        savepointStack.clear();
        savepointCounter = 0;
        forEachConnection(Connection::commit, Connection::rollback);
    }

    @Override
    public void rollback() throws SQLException {
        savepointStack.clear();
        savepointCounter = 0;
        forEachConnection(Connection::rollback);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        handleSavepoint(savepoint, Connection::rollback);
    }

    @Override
    public void close() throws SQLException {
        this.closed = true;
        //TODO 循环每一个链接close请求
        forEachConnection(Connection::close);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getConnection().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
        //TODO 遍历每一个链接设置只读属性
        forEachConnection(connection -> connection.setReadOnly(readOnly));
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.readOnly;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        getConnection().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return getConnection().getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this.transactionIsolation = level;
        //TODO 循环每一个链接，设置事务隔离
        forEachConnection(connection -> connection.setTransactionIsolation(transactionIsolation));
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.transactionIsolation;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        Connection connection = getConnection();
        if (null == connection) {
            return null;
        }
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        Connection connection = getConnection();
        if (null == connection) {
            return;
        }
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getConnection().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getConnection().setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        getConnection().setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getConnection().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        GroupSavepoint groupSavepoint = new GroupSavepoint(++savepointCounter);
        forEachConnection(connection -> {
            Savepoint savepoint = connection.setSavepoint();
            groupSavepoint.addSavepoint(connection, savepoint);
        });
        savepointStack.push(groupSavepoint);
        return groupSavepoint;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        GroupSavepoint groupSavepoint = new GroupSavepoint(name);
        forEachConnection(connection -> {
            Savepoint savepoint = connection.setSavepoint(name);
            groupSavepoint.addSavepoint(connection, savepoint);
        });
        savepointStack.push(groupSavepoint);
        return groupSavepoint;
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            handleSavepoint(savepoint, Connection::releaseSavepoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return getConnection().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return getConnection().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return getConnection().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return getConnection().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return getConnection().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return getConnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return getConnection().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return getConnection().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            getConnection().setClientInfo(name, value);
        } catch (SQLException e) {
            throw new SQLClientInfoException(Collections.emptyMap(), e);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            getConnection().setClientInfo(properties);
        } catch (SQLException e) {
            throw new SQLClientInfoException(Collections.emptyMap(), e);
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return getConnection().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return getConnection().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getConnection().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getConnection().createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        getConnection().setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return getConnection().getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        getConnection().abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        getConnection().setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return getConnection().getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return (T) this;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
}
