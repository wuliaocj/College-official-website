package cn.edu.guet.service.impl;

import cn.edu.guet.bean.Log;
import cn.edu.guet.bean.Permission;
import cn.edu.guet.bean.User;
import cn.edu.guet.dao.LogDao;
import cn.edu.guet.dao.UserDao;
import cn.edu.guet.dao.impl.LogDaoImpl;
import cn.edu.guet.dao.impl.UserDaoimplOracle;
import cn.edu.guet.http.HttpResult;
import cn.edu.guet.service.UserService;
import com.alibaba.fastjson.JSON;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserServiceImpl implements UserService {

    private LogDao logDao;
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoimplOracle();
        logDao = new LogDaoImpl();
    }


    @Override
    public List<Permission> getPermissionByUsername(String username) {

        List<Permission> permissions = userDao.getPermissionByUsername(username);

        List<Permission> secondPermissions = new ArrayList<Permission>();
        //处理一级菜单和二级菜单的关系
        permissions.forEach(permission -> {
            if(permission.getParentId()!=0){
                secondPermissions.add(permission);
            }
        });
        permissions.forEach(permission -> {
            List<Permission> tempList = new ArrayList<>();
            secondPermissions.forEach(secondPermission -> {
                if (secondPermission.getParentId() == permission.getPerId()) {
                    tempList.add(secondPermission);
                }
            });
            permission.setChildren(tempList);
        });

        //集合运算，移除二级菜单
        permissions.removeAll(secondPermissions);
        System.out.println(JSON.toJSON(permissions));
        return permissions;
    }

    @Override
    public List<User> getUserList() throws SQLException {
        return userDao.getUserList();
    }

    @Override
    public HttpResult updateUser(User user) throws SQLException {
        int Random = new Random().nextInt(200000);
        userDao.updateUser(user);
        Log log = new Log();
        log.setLogId(Random);
        log.setLogTime((java.sql.Date) new Date(System.currentTimeMillis()));
        log.setOperatorId("guet");
        log.setOperationType("updataUser");
        log.setOperationDesc("修改用户");
        log.setCreateTime((java.sql.Date) new Date(System.currentTimeMillis()));
        log.setUpdateTime((java.sql.Date) new Date(System.currentTimeMillis()));
        logDao.save(log);
        return null;
    }

    @Override
    public HttpResult saveUser(User user) throws SQLException {
        Connection conn = null;
            userDao.saveUser(user);
            Log log = new Log();
            Random random=new Random();
            int logId = random.nextInt(20000);
            log.setLogId(logId);
        log.setLogTime(new java.sql.Date(System.currentTimeMillis()));
        log.setOperatorId("tyq");
        log.setOperationType("saveUser");
        log.setOperationDesc("新增用户");
        log.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
        log.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        logDao.save(log);
        return HttpResult.ok("用户保存成功");
    }


    @Override
    public HttpResult deleteUser(int userId) throws SQLException {
        userDao.deleteUser(userId);
        Log log = new Log();
        log.setLogId(111111);
        log.setLogTime(new java.sql.Date(System.currentTimeMillis()));
        log.setOperatorId("guet");
        log.setOperationType("deleteUser");
        log.setOperationDesc("删除用户");
        log.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
        log.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        logDao.save(log);
        return HttpResult.ok("用户删除成功");
    }

}
