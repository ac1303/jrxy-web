package com.anchen.route;

import com.anchen.function.GetLonAndLat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 安宸
 * @create 2022/2/5 11:52
 */
@WebServlet("/getAddressLonAndLat")
public class GetAddressLonAndLat extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String address=req.getParameter("address");
        System.out.println("用户请求的地址是："+address);
        String lonAndLat= String.valueOf(GetLonAndLat.getLonAndLat(address));
        resp.getWriter().write(lonAndLat);
    }
}
