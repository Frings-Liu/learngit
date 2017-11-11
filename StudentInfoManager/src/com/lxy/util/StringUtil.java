package com.lxy.util;

//用于处理字符串的一个工具包
public abstract class StringUtil {
    public static boolean isEmpty(String str){
        if (str.equals("")||str==null){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isNotEmpty(String str){
        if (!str.equals("")&&str!=null){
            return true;
        }else {
            return false;
        }
    }
}
