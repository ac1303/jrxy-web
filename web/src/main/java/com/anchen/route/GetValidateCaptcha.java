package com.anchen.route;

import com.anchen.function.ValidateCaptcha;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 安宸
 * @create 2022/2/7 16:50
 */
@WebServlet("/getValidateCaptcha")
public class GetValidateCaptcha extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/javascript;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String key=req.getParameter("key").replaceAll("\\s*", "");
        String captcha=req.getParameter("captcha").replaceAll("\\s*", "");
        String sign=req.getParameter("sign").replaceAll("\\s*", "");
        if (key==null||key.equals("")||captcha==null||captcha.equals("")||sign==null||sign.equals("")){
            resp.getWriter().write("{\"code\":\"-1\",\"msg\":\"参数错误\"}");
        }else {
            resp.getWriter().write(String.valueOf(ValidateCaptcha.validate(key,captcha)));
        }
    }
}
