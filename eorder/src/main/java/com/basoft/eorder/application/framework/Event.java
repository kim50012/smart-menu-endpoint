package com.basoft.eorder.application.framework;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Event {



    private String name;
    private Object payload;
    private Map<String,Object> contextProps = new ConcurrentHashMap<>();

    public Event(String name, Object payload) {
        this(name,payload,new HashMap<>());
    }

    public Event(String name, Object payload, Map<String, Object> props) {
        this.name = name;
        this.payload = payload;

        if(props != null && !props.isEmpty()){
            this.contextProps.putAll(props);
        }
    }


    public String getName() {
        return name;
    }

    public Object getPayload() {
        return payload;
    }

    public Map<String,Object> getContextProps(){
        return Collections.unmodifiableMap(this.contextProps);
    }

    public static final class Result{

        private String handleId;
        private int status = 200;

        public Result(String handleId2){
            this.handleId = handleId2;
        }

        public Result(String handleId2,int status){
            this.handleId = handleId2;
            this.status = status;
        }


        public String getHandleId() {
            return handleId;
        }

        public int getStatus() {
            return status;
        }
    }

}
