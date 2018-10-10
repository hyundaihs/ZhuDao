#ifndef INCLUDECDEMO_DEV_DATA_H
#define INCLUDECDEMO_DEV_DATA_H

#include "image.h"

typedef struct _DEV_DATA_{
    int cachePoint;
    int p_index;
    int cachePoint_ir;
    int p_index_ir;
    unsigned char raw_data_cache[DATA_RAW_SIZE * CACHE_COUNT];
    unsigned char raw_data_cache_ir[DATA_RAW_SIZE_IR * CACHE_COUNT];
}DEV_DATA,*P_DEV_DATA;

P_DEV_DATA initDevdata();

int getP_Index(P_DEV_DATA);

void addP_Index(P_DEV_DATA);

void setP_Index(P_DEV_DATA,int in);

int getCachePoint(P_DEV_DATA);

void pointNext(P_DEV_DATA p_dev_data);

void getNext(P_DEV_DATA p_dev_data, short *raw);

void getNextUChar(P_DEV_DATA p_dev_data, unsigned char *raw);

void saveFrame(P_DEV_DATA p_dev_data,unsigned char *receiver);

int getP_Index_ir(P_DEV_DATA);

void addP_Index_ir(P_DEV_DATA);

void setP_Index_ir(P_DEV_DATA,int in);

int getCachePoint_ir(P_DEV_DATA);

void pointNext_ir(P_DEV_DATA p_dev_data);

void getNext_ir(P_DEV_DATA p_dev_data, short *raw);

void getNextUChar_ir(P_DEV_DATA p_dev_data, unsigned char *raw);

void saveFrame_ir(P_DEV_DATA p_dev_data,unsigned char *receiver);

#endif // INCLUDECDEMO_DEV_DATA_H
