package com.yymstaygold.lostandfound.server.entity;

import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanyu on 2018/4/16.
 */
public class Item {
    private int itemId;
    private int type;
    private String customTypeName;
    private String imagePath;
    private Map<String, String> properties;

    public Item() {}

    private Item(int itemId) {
        this.itemId = itemId;
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM LostAndFound.Item " +
                    "WHERE itemId = " + itemId);
            if (res.next()) {
                itemId = res.getInt("itemId");
                type = res.getInt("type");
                customTypeName = res.getString("customTypeName");
                imagePath = res.getString("imagePath");
            }
            res.close();

            properties = new HashMap<>();
            res = stat.executeQuery("SELECT * FROM LostAndFound.Property " +
                    "WHERE itemId = " + itemId);
            while (res.next()) {
                properties.put(res.getString("propertyName"),
                        res.getString("propertyValue"));
            }
            res.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Item getInstanceFromDB(int itemId) {
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM LostAndFound.Item " +
                    "WHERE itemId = " + itemId);
            boolean recordExists = res.next();
            res.close();
            stat.close();
            conn.close();
            if (recordExists) {
                return new Item(itemId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void store() {
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = conn.prepareStatement(
                    "INSERT INTO LostAndFound.Item VALUES (null, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pStat.setInt(1, type);
            if (type == 10) {
                pStat.setString(2, customTypeName);
            } else {
                pStat.setNull(2, Types.VARCHAR);
            }
            pStat.setString(3, imagePath);
            pStat.executeUpdate();

            ResultSet res = pStat.getGeneratedKeys();
            if (res.next()) {
                itemId = res.getInt(1);
            }
            res.close();
            pStat.close();

            pStat = conn.prepareStatement(
                    "INSERT INTO LostAndFound.Property VALUES (null, ?, ?, ?)");
            pStat.setInt(3, itemId);
            for (String key : properties.keySet()) {
                pStat.setString(1, key);
                pStat.setString(2, properties.get(key));
                pStat.executeUpdate();
            }
            pStat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCustomTypeName() {
        return customTypeName;
    }

    public void setCustomTypeName(String customTypeName) {
        this.customTypeName = customTypeName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
