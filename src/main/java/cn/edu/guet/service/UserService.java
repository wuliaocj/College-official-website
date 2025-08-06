package cn.edu.guet.service;

import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.User;
import cn.edu.guet.http.HttpResult;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    List<Permission> getPermissionByUsername(String username);
    List<User> getUserList() throws SQLException;
    HttpResult updateUser(User user) throws SQLException;
    HttpResult saveUser(User user) throws SQLException;
    HttpResult deleteUser(int userId) throws SQLException;
}
