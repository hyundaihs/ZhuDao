#ifndef INCLUDECDEMO_MTEMP_H
#define INCLUDECDEMO_MTEMP_H

typedef struct _TEMP_ {
    double envTemp;
    double sutTemp;
    short scalingData[50];
    float cof[50];
    float bbb[50];
    short fancha[(50 - 1) * 5];
} TEMP, *P_TEMP;

P_TEMP initTemp();

void setScalingData(P_TEMP, short scalingData[]);

void countCof(P_TEMP);

void countFan(P_TEMP);

void computxxx1(P_TEMP, long computed, float *coffic, float *bbbbbb);

double temperature(P_TEMP, short gray, short miss, double e, double s,
                   double value);

#endif // INCLUDECDEMO_MTEMP_H
