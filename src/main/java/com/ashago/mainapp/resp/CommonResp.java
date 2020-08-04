package com.ashago.mainapp.resp;

import java.util.HashMap;
import java.util.Map;

public class CommonResp {
    private final String code;
    private final String msg;
    private final Map<String, Object> data = new HashMap<>();

    private CommonResp(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CommonResp create(String code, String msg) {
        return new CommonResp(code, msg);
    }

    public CommonResp appendData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public static CommonResp success() {
        return new CommonResp("0", "success");
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
