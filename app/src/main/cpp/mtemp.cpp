#include <string.h>
#include "mtemp.h"
#include "stdlib.h"

P_TEMP initTemp() {
    P_TEMP p_temp = (P_TEMP) malloc(sizeof(TEMP));
    p_temp->envTemp = 0.0;
    p_temp->sutTemp = 0.0;
    memset(p_temp->scalingData, 0x00, (size_t) 50);
    memset(p_temp->cof, 0x00, (size_t) 50);
    memset(p_temp->bbb, 0x00, (size_t) 50);
    memset(p_temp->fancha, 0x00, (size_t) ((50 - 1) * 5));
    return p_temp;
}

void setScalingData(P_TEMP p_temp, short scalingData[]) {
    memcpy((p_temp)->scalingData, scalingData, 50 * sizeof(short));
    countCof(p_temp);
    countFan(p_temp);
}


void countCof(P_TEMP p_temp) {
    short i;
    int length = 50;
    for (i = 0; i < length - 1; i++) {
        p_temp->cof[i] =
                (float) (1000
                         / (p_temp->scalingData[i + 1] - p_temp->scalingData[i])
                         * 0.001);
        p_temp->bbb[i] = (float) (1 * i
                                  - p_temp->cof[i] * p_temp->scalingData[i]);
    }
}


void countFan(P_TEMP p_temp) {
    int i = 0;
    int length = 50;
    for (i = 0; i < length - 1; i++) {
        p_temp->fancha[i] =
                (short) ((-p_temp->bbb[i] + i * 1) / p_temp->cof[i]);
    }
}


void computxxx1(P_TEMP p_temp, long computed, float *coffic, float *bbbbbb) {
    short i;
    int length = 50;
    for (i = 0; i < length - 1; i++) {
        if ((computed >= p_temp->scalingData[i])
            && (computed <= p_temp->scalingData[i + 1])) {
            *coffic = p_temp->cof[i];
            *bbbbbb = p_temp->bbb[i];
            break;
        } else if (computed < p_temp->scalingData[0]) {
            *coffic = p_temp->cof[0];
            *bbbbbb = p_temp->bbb[0];
            break;
        } else if (computed > p_temp->scalingData[length - 1]) {
            *coffic = p_temp->cof[length - 2];
            *bbbbbb = p_temp->bbb[length - 2];
            break;
        }
    }
}

double temperature(P_TEMP p_temp, short gray, short miss, double e, double s,
                   double value) {
    double o2 = p_temp->sutTemp - s;
    double off;
    off = o2;
    double tempra; // 返回的实际温度
    gray += off * 2;
    float coffic, bbbbbb;
    computxxx1(p_temp, gray, &coffic, &bbbbbb); // 计算系数coffic和bbbbbb
    tempra = (gray * coffic + bbbbbb) + value;
    return tempra;
}
