package com.basoft.eorder.application.config;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.EventProducer;
import com.basoft.eorder.application.framework.spring.SpringCommandHandleEngineConfigure;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringEOrderCommandConfigure extends SpringCommandHandleEngineConfigure {
    @Override
    protected String getCommandScanPackage() {
        return "com.basoft.eorder.interfaces.command";
    }

    @Override
    public CommandHandler<Command> wrapCommand(CommandHandler<Command> handler, ApplicationContext applicationContext) {
        EventProducer eventProducer = applicationContext.getBean(EventProducer.class);
        return new CommandHandlerAdaptor(handler, eventProducer);
        // return handler;
    }
}

