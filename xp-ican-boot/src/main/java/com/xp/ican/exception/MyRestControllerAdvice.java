package com.xp.ican.exception;

import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.config.commonConfig.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author jacky
 * @date 2017/9/6
 */
//@ControllerAdvice

@RestControllerAdvice
public class MyRestControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private CommonConfig commonConfig;


    /**
     * Controller 所有异常捕获
     *
     * @param request
     * @param throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CommonResponse> error(HttpServletRequest request, Throwable throwable) {
        CommonResponse resp = new CommonResponse();

        ResponseEntity<CommonResponse> responseEntity = null;

        // 请求参数错误返回
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) throwable;

            StringBuilder result = new StringBuilder();
            for (ObjectError error : e.getBindingResult().getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    result.append(error.getDefaultMessage());
                }
            }

            resp.setCode(ResponseCodeNum.INVALID_PARAM.getCode());
            resp.setMsg(result.toString());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.OK);
        } else if (throwable instanceof MethodArgumentTypeMismatchException) {
            resp.setCode(ResponseCodeNum.INVALID_PARAM.getCode());
            resp.setMsg(ResponseCodeNum.INVALID_PARAM.getMessage());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.OK);
        } else if (throwable instanceof MissingServletRequestParameterException) { // 参数缺省

            resp.setCode(ResponseCodeNum.LACK_PARAM.getCode());
            resp.setMsg(ResponseCodeNum.LACK_PARAM.getMessage());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.OK);
        }
        // 业务错误返回
        else if (throwable instanceof IcanBusinessException) {

            IcanBusinessException be = (IcanBusinessException) throwable;
            resp.setCode(be.getMessageCode().getCode());
            resp.setMsg(be.getMessage());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.OK);
        } else if (throwable instanceof HttpRequestMethodNotSupportedException) {

            // 请求方法错误

            resp.setCode(ResponseCodeNum.METHOD_NOT_ALLOWED.getCode());
            resp.setMsg(ResponseCodeNum.METHOD_NOT_ALLOWED.getMessage());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (throwable instanceof NoHandlerFoundException) {

            NoHandlerFoundException e = (NoHandlerFoundException) throwable;
            resp.setCode(ResponseCodeNum.RESPONSE_NOT_FOUND.getCode());
            resp.setMsg(ResponseCodeNum.RESPONSE_NOT_FOUND.getMessage() + ":" + e.getHttpMethod() + " " + e.getRequestURL());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.NOT_FOUND);
        } else {
            // 系统出错返回

            resp.setCode(ResponseCodeNum.RESPONSE_SERVER_ERROR.getCode());
            resp.setMsg(ResponseCodeNum.RESPONSE_SERVER_ERROR.getMessage());
            responseEntity = new ResponseEntity<CommonResponse>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 封装业务内容到data字段里面
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof CommonResponse
                || ((selectedConverterType == StringHttpMessageConverter.class) && isExcludeUrl(request))
                || isExcludeUrl(request)
                || isSpecialDbcUrl(request)) {

            return body;
        } else {
            CommonResponse resp = new CommonResponse();
            resp.setData(body);
            resp.setCode(ResponseCodeNum.RESPONSE_SUCCESS.getCode());
            return resp;
        }
    }

    private boolean isExcludeUrl(ServerHttpRequest request) {
        String requestUrl = request.getURI().getPath();
        return commonConfig.getBodyExcludeUrl().stream().anyMatch(url -> antPathMatcher.match(url, requestUrl));
    }

    /**
     * 判断是否为特殊的dbc请求
     *
     * @param request
     * @return
     */
    private boolean isSpecialDbcUrl(ServerHttpRequest request) {
        String requestUrl = request.getURI().getPath();
        List<String> specialDbcUrlList = Arrays.asList("/dbc/getDbcDistinctList", "/getSignalList", "/amap/district/batch_fetch");
        return specialDbcUrlList.stream().anyMatch(url -> antPathMatcher.match(url, requestUrl));
    }


}
