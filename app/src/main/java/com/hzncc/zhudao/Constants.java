package com.hzncc.zhudao;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/6/19.
 */

public class Constants {
    public static final int[] IMAGE_IDS = {
            R.mipmap.home_list_menu_item_info,
            R.mipmap.home_list_menu_item_share,
            R.mipmap.home_list_menu_item_setting,
            R.mipmap.home_list_menu_item_log,
            R.mipmap.file_manager,
            R.mipmap.home_list_menu_item_correction,
            R.mipmap.home_list_menu_item_correction,
            R.mipmap.home_list_menu_item_exit,
            R.mipmap.home_list_menu_item_log};
    public static final String[] LEFT_MENUS = {
            "软件介绍", "信息分享", "设置", "工作日志", "文件管理", "校正", "设备重连", "设置编组", "中心温度"
    };
    public static final String[] RIGHT_MENUS = {
            "调色板1", "调色板2", "调色板3", "调色板4", "调色板5", "调色板6", "调色板7", "调色板8", "调色板9",
    };
    public static final int[] PALETTES = {R.mipmap.palette0, R.mipmap.palette1,
            R.mipmap.palette2, R.mipmap.palette3, R.mipmap.palette4, R.mipmap.palette5,
            R.mipmap.palette6, R.mipmap.palette7, R.mipmap.palette8,};

    /**
     * 电池刷新事件
     */
    public static final String ACTION_BATTERY_UPDATE = "com.hzncc.action.battery_update";
    /**
     * 电池电量低事件
     */
    public static final String ACTION_BATTERY_LOW = "com.hzncc.action.battery_low";
    /**
     * 电池电量满事件
     */
    public static final String ACTION_BATTERY_FULL = "com.hzncc.action.battery_full";
    /**
     * 电池电量关机事件
     */
    public static final String ACTION_OS_SHUTDOWN = "com.hzncc.action.os_shutdown";
    /**
     * SDcard存在
     */
    public static final String ACTION_SD_EXIST = "com.hzncc.action.sd_exist";
    /**
     * SDcard不存在
     */
    public static final String ACTION_SD_UNEXIST = "com.hzncc.action.sd_unexist";

    /**
     * 获取图片列表
     */
    public static final String REQUEST_GET_LIST = "com.hzncc.request_get_list";
    /**
     * 获取图片预览列表
     */
    public static final String REQUEST_GET_CACHE_IMAGES = "com.hzncc.request_get_cache_iamges";
    /**
     * 下载图片列表
     */
    public static final String REQUEST_DOWNLOAD_IMAGES = "com.hzncc.request_download_iamges";
}
