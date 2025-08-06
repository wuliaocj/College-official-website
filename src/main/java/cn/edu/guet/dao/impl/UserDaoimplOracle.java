package cn.edu.guet.dao.impl;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.User;
import cn.edu.guet.dao.UserDao;
import cn.edu.guet.util.DButil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoimplOracle implements UserDao {
    String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
    String USER = "scott";
    String PASSWORD = "tiger";
    @Override
    public List<User> getUserList() throws SQLException {
        List<User> Users = new ArrayList<User>();
        String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
        String USER = "scott";
        String PASSWORD = "tiger";
        String sql = "SELECT user_id,user_name,user_phone FROM users";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUserName(rs.getString("user_name"));
                        user.setPhone(rs.getString("user_phone"));
                        Users.add(user);
                        System.out.println(rs.getInt("user_id")+" "+rs.getString("user_name")+" "+rs.getString("user_phone"));
                    }
                    return Users;
                }
        } catch (
                SQLException e) {
            System.err.println("数据库操作错误: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public boolean login(String username, String password) {
        String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
        String USER = "scott";
        String PASSWORD = "tiger";

        String sql = "SELECT * FROM USERS WHERE USER_NAME = ? AND PASSWORD = ?";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("登录成功");
                    return true;
                }
            }
        } catch (
                SQLException e) {
            System.err.println("数据库操作错误: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Permission> getPermissionByUsername(String username) {
        List<Permission> permissions = new ArrayList<Permission>();
        String URL = "jdbc:oracle:thin:@39.108.123.201:1521:ORCL";
        String USER = "scott";
        String PASSWORD = "tiger";

        String sql = "SELECT p.*\n" +
                "FROM users u,\n" +
                "     user_role ur,\n" +
                "     roles_permission rp,\n" +
                "     permission p\n" +
                "WHERE u.user_id=ur.user_id\n" +
                "AND ur.role_id=rp.role_id\n" +
                "AND rp.per_id=p.per_id\n" +
                "AND u.user_name=?";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setPerId(rs.getInt("per_id"));
                    permission.setPerName(rs.getString("per_name"));
                    permission.setUrl(rs.getString("url"));
                    permission.setIcon(rs.getString("icon"));
                    permission.setParent(rs.getBoolean("is_parent"));
                    permission.setParentId(rs.getInt("parent_id"));
                    permissions.add(permission);
                }
                return permissions;
            }
        } catch (
                SQLException e) {
            System.err.println("数据库操作错误: " + e.getMessage());
        }
        return permissions;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        Connection conn = DButil.getConnection();
        String sql = "UPDATE users SET user_name=?,user_phone=? WHERE user_id=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPhone());
        pstmt.setInt(3,user.getUserId());
        pstmt.executeUpdate();
        System.out.println("更新成功");
        DButil.close(null,pstmt,null);
    }

    @Override
    public void saveUser(User user) throws SQLException {
        Connection conn = DButil.getConnection();
        String sql = "INSERT INTO users(user_name,user_phone) VALUEs(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPhone());
        pstmt.executeUpdate();
        System.out.println("UserDao添加成功");
        DButil.close(null,pstmt,null);
    }



    @Override
    public void deleteUser(int userId) throws SQLException {
        Connection conn = DButil.getConnection();
        String sql = "DELETE FROM users WHERE USER_ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);

        pstmt.executeUpdate();
        DButil.close(null, pstmt, null);
    }
}
