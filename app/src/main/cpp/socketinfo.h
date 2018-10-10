#ifndef INCLUDECDEMO_SOCKETINFO_H
#define INCLUDECDEMO_SOCKETINFO_H

#include <sys/socket.h>
#include <arpa/inet.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>


#define SOCKADDR_IN struct sockaddr_in
#define SOCKADDR struct sockaddr
#define SOCKET int

#define OPEN "OpenIRVideo24"
#define CLOSE "CloseIRVideo88"
#define HEART_PACKET "CWDde"
#define CORRECT "DoFFC82"

#define DEVICE_REV_PORT 50001
#define DEVICE_SEN_PORT 60001

#endif // INCLUDECDEMO_SOCKETINFO_H
