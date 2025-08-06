package cn.edu.guet.web;
/**
 * @Author liwei
 * @Date 2024/7/5 15:41
 * @Version 1.0
 */


import cn.edu.guet.bean.News;
import cn.edu.guet.http.HttpResult;
import cn.edu.guet.service.NewsService;
import cn.edu.guet.service.impl.NewsServiceImpl;
import cn.edu.guet.vo.Data;
import cn.edu.guet.vo.WangEditorVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;


public class NewsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         doPost(req, resp);
    }

    private NewsService newsService = new NewsServiceImpl();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.contains("upload")) {
            upload(request, response);
        }
        else if (requestURI.contains("saveNews")) {
            response.setContentType("application/json;charset=utf-8");
            // 保存新闻的代码写在这里
            // request.setCharacterEncoding("utf-8");
            // String newsContent = request.getParameter("newsContent");
            // System.out.println("新闻数据：" + newsContent);
            // 自己去构建一个News类的对象，然后再调用saveNews
            // newsService.saveNews(news对象);
            StringBuffer sb = new StringBuffer();
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.lines().forEach(line -> {
                sb.append(line);
            });
            System.out.println("新闻内容：" + sb.toString());
            try {
                newsService.saveNews(sb.toString());
                // 响应结果给浏览器（JSON格式）
                String result = com.alibaba.fastjson2.JSON.toJSONString(HttpResult.ok("新闻保存成功!"));
                PrintWriter out = response.getWriter();
                out.write(result);
                out.flush();
                out.close();
            } catch (SQLException e) {
                String result = com.alibaba.fastjson2.JSON.toJSONString(HttpResult.error(400, "新闻保存失败，请联系管理员!"));
                PrintWriter out = response.getWriter();
                out.write(result);
                out.flush();
                out.close();
                throw new RuntimeException(e);
            }
        }
        else if(requestURI.contains("getNews")) {
            System.out.println("点到了查询按钮");
            try {
                response.setContentType("application/json;charset=utf-8");
                List<News> newsList = newsService.showNews();
                PrintWriter out = response.getWriter();
                System.out.println("新闻："+newsList.toString());
                String newsJson = JSON.toJSONString(newsList);
                System.out.println(newsJson);
                out.write(newsJson);
                out.flush();
                out.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void upload(HttpServletRequest request, HttpServletResponse response) {
        String dir = System.getProperty("user.dir");
        System.out.println("我的路径："+dir);
        dir = dir.substring(0, dir.lastIndexOf("\\"));
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf("\\");
        uri = uri.substring(index + 1);
        String realPath = dir + "\\webapps\\upload";
        System.out.println("实际路径：" + realPath);
        // 检查输入请求是否为multipart表单数据。
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart == true) {
            // 为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            Iterator<FileItem> itr = items.iterator();
            // String filePath=System.getProperty("user.dir")+ File.separator;
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                // 检查当前项目是普通表单项目还是上传文件。
                // 如果是普通表单项目，显示表单内容。
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String value = item.getString();
                    if (fieldName.equals("id")) {

                    } else if (fieldName.equals("title")) {

                    }
                } else {// 如果是上传文件，显示文件名。
                    File fullFile = new File(item.getName());
                    File savedFile = new File(realPath + "\\", fullFile.getName());
                    try {
                        item.write(savedFile);
                        response.setContentType("application/json;utf-8");
                        PrintWriter out = response.getWriter();
                        String url = "http://localhost:8080/upload/" + fullFile.getName();
//                        WangEditorVo editor = new WangEditorVo();
//                        System.out.println(JSON.toJSON(editor));
//                        out.print(JSON.toJSON(editor));
                        Data data = new Data();
                        data.setUrl(url);
                        WangEditorVo wangEditorVo = new WangEditorVo();
                        wangEditorVo.setData(data);
                        wangEditorVo.setErrno(0);
                        String uploadJson = JSON.toJSONString(wangEditorVo);
                        out.print(uploadJson);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.print("the enctype must be multipart/form-data");
        }
    }
}
