#ifndef INCLUDECDEMO_NET_CONFIG_H
#define INCLUDECDEMO_NET_CONFIG_H

#define MAX_RESERVED_LEN    10
#define MAX_MAC_STR_LEN        6
#define MAX_IP_STR_LEN        4
#define MAX_SERIAL_CMD_LEN    16
#define MAX_SERIAL_NUM_LEN    16

typedef struct NET_CONFIG_t {
    unsigned char mode;    // 0 for Static; 1 for Dynamic
    unsigned char mac[MAX_MAC_STR_LEN];
    unsigned char ip[MAX_IP_STR_LEN];
    unsigned char netmask[MAX_IP_STR_LEN];
    unsigned char gateway[MAX_IP_STR_LEN];
    unsigned char dns1[MAX_IP_STR_LEN];
    unsigned char dns2[MAX_IP_STR_LEN];
    char rev[5];
    short iWith;
    short iHeight;
    short iPackCnt;
    char ID[26];
} NET_CONFIG, *P_NET_CONFIG;

#endif // INCLUDECDEMO_NET_CONFIG_H
