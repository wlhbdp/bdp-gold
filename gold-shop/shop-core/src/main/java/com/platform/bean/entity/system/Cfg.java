package com.platform.bean.entity.system;

import com.platform.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author wlhbdp
 */
@Entity(name="t_sys_cfg")
@Table(appliesTo = "t_sys_cfg",comment = "系统参数")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Cfg  extends BaseEntity {
    @NotBlank(message = "参数名不能为空")
    @Column(name = "cfg_name",columnDefinition = "VARCHAR(256) COMMENT '参数名'")
    private String cfgName;
    @NotBlank(message = "参数值不能为空")
    @Column(name = "cfg_value",columnDefinition = "VARCHAR(512) COMMENT '参数值'")
    private String cfgValue;
    @Column(name = "cfg_desc",columnDefinition = "TEXT COMMENT '备注'")
    private String cfgDesc;

}
