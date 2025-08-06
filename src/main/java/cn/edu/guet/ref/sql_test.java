package cn.edu.guet.ref;

import cn.edu.guet.bean.News;
import cn.edu.guet.dao.NewsDao;
import cn.edu.guet.dao.impl.NewsDaoImpl;
import cn.edu.guet.service.NewsService;
import cn.edu.guet.service.impl.NewsServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sql_test {

    public static void main(String[] args) throws SQLException {

        NewsService newsService = new NewsServiceImpl();

        newsService.showNews();
    }
}
