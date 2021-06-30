package com.basoft.core.ware.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) 
public @interface SignFieldMeta {

	/**
	 * 是否参加签名
	 * 
	 * @return
	 */
	boolean isSignField() default true;

	/**
	 * 是否必填
	 * 
	 * @return
	 */
	boolean isRequired() default true;

	/**
	 * 字段类型
	 * 
	 * @return
	 */
	SignFieldType type() default SignFieldType.String;
}
