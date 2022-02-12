package com.anchen.route;

import com.anchen.function.SendCaptcha;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 安宸
 * @create 2022/2/7 16:51
 */
@WebServlet("/getRequestCaptcha")
public class GetRequestCaptcha extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            返回字符串并用UTF-8编码
        resp.setContentType("text/javascript;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String key=req.getParameter("key").replaceAll("\\s*", "");
        String channel=req.getParameter("channel").replaceAll("\\s*", "");
        if (key==null||channel==null||key.equals("")||channel.equals("")){
            resp.getWriter().write("{\"code\":\"-1\",\"msg\":\"参数错误\"}");
        }else {
            resp.getWriter().write(String.valueOf(SendCaptcha.send(key,channel)));
        }
    }
}
