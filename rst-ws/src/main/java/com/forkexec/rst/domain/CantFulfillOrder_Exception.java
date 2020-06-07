package com.forkexec.rst.domain;

public class CantFulfillOrder_Exception extends Exception {
    protected String msg;

    public CantFulfillOrder_Exception(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMessage(){
        return msg;
    }

    public void setMessage(String value){
        this.msg = value;
    }

}