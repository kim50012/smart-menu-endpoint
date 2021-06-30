package com.basoft.eorder.application.framework;

public class CommandHandleException extends Exception {

    public static final int SYS_ERROR = 500;
    private final int errorCode;
    private final String errorMessage;

    public CommandHandleException(int errorCode,Exception ex){
        super(ex);
        this.errorCode = errorCode;
        this.errorMessage = "";
    }

    public CommandHandleException(int errorCode,String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode(){
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
