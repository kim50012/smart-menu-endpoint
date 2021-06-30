package com.basoft.eorder.application.framework;

public interface RunCommand extends Command{
    Object exec() throws CommandHandleException;
}
