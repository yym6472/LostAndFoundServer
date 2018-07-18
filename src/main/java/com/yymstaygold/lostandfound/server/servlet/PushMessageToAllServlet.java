package com.yymstaygold.lostandfound.server.servlet;

import com.yymstaygold.lostandfound.server.util.baidupush.BaiduPushUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by StayGold on 2018/7/17.
 */
@WebServlet(name = "PushMessageToAllServlet", urlPatterns = {"/push/all"})
public class PushMessageToAllServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msgType = request.getParameter("msgType");
        String msg = request.getParameter("msg");
        String expireSec = request.getParameter("expireSec");
        if (expireSec == null) {
            expireSec = "3600";
        }
        if (msgType == null) {
            msgType = "0";
        }
        BaiduPushUtils.pushMsgToAll(msg, Integer.valueOf(msgType), Integer.valueOf(expireSec));
    }
}
