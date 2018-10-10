package com.hzncc.observerdemo.entity;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/5/3.
 */

public class Student {
    public static final String TAB_NAME = "student";
    private int id;
    private String name;
    private int age;
    private double high;
    private String school;

    public Student() {
        super();
    }

    public Student(int id, String name, int age, double high, String school) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.high = high;
        this.school = school;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", high=" + high +
                ", school='" + school + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student dst = (Student) obj;
            return id == dst.id
                    && name.equals(dst.name)
                    && age == dst.age
                    && high == dst.high
                    && school.equals(dst.school);
        } else {
            return false;
        }
    }
}
