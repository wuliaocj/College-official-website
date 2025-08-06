package cn.edu.guet.ref;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //传统方式创建对象
//        Student stu = new Student("zhangsan","桂林市");
//        System.out.println(stu);
       Student stu= (Student) Class.forName("cn.edu.guet.ref.Student").newInstance();//new 新建
        System.out.println(stu);
        Class clazz = Class.forName("cn.edu.guet.ref.Student");
        Student stu1= (Student) clazz.newInstance();
        Method method = clazz.getDeclaredMethod("setName", String.class);
        method.invoke(stu,"tyq");
        System.out.println(stu);

    }
}
