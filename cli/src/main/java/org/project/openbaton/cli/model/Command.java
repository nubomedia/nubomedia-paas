package org.project.openbaton.cli.model;

import java.lang.reflect.Method;

/**
 * Created by lto on 14/07/15.
 */
public class Command {

    private final Class clazz;
    private Object instance;
    private Method method;
    private Class[] params;

    public Command(Object instance, Method method, Class[] params, Class clazz) {
        this.instance = instance;
        this.method = method;
        this.params = params;
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class[] getParams() {
        return params;
    }

    public void setParams(Class[] params) {
        this.params = params;
    }
}
