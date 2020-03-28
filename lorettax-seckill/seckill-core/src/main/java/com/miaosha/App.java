package com.miaosha;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
//@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.miaosha"})
@RestController
@MapperScan("com.miaosha.Dao")
public class App 
{

    @Autowired
    private UserDOMapper userDoMapper;



    @RequestMapping("/")
    public String home(){
        UserDO userdo = userDoMapper.selectByPrimaryKey(1);
        if(userdo==null){
            return "用户对象不存在";
        }else{
            return userdo.getName();
        }

    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );


        SpringApplication.run(App.class,args);
    }
}
