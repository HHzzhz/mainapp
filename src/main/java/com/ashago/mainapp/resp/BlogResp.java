package com.ashago.mainapp.resp;

import java.util.List;
import java.util.Optional;
import lombok.ToString;

@ToString
public class BlogResp {
    private final String code;
    private final String msg;
    private List<?> dataList;
    private Optional<?> data;

    public BlogResp(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public BlogResp appendDataList(List<?> dataList) {
        this.dataList = dataList;
        return this;
    }

    public BlogResp appendData(Optional<?> data) {
        this.data = data;
        return this;
    }

    public static BlogResp create(String code, String msg) {
        return new BlogResp(code, msg);
    }

    public static BlogResp success() {
        return new BlogResp("1", "success"); 
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public Optional<?> getData() {
        return data;
    }
}
