package com.platform.bean.enumeration;

/**
 * redis队列名称
 */
public enum RedisQueueName {
    CREDIT_LIMIT("credit_limit"),COLLECTION_TAG("collection_tag");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    RedisQueueName(String value) {
        this.value = value;
    }
}
