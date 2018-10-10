
#ifndef INCLUDECDEMO_LEPTONSDK_H_
#define INCLUDECDEMO_LEPTONSDK_H_

#include <jni.h>
#include "mbasic.h"

typedef void (*SEARCH_CALLBACK)(char *ip, long pUser);

int SDK_initLepton(JNIEnv *env, jobject thiz);

int SDK_initSearch(SEARCH_CALLBACK callback, void *pUser);

int SDK_SearchDevices();

bool isDevRun(long handle);

long openLepton(char *ip, int port);

int reOpenLepton(long handle);

int startLepton(long handle);

int pauseLepton(long handle);

int closeLepton(long handle);

int getLeptonNextFrame(long handle, short *array);

int getLeptonNextFrameUChar(long handle, unsigned char *array);

int img_114To8(short *shortArray, unsigned char *charArray, int colorName);

int img_116To8(unsigned char *image, unsigned char *image888, int colorName);

int getLeptonMaxAndMin(short *shortArray, int *intArray);

double getLeptonEnvTemp(long handle);

double getLeptonSutTemp(long handle);

int getColorsFromTable(int x, int y, int z);

int getLeptonColorName();

void setLeptonColorName(int colorName);

int setLepStatus(long handle, int statu);

int getLepStatus(long handle);

int getCurrentFrameNum(long handle);

int setLeptonScalingData(long handle, short *shortArray);

double temperatureLepton(long handle, short gray, double e, double s, double value);

double getLepFps(long handle);

#endif //INCLUDECDEMO_LEPTONSDK_H_




