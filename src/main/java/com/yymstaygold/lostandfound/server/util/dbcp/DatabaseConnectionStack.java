package com.yymstaygold.lostandfound.server.util.dbcp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by yanyu on 2018/4/8.
 * From https://blog.csdn.net/deginch/article/details/70059409.
 */
public class DatabaseConnectionStack {
    private final int timeout; // the max timeout time in seconds
    private final int capacity; // the capacity of the connection stack
    private LinkedList<ConnWithTime> list = new LinkedList<>();

    /**
     * Inner class, used to package the connection and its last used time.
     */
    private class ConnWithTime {
        // last used time of the connection
        private final long time = new Date().getTime();
        private  Connection conn;
        public ConnWithTime(Connection conn) {
            this.conn = conn;
        }
        boolean isTimeout() {
            return new Date().getTime() - time > timeout * 1000;
        }
    }

    public DatabaseConnectionStack(int capacity, int timeout) {
        this.capacity = capacity;
        this.timeout = timeout;
    }

    public synchronized void push(Connection conn) throws SQLException {
        clearTimeoutConnection();
        if (list.size() < capacity) {
            list.addFirst(new ConnWithTime(conn));
        } else {
            conn.close();
        }
    }

    private void clearTimeoutConnection() throws SQLException {
        Iterator<ConnWithTime> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            ConnWithTime connWithTime = iterator.next();
            if (connWithTime.isTimeout()) {
                connWithTime.conn.close();
                iterator.remove();
            } else {
                break;
            }
        }
    }

    public synchronized  Connection pop() {
        if (list.isEmpty()) return null;
        return list.removeFirst().conn;
    }

    public synchronized void close() throws SQLException {
        Iterator<ConnWithTime> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            ConnWithTime connWithTime = iterator.next();
            connWithTime.conn.close();
            iterator.remove();
        }
    }
}
