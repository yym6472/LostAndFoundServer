package com.yymstaygold.lostandfound.server.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yymstaygold.lostandfound.server.entity.Lost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by yanyu on 2018/4/20.
 */
@WebServlet(name = "DownloadLostInfoServlet", urlPatterns = {"/download_lost_info"})
public class DownloadLostInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataInputStream in = new DataInputStream(request.getInputStream());
        int lostId = in.readInt();
        in.close();

        outputLostInfoToStream(lostId, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int lostId = new Integer(request.getParameter("lostId"));

        outputLostInfoToStream(lostId, response);
    }

    private void outputLostInfoToStream(int lostId, HttpServletResponse response) {
        Lost lost = Lost.getInstanceFromDB(lostId);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            mapper.writeValue(response.getOutputStream(), lost);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
