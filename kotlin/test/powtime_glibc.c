/* powtime_glibc.c - the PowTime.kt loop over the system libm (gcc -O2 powtime_glibc.c -lm). Written by Claude (Anthropic). */
#include <stdio.h>
#include <math.h>
#include <stdint.h>
#include <time.h>
static double now(void){ struct timespec t; clock_gettime(CLOCK_MONOTONIC, &t); return t.tv_sec*1e9 + t.tv_nsec; }
int main(void){ enum{N=2000000}; static double xs[N], ys[N]; uint64_t s = 0x2545F4914F6CDD1Dull;
 for (int i=0;i<N;i++){ s^=s<<13; s^=s>>7; s^=s<<17; xs[i]=0.01+100.0*((double)(s>>11)/9007199254740992.0);
   ys[i]= i%3==0 ? 5.0/3.0 : i%3==1 ? 0.5 : -2.5+5.0*((double)(s>>20)/8796093022208.0); }
 for (int r=0;r<3;r++){ volatile double a=0,b=0,c=0; double t0=now(); for(int i=0;i<N;i++) a+=pow(xs[i],ys[i]); double t1=now();
   for(int i=0;i<N;i++) b+=exp(ys[i]); double t2=now(); for(int i=0;i<N;i++) c+=sin(xs[i]); double t3=now();
   printf("glibc rep %d: pow %.1f ns, exp %.1f ns, sin %.1f ns\n", r, (t1-t0)/N, (t2-t1)/N, (t3-t2)/N); } }
