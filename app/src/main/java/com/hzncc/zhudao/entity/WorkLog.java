package com.hzncc.zhudao.entity;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/29.
 */
public class WorkLog {
    public static String TAB_NAME = "work_log";
    public static String[] EVENTS = {"登入", "登出","电量低","内存不足","已连接电脑"};

    private int id;
    private long dateTime;
    private String dateTag;
    private String loginfo;

    public WorkLog() {
        super();
    }

    public WorkLog(int id, long dateTime, String dateTag, String loginfo) {
        this.id = id;
        this.dateTime = dateTime;
        this.dateTag = dateTag;
        this.loginfo = loginfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTag() {
        return dateTag;
    }

    public void setDateTag(String dateTag) {
        this.dateTag = dateTag;
    }

    public String getLoginfo() {
        return loginfo;
    }

    public void setLoginfo(String loginfo) {
        this.loginfo = loginfo;
    }

    @Override
    public String toString() {
        return "WorkLog{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", dateTag='" + dateTag + '\'' +
                ", loginfo='" + loginfo + '\'' +
                '}';
    }
}
