#ifndef INCLUDECDEMO_MHANDLE_H
#define INCLUDECDEMO_MHANDLE_H

#include "mdevice.h"
#include "mtemp.h"
#include "dev_data.h"
#include "dev_fps.h"
#include "net_config.h"
#include "socketinfo.h"
#include "mbasic.h"

typedef struct _HANDLE_ {
    int id;
    P_DEVICE p_device;
    P_TEMP p_temp;
    P_DEV_DATA p_dev_data;
    P_FPS p_Fps;
    bool isRun;
    SOCKET client;
} HANDLE, *P_HANDLE;

P_HANDLE initHandle(int);

#endif // INCLUDECDEMO_MHANDLE_H
