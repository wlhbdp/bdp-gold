package com.platform.bean.vo.shop;

import lombok.Data;

/**
 * @author wlhbdp
 * @date ：Created in 12/13/2020 2:50 PM
 */
@Data
public class CartVo {
    private Long idGoods;
    private Integer count;
    private Long idSku;
    private Long idUser;

}
