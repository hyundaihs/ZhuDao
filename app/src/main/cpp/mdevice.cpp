#include <memory.h>
#include <malloc.h>
#include "mdevice.h"

P_DEVICE initDevice() {
    P_DEVICE p_device = (P_DEVICE) malloc(sizeof(DEVICE));
    p_device->port = 6666;
    p_device->localPort = 9000;
    p_device->status = UN_CONNECT;
    p_device->name = (char *) malloc(sizeof(char) * 50);
    memset(p_device->name, 0x00, sizeof(char) * 50);
    p_device->ip = (char *) malloc(sizeof(char) * 20);
    memset(p_device->ip, 0x00, sizeof(char) * 20);
    return p_device;
}

