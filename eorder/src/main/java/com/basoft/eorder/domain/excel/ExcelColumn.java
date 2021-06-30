package com.basoft.eorder.domain.excel;

import java.lang.annotation.*;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:55 2019/8/22
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

    String valueKor() default "";
    String valueChn() default "";

    int col() default 0;
}
