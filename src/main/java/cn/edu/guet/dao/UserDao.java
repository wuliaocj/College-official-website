package cn.edu.guet.dao;
import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
//对用户的数据库操作
//1、验证用户
//2、添加、删除用户
//3、查询
//4、获取用户权限
public interface UserDao {
     List<User> getUserList() throws SQLException;
    boolean login(String username, String password);
    //返回类型 方法名（参数）
    //List<Permission> getPermissionByUsername(String username)
    List<Permission> getPermissionByUsername(String username);
    void updateUser(User user) throws SQLException;
    void saveUser(User user) throws SQLException;
    void deleteUser(int userId) throws SQLException;
}
