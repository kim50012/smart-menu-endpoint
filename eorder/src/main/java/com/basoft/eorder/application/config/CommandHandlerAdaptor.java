package com.basoft.eorder.application.config;

import com.basoft.eorder.application.framework.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommandHandlerAdaptor
 */
public class CommandHandlerAdaptor implements CommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CommandHandler<Command> handler;
    private EventProducer eventProducer;
    private ObjectMapper payloadMapper = new ObjectMapper();

    public CommandHandlerAdaptor() {
    }

    public CommandHandlerAdaptor(CommandHandler<Command> handler, EventProducer eventProducer) {
        this.handler = handler;
        this.eventProducer = eventProducer;
    }

    /**
     * 命令处理器的核心方法实现
     *
     * @param command
     * @param context
     * @return
     * @throws CommandHandleException
     */
    @Override
    public Object exec(Command command, CommandHandlerContext context) throws CommandHandleException {
        // 命令执行
        Object retunVal = handler.exec(command, context);

        // 命令执行结果处理和返回
        try {
            final String s = payloadMapper.writeValueAsString(command);
            Event event = new Event(context.getCommandMeta().name(), s, context.props());
            this.eventProducer.produce(event);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        logger.info("Return command handle result->" + retunVal);

        return retunVal;
    }
}