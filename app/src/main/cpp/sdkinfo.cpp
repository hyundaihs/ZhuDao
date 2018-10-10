#include "sdkinfo.h"


P_SDK_INFO initSdkinfo() {
    P_SDK_INFO p_sdk_info = (P_SDK_INFO) malloc(sizeof(SDK_INFO));
    int i = 0;
    for (i = 0; i < MAX_DEV_COUNT; i++) {
        p_sdk_info->p_handles[i] = initHandle(i);
        p_sdk_info->net_config[i] = (P_NET_CONFIG) malloc(sizeof(NET_CONFIG));
        p_sdk_info->ips[i] = (char *) malloc(20);
        memset(p_sdk_info->ips[i], 0x00, sizeof(char) * 20);
    }
    p_sdk_info->search_num = 0;
    p_sdk_info->isInit = false;
    p_sdk_info->handle_num = 0;
    return p_sdk_info;
}
