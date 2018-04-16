package com.yymstaygold.lostandfound.server.entity;

import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yanyu on 2018/4/16.
 */
public class Lost {
    private int lostId;
    private String lostName;
    private int userId;
    private Item item;
    private ArrayList<Date> lostPositionInfoTime;
    private ArrayList<Double> lostPositionInfoPositionX;
    private ArrayList<Double> lostPositionInfoPositionY;

    public Lost() {}

    private Lost(int lostId) {
        this.lostId = lostId;
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM LostAndFound.Lost " +
                    "WHERE lostId = " + lostId);
            if (res.next()) {
                lostName = res.getString("lostName");
                userId = res.getInt("userId");
                item = Item.getInstanceFromDB(res.getInt("itemId"));
            }
            res.close();
            lostPositionInfoTime = new ArrayList<>();
            lostPositionInfoPositionX = new ArrayList<>();
            lostPositionInfoPositionY = new ArrayList<>();
            res = stat.executeQuery("SELECT * FROM LostAndFound.LostPositionInfo " +
                    "WHERE lostId = " + lostId + " ORDER BY time ASC");
            while (res.next()) {
                lostPositionInfoTime.add(new Date(
                        res.getDate("time").getTime() +
                                res.getTime("time").getTime()
                ));
                lostPositionInfoPositionX.add(res.getDouble("positionX"));
                lostPositionInfoPositionY.add(res.getDouble("positionY"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Lost getInstanceFromDB(int lostId) {
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM LostAndFound.Lost " +
                    "WHERE lostId = " + lostId);
            boolean recordExists = res.next();
            res.close();
            stat.close();
            conn.close();
            if (recordExists) {
                return new Lost(lostId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getLostId() {
        return lostId;
    }

    public void setLostId(int lostId) {
        this.lostId = lostId;
    }

    public String getLostName() {
        return lostName;
    }

    public void setLostName(String lostName) {
        this.lostName = lostName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ArrayList<Date> getLostPositionInfoTime() {
        return lostPositionInfoTime;
    }

    public void setLostPositionInfoTime(ArrayList<Date> lostPositionInfoTime) {
        this.lostPositionInfoTime = lostPositionInfoTime;
    }

    public ArrayList<Double> getLostPositionInfoPositionX() {
        return lostPositionInfoPositionX;
    }

    public void setLostPositionInfoPositionX(ArrayList<Double> lostPositionInfoPositionX) {
        this.lostPositionInfoPositionX = lostPositionInfoPositionX;
    }

    public ArrayList<Double> getLostPositionInfoPositionY() {
        return lostPositionInfoPositionY;
    }

    public void setLostPositionInfoPositionY(ArrayList<Double> lostPositionInfoPositionY) {
        this.lostPositionInfoPositionY = lostPositionInfoPositionY;
    }
}
