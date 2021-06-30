package com.basoft.api.support;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.basoft.api.controller.BaseController;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;

/**
 * ClassName: CommonExceptionHandler 异常处理器
 *
 * @author basoft
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler extends BaseController {
    private static final String LOG_EXCEPTION_FORMAT = "Capture Exception By CommonExceptionHandler: Code: %s Detail: %s";
    private final static Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    /**
     * 校验form表单参数.
     *
     * @param e
     * @param req
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Echo<?> handleArgmentNotValidException(Exception e, WebRequest req) {
        BindingResult result = ((MethodArgumentNotValidException) e).getBindingResult();
        String message = result.getFieldError().getDefaultMessage();
        String fieldName = result.getFieldError().getField();
        String errorMsg = fieldName + ":" + message;
        logger.error(errorMsg, e);
        return new Echo(null, ErrorCode.PARAM_INVALID.getCode(), errorMsg);
    }

    // 运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Echo<?> runtimeExceptionHandler(RuntimeException ex) {
        return resultFormat(1, ex);
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    public Echo<?> nullPointerExceptionHandler(NullPointerException ex) {
        return resultFormat(2, ex);
    }

    // 类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public Echo<?> classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(3, ex);
    }

    // IO异常
    @ExceptionHandler(IOException.class)
    public Echo<?> iOExceptionHandler(IOException ex) {
        return resultFormat(4, ex);
    }

    // 未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public Echo<?> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return resultFormat(5, ex);
    }

    // 数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Echo<?> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return resultFormat(6, ex);
    }

    // 400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Echo<?> requestNotReadable(HttpMessageNotReadableException ex) {
        // System.out.println("400..requestNotReadable");
        log.error("HttpMessageNotReadableException，400..requestNotReadable");
        return resultFormat(7, ex);
    }

    // 400错误
    @ExceptionHandler({TypeMismatchException.class})
    public Echo<?> requestTypeMismatch(TypeMismatchException ex) {
        // System.out.println("400..TypeMismatchException");
        log.error("TypeMismatchException，400..TypeMismatchException");
        return resultFormat(8, ex);
    }

    // 400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Echo<?> requestMissingServletRequest(MissingServletRequestParameterException ex) {
        logger.error(ex.getMessage(), ex);
        return new Echo(null, ErrorCode.PARAM_MISSING.getCode(), ex.getMessage());
    }

    // 405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Echo<?> request405(HttpRequestMethodNotSupportedException ex) {
        return resultFormat(10, ex);
    }

    // 406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public Echo<?> request406(HttpMediaTypeNotAcceptableException ex) {
        // System.out.println("406...");
        log.error("HttpMediaTypeNotAcceptableException，406..");
        return resultFormat(11, ex);
    }

    // 500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public Echo<?> server500(RuntimeException ex) {
        // System.out.println("500...");
        log.error("ConversionNotSupportedException or HttpMessageNotWritableException，500..");
        return resultFormat(12, ex);
    }

    // 栈溢出
    @ExceptionHandler({StackOverflowError.class})
    public Echo<?> requestStackOverflow(StackOverflowError ex) {
        return resultFormat(13, ex);
    }

    // 其他错误
    @ExceptionHandler({Exception.class})
    public Echo<?> exception(Exception ex) {
        return resultFormat(14, ex);
    }

    private <T extends Throwable> Echo<?> resultFormat(Integer code, T ex) {
        try {
            logger.error(String.format(LOG_EXCEPTION_FORMAT, code, ex.getMessage()));
            logger.error(String.format(LOG_EXCEPTION_FORMAT, code, ex.getMessage()), ex);
            if (ex instanceof BizException) {
                BizException ee = (BizException) ex;
                logger.error("BizException First Info::"+ee.getExceptionErrorMsg());
                String msg = StringUtils.EMPTY;
                if (StringUtils.isBlank(ee.getExceptionErrorMsg())) {
                	String lang = getLanguage();
                	String error = StringUtils.EMPTY;
                	// 如果语言为空或中文
                	if(StringUtils.isBlank(lang) || "ZH_CN".equals(lang)) {
                		error = ee.getEc().name();
                		lang = "ZH_CN";
                	} else {
                		error = ee.getEc().name() + "_" + lang;
                	}
                	boolean isExit = false;
                	for (ErrorCode errorCode : ErrorCode.values()) {
                        if (error.equals(errorCode.name())) {
                            msg = ErrorCode.valueOf(error).getMsg();
                            isExit = true;
                            break;
                        }
                    }
                	if (!isExit) {
                        logger.info(ee.getEc().name() + "的【" + lang + "】异常信息没有配置");
                        msg = ee.getEc().getMsg();
                    }
                } else {
                    msg = ee.getExceptionErrorMsg();
                }
                //return new Echo(null, ee.getEc().getCode(), ee.getEc().getMsg());
                return new Echo(null, ee.getEc().getCode(), msg);
            } else {
                logger.error(ErrorCode.SYS_ERROR.getMsg(), ex);
                ex.printStackTrace();
                
                String lang = getLanguage();
                String error = StringUtils.EMPTY;
            	// 如果语言为空或中文
            	if(StringUtils.isBlank(lang) || "ZH_CN".equals(lang)) {
            		error = ErrorCode.SYS_ERROR.name();
            		lang = "ZH_CN";
            	} else {
            		error = ErrorCode.SYS_ERROR.name() + "_" + lang;
            	}
            	
            	int eCode = 0;
            	String msg = StringUtils.EMPTY;
            	boolean isExit = false;
            	for (ErrorCode errorCode : ErrorCode.values()) {
                    if (error.equals(errorCode.name())) {
                    	eCode = ErrorCode.valueOf(error).getCode();
                        msg = ErrorCode.valueOf(error).getMsg();
                        isExit = true;
                        break;
                    }
                }
            	if (!isExit) {
                    logger.info(ErrorCode.SYS_ERROR.name() + "的【" + lang + "】异常信息没有配置");
                    eCode = ErrorCode.SYS_ERROR.getCode();
                    msg = ErrorCode.SYS_ERROR.getMsg();
                }
                //return new Echo(null, ErrorCode.SYS_ERROR.getCode(), ErrorCode.SYS_ERROR.getMsg());
                return new Echo(null, eCode, msg);
            }
        } catch (Exception e) {
            logger.error(ErrorCode.SYS_ERROR.getMsg(), e);
            e.printStackTrace();
        }
        logger.error("异常", ex);
        // 不会执行到
        return new Echo(null, ErrorCode.SYS_ERROR.getCode(), ErrorCode.SYS_ERROR.getMsg());
    }
}
