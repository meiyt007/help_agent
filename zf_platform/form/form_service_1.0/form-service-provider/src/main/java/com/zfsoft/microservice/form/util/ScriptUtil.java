package com.zfsoft.microservice.form.util;

import lombok.SneakyThrows;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author: kkfan
 * @create: 2021-09-10 17:09:29
 * @description: js util
 */
public class ScriptUtil {

    /**
     * 执行js脚本
     * @author: kkfan
     * @param script    js脚本
     * @param args  参数
     * @return
     */
    public static Object execJs(String script, Object... args) {
        Assert.hasLength(script, "脚本不能空！");
        return execJs("var aa = " + script, "aa", args);
    }

    /**
     * 执行js脚本
     * @author: kkfan
     * @param script    js脚本
     * @param methodName    执行方法名称
     * @param args  参数
     * @return
     */
    @SneakyThrows({ScriptException.class, NoSuchMethodException.class})
    public static Object execJs(String script, String methodName, Object... args) {
        Assert.hasLength(script, "脚本不能空！");
        Assert.hasLength(methodName, "执行方法名称不能空！");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(script);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(methodName, args);
    }

    /**
     * 执行js脚本
     * @author: kkfan
     * @param script js脚本
     * @return
     */
    public static Object execJs(String script) {
        return execJs(script, null);
    }

}
