package com.basoft.eorder.application.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class MultiCommandHandler implements CommandHandler<Command> {
    /*************************************handlerMap定义和初始化--START*************************************/
    // 存储命令和命令对应的命令处理器
    private Map<String, SAutoCommandHandler> handlerMap;

    /**
     * 构造方法：初始化handlerMap
     *
     * @param objects
     */
    public MultiCommandHandler(List<Object> objects) {
        this.handlerMap = get(objects);
    }

    /**
     * 构造方法：初始化handlerMap
     *
     * @param ssh
     */
    public MultiCommandHandler(Object ssh) {
        this.handlerMap = getObject(ssh);
    }

    protected Map<String, SAutoCommandHandler> getObject(Object single) {
        ArrayList<Object> obsList = new ArrayList<>();
        obsList.add(single);
        return get(obsList);
    }

    protected Map<String, SAutoCommandHandler> get(List<Object> handlers) {
        Map<String, SAutoCommandHandler> handlerMap = new HashMap<>();
        for (Object handler : handlers) {
            final Method[] methods = handler.getClass().getDeclaredMethods();
            for (Method m : methods) {
                final AutoCommandHandler annotation = getMethodAnnotation(m);
                if (annotation != null) {
                    final Class<?>[] parameterTypes = m.getParameterTypes();
                    if (parameterTypes != null) {
                        if (parameterTypes.length == 1) {
                            if (!Command.class.isAssignableFrom(parameterTypes[0])) {
                                throw new IllegalArgumentException("Method:" + m.getName() + "  parameter  must implements of :" + Command.class.getName());
                            }
                            //System.out.println(annotation.value());
                            Class<? extends Command> commandClass = (Class<? extends Command>) parameterTypes[0];
                            handlerMap.put(annotation.value(), new SingleSAutoCommandHandler(annotation.value(), handler, m, commandClass));
                        } else if (parameterTypes.length == 2) {
                            if (Command.class.isAssignableFrom(parameterTypes[0]) && CommandHandlerContext.class.isAssignableFrom(parameterTypes[1])) {
                                Class<? extends Command> commandClass = (Class<? extends Command>) parameterTypes[0];
                                handlerMap.put(annotation.value(), new SAutoCommandHandler(annotation.value(), handler, m, commandClass));
                            } else if (CommandHandlerContext.class.isAssignableFrom(parameterTypes[0]) && Command.class.isAssignableFrom(parameterTypes[1])) {
                                Class<? extends Command> commandClass = (Class<? extends Command>) parameterTypes[1];
                                handlerMap.put(annotation.value(), new SAutoCommandHandler(annotation.value(), handler, m, commandClass));
                            } else {
                                throw new IllegalArgumentException("Method:" + m.getName() +
                                        "  first parameter  must implements of :" + Command.class.getName() +
                                        "  and second parameter must implements of " + CommandHandlerContext.class.getName());
                            }
                        }
                    } else
                        throw new IllegalStateException("AutoCommandHandler.Method.Parameter type error:" + parameterTypes[0]);
                }
            }
        }
        return handlerMap;
    }

    protected AutoCommandHandler getMethodAnnotation(Method m) {
        return m.getAnnotation(AutoCommandHandler.class);
    }
    /*************************************handlerMap定义和初始化--END*************************************/

    /**
     * MultiCommandHandler exec方法
     *
     * @param command
     * @param context
     * @return
     * @throws CommandHandleException
     */
    @Override
    public Object exec(Command command, CommandHandlerContext context) throws CommandHandleException {
        //String cmdName = command.getClass().getName();
        handlerMap.values().stream().forEach(new Consumer<SAutoCommandHandler>() {
            @Override
            public void accept(SAutoCommandHandler sAutoCommandHandler) {
                // System.out.println(sAutoCommandHandler.name + "  :" + sAutoCommandHandler.commandType);
                log.debug(sAutoCommandHandler.name + "  :" + sAutoCommandHandler.commandType);
            }
        });

        final SAutoCommandHandler sAutoCommandHandler = handlerMap.get(context.getCommandMeta().name());
        if (sAutoCommandHandler == null) {
            // System.err.println(context.getCommandMeta().name() + "  type:" + context.getCommandMeta().type().getName());
            log.error(context.getCommandMeta().name() + "  type:" + context.getCommandMeta().type().getName());
            throw new CommandHandleException(400, "Unsupported Command:" + context.getCommandMeta().name());
        }

        return sAutoCommandHandler.exec(command, context);
    }

    /**
     * 静态内部类SAutoCommandHandler
     */
    public static class SAutoCommandHandler implements CommandHandler<Command> {
        private Method action;
        private Object handler;
        private Class<? extends Command> commandType;
        private String name;

        private BiFunction<Command, CommandHandlerContext, Object> execFunc;

        public SAutoCommandHandler(String name, Object handler, Method m, Class<? extends Command> commandType) {
            //Validate.notNull(handler2);
            //Validate.notNull(m);
            //Validate.notNull(commandType);

            this.handler = handler;
            this.action = m;
            this.commandType = commandType;
            this.name = name;
            this.execFunc = newExecFunc(m, handler);
        }

        /**
         * exec
         *
         * @param command
         * @param context
         * @return
         * @throws CommandHandleException
         */
        public Object exec(Command command, CommandHandlerContext context) throws CommandHandleException {
            return this.execFunc.apply(command, context);
        }

        /**
         * newExecFunc
         *
         * @param action
         * @param handler
         * @return
         */
        protected BiFunction<Command, CommandHandlerContext, Object> newExecFunc(Method action, Object handler) {
            return new BiFunction<Command, CommandHandlerContext, Object>() {
                @Override
                public Object apply(Command command, CommandHandlerContext context) {
                    return ReflectionUtils.invokeMethod(action, handler, command, context);
                }
            };
        }

        /**
         * toCommandMapper
         *
         * @param handlerMap
         * @return
         */
        public static Function<String, Class<? extends Command>> toCommandMapper(Map<String, SAutoCommandHandler> handlerMap) {
            final Collection<SAutoCommandHandler> handlers = handlerMap.values();
            return new Function<String, Class<? extends Command>>() {
                @Override
                public Class<? extends Command> apply(String s) {
                    for (SAutoCommandHandler handler : handlers) {
                        if (handler.name.equalsIgnoreCase(s)) {
                            return handler.commandType;
                        }
                    }
                    return null;
                }
            };
        }
    }

    /**
     * 静态内部类SingleSAutoCommandHandler
     */
    public static class SingleSAutoCommandHandler extends SAutoCommandHandler {
        public SingleSAutoCommandHandler(String name, Object handler, Method m, Class<? extends Command> commandType) {
            super(name, handler, m, commandType);
        }

        protected BiFunction<Command, CommandHandlerContext, Object> newExecFunc(Method action, Object handler) {
            return new BiFunction<Command, CommandHandlerContext, Object>() {
                @Override
                public Object apply(Command command, CommandHandlerContext context) {
                    return ReflectionUtils.invokeMethod(action, handler, command);
                }
            };
        }
    }
}