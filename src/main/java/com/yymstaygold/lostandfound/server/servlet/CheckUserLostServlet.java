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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yanyu on 2018/4/20.
 */
@WebServlet(name = "CheckUserLostServlet", urlPatterns = {"check_user_lost"})
public class CheckUserLostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataInputStream in = new DataInputStream(request.getInputStream());
        int userId = in.readInt();
        in.close();

        outputUserLostToStream(userId, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = new Integer(request.getParameter("userId"));

        outputUserLostToStream(userId, response);
    }

    private void outputUserLostToStream(int userId, HttpServletResponse response) {
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = conn.prepareStatement(
                    "SELECT lostId FROM LostAndFound.Lost WHERE userId = ?");
            pStat.setInt(1, userId);
            ResultSet res = pStat.executeQuery();
            DataOutputStream out = new DataOutputStream(response.getOutputStream());
            while (res.next()) {
                out.writeInt(res.getInt("lostId"));
            }
            res.close();
            pStat.close();
            conn.close();
            out.writeInt(-1);
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
