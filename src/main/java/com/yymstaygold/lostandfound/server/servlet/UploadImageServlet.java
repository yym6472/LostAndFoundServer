package com.yymstaygold.lostandfound.server.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;
import java.util.Random;

/**
 * Created by yanyu on 2018/4/8.
 */
@WebServlet(name = "UploadImageServlet", urlPatterns = {"/upload_image"})
public class UploadImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Properties prop = new Properties();
        FileInputStream settings = new FileInputStream(
                "./webapps/LostAndFoundServer/WEB-INF/classes/settings.properties");
        prop.load(settings);
        settings.close();
        String imagePath = prop.getProperty("server.imagePath");

        int bound = new Integer(prop.getProperty("server.maxImageNumber"));
        Random random = new Random();
        String filename = "" + random.nextInt(bound);
        if (new File(imagePath + filename).exists()) {
            filename = "" + random.nextInt(bound);
        }

        DataInputStream in = new DataInputStream(request.getInputStream());
        FileOutputStream file = new FileOutputStream(imagePath + filename);
        int b;
        while ((b = in.read()) != -1) {
            file.write(b);
        }
        file.close();
        in.close();

        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        out.writeUTF(imagePath + filename);
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
