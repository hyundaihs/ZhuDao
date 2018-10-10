//
// Created by Kevin on 16/12/2.
//

//#include <sys/timeb.h>
#include <sys/time.h>
#include <signal.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#include "SocketManager.h"
#include "unistd.h"
#include "sdkinfo.h"
#include "jni_util.h"

long long s_time = 0;
double p_count = 0;

extern P_SDK_INFO p_sdk_info;
extern JNIEnv *g_env;
extern jobject g_thiz;
extern JavaVM *g_jvm;

void openSearchReceiver(P_CALLBACK_INFO callback_info) {
//    (g_jvm)->AttachCurrentThread(&g_env, NULL);
    SOCKET client = socket(AF_INET, SOCK_DGRAM, IPPROTO_IP);
    if (client < 0) {
        LOGD("search revicer socket open fail\n");
        return;
    }
    SOCKADDR_IN sin;
    sin.sin_family = AF_INET;
    sin.sin_port = htons(DEVICE_SEN_PORT);
    sin.sin_addr.s_addr = INADDR_ANY;
    if (bind(client, (const sockaddr *) &sin, sizeof(SOCKADDR_IN)) == -1) {
        LOGD("search revicer socket bind fail\n");
        return;
    }
    unsigned char receiverBuf[64];
    int receiverLen = sizeof(SOCKADDR);
    while (p_sdk_info->isInit) {
        int ret = recvfrom(client, receiverBuf, 64, 0,
                           (SOCKADDR *) &sin, (socklen_t *) &receiverLen);
        if (ret < 0) {
            LOGD("search receive fail\n");
            continue;
        } else {
            LOGD("search receive success %d\n", USHRT_MAX);
            PMESSAGE_STRUCT pmessage = (PMESSAGE_STRUCT) receiverBuf;
            unsigned char temppp = (unsigned char) 128;
            unsigned short tempp = (short) 32770;
            int type = (int) pmessage->type;
            switch (type) {
                case MESSAGE_TYPE_NET_CFG: {
                    unsigned char *pData = receiverBuf + sizeof(MESSAGE_STRUCT);
                    P_NET_CONFIG p_net_config = p_sdk_info->net_config[p_sdk_info->search_num];
                    p_net_config = (P_NET_CONFIG) pData;
                    char temp[20];
                    LOGD(temp, "%d.%d.%d.%d", p_net_config->ip[0], p_net_config->ip[1],
                         p_net_config->ip[2], p_net_config->ip[3]);
                    strcpy(p_sdk_info->ips[p_sdk_info->search_num], temp);
                    p_sdk_info->search_num++;
                    callback_info->callback(temp, callback_info->pUser);
                }
                    break;
                case MESSAGE_TYPE_VIDEO_OPEN: {
                }
                    break;
                case MESSAGE_TYPE_VIDEO_CLOSE: {
                }
                    break;
                default:
                    break;
            }
        }
    }
    LOGD("socket search receive is closed\n");
//    (g_jvm)->DetachCurrentThread();
}

static void
memcpy_ex(unsigned char *dst, int dstIndex, unsigned char *src, int srcIndex, int length) {
    int i = 0;
    for (i = 0; i < length; i += 2) {
        *(dst + dstIndex + i) = *(src + srcIndex - i - 1);
        *(dst + dstIndex + i + 1) = *(src + srcIndex - i);
    }
}


