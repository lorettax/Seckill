package com.miaosha.service.impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDoMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDoMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserModel getUserById(Integer id) {
        //--调用userDoMapper获取到对应的用户的dataobject
        UserDO userDo = userDoMapper.selectByPrimaryKey(id);
        if(userDo==null){
            return null;
        }
        //--通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());

        return convertFromDataObject(userDo,userPasswordDo);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())||
//            StringUtils.isEmpty(userModel.getTelphone())||
//                userModel.getGender()==null ||
//                userModel.getAge()==null){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult validationResult = validator.validate(userModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }

        //--实现model-->dataobject方法

        UserDO userDO = convertFromModel(userModel);
        try{
            userDoMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复注册");
        }


        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDO);

        return;

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        UserDO userDO = userDoMapper.selectByTelphone(telphone);
        if (userDO==null){
            throw new BusinessException(EmBusinessError.USER_NOT_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDoMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //--比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_NOT_FAIL);
        }
        return userModel;

    }

    @Override
    public UserModel getUserByIdInCache(Integer id) {
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get("user_validate_"+id);
        if(userModel==null){
            userModel = this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_"+id,userModel);
            redisTemplate.expire("user_validate_"+id,10, TimeUnit.MINUTES);
        }
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }


    private UserDO convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }


    private UserModel convertFromDataObject(UserDO userDo, UserPasswordDO userPasswordDo){
        if(userDo==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo,userModel);
        if(userPasswordDo!=null){
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }
        return userModel;
    }
}
