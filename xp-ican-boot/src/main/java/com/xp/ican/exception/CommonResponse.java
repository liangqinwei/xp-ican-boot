package com.xp.ican.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基本返回体
 *
 * @author liangqw
 * @date 2019/9/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {

    /**
     * 返回码，详见MessageCode.java
     */
    private int code;

    /**
     * 业务内容
     */
    private Object data;

    /**
     * 错误提示
     */
    private String msg;
}
