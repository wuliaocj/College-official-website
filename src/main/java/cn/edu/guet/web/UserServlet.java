package cn.edu.guet.web;

import cn.edu.guet.bean.User;
import cn.edu.guet.http.HttpResult;
import cn.edu.guet.proxy.TransactionHandler;
import cn.edu.guet.service.UserService;
import cn.edu.guet.service.impl.UserServiceImpl;
import com.alibaba.fastjson2.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class UserServlet extends HttpServlet {

    UserService userService = (UserService) new TransactionHandler(new UserServiceImpl()).getProxy();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.contains("getUserList")) {
            System.out.println("getUser");
            try {
                List<User> users = userService.getUserList();
                resp.setContentType("application/json;charset=utf-8");
                String result = com.alibaba.fastjson2.JSON.toJSONString(HttpResult.ok("获取用户列表成功!",users));
                PrintWriter out = resp.getWriter();
                out.write(result);
                out.flush();
                out.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (uri.contains("saveUser")) {
            System.out.println("saveUser");
            resp.setContentType("application/json;charset=utf-8");
            StringBuffer sb = new StringBuffer();
            InputStream inputStream = req.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.lines().forEach(line -> {
                sb.append(line);
            });
            System.out.println("用户内容：" + sb.toString());
            //把Json转java对象
            User user = JSON.parseObject(sb.toString(),User.class);
            //调用Service
            HttpResult httpResult = null;
            try {
                httpResult = userService.saveUser(user);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("cccascas");
                throw new RuntimeException(e);
            }

            resp.setContentType("application/json;charset=utf-8");
            String result = JSON.toJSONString(httpResult);
            PrintWriter out = resp.getWriter();
            out.write(result);
            out.flush();
            out.close();
        }
        else if (uri.contains("updateUser")) {
            System.out.println("updateUser");
            resp.setContentType("application/json;charset=utf-8");
            StringBuffer sb = new StringBuffer();
            InputStream inputStream = req.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.lines().forEach(line -> {
                sb.append(line);
            });
            System.out.println("用户内容：" + sb.toString());
            //把Json转java对象
            User user = JSON.parseObject(sb.toString(),User.class);


            resp.setContentType("application/json;charset=utf-8");
            //调用update,并返回前端
            String result = null;
            try {
                result = JSON.toJSONString(userService.updateUser(user));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            PrintWriter out = resp.getWriter();
            out.write(result);
            out.flush();
            out.close();
        }
        else if (uri.contains("deleteUser")) {
            int userId = Integer.parseInt(req.getParameter("userId"));
            HttpResult httpResult = null;
            try {
                httpResult = userService.deleteUser(userId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.setContentType("application/json;charset=utf-8");
            String result = JSON.toJSONString(httpResult);
            PrintWriter out = resp.getWriter();
            out.write(result);
            out.flush();
            out.close();
        }
    }
}
