package com.xp.ican.exception;


import com.xp.ican.common.constants.ResponseCodeNum;

/**
 *
 * @author jacky
 * @date 2017/4/13
 */
public class IcanBusinessException extends Exception {

    private String message;

    private ResponseCodeNum msgCode;

    public IcanBusinessException() {
    }

    public IcanBusinessException(ResponseCodeNum msgCode, Object... args) {
        this.msgCode = msgCode;
        this.message = String.format(msgCode.getMessage(), args);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public ResponseCodeNum getMessageCode() {
        return this.msgCode;
    }
}
