package cn.edu.guet.dao.impl;

import cn.edu.guet.bean.News;
import cn.edu.guet.dao.NewsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDaoImpl implements  NewsDao {

    @Override
    public void save(News news) throws SQLException {
        String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
        String USER = "scott";
        String PASSWORD = "tiger";
        String sql = "INSERT INTO news VALUES(?,?,?,?,?,?,?,?)";
        try {
            // 加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, news.getId());
        pstmt.setString(2, news.getTitle());
        pstmt.setString(3, news.getAuthor());
        pstmt.setDate(4, news.getPubTime());
        pstmt.setInt(5, news.getClickCount());
        pstmt.setString(6, news.getContent());
        pstmt.setString(7, news.getProvider());
        pstmt.setString(8, news.getReviewer());
        pstmt.executeUpdate();
    }

    @Override
    public List<News> getNews() throws SQLException {
            String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
            String USER = "scott";
            String PASSWORD = "tiger";
            String sql = "SELECT * FROM news ORDER BY pub_time DESC";

            // 声明资源变量
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<News> newsList = new ArrayList<>();

            try {
                // 加载驱动
                Class.forName("oracle.jdbc.driver.OracleDriver");

                // 获取数据库连接
                conn = DriverManager.getConnection(URL, USER, PASSWORD);

                // 创建预编译语句
                pstmt = conn.prepareStatement(sql);

                // 执行查询
                rs = pstmt.executeQuery();

                // 处理结果集
                while (rs.next()) {
                    News news = new News();
                    news.setId(rs.getString("id"));
                    news.setTitle(rs.getString("title"));
                    news.setAuthor(rs.getString("author"));
                    news.setPubTime(rs.getDate("pub_time"));
                    news.setClickCount(rs.getInt("click_count"));
                    news.setContent(rs.getString("content"));
                    news.setProvider(rs.getString("provider"));
                    news.setReviewer(rs.getString("reviewer"));

                    newsList.add(news);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Oracle JDBC 驱动加载失败", e);
            } catch (SQLException e) {
                throw new SQLException("查询新闻列表失败: " + e.getMessage(), e);
            } finally {
                // 关闭资源
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    throw new SQLException("关闭数据库资源失败: " + e.getMessage(), e);
                }
            }
            return newsList;
    }
}


