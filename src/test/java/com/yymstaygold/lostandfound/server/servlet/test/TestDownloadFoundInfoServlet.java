package com.yymstaygold.lostandfound.server.servlet.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yymstaygold.lostandfound.server.entity.Found;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yanyu on 2018/4/16.
 */
public class TestDownloadFoundInfoServlet {
    public static void main(String[] args) throws IOException {
        String urlString = "http://localhost:8080/LostAndFoundServer" +
                "/download_found_info";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setReadTimeout(6000);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeInt(2);
        out.flush();
        out.close();
        if (conn.getResponseCode() == 200) {
            DataInputStream in = new DataInputStream(conn.getInputStream());

            ObjectMapper mapper = new ObjectMapper();

            Found found = mapper.readValue(in, Found.class);

            if (found == null) {
                System.out.println("null");
            } else {
                System.out.println(found.getFoundName() + " " +
                        found.getUserId() + " " +
                        found.getItem().getImagePath() + " " +
                        found.getItem().getProperties().get("卡号"));
            }
            conn.disconnect();
        }
    }
}
