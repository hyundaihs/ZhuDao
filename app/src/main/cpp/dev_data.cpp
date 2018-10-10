#include <memory.h>
#include <malloc.h>
#include "dev_data.h"
#include "jni_util.h"

P_DEV_DATA initDevdata()
{
    P_DEV_DATA p_dev_data = (P_DEV_DATA)malloc(sizeof(DEV_DATA));
    p_dev_data->cachePoint = 0;
    p_dev_data->p_index = 0;
    p_dev_data->cachePoint_ir = 0;
    p_dev_data->p_index_ir = 0;
    memset(p_dev_data->raw_data_cache, 0x00 , DATA_RAW_SIZE * CACHE_COUNT);
    memset(p_dev_data->raw_data_cache_ir, 0x00 , DATA_RAW_SIZE_IR * CACHE_COUNT);
    return p_dev_data;
}

int getP_Index(P_DEV_DATA p_dev_data){
    return p_dev_data->p_index;
}

void addP_Index(P_DEV_DATA p_dev_data){
    p_dev_data->p_index++;
    if(p_dev_data->p_index >= ONE_FRAME_PACKET_NUM){
        p_dev_data->p_index = 0;
    }
}

void setP_Index(P_DEV_DATA p_dev_data,int in){
    p_dev_data->p_index = in;
    if(p_dev_data->p_index >= ONE_FRAME_PACKET_NUM){
        p_dev_data->p_index = 0;
    }
}

int getCachePoint(P_DEV_DATA p_dev_data){
    int temp = p_dev_data->cachePoint - 1;
    if (temp < 0) {
        temp = CACHE_COUNT - 1;
    }
    return temp;
}

void pointNext(P_DEV_DATA p_dev_data) {
    p_dev_data->cachePoint++;
    if (p_dev_data->cachePoint >= CACHE_COUNT) {
        p_dev_data->cachePoint = 0;
    }
}

void getNext(P_DEV_DATA p_dev_data, short *raw) {
    //printf("get visible\n");
    int temp = p_dev_data->cachePoint - 1;
    if (temp < 0) {
        temp = CACHE_COUNT - 1;
    }
    memcpy(raw,p_dev_data->raw_data_cache + temp * DATA_RAW_SIZE,
           DATA_RAW_SIZE);
}

void getNextUChar(P_DEV_DATA p_dev_data, unsigned char *raw){
    int temp = p_dev_data->cachePoint - 1;
    if (temp < 0) {
        temp = CACHE_COUNT - 1;
    }
    memcpy(raw,p_dev_data->raw_data_cache + temp * DATA_RAW_SIZE ,
           DATA_RAW_SIZE);
}

void saveFrame(P_DEV_DATA p_dev_data,unsigned char *receiver) {
//    LOGD("save visible\n");
    memcpy(p_dev_data->raw_data_cache + p_dev_data->cachePoint * DATA_RAW_SIZE,
           receiver, DATA_RAW_SIZE);
    pointNext(p_dev_data);
}

// ir method

int getP_Index_ir(P_DEV_DATA p_dev_data){
    return p_dev_data->p_index_ir;
}

void addP_Index_ir(P_DEV_DATA p_dev_data){
    p_dev_data->p_index_ir++;
    if(p_dev_data->p_index_ir >= ONE_FRAME_PACKET_NUM_IR){
        p_dev_data->p_index_ir = 0;
    }
}

void setP_Index_ir(P_DEV_DATA p_dev_data,int in){
    p_dev_data->p_index_ir = in;
    if(p_dev_data->p_index_ir >= ONE_FRAME_PACKET_NUM_IR){
        p_dev_data->p_index_ir = 0;
    }
}

int getCachePoint_ir(P_DEV_DATA p_dev_data){
    int temp_ir = p_dev_data->cachePoint_ir - 1;
    if (temp_ir < 0) {
        temp_ir = CACHE_COUNT - 1;
    }
    return temp_ir;
}

void pointNext_ir(P_DEV_DATA p_dev_data) {
    p_dev_data->cachePoint_ir++;
    if (p_dev_data->cachePoint_ir >= CACHE_COUNT) {
        p_dev_data->cachePoint_ir = 0;
    }
}

void getNext_ir(P_DEV_DATA p_dev_data, short *raw) {
    //printf("get infrared\n");
    int temp_ir = p_dev_data->cachePoint_ir - 1;
    if (temp_ir < 0) {
        temp_ir = CACHE_COUNT - 1;
    }
    memcpy(raw,p_dev_data->raw_data_cache_ir + temp_ir * DATA_RAW_SIZE_IR,
           DATA_RAW_SIZE_IR);
//    for(int i = 0 ;i<DATA_RAW_SIZE_IR;i++){
//        LOGD("ir = %d",raw[i]);
//    }
}

void getNextUChar_ir(P_DEV_DATA p_dev_data, unsigned char *raw){
    int temp_ir = p_dev_data->cachePoint_ir - 1;
    if (temp_ir < 0) {
        temp_ir = CACHE_COUNT - 1;
    }
    memcpy(raw,p_dev_data->raw_data_cache_ir + temp_ir * DATA_RAW_SIZE_IR ,
           DATA_RAW_SIZE_IR);
}

void saveFrame_ir(P_DEV_DATA p_dev_data,unsigned char *receiver) {
//    LOGD("save ir\n");
    memcpy(p_dev_data->raw_data_cache_ir + p_dev_data->cachePoint_ir * DATA_RAW_SIZE_IR,
           receiver, DATA_RAW_SIZE_IR);
//    for(int i = p_dev_data->cachePoint_ir * DATA_RAW_SIZE_IR ;i<DATA_RAW_SIZE_IR;i++){
//        LOGD("ir = %d",p_dev_data->raw_data_cache_ir[i]);
//    }
    pointNext_ir(p_dev_data);
}
