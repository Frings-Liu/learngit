package com.lxy.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//用于将获得的数据返回并打印
public class ResponseUtil {
    //为了通用性,把原来的jsonObject改成Object了,因为我在做combobox的servlet的时候只需要返回一个jsonArray,所以会传jsonArray进来
    //不论传jsonArray还是jsonObject,最后都是tostring了再println,所以可以改成object
    public static void write(HttpServletResponse response, Object o) throws IOException {
        //给response塞一个数据头
        //来确保不出现编码问题等问题,确定输出的格式
        response.setContentType("text/html;charset=utf-8");
        //io流操作输出
        PrintWriter printWriter=response.getWriter();
        printWriter.println(o.toString());
        printWriter.flush();
        printWriter.close();
    }
}
