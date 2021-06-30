package com.basoft.eorder.application.framework;

public interface ComponentProvider {

    <T> T getComponent(Class<T> type);
}
