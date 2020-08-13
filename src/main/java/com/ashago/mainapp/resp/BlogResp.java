package com.ashago.mainapp.resp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ashago.mainapp.domain.Blog;
import com.mysql.cj.jdbc.Blob;

import lombok.Builder;
import lombok.ToString;

@ToString
public class BlogResp {
    private final String status;
    private final String message;
    private List<?> dataList;
    private Optional<?> data;

    public BlogResp(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public BlogResp appendDataList(List<?> dataList) {
        this.dataList = dataList;
        return this;
    }

    public BlogResp appendData(Optional<?> data) {
        this.data = data;
        return this;
    }

    public static BlogResp create(String status, String message) {
        return new BlogResp(status, message);
    }

    public static BlogResp success() {
        return new BlogResp("1", "success");
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public Optional<?> getData() {
        return data;
    }
}
