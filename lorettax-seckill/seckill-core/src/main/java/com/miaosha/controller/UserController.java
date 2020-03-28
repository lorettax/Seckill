package com.miaosha.controller;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.druid.util.StringUtils;
import com.miaosha.controller.viewobject.UserVo;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //--入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
                org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //--用户登录服务，用来校验用户登录是否合法

        UserModel userModel = userService.validateLogin(telphone,this.EncodeByMd5(password));


        //修改成若用户登录成功后将对应的登录信息和登录凭证一起存入到redis

        //生成登录凭证token
        String uuidToken = UUID.randomUUID().toString();
        //建立token和用户登录态之间的关系
        redisTemplate.opsForValue().set(uuidToken,userModel);
        redisTemplate.expire(uuidToken,1, TimeUnit.HOURS);


//        //--将登录凭证加入到用户登录的Session内
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        //下发了token
        return CommonReturnType.create(uuidToken);

    }



    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Byte gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //--验证对应的手机号是否和otpCode相等
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
//        if(!StringUtils.equals(inSessionOtpCode,otpCode)){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
//        }
        //--注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setGender(gender);
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //--确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //--加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }


    //--用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //--按照一定的规则生成otp验证码

        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt +=10000;
        String otpCode = String.valueOf(randomInt);


        //--将otp验证码同对应的手机号关联,,使用httpSession的方式绑定他的手机号与otpCode
        httpServletRequest.setAttribute(telphone,otpCode);


        //--将otp验证码通过短信通道发送给用户，省略
        System.out.println("telphone = "+telphone+" & otpCode = "+otpCode);

        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {

        //--调用service服务获取对应的用户对象返回给前端
        UserModel userModel = userService.getUserById(id);

        //--若获取的对应用户信息不存在
        if(userModel==null){
//            userModel.setEncrptPassword("124");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }


        //--将核心领域模型转化为可供UI使用的viewobeject
        UserVo userVo = convertFromModel(userModel);

        return CommonReturnType.create(userVo);
    }

    private UserVo convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }


}
