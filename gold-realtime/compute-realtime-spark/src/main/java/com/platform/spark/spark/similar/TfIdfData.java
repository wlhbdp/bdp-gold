package com.platform.spark.spark.similar;

import lombok.Data;

/**
 * @Author wlhbdp
 * @Description //TODO
 * @ClassName TfIdfData
 * @Date 2020-08-10 14:01
 **/
@Data
public class TfIdfData {
    private String id;
    private String publish_timestamp;
    private String title;
    private String content;
    private String segment;
    private String source;
}
