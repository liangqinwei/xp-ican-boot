package com.xp.ican.utils;

import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.Random;

public class EncryptUtil {

    private static String KEY="QWER1234!";

    public static String MD5Pwd(String pwd){
        return DigestUtils.md5DigestAsHex((pwd+KEY).getBytes());
    }

    /**
     * 获取指定位数的随机数
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * MD5加密
     * @param content
     * @return
     */
    public static String md5(String content) {
        // 用于加密的字符
        char[] md5String = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 使用平台默认的字符集将md5String编码为byte序列,并将结果存储到一个新的byte数组中
            byte[] byteInput = content.getBytes();

            // 信息摘要是安全的单向哈希函数,它接收任意大小的数据,并输出固定长度的哈希值
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // MessageDigest对象通过使用update方法处理数据,使用指定的byte数组更新摘要
            mdInst.update(byteInput);

            //摘要更新后通过调用digest() 执行哈希计算,获得密文
            byte[] md = mdInst.digest();

            //把密文转换成16进制的字符串形式
            int j = md.length;
            char[] str = new char[j*2];
            int k = 0;
            for (int i=0;i<j;i++) {
                byte byte0 = md[i];
                str[k++] = md5String[byte0 >>> 4 & 0xf];
                str[k++] = md5String[byte0 & 0xf];
            }
            // 返回加密后的字符串
            return new String(str);
        }catch (Exception e) {

            return null;
        }

    }


    public static void main(String args[]){
        System.out.println(MD5Pwd("123456"));
    }

}
