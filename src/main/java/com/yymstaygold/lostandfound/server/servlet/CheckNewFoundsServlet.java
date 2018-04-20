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
import java.sql.*;

/**
 * Created by yanyu on 2018/4/12.
 */
@WebServlet(name = "CheckNewFoundsServlet", urlPatterns = {"/check_new_founds"})
public class CheckNewFoundsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataInputStream in = new DataInputStream(request.getInputStream());
        int userId = in.readInt();
        in.close();

        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = conn.prepareStatement("SELECT lostId, foundId " +
                    "FROM LostAndFound.MatchInfo NATURAL JOIN LostAndFound.Lost " +
                    "WHERE userId = ?");
            pStat.setInt(1, userId);
            ResultSet res = pStat.executeQuery();
            PreparedStatement pStat2 = conn.prepareStatement(
                    "DELETE FROM LostAndFound.MatchInfo " +
                            "WHERE lostId = ? AND foundId = ?");
            while (res.next()) {
                int lostId = res.getInt("lostId");
                int foundId = res.getInt("foundId");
                out.writeInt(lostId);
                out.writeInt(foundId);
                pStat2.setInt(1, lostId);
                pStat2.setInt(2, foundId);
                pStat2.executeUpdate();
            }
            pStat2.close();
            res.close();
            pStat.close();
            conn.close();
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
