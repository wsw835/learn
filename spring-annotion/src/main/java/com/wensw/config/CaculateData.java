package com.wensw.config;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

public class CaculateData {

    public static void main(String[] args) {
        //每一次自增 + 0.000001
        float Q = 1 / 1000000;
        //每一次自增 + 0.01
        float P = 0;
        //接收最终i自增到600后 M 的值
        float M = 0;
        //存放600个每次循环得到的自变量x_by_arr
        float[] x_by_arr = new float[601];

        //存放600个每次循环得到的因变量Sx
        float[] Sx_arr = new float[601];

        //初始化600个数组内数值为0
        for (int i = 0; i <= 600; i++) {
            Sx_arr[i] = 0;
        }

        // 对应s(35)
        float y_35 = 0;

        // 对应s(565)
        float y_565 = 0;
        //当满足 Sx_arr[0] > 0.0033 跳出自增0.000001的循环
        while (Math.abs(Sx_arr[0]) <= 0.0033) {
            //接收最终i自增到600后 :
            //  = > (Sx(i=1) + Sy(i=1))*250) + (Sx(i=2)+Sy(i=2))*250) + ...+ + y_565*1140 的值
            float N = 0;
            //当满足|N| > 20000 时跳出自增0.01的参数对应的循环
            while (Math.abs(N) > 20000) {
                //i循环+1至i=600
                for (int i = 0; i <= 600; i++) {

                    //最开始进循环 Sx(i) = Sy(i) = (P + i - 300) * Q
                    //自变量
                    float x_by = (P + i - 300) * Q;
                    x_by_arr[i] = x_by;
                    //结果
                    float Sx = 0;

                    // |Sx| <= 0.002 ,Sx = 23.4* ( (2 * Sx / 0.02) - ( ( Sx / 0.02) * ( Sx / 0.02) ))
                    if (Math.abs(x_by) <= 0.002) {
                        if (x_by < 0) {
                            //Sx = 23.4*(2* x_by /0.02+(x_by / 0.02)^2)
                            Sx = (float) (23.4 * ((2 * x_by / 0.002) + ((x_by / 0.002) * (x_by / 0.002))));
                        } else {
                            //Sx = 0
                            Sx = 0;
                        }
                    } else if (x_by < 0) {
                        //Sx = 23.4*(1-0.15*((Sx+0.002/(Sx-0.0013)^2))
                        Sx = (float) (23.4 * (1 - 0.15 * (((x_by + 0.002) / (x_by- 0.0013)) * ((x_by + 0.002) / (x_by- 0.0013)))));
                    } else {
                        //Sx = 0
                        Sx = 0;
                    }

                    //把每一次循环得到的Sx(i)结果存入数组
                    Sx_arr[i] = Sx;
                }

                y_35 = (P + 35 - 300) * Q;
                if (y_35 < -0.0018) {
                    y_35 = -360;

                } else {
                    y_35 = 2 * 100000 * y_35;
                }
                y_565 = (P + 565 - 300) * Q;
                if (y_565 > 0.0018) {
                    y_565 = 360;
                } else {
                    y_565 = 2 * 100000 * y_565;
                }
                //数组下标从0开始，取出对应所有下标的(Sx) *250累加的值+ y_565 * 1140
                for (int i = 0; i <= 600; i++) {
                    N = N + (Sx_arr[i]) * 250;
                }
                N = N + y_565 * 1140;
                //自增0.01
                P = (float) (P + 0.01);
            }
            //跳出循环满足->  |N| > 20000
            System.out.println("输出满足|N|  > 20000 条件下 N 的值-->" + new BigDecimal(N).setScale(5, BigDecimal.ROUND_UP).toString());
            //计算此时M的值 ： M = ( Sx(1) + * 250 * (1-300) + ( Sx(2) ) * 250 * (2-300)
            //    - (y_35 * 628 - y_565 * 1140) * 265
            for (int i = 0; i <= 600; i++) {
                M = M + (Sx_arr[i] * 250 * (i - 300));
            }
            M = M - (y_35 * 628 - y_565 * 1140) * 265;
            //满足 |N| > 20000  条件并计算完此刻M的值时，打印出当前：Q 及 M 的值
            System.out.println("输出满足|N| > 20000 条件下 Q 的值--> ： " + new BigDecimal(Q).setScale(5, BigDecimal.ROUND_UP).toString());
            System.out.println("输出满足|N| >  20000 条件下 M 的值--> : " + new BigDecimal(M).setScale(5, BigDecimal.ROUND_UP).toString());
            Q = Q + 1 / 1000000;
        }
        // 满足  M >=2.31*10^8 , 跳出循环获取M最终结果
        System.out.println("M 的 最终计算结果：---->" + M);
    }


    @Test
    public void testCount() {
        System.out.println(Math.abs(-1));
    }


}
