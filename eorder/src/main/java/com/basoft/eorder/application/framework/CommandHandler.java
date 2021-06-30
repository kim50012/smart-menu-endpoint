package com.basoft.eorder.application.framework;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * interface CommandHandler
 *
 * @param <T>
 */
public interface CommandHandler<T extends Command> {
    /**
     * 命令处理器的核心方法
     *
     * @param command
     * @param context
     * @return
     * @throws CommandHandleException
     */
    Object exec(T command, CommandHandlerContext context) throws CommandHandleException;

    /**
     * AutoCommandHandler注解
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface AutoCommandHandler {
        String value();
    }
}