package com.cc.sdk2.jsdk.datasource.tomcat;

import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.JdbcInterceptor;
import org.apache.tomcat.jdbc.pool.PooledConnection;

import java.lang.reflect.Method;

/**
 * Tomcat连接池的连接关闭时的拦截器
 *
 * 当连接由于网络异常、远程服务器强制关闭等原因被关闭时，会导致连接无法回收， 该拦截器会判断状态，并修改相应的标志，从而释放异常的连接
 *
 * @author cc.sen
 */
public class CloseInterceptor extends JdbcInterceptor {

    private PooledConnection pooledConnection;

    @Override
    public void reset(ConnectionPool parent, PooledConnection con) {
        if (parent == null) {
            pooledConnection = null;
        } else {
            pooledConnection = con;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (compare(CLOSE_VAL, method)) {
            if (pooledConnection != null && pooledConnection.getConnection().isClosed()) {
                pooledConnection.setDiscarded(true);
            }
        }
        return super.invoke(proxy, method, args);
    }
}
