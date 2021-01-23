package com.platform.bean.entity.shop;

import com.platform.bean.entity.BaseEntity;
import com.platform.bean.entity.cms.Banner;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author wlhbdp
 * @date ：Created in 2019/10/29 17:40
 */
@Data
@Table(appliesTo = "t_shop_category",comment = "商品类别")
@Entity(name="t_shop_category")
@EntityListeners(AuditingEntityListener.class)
public class Category extends BaseEntity {
    @Column(columnDefinition = "VARCHAR(16) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '链接地址'")
    private String url;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '图标'")
    private String icon;
    @Transient
    private List<Banner> bannerList;

}
