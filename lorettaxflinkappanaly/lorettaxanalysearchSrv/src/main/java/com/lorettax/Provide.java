package com.lorettax;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by li on 2018/12/17.
 */
public class Provide {
        public static void main(String[] args) throws Exception {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                    new String[] {"application-context-provide.xml","application-context.xml"});
            context.start();
            // press any key to exit
            System.in.read();
        }
}
