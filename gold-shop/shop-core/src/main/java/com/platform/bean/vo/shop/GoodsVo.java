package com.platform.bean.vo.shop;

import com.platform.bean.entity.shop.Goods;
import com.platform.bean.entity.shop.GoodsSku;
import lombok.Data;

import java.util.List;

/**
 * @author wlhbdp
 * @date ：Created in 12/10/2020 7:57 PM
 */
@Data
public class GoodsVo {
    private Goods goods;
    private List<GoodsSku> skuList;

}
