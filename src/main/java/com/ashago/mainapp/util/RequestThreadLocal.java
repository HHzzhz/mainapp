package com.ashago.mainapp.util;


public class RequestThreadLocal {
    private static ThreadLocal<String> sessionId = new ThreadLocal<>();

    public static String getSessionId() {
        return sessionId.get();
    }

    public static void setSessionId(String sessionId) {
        RequestThreadLocal.sessionId.set(sessionId);
    }
}
