package com.piesat.util;

import java.io.Serializable;

/**
 *@program: backup
 *@描述 返回信息公共类
 *@author  zzj
 *@创建时间  2019/4/8 14:25
 **/
public class ResultT<T> implements Serializable {

    private int code = ReturnCodeEnum.SUCCESS.getKey();
    private String msg;
    private T data;
    private int eiCode;


    public boolean isSuccess() {
        return ReturnCodeEnum.SUCCESS.getKey() == code;
    }

    public void setErrorMessage(ReturnCodeEnum code) {
        this.code = code.getKey();
        this.msg = code.getValue();
    }

    public void setErrorMessage(String message) {
        this.code = ReturnCodeEnum.FIAL.getKey();
        this.msg = message;
    }

    public void setMessage(ReturnCodeEnum code, String message) {
        this.code = code.getKey();
        this.msg = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnT [code=" + code + ", msg=" + msg + ", result=" + data + "]";
    }

    public int getEiCode() {
        return eiCode;
    }

    public void setEiCode(int eiCode) {
        this.eiCode = eiCode;
    }
}
