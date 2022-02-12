package com.anchen.route;

import com.anchen.function.VerifyStudentIDAndPassword;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 安宸
 * @create 2022/2/10 22:11
 */
@WebServlet("/getStudentIdAndPassword")
public class GetStudentIdAndPassword extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/javascript;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String studentId = req.getParameter("studentId").replaceAll("\\s*", "");
        String password = req.getParameter("password").replaceAll("\\s*", "");
        System.out.println(studentId);
        System.out.println(password);
        if (studentId.equals("") || password.equals("")||studentId==null||password==null){
            resp.getWriter().write("{\"code\":400,\"msg\":\"请输入学号和密码\"}");
        }else {
            resp.getWriter().write(String.valueOf(VerifyStudentIDAndPassword.getIDAndPassword(studentId,password)));
        }
    }
}
