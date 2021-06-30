package com.basoft.eorder.interfaces.controller;

/**
 * ClassName: CommonExceptionHandler 异常处理器
 *
 * @author basoft
 */

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;


//@SuppressWarnings({"unchecked", "rawtypes"})
//@ControllerAdvice
@RestControllerAdvice
@ResponseBody
@Slf4j
public class CommonExceptionHandler  {
    private static final String LOG_EXCEPTION_FORMAT = "Capture Exception By CommonExceptionHandler: Code: %s Detail: %s";

    @Autowired private AppConfigure configer;


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
        log.error(errorMsg, e);
        return new Echo(null, ErrorCode.PARAM_INVALID.getCode(), errorMsg);
    }

    // 运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Echo<?> runtimeExceptionHandler(RuntimeException ex,WebRequest req) {
        return resultFormat(1, ex,req);
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    public Echo<?> nullPointerExceptionHandler(NullPointerException ex,WebRequest req) {
        return resultFormat(2, ex,req);
    }

    // 类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public Echo<?> classCastExceptionHandler(ClassCastException ex,WebRequest req) {
        return resultFormat(3, ex,req);
    }

    // IO异常
    @ExceptionHandler(IOException.class)
    public Echo<?> iOExceptionHandler(IOException ex,WebRequest req) {
        return resultFormat(4, ex,req);
    }

    // 未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public Echo<?> noSuchMethodExceptionHandler(NoSuchMethodException ex,WebRequest req) {
        return resultFormat(5, ex,req);
    }

    // 数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Echo<?> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex,WebRequest req) {
        return resultFormat(6, ex,req);
    }

    // 400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Echo<?> requestNotReadable(HttpMessageNotReadableException ex,WebRequest req) {
        // System.out.println("400..requestNotReadable");
		log.error("HttpMessageNotReadableException，400..requestNotReadable");
        return resultFormat(7, ex,req);
    }

    // 400错误
    @ExceptionHandler({TypeMismatchException.class})
    public Echo<?> requestTypeMismatch(TypeMismatchException ex,WebRequest req) {
        // System.out.println("400..TypeMismatchException");
		log.error("TypeMismatchException，400..TypeMismatchException");
        return resultFormat(8, ex,req);
    }

    // 400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Echo<?> requestMissingServletRequest(MissingServletRequestParameterException ex,WebRequest req) {
        log.error(ex.getMessage(), ex,req);

        return new Echo(null, ErrorCode.PARAM_MISSING.getCode(), ex.getMessage());
    }

    // 405错误
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Echo<?> request405(HttpRequestMethodNotSupportedException ex,WebRequest req) {
        return resultFormat(10, ex,req);
    }

    // 406错误
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public Echo<?> request406(HttpMediaTypeNotAcceptableException ex,WebRequest req) {
        // System.out.println("406...");
        log.error("HttpMediaTypeNotAcceptableException，406..");
		return resultFormat(11, ex,req);
    }

    // 500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public Echo<?> server500(RuntimeException ex,WebRequest req) {
        // System.out.println("500...");
		log.error("ConversionNotSupportedException or HttpMessageNotWritableException，500..");
        return resultFormat(12, ex,req);
    }

    // 栈溢出
    @ExceptionHandler({StackOverflowError.class})
    public Echo<?> requestStackOverflow(StackOverflowError ex,WebRequest req) {
        return resultFormat(13, ex,req);
    }

    // 其他错误
    @ExceptionHandler({Exception.class})
    public Echo<?> exception(Exception ex,WebRequest req) {
        return resultFormat(14, ex,req);
    }

    private <T extends Throwable> Echo<?> resultFormat(Integer code, T ex,WebRequest req) {
        try {
            log.error(String.format(LOG_EXCEPTION_FORMAT, code, ex.getMessage()));
            if (ex instanceof BizException) {
                BizException ee = (BizException) ex;
                log.error("BizException First Info::"+ee.getExceptionErrorMsg());
                log.error("BizException First Info::"+ee.getExceptionErrorMsg(), ex);
                String msg = StringUtils.EMPTY;
                if (StringUtils.isBlank(ee.getExceptionErrorMsg())) {
                	String lang = getLanguage(req);
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
                        log.info(ee.getEc().name() + "的【" + lang + "】异常信息没有配置");
                        msg = ee.getEc().getMsg();
                    }
                } else {
                    msg = ee.getExceptionErrorMsg();
                }
                //return new Echo(null, ee.getEc().getCode(), ee.getEc().getMsg());
                return new Echo(null, ee.getEc().getCode(), msg);
            } else {
                log.error(ErrorCode.SYS_ERROR.getMsg(), ex);
                ex.printStackTrace();
                
                String lang = getLanguage(req);
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
                    log.info(ErrorCode.SYS_ERROR.name() + "的【" + lang + "】异常信息没有配置");
                    eCode = ErrorCode.SYS_ERROR.getCode();
                    msg = ErrorCode.SYS_ERROR.getMsg();
                }
                //return new Echo(null, ErrorCode.SYS_ERROR.getCode(), ErrorCode.SYS_ERROR.getMsg());
                return new Echo(null, eCode, msg);
            }
        } catch (Exception e) {
            log.error(ErrorCode.SYS_ERROR.getMsg(), e);
            e.printStackTrace();
        }
        log.error("异常", ex);
        // 不会执行到
        return new Echo(null, ErrorCode.SYS_ERROR.getCode(), ErrorCode.SYS_ERROR.getMsg());
    }


    protected String getLanguage(WebRequest req) {

//        @Value("${basoft.client.language.header}")
//        public String languageHeader;
        String languageHeader = this.configer.get(AppConfigure.BASOFT_CLIENT_LANGUAGE_PROP);
        String lang = req.getHeader(languageHeader);
        if (StringUtils.isBlank(lang)) {
            return "ZH_CN";
        } else {
            return lang.toUpperCase().replace("-", "_");
        }
    }
}

