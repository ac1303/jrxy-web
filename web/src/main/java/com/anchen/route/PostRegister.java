package com.anchen.route;

import com.alibaba.fastjson.JSONObject;
import com.anchen.function.Register;
import com.anchen.httprequest.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author 安宸
 * @create 2022/2/11 15:45
 */
@WebServlet("/postRegister")
public class PostRegister extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/javascript;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object= HttpRequest.getBody(req.getReader());
        System.out.println(object);
        resp.getWriter().write(String.valueOf(Register.GenerateStudentModel(object)));
    }
}
