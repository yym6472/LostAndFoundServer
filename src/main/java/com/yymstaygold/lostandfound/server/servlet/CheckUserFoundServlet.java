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
@WebServlet(name = "CheckUserFoundServlet", urlPatterns = {"/check_user_found"})
public class CheckUserFoundServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataInputStream in = new DataInputStream(request.getInputStream());
        int foundId = in.readInt();
        in.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int foundId = new Integer(request.getParameter("foundId"));
    }

    private void outputUserFoundToStream(int foundId, HttpServletResponse response) {
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = conn.prepareStatement(
                    "SELECT foundId FROM LostAndFound.Found WHERE foundId = ?");
            pStat.setInt(1, foundId);
            ResultSet res = pStat.executeQuery();
            DataOutputStream out = new DataOutputStream(response.getOutputStream());
            while (res.next()) {
                out.writeInt(res.getInt("foundId"));
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
