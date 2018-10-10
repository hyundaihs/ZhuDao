//
// Created by Kevin on 16/12/2.
//

#ifndef INCLUDECDEMO_SOCKETMANAGER_H
#define INCLUDECDEMO_SOCKETMANAGER_H

#define MILLION 1000000

#include "LeptonSDK.h"
#include "message.h"
#include "mhandle.h"

typedef struct _CALLBACK_INFO_ {
    long pUser;
    SEARCH_CALLBACK callback;
} CALLBACK_INFO, *P_CALLBACK_INFO;

void openSearchReceiver(P_CALLBACK_INFO callback_info);

void openReceiver(P_HANDLE handle);

long long getSystemTime();

void initMsg(PMESSAGE_STRUCT pMsg);

extern int sendSearchCmd();

extern int sendOpenCmd(P_HANDLE handle);

extern int sendCloseCmd(P_HANDLE handle);

extern void sendHeartPacket(P_HANDLE handle);

extern int sendCorrect(P_HANDLE handle);

extern int sendMessage(P_HANDLE handle, char *message, int length);

//extern int ping(char *ip);
#endif //INCLUDECDEMO_SOCKETMANAGER_H

