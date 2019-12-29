package com.xp.ican.utils;

import org.springframework.util.DigestUtils;

public class EncryptUtil {

    private static String KEY="QWER1234!";

    public static String MD5Pwd(String pwd){
        return DigestUtils.md5DigestAsHex((pwd+KEY).getBytes());
    }

    public static void main(String args[]){
        System.out.println(MD5Pwd("123456"));
    }

}
