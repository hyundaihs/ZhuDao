/*
 * LeptonSDK.c
 *
 *  Created on: 2017-2-13
 *      Author: kevin
 */

#include "LeptonSDK.h"
#include "SocketManager.h"
#include "colortable.h"
#include "LeptonControl.h"

int SDK_initLepton(JNIEnv *env, jobject thiz) {
    return init(env,thiz);
}

int SDK_initSearch(SEARCH_CALLBACK callback, void *pUser) {
    return initSearch(callback, pUser);
}

int SDK_SearchDevices() {
    return searchDevices();
}

bool isDevRun(long handle) {
    return isRun(handle);
}

long openLepton(char *ip, int port) {
    long handle = openCon(ip, port);
    return handle;
}

int reOpenLepton(long handle) {
    return reOpen(handle);
}

int startLepton(long handle) {
    return startCon((long) handle);
}

int pauseLepton(long handle) {
    return pauseCon((long) handle);
}

int closeLepton(long handle) {
    return closeCon((long) handle);
}

int getLeptonNextFrame(long handle, short *array) {
    getNextFrame((long) handle, array);
    return 0;
}

int getLeptonNextFrameUChar(long handle, unsigned char *array) {
    getNextFrameUChar((long) handle, array);
    return 0;
}

int img_114To8(short *shortArray, unsigned char *charArray, int colorName) {
    setLeptonColorName(colorName);
    return _14To8(shortArray, charArray);
}

int img_116To8(unsigned char *image, unsigned char *image888, int colorName) {
    setLeptonColorName(colorName);
    return _16To8(image, image888);
}

int getLeptonMaxAndMin(short *shortArray, int *intArray) {
    int max = 0, min = 0;
    getMaxAndMin(shortArray, &max, &min);
    intArray[0] = max;
    intArray[1] = min;
    return 0;
}

double getLeptonEnvTemp(long handle) {
    return ((P_HANDLE) handle)->p_temp->envTemp;
}

double getLeptonSutTemp(long handle) {
    return ((P_HANDLE) handle)->p_temp->sutTemp;
}

int getColorsFromTable(int x, int y, int z) {
    return colorsTable[x][y][z];
}

int getLeptonColorName() {
    return getColorName();
}

void setLeptonColorName(int colorName) {
    setColorName(colorName);
}

int setLepStatus(long handle, int statu) {
    ((P_HANDLE) handle)->p_device->status = (STATUS) statu;
    return 0;
}

int getLepStatus(long handle) {
    return ((P_HANDLE) handle)->p_device->status;
}

int getCurrentFrameNum(long handle) {
    return ((P_HANDLE) handle)->p_Fps->frameId;
}

int setLeptonScalingData(long handle, short *shortArray) {
    setScalingDataByHandle((long) handle, shortArray);
    return 0;
}

double temperatureLepton(long handle, short gray, double e, double s, double value) {
    return temperatureByGray((long) handle, gray, e, s, value);
}

double getLepFps(long handle) {
    return ((P_HANDLE) handle)->p_Fps->fps;
}
