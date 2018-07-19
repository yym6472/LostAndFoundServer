package com.yymstaygold.lostandfound.server.servlet;

import com.google.gson.Gson;
import com.yymstaygold.lostandfound.server.entity.ResultWithoutData;
import com.yymstaygold.lostandfound.server.entity.UserInfo;
import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by StayGold on 2018/7/18.
 */
@WebServlet(name = "ChangeInfoServlet", urlPatterns = {"/change_info"})
public class ChangeInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(request.getReader(), UserInfo.class);

        ResultWithoutData result = new ResultWithoutData();
        try {
            Connection connection = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = connection.prepareStatement(
                    "UPDATE LostAndFound.User " +
                    "SET name = ?, work = ?, address = ?, mail = ? " +
                            "WHERE userId = ?");
            pStat.setString(1, userInfo.getName());
            pStat.setString(2, userInfo.getWork());
            pStat.setString(3, userInfo.getAddress());
            pStat.setString(4, userInfo.getMail());
            pStat.setInt(5, userInfo.getUserId());
            pStat.executeUpdate();
            pStat.close();
            connection.close();

            result.setCode(0);
            result.setMessage("成功");
        } catch (SQLException e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("服务端错误：" + e.getMessage());
        }

        gson.toJson(result, response.getWriter());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
