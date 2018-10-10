//
// Created by Administrator on 2017/4/20.
//

#include <jni.h>
#include <android/log.h>

#ifndef INCLUDECDEMO_JNI_UTIL_H
#define INCLUDECDEMO_JNI_UTIL_H


#define TAG "native-tag" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

/**
 * jstring 2 char[]
 * @param env
 * @param buf char* buffer
 * @param string source data
 */
void jstring2chars(JNIEnv *env, char *buf, jstring string);
#endif //INCLUDECDEMO_JNI_UTIL_H
