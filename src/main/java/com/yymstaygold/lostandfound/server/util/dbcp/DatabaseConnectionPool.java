package com.yymstaygold.lostandfound.server.util.dbcp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Properties;

/**
 * Created by yanyu on 2018/4/8.
 * From https://blog.csdn.net/deginch/article/details/70059409.
 */
public class DatabaseConnectionPool {
    private static DatabaseConnectionPool ourInstance = new DatabaseConnectionPool();
    public static DatabaseConnectionPool getInstance() {
        return ourInstance;
    }

    private DatabaseConnectionStack stack;
    private final String url;
    private final String username;
    private final String password;

    private volatile boolean isRun = true;

    private DatabaseConnectionPool() {
        Properties prop = new Properties();
        try {
            FileInputStream in = new FileInputStream(
            "./webapps/LostAndFoundServer/WEB-INF/classes/jdbc.properties");
            prop.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(prop.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        url = prop.getProperty("jdbc.url");
        username = prop.getProperty("jdbc.username");
        password = prop.getProperty("jdbc.password");

        stack = new DatabaseConnectionStack(
                new Integer(prop.getProperty("dbcp.capacity")),
                new Integer(prop.getProperty("dbcp.timeout")));
    }

    public Connection getConnection() throws SQLException {
        if (!isRun) {
            throw new SQLException("pool closed");
        }
        Connection conn = stack.pop();
        if (conn == null) {
            conn = DriverManager.getConnection(url, username, password);
        }
        final Connection myConn = conn;
        return (Connection) Proxy.newProxyInstance(
                DatabaseConnectionPool.class.getClassLoader(),
                conn.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    if (method.getName().equals("close") && isRun) {
                        stack.push(myConn);
                    } else {
                        return method.invoke(myConn, args);
                    }
                    return null;
                }
        );
    }

    public void close() throws SQLException {
        isRun = false;
        stack.close();
    }

    /**
     * Simple test for DatabaseConnectionPool.
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        // for local mysql configuration test
        Connection conn = DatabaseConnectionPool.getInstance().getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM SimpleBlog.user");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(2));
        }
        conn.close();
        DatabaseConnectionPool.getInstance().close();

//        // for remote mysql configuration test
//        Connection conn = DatabaseConnectionPool.getInstance().getConnection();
//        Statement statement = conn.createStatement();
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM test.test");
//        while (resultSet.next()) {
//            System.out.println(resultSet.getString(2));
//        }
//        resultSet.close();
//        statement.close();
//        conn.close();
//        DatabaseConnectionPool.getInstance().close();
    }
}
