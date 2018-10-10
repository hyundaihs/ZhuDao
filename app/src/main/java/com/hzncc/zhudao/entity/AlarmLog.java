package com.hzncc.zhudao.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/21.
 */
public class AlarmLog implements Parcelable {
    public static final int PICTURE = 0;
    public static final int VIDEO = 1;
    public static final Creator<AlarmLog> CREATOR = new Creator<AlarmLog>() {
        @Override
        public AlarmLog createFromParcel(Parcel in) {
            return new AlarmLog(in);
        }

        @Override
        public AlarmLog[] newArray(int size) {
            return new AlarmLog[size];
        }
    };
    public static String TAB_NAME = "alarm_log";
    private int id;
    private long dateTime;
    private int year;
    private int month;
    private int day;
    private String dateTag;
    private float cenTemp;
    private float alarmTemp1;
    private float alarmTemp2;
    private float alarmTemp3;
    private String wheelNum;
    private long videoLength;
    private int type;
    private String vlPath;
    private String irPath;
    private String vlVideoPath;
    private String irVideoPath;

    public AlarmLog() {
        super();
    }

    public AlarmLog(int id, long dateTime, int year, int month, int day, String dateTag, float cenTemp, float alarmTemp1, float alarmTemp2, float alarmTemp3, String wheelNum, long videoLength, int type, String vlPath, String irPath, String vlVideoPath, String irVideoPath) {
        this.id = id;
        this.dateTime = dateTime;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateTag = dateTag;
        this.cenTemp = cenTemp;
        this.alarmTemp1 = alarmTemp1;
        this.alarmTemp2 = alarmTemp2;
        this.alarmTemp3 = alarmTemp3;
        this.wheelNum = wheelNum;
        this.videoLength = videoLength;
        this.type = type;
        this.vlPath = vlPath;
        this.irPath = irPath;
        this.vlVideoPath = vlVideoPath;
        this.irVideoPath = irVideoPath;
    }

    protected AlarmLog(Parcel in) {
        id = in.readInt();
        dateTime = in.readLong();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        dateTag = in.readString();
        cenTemp = in.readFloat();
        alarmTemp1 = in.readFloat();
        alarmTemp2 = in.readFloat();
        alarmTemp3 = in.readFloat();
        wheelNum = in.readString();
        videoLength = in.readLong();
        type = in.readInt();
        vlPath = in.readString();
        irPath = in.readString();
        vlVideoPath = in.readString();
        irVideoPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(dateTime);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeString(dateTag);
        dest.writeFloat(cenTemp);
        dest.writeFloat(alarmTemp1);
        dest.writeFloat(alarmTemp2);
        dest.writeFloat(alarmTemp3);
        dest.writeString(wheelNum);
        dest.writeLong(videoLength);
        dest.writeInt(type);
        dest.writeString(vlPath);
        dest.writeString(irPath);
        dest.writeString(vlVideoPath);
        dest.writeString(irVideoPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmLog alarmLog = (AlarmLog) o;

        if (id != alarmLog.id) return false;
        if (dateTime != alarmLog.dateTime) return false;
        if (year != alarmLog.year) return false;
        if (month != alarmLog.month) return false;
        if (day != alarmLog.day) return false;
        if (Float.compare(alarmLog.cenTemp, cenTemp) != 0) return false;
        if (Float.compare(alarmLog.alarmTemp1, alarmTemp1) != 0) return false;
        if (Float.compare(alarmLog.alarmTemp2, alarmTemp2) != 0) return false;
        if (Float.compare(alarmLog.alarmTemp3, alarmTemp3) != 0) return false;
        if (!wheelNum.equals(alarmLog.wheelNum)) return false;
        if (videoLength != alarmLog.videoLength) return false;
        if (type != alarmLog.type) return false;
        if (!dateTag.equals(alarmLog.dateTag)) return false;
        if (!vlPath.equals(alarmLog.vlPath)) return false;
        if (!irPath.equals(alarmLog.irPath)) return false;
        if (!vlVideoPath.equals(alarmLog.vlVideoPath)) return false;
        return irVideoPath.equals(alarmLog.irVideoPath);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (dateTime ^ (dateTime >>> 32));
        result = 31 * result + year;
        result = 31 * result + month;
        result = 31 * result + day;
        result = 31 * result + dateTag.hashCode();
        result = 31 * result + (cenTemp != +0.0f ? Float.floatToIntBits(cenTemp) : 0);
        result = 31 * result + (alarmTemp1 != +0.0f ? Float.floatToIntBits(alarmTemp1) : 0);
        result = 31 * result + (alarmTemp2 != +0.0f ? Float.floatToIntBits(alarmTemp2) : 0);
        result = 31 * result + (alarmTemp3 != +0.0f ? Float.floatToIntBits(alarmTemp3) : 0);
        result = 31 * result + wheelNum.hashCode();
        result = 31 * result + (int) (videoLength ^ (videoLength >>> 32));
        result = 31 * result + type;
        result = 31 * result + vlPath.hashCode();
        result = 31 * result + irPath.hashCode();
        result = 31 * result + vlVideoPath.hashCode();
        result = 31 * result + irVideoPath.hashCode();
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTag() {
        return dateTag;
    }

    public void setDateTag(String dateTag) {
        this.dateTag = dateTag;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getAlarmTemp1() {
        return alarmTemp1;
    }

    public void setAlarmTemp1(float alarmTemp1) {
        this.alarmTemp1 = alarmTemp1;
    }

    public double getAlarmTemp2() {
        return alarmTemp2;
    }

    public void setAlarmTemp2(float alarmTemp2) {
        this.alarmTemp2 = alarmTemp2;
    }

    public double getAlarmTemp3() {
        return alarmTemp3;
    }

    public void setAlarmTemp3(float alarmTemp3) {
        this.alarmTemp3 = alarmTemp3;
    }

    public String getWheelNum() {
        return wheelNum;
    }

    public void setWheelNum(String wheelNum) {
        this.wheelNum = wheelNum;
    }

    public long getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(long videoLength) {
        this.videoLength = videoLength;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVlPath() {
        return vlPath;
    }

    public void setVlPath(String vlPath) {
        this.vlPath = vlPath;
    }

    public String getIrPath() {
        return irPath;
    }

    public void setIrPath(String irPath) {
        this.irPath = irPath;
    }

    public String getVlVideoPath() {
        return vlVideoPath;
    }

    public void setVlVideoPath(String vlVideoPath) {
        this.vlVideoPath = vlVideoPath;
    }

    public String getIrVideoPath() {
        return irVideoPath;
    }

    public void setIrVideoPath(String irVideoPath) {
        this.irVideoPath = irVideoPath;
    }

    public float getCenTemp() {
        return cenTemp;
    }

    public void setCenTemp(float cenTemp) {
        this.cenTemp = cenTemp;
    }

    @Override
    public String toString() {
        return "AlarmLog{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", dateTag='" + dateTag + '\'' +
                ", cenTemp=" + cenTemp +
                ", alarmTemp1=" + alarmTemp1 +
                ", alarmTemp2=" + alarmTemp2 +
                ", alarmTemp3=" + alarmTemp3 +
                ", wheelNum=" + wheelNum +
                ", videoLength=" + videoLength +
                ", type=" + type +
                ", vlPath='" + vlPath + '\'' +
                ", irPath='" + irPath + '\'' +
                ", vlVideoPath='" + vlVideoPath + '\'' +
                ", irVideoPath='" + irVideoPath + '\'' +
                '}';
    }
}
