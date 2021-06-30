package com.basoft.eorder.application.framework;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author woonill
 * @since 2018/12/07
 */
public final class DefaultCommandHandlerEngine implements CommandHandleEngine {
    private Map<String, Class<? extends Command>> commandMap;

    private CommandHandler<Command> commandHandler;

    public DefaultCommandHandlerEngine(Map<String, Class<? extends Command>> commandMap, CommandHandler<Command> commandHandler) {
        this.commandMap = commandMap;
        this.commandHandler = commandHandler;
    }

    @Override
    public Class<? extends Command> getCommandClass(String cmdName) {
        return commandMap.get(cmdName);
    }

    @Override
    public CommandHandlerContext newCommandHandlerContext(String cmdName) {
        return newCommandHandlerContext(cmdName, Collections.EMPTY_MAP);
    }

    @Override
    public CommandHandlerContext newCommandHandlerContext(String cmdName, Map<String, Object> props) {
        Class<? extends Command> commandType = this.commandMap.get(cmdName);
        return new DefaultCommandHandlerContext(cmdName,commandType,props);
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Inner static class-The Builder of DefaultCommandHandlerEngine
     */
    public static final class Builder {
        private Map<String, Class<? extends Command>> commandMap = new HashMap<>();

        private CommandHandler handler;

        public Builder setCommand(String name, Class<? extends Command> commandClass) {
            this.commandMap.put(name, commandClass);
            return this;
        }

        public Builder setCommandHandler(CommandHandler commandHandler) {
            this.handler = commandHandler;
            return this;
        }

        public CommandHandleEngine build() {
            return new DefaultCommandHandlerEngine(this.commandMap, this.handler);
        }
    }

    /**
     * DefaultCommandHandlerContext
     */
    static final class DefaultCommandHandlerContext implements CommandHandlerContext {
        // 命令名称
        private String commandName;

        // 命令对应的Command Class对象
        private Class<? extends Command> cmdType;

        // 命令执行所需属性组
        private Map<String, Object> props;

        /**
         * Constructor
         *
         * @param cmdName
         * @param commandType
         * @param props
         */
        public DefaultCommandHandlerContext(String cmdName, Class<? extends Command> commandType, Map<String, Object> props) {
            this.commandName = cmdName;
            this.cmdType = commandType;
            this.props = props;
        }

        /**
         * @return CommandMeta
         */
        @Override
        public CommandMeta getCommandMeta() {
            return new CommandMeta(this.commandName, this.cmdType);
        }

        /**
         * @return props Map
         */
        @Override
        public Map<String, Object> props() {
            return props;
        }
    }
}
