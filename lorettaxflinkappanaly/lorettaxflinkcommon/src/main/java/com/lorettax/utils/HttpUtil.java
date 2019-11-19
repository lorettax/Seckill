package com.lorettax.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by li on 2018/12/11.
 */
public class HttpUtil {
    public static PrintWriter getprintWrite(HttpServletResponse resp) {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter printWriter = null;
        try {
            printWriter = resp.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printWriter;
    }

    public static void closeStream(PrintWriter printWriter){
        if(printWriter != null){
            printWriter.flush();
            printWriter.close();
        }
    }
}
