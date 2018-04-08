package com.yymstaygold.lostandfound.server.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by yanyu on 2018/4/8.
 */
@WebServlet(name = "DownloadImageServlet", urlPatterns = {"/download_image"})
public class DownloadImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataInputStream in = new DataInputStream(request.getInputStream());
        String imagePath = in.readUTF();
        in.close();

        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        DataInputStream image = new DataInputStream(new FileInputStream(imagePath));
        int b;
        while ((b = image.read()) != -1) {
            out.write(b);
        }
        image.close();
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
