package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.UserModel;

public interface UserService {

    //--通过用户Id获取用户对象的方法
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;

    //通过缓存获取用户对象
    UserModel getUserByIdInCache(Integer id);
}
