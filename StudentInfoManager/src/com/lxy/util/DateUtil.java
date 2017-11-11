package com.lxy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatDateToString(Date date,String format){
        String result="";
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        if (date!=null){
            result=sdf.format(date);
        }else {
            return "";
        }
        return result;
    }
    public static Date formatStringToDate(String date,String format) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        if (date!=null){
            return sdf.parse(date);
        }else{
            return null;
        }
    }
}
