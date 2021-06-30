package com.basoft.eorder.application.framework;

/**
 *
 * @param <R>
 */
public class DefaultCommandHandler<R extends RunCommand> implements CommandHandler {
    @Override
    public Object exec(Command command, CommandHandlerContext context) throws CommandHandleException {
        return ((R) command).exec();
    }
}
