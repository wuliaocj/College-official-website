package cn.edu.guet.proxy;

import cn.edu.guet.bean.Log;
import cn.edu.guet.http.HttpResult;
import cn.edu.guet.util.DButil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class TransactionHandler implements InvocationHandler {
    private Object target;

    public TransactionHandler(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        Proxy proxy = (Proxy) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object retVal = null;
        Connection conn = DButil.getConnection();
        try {
            // 开启事务
            conn.setAutoCommit(false);
            //执行
            retVal = method.invoke(target, args);
            //提交事务
            conn.commit();
            return retVal;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            try {
                System.out.println("有错误回滚");
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }finally {
            System.out.println("关闭conn");
            DButil.close(conn,null,null);
        }
        return null;
    }
}
