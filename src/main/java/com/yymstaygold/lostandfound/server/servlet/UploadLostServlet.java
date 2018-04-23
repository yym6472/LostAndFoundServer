package com.yymstaygold.lostandfound.server.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yymstaygold.lostandfound.server.entity.Found;
import com.yymstaygold.lostandfound.server.entity.Lost;
import com.yymstaygold.lostandfound.server.util.dbcp.DatabaseConnectionPool;
import com.yymstaygold.lostandfound.server.util.match.LostAndFoundPair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yanyu on 2018/4/16.
 */
@WebServlet(name = "UploadLostServlet", urlPatterns = {"/upload_lost"})
public class UploadLostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Lost lost = mapper.readValue(request.getInputStream(), Lost.class);
        lost.store();

        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = conn.prepareStatement(
                    "SELECT MIN(time) FROM LostAndFound.LostPositionInfo " +
                            "WHERE lostId = ?");
            pStat.setInt(1, lost.getLostId());
            ResultSet res = pStat.executeQuery();

            Timestamp minTime = null;
            if (res.next()) {
                minTime = res.getTimestamp(1);
            }
            res.close();
            pStat.close();

            pStat = conn.prepareStatement(
                    "SELECT foundId FROM LostAndFound.Found " +
                            "WHERE foundTime > ? AND solved = 0");
            pStat.setTimestamp(1, minTime);
            res = pStat.executeQuery();

            ArrayList<LostAndFoundPair> pairs = new ArrayList<>();
            while (res.next()) {
                pairs.add(new LostAndFoundPair(
                        lost, Found.getInstanceFromDB(res.getInt(1))));
            }
            res.close();
            pStat.close();
            conn.close();

            Collections.sort(pairs);
            DataOutputStream out = new DataOutputStream(response.getOutputStream());
            for (int i = 0; i < Math.min(pairs.size(), 5); ++i) {
                out.writeInt(pairs.get(i).getFoundId());
            }
            out.writeInt(-1);
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
