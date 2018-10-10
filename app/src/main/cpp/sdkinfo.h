#ifndef INCLUDECDEMO_SDKINFO_H
#define INCLUDECDEMO_SDKINFO_H

#include "mhandle.h"
#include "mbasic.h"

#define MAX_DEV_COUNT 10

typedef struct _SDK_INFO_ {
    P_HANDLE p_handles[MAX_DEV_COUNT];
    P_NET_CONFIG net_config[MAX_DEV_COUNT];
    char *ips[MAX_DEV_COUNT];
    int search_num;
    int handle_num;
    bool isInit;
} SDK_INFO, *P_SDK_INFO;



P_SDK_INFO initSdkinfo();

#endif // INCLUDECDEMO_SDKINFO_H
