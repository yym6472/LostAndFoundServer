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
 * Created by yanyu on 2018/4/8.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataInputStream in = new DataInputStream(request.getInputStream());
        String phoneNumber = in.readUTF();
        String password = in.readUTF();
        in.close();

        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        try {
            Connection conn = DatabaseConnectionPool.getInstance().getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(
                    "SELECT * FROM LostAndFound.User " +
                            "WHERE phoneNumber = \"" + phoneNumber + "\"");
            if (res.next()) {
                out.writeBoolean(false);
            } else {
                stat.execute("INSERT INTO LostAndFound.User " +
                        "VALUE(NULL, \"" + password + "\", \"" + phoneNumber + "\")");
                out.writeBoolean(true);
            }
            res.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            out.writeBoolean(false);
            e.printStackTrace();
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
