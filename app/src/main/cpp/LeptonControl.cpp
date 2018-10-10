//
// Created by Kevin on 16/12/2.
//

#include <pthread.h>
#include "LeptonControl.h"
#include "sdkinfo.h"
#include "jni_util.h"

int colorName = 9;

P_SDK_INFO p_sdk_info;

JNIEnv *g_env;
jobject g_thiz;
JavaVM *g_jvm;

int init(JNIEnv *env, jobject thiz) {
    p_sdk_info = initSdkinfo();
    if (g_thiz) {
        (g_env)->DeleteGlobalRef(g_thiz);
    }
    g_thiz = (env)->NewGlobalRef(thiz);
    g_env = env;
    return 0;
}

int initSearch(SEARCH_CALLBACK callback, void *pUser) {
    if (!p_sdk_info->isInit) {
        P_CALLBACK_INFO callback_info = (P_CALLBACK_INFO) malloc(sizeof(CALLBACK_INFO));
        callback_info->callback = callback;
        callback_info->pUser = (long) pUser;
        p_sdk_info->isInit = true;

        pthread_t pthread_id;
        pthread_attr_t attr;
        pthread_attr_init(&attr);
        pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
        int ret = pthread_create(&pthread_id, &attr, (void *(*)(void *)) (void *)openSearchReceiver, callback_info);
        return ret;
    }
    return 0;
}

extern bool isRun(long handle) {
    return ((P_HANDLE) handle)->isRun;
}

int searchDevices() {
    return sendSearchCmd();
}

long openCon(const char *ip, int port) {
    int i = 0;
    for (i = 0; i < p_sdk_info->handle_num; i++) {
        if (strcmp(ip, p_sdk_info->p_handles[i]->p_device->ip) && p_sdk_info->p_handles[i]->isRun) {
            //sendOpenCmd(p_sdk_info->p_handles[i]);
            return ((long) p_sdk_info->p_handles[i]);
        }
    }
    if (p_sdk_info->handle_num >= MAX_DEV_COUNT) {
        LOGE("too many devices!");
        return -1;
    }
    SOCKET client = socket(AF_INET, SOCK_DGRAM, IPPROTO_IP);
    if (client < 0) {
        LOGE("data revicer socket open fail\n");
        return -1;
    }
    int nRecvBuf = 32 * 1024 * 1024;         //设置为32K
    if (setsockopt(client, SOL_SOCKET, SO_RCVBUF, (const char *) &nRecvBuf, sizeof(int)) < 0) {
        LOGE("set socket buffer fail\n");
        return -1;
    }

    P_HANDLE handle = p_sdk_info->p_handles[p_sdk_info->handle_num];
    SOCKADDR_IN sin;
    sin.sin_family = AF_INET;
    int bindPort = (handle->p_device->localPort + handle->id);
    sin.sin_port = htons(bindPort);
    sin.sin_addr.s_addr = INADDR_ANY;
    if (bind(client, (const sockaddr *) &sin, sizeof(SOCKADDR_IN)) == -1) {
        LOGE("data revicer socket bind fail\n");
        return -1;
    }
    handle->client = client;
    strcpy(handle->p_device->ip, ip);
    handle->p_device->port = port;
    handle->p_device->localPort = bindPort;
    reOpen((long) handle);
//    handle->isRun = true;
//    pthread_t pthread_id;
//    int ret = pthread_create(&pthread_id, NULL, (void*) &openReceiver,
//                             (P_HANDLE) handle);
//    if (ret != 0) {
//        LOGE("reOpen thread create error. code :%d\n", ret);
//    }
    p_sdk_info->handle_num++;
    return (long) handle;
}

void setScalingDataByHandle(long handle, short scalingData[]) {
    setScalingData((P_TEMP) handle, scalingData);
}

double temperatureByGray(long handle, short gray, double e, double s,
                         double value) {
    return temperature(((P_HANDLE) handle)->p_temp, gray, 100, e, s, value);
}

int reOpen(long handle) {
    if (!((P_HANDLE) handle)->isRun) {
        ((P_HANDLE) handle)->isRun = true;
        pthread_t pthread_id;
        pthread_attr_t attr;
        pthread_attr_init(&attr);
        pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
        int ret = pthread_create(&pthread_id, NULL, (void *(*)(void *)) (void *) &openReceiver,
                                 (P_HANDLE) handle);
        if (ret != 0) {
            LOGE("reOpen thread create error. code :%d\n", ret);
        }
        return 0;
    } else {
        return 1;
    }
    return -1;
}

int startCon(long handle) {
    sendOpenCmd((P_HANDLE) handle);
    return 0;
}

int pauseCon(long handle) {
    sendCloseCmd((P_HANDLE) handle);
    return 0;
}

