#ifndef INCLUDECDEMO_DEV_FPS_H
#define INCLUDECDEMO_DEV_FPS_H

typedef struct _FPS_{
    unsigned int badFrame;
    unsigned int lostPack;
    double fps;
    unsigned int frameId;
}FPS,*P_FPS;

P_FPS initFps();

#endif // INCLUDECDEMO_DEV_FPS_H
