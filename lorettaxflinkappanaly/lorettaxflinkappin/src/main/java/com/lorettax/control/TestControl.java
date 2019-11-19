package com.lorettax.control;

import com.lorettax.utils.HttpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by li on 2018/12/10.
 */
@Controller
@RequestMapping("testControl")
public class TestControl {

    @RequestMapping("sayhellword")
    public void sayhellword(HttpServletRequest req, HttpServletResponse resp){
        PrintWriter printWriter = HttpUtil.getprintWrite(resp);
        resp.setStatus(HttpStatus.OK.value());
        printWriter.write("helloworld:"+req.getRemoteAddr());
        HttpUtil.closeStream(printWriter);
    }


    @RequestMapping("checkhealth")
    public void checkhealth(HttpServletRequest req, HttpServletResponse resp){
        PrintWriter printWriter = HttpUtil.getprintWrite(resp);
        resp.setStatus(HttpStatus.OK.value());
        printWriter.write("success");
        HttpUtil.closeStream(printWriter);
    }



}
