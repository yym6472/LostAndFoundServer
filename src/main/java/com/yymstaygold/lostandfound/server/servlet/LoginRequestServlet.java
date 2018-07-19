package com.yymstaygold.lostandfound.server.servlet;

import com.baidu.yun.push.client.BaiduPush;
import com.google.gson.Gson;
import com.yymstaygold.lostandfound.server.entity.LoginResult;
import com.yymstaygold.lostandfound.server.util.baidupush.BaiduPushUtils;
import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by StayGold on 2018/7/16.
 */
@WebServlet(name = "LoginRequestServlet", urlPatterns = {"/login"})
public class LoginRequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phoneNumber = request.getParameter("phoneNumber");
        String channelId = request.getParameter("channelId");
        try {
            Connection connection = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = connection.prepareStatement(
                    "SELECT * FROM LostAndFound.User " +
                    "WHERE phoneNumber = ?");
            pStat.setString(1, phoneNumber);
            ResultSet res = pStat.executeQuery();
            int userId = -1;

            LoginResult.ResultBean resultBean = new LoginResult.ResultBean();

            if (res.next()) {
                resultBean.setFirstLogin(false);
                userId = res.getInt("userId");
                String oldChannelId = res.getString("channelId");
                int isOnline = res.getInt("isOnline");
                if (!oldChannelId.equals(channelId)) {
                    PreparedStatement pStat2 = connection.prepareStatement(
                            "UPDATE LostAndFound.User SET channelId = ? " +
                                    "WHERE userId = ?");
                    pStat2.setString(1, channelId);
                    pStat2.setInt(2, userId);
                    pStat2.executeUpdate();
                    pStat2.close();

                    BaiduPushUtils.forceOffline(oldChannelId);
                }
            } else {
                resultBean.setFirstLogin(true);
                PreparedStatement pStat2 = connection.prepareStatement(
                        "INSERT INTO LostAndFound.User " +
                                "VALUES (null, ?, ?, 1, null, null, null, null)", Statement.RETURN_GENERATED_KEYS);
                pStat2.setString(1, phoneNumber);
                pStat2.setString(2, channelId);
                pStat2.executeUpdate();
                ResultSet res2 = pStat2.getGeneratedKeys();
                if (res2.next()) {
                    userId = res2.getInt(1);
                }
                res2.close();
                pStat2.close();
            }
            res.close();
            pStat.close();
            connection.close();

            resultBean.setUserId(userId);
            LoginResult result = new LoginResult();
            result.setCode(0);
            result.setMessage("成功");
            result.setResult(resultBean);

            Gson gson = new Gson();
            gson.toJson(result, response.getWriter());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
