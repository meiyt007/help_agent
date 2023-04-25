package com.zfsoft.ha.util;

/**
 * @Description //生成10位唯一随机数
 * @Author: Wangyh
 * @Date: 2023/3/14 11:45
 */
import java.util.Random;
public class CaRandom {
    public static void main(String args[]){

            System.out.println(GetRandomString(10));
    }

    public static String GetRandomString(int Len) {

        String[] baseString={"0","1","2","3",
                "4","5","6","7","8","9"};
        Random random = new Random();
        int length=baseString.length;
        String randomString="";
        for(int i=0;i<length;i++){
            randomString+=baseString[random.nextInt(length)];
        }
        random = new Random(System.currentTimeMillis());
        String resultStr="";
        for (int i = 0; i < Len; i++) {
            resultStr += randomString.charAt(random.nextInt(randomString.length()-1));
        }
        return resultStr;
    }

}
