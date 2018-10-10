#include "mhandle.h"

P_HANDLE initHandle(int id) {

    P_HANDLE handle = (P_HANDLE) malloc(sizeof(HANDLE));
    handle->id = id;
    handle->p_device = initDevice();
    handle->p_dev_data = initDevdata();
    handle->p_temp = initTemp();
    handle->p_Fps = initFps();
    handle->client = -1;
    handle->isRun = false;
    return handle;
}
