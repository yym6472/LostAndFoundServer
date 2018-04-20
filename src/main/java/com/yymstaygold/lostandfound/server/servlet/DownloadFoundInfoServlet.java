package com.yymstaygold.lostandfound.server.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yymstaygold.lostandfound.server.entity.Found;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by yanyu on 2018/4/16.
 */
@WebServlet(name = "DownloadFoundInfoServlet", urlPatterns = {"/download_found_info"})
public class DownloadFoundInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataInputStream in = new DataInputStream(request.getInputStream());
        int foundId = in.readInt();
        in.close();

        outputFoundInfoToStream(foundId, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int foundId = new Integer(request.getParameter("foundId"));

        outputFoundInfoToStream(foundId, response);
    }

    private void outputFoundInfoToStream(int foundId, HttpServletResponse response) {

        Found found = Found.getInstanceFromDB(foundId);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            mapper.writeValue(response.getOutputStream(), found);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
