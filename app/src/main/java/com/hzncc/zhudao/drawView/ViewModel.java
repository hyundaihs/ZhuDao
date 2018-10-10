package com.hzncc.zhudao.drawView;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/4/13.
 */

public enum  ViewModel{
    VL(0), IR(1);
    private final int value;

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    ViewModel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
