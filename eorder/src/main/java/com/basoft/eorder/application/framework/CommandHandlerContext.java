package com.basoft.eorder.application.framework;

import java.util.Map;

/**
 * 命令处理器的上下文
 *
 * 1.提供获取Command Meta对象的方法
 * 2.提供获取命令执行属性组的方法
 * 3.并定义了Command Meta，即命令元数据
 * @version 1.0
 * @since 20181210
 */
public interface CommandHandlerContext {
    /**
     * Get CommandMeta
     *
     * @return CommandMeta
     */
    CommandMeta getCommandMeta();

    /**
     * Get Command Props
     *
     * @return Map
     */
    Map<String, Object> props();

    /**
     * Command Meta
     */
    public static final class CommandMeta {
        private String name;

        private Class<? extends Command> cmdType;


        public CommandMeta(String commandName, Class<? extends Command> cmdType) {
            this.name = commandName;
            this.cmdType = cmdType;
        }

        public Class<? extends Command> type() {
            return this.cmdType;
        }

        public String name() {
            return this.name;
        }
    }
}
