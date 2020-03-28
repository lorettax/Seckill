package com.miaosha.response;

public class CommonReturnType {
    //--返回success,fail
    private String status;

    //--若status=success,则data内返回前端所需要的数据
    //--若status=fail,则data使用通用的错误码格式
    private Object data;

    //--定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setData(result);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
