package com.yymstaygold.lostandfound.server.servlet;

import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yanyu on 2018/4/12.
 */
@WebServlet(name = "CheckNewFoundsServlet")
public class CheckNewFoundsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataInputStream in = new DataInputStream(request.getInputStream());
        int userId = in.readInt();
        in.close();

        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT foundId " +
                    "FROM LostAndFound.MatchInfo " +
                    "WHERE userId = " + userId);
            while (res.next()) {
                out.writeInt(res.getInt("foundId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.writeInt(-1);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
