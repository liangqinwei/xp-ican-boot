package com.xp.ican.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CommonUtil {
    /**
     * 分割字符串进SET
     */
    @SuppressWarnings("unchecked")
    public static Set<String> split(String str) {

        Set<String> set = new HashSet<>();
        if (StringUtils.isEmpty(str))
            return set;
        set.addAll(CollectionUtils.arrayToList(str.split(",")));
        return set;
    }

    /**
     * 检查字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        return (str == null || str.equals("") ? true : false);
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
}
