#ifndef INCLUDECDEMO_MDEVICE_H
#define INCLUDECDEMO_MDEVICE_H

typedef enum _STATUS_ {
    DEVICE_ERROR = -4, //else error;
    REVICE_ERROR = -3, //revice data error;
    CLOSED = -2, //Revicer is closed;
    UN_CONNECT = -1, //Device is not found;
    CONNECT = 1, //Device is be found;
    OPENED = 1, //Revicer is opened;
    RUNING = 2, //data is sending;
} STATUS;

typedef struct _DEVICE_ {
    char *name;
    char *ip;
    int port;
    int localPort;
    STATUS status;
} DEVICE, *P_DEVICE;

P_DEVICE initDevice();

#endif // INCLUDECDEMO_MDEVICE_H