void openReceiver(P_HANDLE handle) {
    //用于标记线程的状态，用于开启，释放
//    bool isAttached = false;
//    //获取当前状态，查看是否已经拥有过JNIEnv指针
//    int status = g_jvm->GetEnv((void**) &g_env, JNI_VERSION_1_4);
//    if (status < 0) {
//        status = g_jvm->AttachCurrentThread(&g_env, NULL);
//        if (status < 0)
//            return;
//        isAttached = true;
//    }
//    (g_jvm)->AttachCurrentThread(&g_env, NULL);
    LOGD("openReceiver\n");
    SOCKADDR_IN sin;
    unsigned char rawData[DATA_RAW_SIZE];
    unsigned char rawData_ir[DATA_RAW_SIZE_IR];
    int receiverLen = sizeof(SOCKADDR);
    while (handle->isRun) {
        if (handle->p_device->status != OPENED) {
            handle->p_device->status = OPENED;
        }
        P_DEV_DATA p_dev_data = handle->p_dev_data;
        unsigned char receiver[PACKAGE_SIZE];
        int ret = recvfrom(handle->client, receiver, PACKAGE_SIZE, 0,
                           (SOCKADDR *) &sin, (socklen_t *) &receiverLen);
        if (ret < 0) {
            LOGD("socket receive fail\n");
            if (handle->p_device->status != RUNING) {
                handle->p_device->status = REVICE_ERROR;
            }
        } else {
            if (handle->p_device->status != RUNING) {
                handle->p_device->status = RUNING;
            }
            PIR_PACKET packet = (PIR_PACKET) receiver;
            if (packet->type == 0) {
                //LOGD("visible\n");
                int len = ONE_FRAME_PACKET_NUM - PARAMS_RAW;
                if (packet->dataFol >= 0 && packet->dataFol < len) {
//                    int i = 0,j = 0;
//                    for(j = 0;j < 2;j++){
//                        for(i = 0;i < packet->dataLen / 2;i++){
//                          *(rawData + (((len - 1 - packet->dataFol) *
//                                      packet->dataLen ) + i + (j * packet->dataLen / 2)))
//                                    = *(receiver + (((j + 1) * packet->dataLen / 2) - 1 - i));
//                        }
//                    }
                    int i = 0;
                    for (i = 0; i < 2; i++) {
                        memcpy_ex(rawData, ((len - 1 - packet->dataFol) * packet->dataLen +
                                            (i * packet->dataLen / 2)), receiver,
                                  (((i + 1) * packet->dataLen / 2) + sizeof(IR_PACKET) - 1),
                                  packet->dataLen / 2);
                    }
//                    memcpy(rawData + ((len - 1 - packet->dataFol) * packet->dataLen),
//                           receiver + sizeof(IR_PACKET),packet->dataLen);
                    if (packet->dataFol == len - 1) {
                        setP_Index(p_dev_data, 0);
                    }
//                    if (packet->dataFol == 0 && getP_Index(p_dev_data) == (len - 1)) {
                    if (packet->dataFol == 0 ) {
                        p_count++;
//                        if(s_time == 0){
//                            s_time = getSystemTime();
//                        }else{
//                            long long e_time = getSystemTime();
//                            long long o_time = e_time - s_time;
//                            if(o_time >= 1000){
//                                double fps = p_count / o_time * 1000;
//                                handle->p_Fps->fps = fps;
//                                s_time = e_time;
//                                p_count = 0;
//                            }
//                        }
                        setP_Index(p_dev_data, 0);
                        saveFrame(p_dev_data, rawData);
                        continue;
                    } else if (packet->dataFol == 0 && getP_Index(p_dev_data) !=
                                                       (len - 1)) {
                        handle->p_Fps->badFrame++;
                        setP_Index(p_dev_data, 0);
                        LOGD("p_index is %d\n", getP_Index(p_dev_data));
                        continue;
                    } else if (packet->dataFol != 0 && getP_Index(p_dev_data) ==
                                                       (len - 1)) {
                        setP_Index(p_dev_data, 0);
                        LOGD("dataFol is %d\n", packet->dataFol);
                        handle->p_Fps->badFrame++;
                        continue;
                    }
                    addP_Index(p_dev_data);
                    handle->p_Fps->frameId = packet->frameID;
                }
            } else if (packet->type == 255) {
                //LOGD("infrared\n");
                int len = ONE_FRAME_PACKET_NUM_IR - PARAMS_RAW_IR;
                if (packet->dataFol >= 0 && packet->dataFol < len) {
                    memcpy(rawData_ir + ((len - 1 - packet->dataFol) * packet->dataLen),
                           receiver + sizeof(IR_PACKET), packet->dataLen);
                    if (packet->dataFol == len - 1) {
                        setP_Index_ir(p_dev_data, 0);
                    }
//                    if (packet->dataFol == 0 && getP_Index_ir(p_dev_data) == (len - 1)) {
                    if (packet->dataFol == 0) {
                        p_count++;
//                        if(s_time == 0){
//                            s_time = getSystemTime();
//                        }else{
//                            long long e_time = getSystemTime();
//                            long long o_time = e_time - s_time;
//                            if(o_time >= 1000){
//                                double fps = p_count / o_time * 1000;
//                                handle->p_Fps->fps = fps;
//                                s_time = e_time;
//                                p_count = 0;
//                            }
//                        }
                        setP_Index_ir(p_dev_data, 0);
                        saveFrame_ir(p_dev_data, rawData_ir);
                        continue;
                    } else if (packet->dataFol == 0 && getP_Index_ir(p_dev_data) !=
                                                       (len - 1)) {
                        handle->p_Fps->badFrame++;
                        setP_Index_ir(p_dev_data, 0);
                        LOGE("p_index is %d\n", getP_Index_ir(p_dev_data));
                        continue;
                    } else if (packet->dataFol != 0 && getP_Index_ir(p_dev_data) ==
                                                       (len - 1)) {
                        setP_Index_ir(p_dev_data, 0);
                        LOGE("dataFol is %d\n", packet->dataFol);
                        handle->p_Fps->badFrame++;
                        continue;
                    }
                    addP_Index_ir(p_dev_data);
                    handle->p_Fps->frameId = packet->frameID;
                }
            } else {
                //LOGD("else\n");
            }
        }
    }
    if (handle->p_device->status != CLOSED) {
        handle->p_device->status = CLOSED;
    }
    LOGD("socket receive is closed\n");
    //执行完了释放
//    if (isAttached)
//        g_jvm->DetachCurrentThread();
//    (g_jvm)->DetachCurrentThread();
}


