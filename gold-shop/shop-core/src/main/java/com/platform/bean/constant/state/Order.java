package com.platform.bean.constant.state;

/**
 * 数据库排序
 *
 * @author wlhbdp
 * @date 2020年5月31日20:48:41
 */
public enum Order {

    ASC("asc"), DESC("desc");

    private String des;

    Order(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
