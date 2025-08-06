package cn.edu.guet.service;

import cn.edu.guet.bean.News;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface NewsService {
    void saveNews(String newsContent) throws IOException, SQLException;
    List<News> showNews() throws SQLException;
}