void writeFile(char *fileName, char *str) {
    FILE *file;
    file = fopen(fileName, "a+");
    fwrite(str, strlen(str), 1, file);
    fclose(file);
}

//long long getSystemTime(){
//    struct timeb t;
//    ftime(&t);
//    return 1000*t.time+t.millitm;
//}

void initMsg(PMESSAGE_STRUCT pMsg) {
    if (pMsg != 0) {
        pMsg->type = MESSAGE_TYPE_NONE;
        pMsg->dir = 0;
        pMsg->datLen = 0;
        memset(pMsg->resv, 0, MAX_RESERVED_LEN);
    }
}

int sendSearchCmd() {
    SOCKET searchClient = socket(AF_INET, SOCK_DGRAM, IPPROTO_IP);
    if (searchClient < 0) {
        LOGD("socket open fail\n");
        return -1;
    }
    const int optval = 1;
    int ret = setsockopt(searchClient, SOL_SOCKET, SO_BROADCAST, (char *) &optval, sizeof(int));
    if (ret < 0) {
        LOGD("set socket  fail\n");
        return -1;
    }
    char sendBuf[sizeof(MESSAGE_STRUCT)] = {0};
    MESSAGE_STRUCT msg;
    //Message Construct
    initMsg(&msg);
    msg.type = MESSAGE_TYPE_SEARCH;
    msg.datLen = 0;
    memcpy(&sendBuf[0], &msg, sizeof(MESSAGE_STRUCT));

    //    int rel = system("echo '558600'|sudo -S");
    //    LOGD("rel = %d",rel);
    //    rel = system("sudo route add -host 255.255.255.255 dev eth0");
    //    LOGD("rel = %d",rel);

    SOCKADDR_IN sin;
    sin.sin_family = AF_INET;
    sin.sin_port = htons(DEVICE_REV_PORT);
    sin.sin_addr.s_addr = htonl(INADDR_BROADCAST);

    int result = sendto(searchClient, sendBuf, sizeof(MESSAGE_STRUCT), 0,
                        (SOCKADDR *) &sin, sizeof(sin));
    //int result = sendMessage(handle, sendBuf, sizeof(MESSAGE_STRUCT));
    if (result != -1) {
        LOGD("send search success\n");
    }
    return result;
}

int sendOpenCmd(P_HANDLE handle) {
    char sendBuf[sizeof(MESSAGE_STRUCT) + sizeof(VIDEO_TRANS_DEF)] = {0};
    MESSAGE_STRUCT msg;
    VIDEO_TRANS_DEF vCfg;
    //Message Construct
    initMsg(&msg);
    msg.type = MESSAGE_TYPE_VIDEO_OPEN;
    msg.datLen = sizeof(VIDEO_TRANS_DEF);
    vCfg.port = (UINT16) handle->p_device->localPort;
    vCfg.mode = 0;
    memcpy(&sendBuf[0], &msg, sizeof(MESSAGE_STRUCT));
    memcpy(&sendBuf[sizeof(MESSAGE_STRUCT)], &vCfg, sizeof(VIDEO_TRANS_DEF));
    LOGD("message = %s \n",sendBuf);
    int result = sendMessage(handle, sendBuf,
                             sizeof(MESSAGE_STRUCT) + sizeof(VIDEO_TRANS_DEF));
    return result;
}

int sendCloseCmd(P_HANDLE handle) {
    s_time = 0;
    p_count = 0;
    char sendBuf[sizeof(MESSAGE_STRUCT)] = {0};
    MESSAGE_STRUCT msg;
    //Message Construct
    initMsg(&msg);
    msg.type = MESSAGE_TYPE_VIDEO_CLOSE;
    msg.datLen = 0;
    memcpy(&sendBuf[0], &msg, sizeof(MESSAGE_STRUCT));

    int result = sendMessage(handle, sendBuf, sizeof(MESSAGE_STRUCT));
    return result;
}


void sendHeartPacket(P_HANDLE handle) {
    while (handle->isRun) {
        sendMessage(handle, (char *) HEART_PACKET, 0);
        sleep(3);
    }
}


int sendCorrect(P_HANDLE handle) {
    int result = sendMessage(handle, (char *) CORRECT, 0);
    return result;
}


int sendMessage(P_HANDLE handle, char *message, int length) {
    SOCKADDR_IN sin;
    sin.sin_family = AF_INET;
    sin.sin_port = htons(handle->p_device->port);
    sin.sin_addr.s_addr = inet_addr(handle->p_device->ip);
    LOGD("send %s,ip = %s,port = %d \n", message, handle->p_device->ip, handle->p_device->port);
    int result = (int) sendto(handle->client, message, (size_t) length, 0, (SOCKADDR *) &sin,
                              sizeof(SOCKADDR));
    return result;
}

