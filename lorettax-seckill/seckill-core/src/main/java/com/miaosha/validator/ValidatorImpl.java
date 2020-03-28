package com.miaosha.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //--实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult validationResult = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);
        if(constraintViolations.size()>0){
            //--有错误
            validationResult.setHasErrors(true);
            constraintViolations.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();
                String propertyName =constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName,errMsg);
            });
        }
        return validationResult;

    }


    @Override
    public void afterPropertiesSet() throws Exception {

        //--将hibernate 的validator 通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }
}
