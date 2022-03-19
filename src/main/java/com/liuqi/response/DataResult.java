package com.liuqi.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataResult<T> implements Serializable{
    private int code;
    private String msg;
    private Long count;
    private List<T> data;

    public DataResult(){}

    public DataResult(Long count, List<T> data) {
        this.count = count;
        this.data = data;
    }
}
