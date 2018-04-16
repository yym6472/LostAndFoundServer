package com.yymstaygold.lostandfound.server.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yymstaygold.lostandfound.server.entity.Lost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yanyu on 2018/4/16.
 */
@WebServlet(name = "UploadLostServlet", urlPatterns = {"/upload_lost"})
public class UploadLostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Lost lost = mapper.readValue(request.getInputStream(), Lost.class);

        // TODO: sort the founds by matching score and returned its foundId.
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
