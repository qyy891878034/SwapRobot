package com.liuqi.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tanyan
 * @create 2020-01=10
 * @description
 */
@Data
public class PicDto implements Serializable {

    private Long id;
    private String picMain;
    private String pic2;
    private String pic3;
    private String pic4;
    private String pic5;
    private String picDetail;
}