int closeCon(long handle) {
    P_HANDLE p_handle = (P_HANDLE) handle;
    if (p_handle->p_device->status > CLOSED) {
        pauseCon(handle);
    }
    p_handle->isRun = false;
    if (p_handle->p_device->status != UN_CONNECT) {
        LOGE("close set un_connect\n");
        p_handle->p_device->status = UN_CONNECT;
    }
    return 0;
}

void getNextFrame(long handle, short *raw) {
    getNext_ir(((P_HANDLE) handle)->p_dev_data, raw);
}

void getNextFrameUChar(long handle, unsigned char *raw) {
    getNextUChar(((P_HANDLE) handle)->p_dev_data, raw);
}

int _14To8(short *grayData, unsigned char *bmpData) {
    unsigned char m_p8BitBmp[GRAY_SIZE_IR];

    short *pSrc = grayData;
    int nLen = GRAY_SIZE_IR;
    int nSum = 0;
    int nMin = 0;
    int nMax = 0;
    int nSigma = 0;

    int pHist[HIST_DEFAULT];
    memset((void *) pHist, 0, HIST_DEFAULT * sizeof(int));
    int i = 0;
    for (i = 0; i < nLen; i++) {
        pHist[pSrc[i] + AD_OFFSET_VALUE]++;
    }
    nSum = 0;
    //
    int cDicardADPerCentBottom = 1;
    nSigma = nLen * ((cDicardADPerCentBottom * 1.0) / 100);
    for (i = 0; i < HIST_DEFAULT; i++) {
        nSum += pHist[i];
        if (nSum >= nSigma) {
            nMin = i;
            break;
        }
    }

    nSum = 0;
    int cDicardADPerCentTop = 1;
    nSigma = nLen * ((cDicardADPerCentTop * 1.0) / 100);
    for (i = HIST_DEFAULT - 1; i >= 0; i--) {
        nSum += pHist[i];
        if (nSum >= nSigma) {
            nMax = i;
            break;
        }
    }

    nMin -= AD_OFFSET_VALUE;
    nMax -= AD_OFFSET_VALUE;
    if (1) {

        float K = (float) ((220.0 - 30) / (nMax - nMin + 1)); //对比度
        float C = (float) ((30.0 * nMax - 220 * nMin) / (nMax - nMin + 1));//亮度



        if (K > 1.5) {
            K = (float) 1.5;
            int nSigma = 0.5 * nLen;
            int nSum = 0;
            int nMid = 0;

            for (i = 0; i < HIST_DEFAULT; i++) {
                nSum += pHist[i];
                if (nSum >= nSigma) {
                    nMid = i;
                    break;
                }
            }

            nMid -= AD_OFFSET_VALUE;
            C = (float) (128 - K * nMid);
        }


        for (i = 0; i < nLen; i++) {
            int nValue = (int) (K * pSrc[i] + C);
            if (nValue < 0) {
                m_p8BitBmp[i] = 0;
            } else if (nValue > 255) {
                m_p8BitBmp[i] = 255;
            } else {
                m_p8BitBmp[i] = (unsigned char) nValue;
            }
        }

        memcpy(bmpData, m_p8BitBmp, GRAY_SIZE_IR);
    }
    return 0;
}

int _16To8(unsigned char *image, unsigned char *image888) {
    short *temp = (short *) image;
    int i = 0, j = 0;
    for (i = 0; i < GRAY_SIZE; i++) {
        rgb565_2_rgb24(image888 + j, temp[i]);
        j += 3;
    }
    return 0;
}

void rgb565_2_rgb888(unsigned char *rgb24, short rgb565) {

}

void rgb565_2_rgb24(unsigned char *rgb24, short rgb565) {
#if 0
    rgb24[2] = (rgb565 & RGB565_MASK_RED) >> 11;
    rgb24[1] = (rgb565 & RGB565_MASK_GREEN) >> 5;
    rgb24[0] = (rgb565 & RGB565_MASK_BLUE);

    rgb24[2] <<= 3;
    rgb24[1] <<= 2;
    rgb24[0] <<= 3;
#endif
    rgb24[0] = (rgb565 & RGB565_MASK_RED) >> 8;
    rgb24[1] = (rgb565 & RGB565_MASK_GREEN) >> 3;
    rgb24[2] = (rgb565 & RGB565_MASK_BLUE) << 3;


    rgb24[0] = (rgb24[0] & 0xF8) | rgb24[0] >> 5;
    rgb24[1] = (rgb24[1] & 0xFC) | rgb24[1] >> 6;
    rgb24[2] = (rgb24[2] & 0xF8) | rgb24[2] >> 5;
}

