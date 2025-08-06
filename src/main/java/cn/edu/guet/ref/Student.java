package cn.edu.guet.ref;

public class Student {
    private String name;
    private String address;
    public Student(){
        System.out.println("无参构造函数");
    }
    public Student(String name, String address){
        this.name = name;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
