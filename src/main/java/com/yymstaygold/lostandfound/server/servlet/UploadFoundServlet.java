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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yanyu on 2018/4/16.
 */
@WebServlet(name = "UploadFoundServlet", urlPatterns = {"/upload_found"})
public class UploadFoundServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Found found = mapper.readValue(request.getInputStream(), Found.class);
        found.store();

        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            PreparedStatement pStat = conn.prepareStatement(
                    "SELECT a.lostId, MIN(b.time) AS minTime " +
                            "FROM LostAndFound.Lost a " +
                            "NATURAL JOIN LostAndFound.LostPositionInfo b " +
                            "WHERE a.solved = 0 " +
                            "GROUP BY a.lostId " +
                            "HAVING minTime < ?");
            pStat.setDate(1, new java.sql.Date(found.getFoundTime().getTime()));
            ResultSet res = pStat.executeQuery();

            ArrayList<LostAndFoundPair> pairs = new ArrayList<>();
            while (res.next()) {
                pairs.add(new LostAndFoundPair(
                        Lost.getInstanceFromDB(res.getInt(1)), found));
            }
            res.close();
            pStat.close();

            Collections.sort(pairs);

            pStat = conn.prepareStatement("INSERT INTO LostAndFound.MatchInfo " +
                    "VALUES (null, ?, ?)");
            for (int i = 0; i < Math.min(pairs.size(), 5); ++i) {
                pStat.setInt(1, pairs.get(i).getLostId());
                pStat.setInt(2, pairs.get(i).getFoundId());
                pStat.executeUpdate();
            }
            pStat.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
