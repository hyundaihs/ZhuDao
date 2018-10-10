package com.hzncc.zhudao.entity;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/6.
 */

public class User {
    public static String TAB_NAME = "user";

    private int id;
    private String name;
    private int number;
    private String password;

    public User() {
        super();
    }

    public User(int id, String name, int number, String password) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", password='" + password + '\'' +
                '}';
    }
}
