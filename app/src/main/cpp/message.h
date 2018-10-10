#ifndef INCLUDECDEMO_MESSAGE_H
#define INCLUDECDEMO_MESSAGE_H

#define MAX_RESERVED_LEN    10

/* RAW Defeinition */
typedef unsigned char UINT8;
typedef unsigned short UINT16;
typedef unsigned int UINT32;

//Basic Message Structure
typedef struct MESSAGE_STRUCT_t {
    UINT16 type;
    UINT16 dir;
    UINT16 datLen;
    UINT8 resv[MAX_RESERVED_LEN];
} MESSAGE_STRUCT, *PMESSAGE_STRUCT;

typedef struct VIDEO_TRANS_DEF_t {
    UINT16 port;
    UINT16 mode;
} VIDEO_TRANS_DEF, *PVIDEO_TRANS_DEF;

typedef enum MESSAGE_TYPE_e {
    MESSAGE_TYPE_NONE = 0x8000,
    MESSAGE_TYPE_SEARCH = 0x8001,    //Search Request
    MESSAGE_TYPE_NET_CFG = 0x8002,    //Config Target Net Configuration
    MESSAGE_TYPE_VIDEO_CFG_GET = 0x8003,    //Get Video Configuration
    MESSAGE_TYPE_VIDEO_OPEN = 0x8004,    //Open Video
    MESSAGE_TYPE_VIDEO_PAUSE = 0x8005,    //Pause Video
    MESSAGE_TYPE_VIDEO_CLOSE = 0x8006,    //Close Video
    MESSAGE_TYPE_SERIAL_CMD = 0x8007,    //Serial Command & Response
    MESSAGE_TYPE_SERIAL_NO = 0x8008,    //Get Target Serial Number
    MESSAGE_TYPE_VIDEO_MODE_SW = 0x8009,   //Video Mode Switch Command
    MESSAGE_TYPE_VIDEO_FETCH_FR = 0x800a,   //Fetch a frame from Device
} MESSAGE_TYPE;

#endif // INCLUDECDEMO_MESSAGE_H
