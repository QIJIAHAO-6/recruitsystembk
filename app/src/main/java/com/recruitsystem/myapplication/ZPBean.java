package com.recruitsystem.myapplication;


public class ZPBean {
    public String name = "欧阳威威";
    public String sex = "男";
    public String q1;
    public String q2;
    public String q3;
    public String q4;
    public String xk ="数学";

    public ZPBean(String name, String q2, String sex) {
        this.name = name;
        this.q2 = q2;
        this.sex = sex;
    }


    public ZPBean(String q1, String q2, String q3, String q4) {
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
    }

    public ZPBean() {

    }
}
