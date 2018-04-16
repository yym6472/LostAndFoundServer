package com.yymstaygold.lostandfound.server.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yymstaygold.lostandfound.server.entity.Found;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yanyu on 2018/4/16.
 */
@WebServlet(name = "UploadFoundServlet", urlPatterns = {"/upload_found"})
public class UploadFoundServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Found found = mapper.readValue(request.getInputStream(), Found.class);
        found.store();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
