package com.xp.ican.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyValidator {


    public static boolean isValidate(String name,String pattern){

        boolean isMatch=false;
        Pattern patterns=Pattern.compile(pattern);
        Matcher matcher=patterns.matcher(name);
        if(matcher.matches()){
            isMatch=true;
        }
        return isMatch;
    }

}