void getMaxAndMin(short *grayData, int *max, int *min) {
    int i = 0;
    short nMin = 9999, nMax = 0;
    for (i = 0; i < GRAY_SIZE; i++) {
        if (nMin > grayData[i]) {
            nMin = grayData[i];
        }
        if (nMax < grayData[i]) {
            nMax = grayData[i];
        }
        // sum += huiduTempByte[i];// 计算和pp
    }
    if (nMax <= nMin) {
        nMax = (short) (nMin + 1);
    }

    for (i = 0; i < GRAY_SIZE; i++) { // 计算最高温坐标
        if (nMax == grayData[i]) {
            *max = i;
        }
        if (nMin == grayData[i]) {
            *min = i;
        }
    }
}

/**
 * BMP文件信息头
 *
 * @param w
 * @param h
 * @return
 */
//void addBMPImage_Info_Header(int w, int h) {
//    // 这个是固定的 BMP 信息头要40个字节
//    infohearder[0] = 0x28;
//    infohearder[1] = 0x00;
//    infohearder[2] = 0x00;
//    infohearder[3] = 0x00;
//    // 宽度 地位放在序号前的位置 高位放在序号后的位置
//    infohearder[4] = (unsigned char) (w >> 0);
//    infohearder[5] = (unsigned char) (w >> 8);
//    infohearder[6] = (unsigned char) (w >> 16);
//    infohearder[7] = (unsigned char) (w >> 24);
//    h = -h;
//    // 长度 同上
//    infohearder[8] = (unsigned char) (h >> 0);
//    infohearder[9] = (unsigned char) (h >> 8);
//    infohearder[10] = (unsigned char) (h >> 16);
//    infohearder[11] = (unsigned char) (h >> 24);
//    // 总是被设置为1
//    infohearder[12] = 0x00;
//    infohearder[13] = 0x00;
//    // 比特数 像素 32位保存一个比特 这个不同的方式(ARGB 32位 RGB24位不同的!!!!)
//    infohearder[14] = 0x08;
//    infohearder[15] = 0x00;
//    // 0-不压缩 1-8bit位图
//    // 2-4bit位图 3-16/32位图
//    // 4 jpeg 5 png
//    infohearder[16] = 0x00;
//    infohearder[17] = 0x00;
//    infohearder[18] = 0x00;
//    infohearder[19] = 0x00;
//    // 说明图像大小
//    infohearder[20] = 0x00;
//    infohearder[21] = 0x00;
//    infohearder[22] = 0x00;
//    infohearder[23] = 0x00;
//    // 水平分辨率
//    infohearder[24] = 0x00;
//    infohearder[25] = 0x00;
//    infohearder[26] = 0x00;
//    infohearder[27] = 0x00;
//    // 垂直分辨率
//    infohearder[28] = 0x00;
//    infohearder[29] = 0x00;
//    infohearder[30] = 0x00;
//    infohearder[31] = 0x00;
//    // 0 使用所有的调色板项
//    infohearder[32] = 0x00;
//    infohearder[33] = 0x00;
//    infohearder[34] = 0x00;
//    infohearder[35] = 0x00;
//    // 不开颜色索引
//    infohearder[36] = 0x00;
//    infohearder[37] = 0x00;
//    infohearder[38] = 0x00;
//    infohearder[39] = 0x00;
//}

/**
 * 头文件
 *
 * @param size
 * @return
 */
//void addFileHeader(int size) {
//    // magic number 'BM'
//    heardData[0] = 0x42;
//    heardData[1] = 0x4D;
//    // 记录大小
//    heardData[2] = (unsigned char) (size >> 0);
//    heardData[3] = (unsigned char) (size >> 8);
//    heardData[4] = (unsigned char) (size >> 16);
//    heardData[5] = (unsigned char) (size >> 24);
//    heardData[6] = 0x00;
//    heardData[7] = 0x00;
//    heardData[8] = 0x00;
//    heardData[9] = 0x00;
//    heardData[10] = 0x36;
//    heardData[11] = 0x00;
//    heardData[12] = 0x00;
//    heardData[13] = 0x00;
//}

void setColorName(int c) {
    colorName = c;
    //    setColorsName(colorName);
}

int getColorName(void) {
    return colorName;
}

//void setColorsName(int colorsName) {
//    int i = 0;
//    for (i = 0; i < 256; i++) {
//        colors[(i * 4)] = (unsigned char) (colorsTable[colorsName][i][2]);
//        colors[(i * 4 + 1)] = (unsigned char) (colorsTable[colorsName][i][1]);
//        colors[(i * 4 + 2)] = (unsigned char) (colorsTable[colorsName][i][0]);
//        colors[(i * 4 + 3)] = (unsigned char) (colorsTable[colorsName][i][3]);
//    }
//}
