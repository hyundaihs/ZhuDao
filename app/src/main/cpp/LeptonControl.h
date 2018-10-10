//
// Created by Kevin on 16/12/12.
//

#ifndef INCLUDECDEMO_LEPTONCONTROL_H_
#define INCLUDECDEMO_LEPTONCONTROL_H_

#include "SocketManager.h"

int init(JNIEnv *env, jobject thiz);

int initSearch(SEARCH_CALLBACK callback, void *pUser);

int searchDevices();

bool isRun(long handle);

long openCon(const char *ip, int port);

int reOpen(long handle);

void setScalingDataByHandle(long handle, short scalingData[]);

double temperatureByGray(long handle, short gray, double e, double s, double value);

int startCon(long handle);

int pauseCon(long handle);

int closeCon(long handle);

int _14To8(short *data, unsigned char *bmpData);

int _16To8(unsigned char *image, unsigned char *image888);

void rgb565_2_rgb888(unsigned char *rgb24, short rgb565);

void rgb565_2_rgb24(unsigned char *rgb24, short rgb565);

void getMaxAndMin(short *grayData, int *max, int *min);

void getNextFrame(long handle, short *raw);

void getNextFrameUChar(long handle, unsigned char *raw);

void setColorsName(int colorsName);

void setColorName(int colorName);

int getColorName(void);

#endif //INCLUDECDEMO_LEPTONCONTROL_H_

