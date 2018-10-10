package com.hzncc.zhudao.entity;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/12/14.
 */

public class Group {
    public static String TAB_NAME = "groups";

    private long id;
    private String group_name;

    public Group() {
        super();
    }

    public Group(int id, String group_name) {
        this.id = id;
        this.group_name = group_name;
    }

    public static String getTabName() {
        return TAB_NAME;
    }

    public static void setTabName(String tabName) {
        TAB_NAME = tabName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", group_name='" + group_name + '\'' +
                '}';
    }
}
