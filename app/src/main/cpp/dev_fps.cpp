#include "dev_fps.h"
#include "stdlib.h"

P_FPS initFps()
{
    P_FPS p_fps = (P_FPS) malloc(sizeof(FPS));
    p_fps->badFrame = 0;
    p_fps->fps = 0.0;
    p_fps->frameId = 0;
    p_fps->lostPack = 0;
    return p_fps;
}
