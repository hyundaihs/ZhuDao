#ifndef INCLUDECDEMO_IMAGE_H
#define INCLUDECDEMO_IMAGE_H

typedef struct _PACKET_ {
    char ver[8];                //版本信息
    unsigned char type;
    char temp1[9];                //1号温传温度（快门温度）
    char temp2[9];                //2号温传温度（环境温度）
    char res[5];                    //预留字节
    unsigned int frameID;
    unsigned short dataLen;       //包数据长度  164*6
    unsigned short dataFol;
    unsigned short packetID;
    char res1[22];					//预留字节
} IR_PACKET, *PIR_PACKET;

#define IMG_WIDTH   320//640//336//80
#define IMG_HEIGHT  240//512//256//60
#define ONE_FRAME_PACKET_NUM 120//514//128//10
#define PACKET_DATA_RAW 2//6
#define PARAMS_RAW 0;
#define DATA_RAW_SIZE ((IMG_WIDTH * PACKET_DATA_RAW) * ONE_FRAME_PACKET_NUM * 2)
#define IMG_RAW_SIZE  (IMG_WIDTH * IMG_HEIGHT * 2)
#define IMG_PACKAGE_SIZE (IMG_WIDTH * PACKET_DATA_RAW * 2)
#define PACKAGE_SIZE (IMG_PACKAGE_SIZE + sizeof(IR_PACKET))
#define GRAY_SIZE IMG_WIDTH * IMG_HEIGHT

#define IMG_WIDTH_IR   80
#define IMG_HEIGHT_IR  60
#define ONE_FRAME_PACKET_NUM_IR 10
#define PACKET_DATA_RAW_IR 6
#define PARAMS_RAW_IR 0;
#define DATA_RAW_SIZE_IR ((IMG_WIDTH_IR * PACKET_DATA_RAW_IR) * ONE_FRAME_PACKET_NUM_IR * 2)
#define IMG_RAW_SIZE_IR  (IMG_WIDTH_IR * IMG_HEIGHT_IR * 2)
#define IMG_PACKAGE_SIZE_IR (IMG_WIDTH_IR * PACKET_DATA_RAW_IR * 2)
#define PACKAGE_SIZE_IR (IMG_PACKAGE_SIZE_IR + sizeof(IR_PACKET))
#define GRAY_SIZE_IR IMG_WIDTH_IR * IMG_HEIGHT_IR

#define CACHE_COUNT 5
#define DEVICE_COUNT 5

#define HIST_DEFAULT 65536
#define AD_OFFSET_VALUE 32768

#define RGB565_MASK_RED 0xF800
#define RGB565_MASK_GREEN 0x07E0
#define RGB565_MASK_BLUE 0x001F

#endif // INCLUDECDEMO_IMAGE_H
