
#include <string>

#include "LeptonSDK.h"
#include "jni_util.h"
#include "android/bitmap.h"


jintArray convertByte2Color(JNIEnv *env, jbyteArray data_);

jbyteArray fillColor2Byte(JNIEnv *env, jbyteArray data_, jint width, jint height);

jbyteArray addBMPImageInfosHeader(JNIEnv *env, int w, int h);

jbyteArray addFileHeader(JNIEnv *env, int size);

jbyteArray getColors(JNIEnv *env);

extern int colorName;

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    LOGD("Hello from C++");
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_initLepton
        (JNIEnv *env, jobject thiz) {
    LOGD("initLepton");
    return SDK_initLepton(env,thiz);
}

JNIEXPORT jboolean JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_isDevRun
        (JNIEnv *, jobject, jlong handle) {
    return (jboolean) isDevRun((long) handle);
}

JNIEXPORT jlong JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_openLepton
        (JNIEnv *env, jobject thiz, jstring ip, jint port) {
    char buf[50] = {0};
    jstring2chars(env, buf, ip);
    long handle = openLepton(buf, (int) port);
    return handle;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_reOpenLepton
        (JNIEnv *env, jobject thiz, jlong handle) {
    return reOpenLepton((long) handle);
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_startLepton
        (JNIEnv *, jobject, jlong handle) {
    return startLepton((long) handle);
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_pauseLepton
        (JNIEnv *, jobject, jlong handle) {
    return pauseLepton((long) handle);
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_closeLepton
        (JNIEnv *, jobject, jlong handle) {
    return closeLepton((long) handle);
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLeptonNextFrame
        (JNIEnv *env, jobject, jlong handle, jshortArray shortArray) {
    short *rawData = env->GetShortArrayElements(shortArray, 0);
    getLeptonNextFrame((long) handle, rawData);
    env->ReleaseShortArrayElements(shortArray, rawData, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLeptonNextFrameUChar
        (JNIEnv *env, jobject obj, jlong handle, jbyteArray byteArray) {
    unsigned char *rawData = (unsigned char *) env->GetByteArrayElements(byteArray, 0);
    getLeptonNextFrameUChar((long) handle, rawData);
    env->ReleaseByteArrayElements(byteArray, (jbyte *) rawData, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_img14To8
        (JNIEnv *env, jobject, jshortArray shortArray, jbyteArray byteArray, jint color) {
    short *rawData = env->GetShortArrayElements(shortArray, 0);
    unsigned char *bmpData = (unsigned char *) env->GetByteArrayElements(byteArray, 0);
    int result = img_114To8(rawData, bmpData, color);
    env->ReleaseShortArrayElements(shortArray, rawData, 0);
    env->ReleaseByteArrayElements(byteArray, (jbyte *) bmpData, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_img16To8
        (JNIEnv *env, jobject, jbyteArray jbyteArray1, jbyteArray jbyteArray2, jint color) {
    unsigned char *img = (unsigned char *) env->GetByteArrayElements(jbyteArray1, 0);
    unsigned char *bmpData = (unsigned char *) env->GetByteArrayElements(jbyteArray2, 0);
    int result = img_116To8(img, bmpData, color);
    env->ReleaseByteArrayElements(jbyteArray1, (jbyte *) img, 0);
    env->ReleaseByteArrayElements(jbyteArray2, (jbyte *) bmpData, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLeptonMaxAndMin
        (JNIEnv *, jobject, jshortArray, jintArray) {
    return 0;
}

JNIEXPORT jdouble JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLeptonEnvTemp
        (JNIEnv *, jobject, jlong) {
    return 0;
}

JNIEXPORT jdouble JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLeptonSutTemp
        (JNIEnv *, jobject, jlong) {
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getColorsFromTable
        (JNIEnv *, jobject, jint x, jint y, jint z) {
    return getColorsFromTable(x, y, z);
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLeptonColorName
        (JNIEnv *, jobject) {
    return 0;
}

JNIEXPORT void JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_setLeptonColorName
        (JNIEnv *, jobject, jint) {

}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_setLepStatus
        (JNIEnv *, jobject, jlong, jint) {
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLepStatus
        (JNIEnv *, jobject, jlong) {
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getCurrentFrameNum
        (JNIEnv *, jobject, jlong) {
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_setLeptonScalingData
        (JNIEnv *, jobject, jlong, jshortArray) {
    return 0;
}

JNIEXPORT jdouble JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_temperatureLepton
        (JNIEnv *, jobject, jlong, jshort, jdouble, jdouble, jdouble) {
    return 0;
}

JNIEXPORT jdouble JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_getLepFps
        (JNIEnv *, jobject, jlong) {
    return 0;
}


JNIEXPORT jintArray JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_convertByteToColor(JNIEnv *env, jclass type, jbyteArray data_) {
    return convertByte2Color(env, data_);
}


JNIEXPORT jint JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_convertByte2ColorFillBit(JNIEnv *env, jclass type,
                                                           jbyteArray data_, jobject bitmap) {
    jintArray color_ = convertByte2Color(env, data_);
    jint *color = env->GetIntArrayElements(color_, NULL);
    AndroidBitmapInfo bmpInfo = {0};
    if (AndroidBitmap_getInfo(env, bitmap, &bmpInfo) < 0) {
        return -1;
    }
    int *dataFromBmp = NULL;
    if (AndroidBitmap_lockPixels(env, bitmap, (void **) &dataFromBmp)) {
        return -1;
    }
    for (int i = 0; i < env->GetArrayLength(color_); i++) {
        dataFromBmp[i] = color[i];
    }
    AndroidBitmap_unlockPixels(env, bitmap);
    env->ReleaseIntArrayElements(color_, color, 0);
    return 0;
}


JNIEXPORT jbyteArray JNICALL
Java_com_hzncc_zhudao_jni_IR_1SDK_fillColor2Byte(JNIEnv *env, jclass type, jbyteArray data_,
                                                 jint width, jint height) {
    jbyteArray result = fillColor2Byte(env, data_, width, height);
    return result;
}


}

jbyteArray fillColor2Byte(JNIEnv *env, jbyteArray data_, jint width, jint height) {
    jint len = env->GetArrayLength(data_);
    jbyteArray header_ = addFileHeader(env, 54 + width * height);
    jint headerLen = env->GetArrayLength(header_);
    jbyteArray infohearder_ = addBMPImageInfosHeader(env, width, height);
    jint infoLen = env->GetArrayLength(infohearder_);
    jbyteArray colors_ = getColors(env);
    jint colorLen = env->GetArrayLength(colors_);
    jbyteArray result_ = env->NewByteArray(len + headerLen + infoLen + colorLen);

    jbyte *result = env->GetByteArrayElements(result_, NULL);

    jbyte *header = env->GetByteArrayElements(header_, NULL);
    for (int i = 0; i < headerLen; i++) {
        result[i] = header[i];
    }
    env->ReleaseByteArrayElements(header_, header, 0);

    jbyte *infohearder = env->GetByteArrayElements(infohearder_, NULL);
    for (int i = 0; i < infoLen; i++) {
        result[headerLen + i] = infohearder[i];
    }
    env->ReleaseByteArrayElements(infohearder_, infohearder, 0);

    jbyte *colors = env->GetByteArrayElements(colors_, NULL);
    for (int i = 0; i < colorLen; i++) {
        result[headerLen + infoLen + i] = colors[i];
    }
    env->ReleaseByteArrayElements(colors_, colors, 0);

    jbyte *data = env->GetByteArrayElements(data_, NULL);
    for (int i = 0; i < len; i++) {
        result[headerLen + infoLen + colorLen + i] = data[i];
    }
    env->ReleaseByteArrayElements(data_, data, 0);
    env->ReleaseByteArrayElements(result_, result, 0);
    return result_;
}

jbyteArray getColors(JNIEnv *env) {
    jbyteArray array = env->NewByteArray(256 * 4);
    jbyte *colors = env->GetByteArrayElements(array, NULL);
    for (int i = 0; i < 256; i++) {
        colors[(i * 4)] = (jbyte) getColorsFromTable(colorName, i, 2);
        colors[(i * 4 + 1)] = (jbyte) getColorsFromTable(colorName, i, 1);
        colors[(i * 4 + 2)] = (jbyte) getColorsFromTable(colorName, i, 0);
        colors[(i * 4 + 3)] = (jbyte) getColorsFromTable(colorName, i, 3);
    }
    env->ReleaseByteArrayElements(array, colors, 0);
}

/**
     * 头文件
     *
     * @param size
     * @return
     */
jbyteArray addFileHeader(JNIEnv *env, int size) {
    jbyteArray array = env->NewByteArray(14);
    jbyte *buffer = env->GetByteArrayElements(array, NULL);
    // magic number 'BM'
    buffer[0] = 0x42;
    buffer[1] = 0x4D;
    // 记录大小
    buffer[2] = (jbyte) (size >> 0);
    buffer[3] = (jbyte) (size >> 8);
    buffer[4] = (jbyte) (size >> 16);
    buffer[5] = (jbyte) (size >> 24);
    buffer[6] = 0x00;
    buffer[7] = 0x00;
    buffer[8] = 0x00;
    buffer[9] = 0x00;
    buffer[10] = 0x36;
    buffer[11] = 0x00;
    buffer[12] = 0x00;
    buffer[13] = 0x00;
    env->ReleaseByteArrayElements(array, buffer, 0);
    return array;
}

/**
    * BMP文件信息头
    *
    * @param w
    * @param h
    * @return
    */
jbyteArray addBMPImageInfosHeader(JNIEnv *env, int w, int h) {
    jbyteArray array = env->NewByteArray(40);
    jbyte *buffer = env->GetByteArrayElements(array, NULL);
    // 这个是固定的 BMP 信息头要40个字节
    buffer[0] = 0x28;
    buffer[1] = 0x00;
    buffer[2] = 0x00;
    buffer[3] = 0x00;
    // 宽度 地位放在序号前的位置 高位放在序号后的位置
    buffer[4] = (jbyte) (w >> 0);
    buffer[5] = (jbyte) (w >> 8);
    buffer[6] = (jbyte) (w >> 16);
    buffer[7] = (jbyte) (w >> 24);
    h = -h;
    // 长度 同上
    buffer[8] = (jbyte) (h >> 0);
    buffer[9] = (jbyte) (h >> 8);
    buffer[10] = (jbyte) (h >> 16);
    buffer[11] = (jbyte) (h >> 24);
    // 总是被设置为1
    buffer[12] = 0x00;
    buffer[13] = 0x00;
    // 比特数 像素 32位保存一个比特 这个不同的方式(ARGB 32位 RGB24位不同的!!!!)
    buffer[14] = 0x08;
    buffer[15] = 0x00;
    // 0-不压缩 1-8bit位图
    // 2-4bit位图 3-16/32位图
    // 4 jpeg 5 png
    buffer[16] = 0x00;
    buffer[17] = 0x00;
    buffer[18] = 0x00;
    buffer[19] = 0x00;
    // 说明图像大小
    buffer[20] = 0x00;
    buffer[21] = 0x00;
    buffer[22] = 0x00;
    buffer[23] = 0x00;
    // 水平分辨率
    buffer[24] = 0x00;
    buffer[25] = 0x00;
    buffer[26] = 0x00;
    buffer[27] = 0x00;
    // 垂直分辨率
    buffer[28] = 0x00;
    buffer[29] = 0x00;
    buffer[30] = 0x00;
    buffer[31] = 0x00;
    // 0 使用所有的调色板项
    buffer[32] = 0x00;
    buffer[33] = 0x00;
    buffer[34] = 0x00;
    buffer[35] = 0x00;
    // 不开颜色索引
    buffer[36] = 0x00;
    buffer[37] = 0x00;
    buffer[38] = 0x00;
    buffer[39] = 0x00;
    env->ReleaseByteArrayElements(array, buffer, 0);
    return array;
}

jintArray convertByte2Color(JNIEnv *env, jbyteArray data_) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    int length = env->GetArrayLength(data_);
    if (length == 0) {
        LOGE("data length is 0");
        return NULL;
    }
    int arg = 0;
    if (length % 3 != 0) {
        arg = 1;
    }
    int colorLength = length / 3 + arg;
    jintArray color_ = env->NewIntArray(colorLength);
    jint *color = env->GetIntArrayElements(color_, NULL);
    if (arg == 0) {
        for (int i = 0; i < colorLength; ++i) {
            color[i] = (data[i * 3 + 2] << 16 & 0x00FF0000)
                       | (data[i * 3 + 1] << 8 & 0x0000FF00)
                       | (data[i * 3] & 0x000000FF)
                       | 0xFF000000;
        }
    } else {
        for (int i = 0; i < colorLength - 1; ++i) {
            color[i] = (data[i * 3 + 2] << 16 & 0x00FF0000)
                       | (data[i * 3 + 1] << 8 & 0x0000FF00)
                       | (data[i * 3] & 0x000000FF)
                       | 0xFF000000;
        }
        color[colorLength - 1] = 0xFF000000;
    }
    env->ReleaseByteArrayElements(data_, data, 0);
    env->ReleaseIntArrayElements(color_, color, 0);
    return color_;
}
