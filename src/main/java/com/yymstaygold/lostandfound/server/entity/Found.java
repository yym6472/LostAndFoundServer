package com.yymstaygold.lostandfound.server.entity;

import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by yanyu on 2018/4/16.
 */
public class Found {
    private int foundId;
    private String foundName;
    private int userId;
    private String phoneNumber;
    private Item item;
    private Date foundTime;
    private double foundPositionX;
    private double foundPositionY;

    private Found(int foundId) {
        this.foundId = foundId;
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM LostAndFound.Found " +
                    "WHERE foundId = " + foundId);
            if (res.next()) {
                foundName = res.getString("foundName");
                userId = res.getInt("userId");
                item = Item.getInstanceFromDB(res.getInt("itemId"));
                foundTime = new Date(
                        res.getDate("foundTime").getTime() +
                                res.getTime("foundTime").getTime());
                foundPositionX = res.getDouble("foundPositionX");
                foundPositionY = res.getDouble("foundPositionY");
            }
            res.close();
            res = stat.executeQuery("SELECT phoneNumber FROM LostAndFound.User " +
                    "WHERE userId = " + userId);
            if (res.next()) {
                phoneNumber = res.getString("phoneNumber");
            }
            res.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Found getInstanceFromDB(int foundId) {
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM LostAndFound.Found " +
                    "WHERE foundId = " + foundId);
            boolean recordExists = res.next();
            res.close();
            stat.close();
            conn.close();
            if (recordExists) {
                return new Found(foundId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getFoundId() {
        return foundId;
    }

    public void setFoundId(int foundId) {
        this.foundId = foundId;
    }

    public String getFoundName() {
        return foundName;
    }

    public void setFoundName(String foundName) {
        this.foundName = foundName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Date getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(Date foundTime) {
        this.foundTime = foundTime;
    }

    public double getFoundPositionX() {
        return foundPositionX;
    }

    public void setFoundPositionX(double foundPositionX) {
        this.foundPositionX = foundPositionX;
    }

    public double getFoundPositionY() {
        return foundPositionY;
    }

    public void setFoundPositionY(double foundPositionY) {
        this.foundPositionY = foundPositionY;
    }
}
