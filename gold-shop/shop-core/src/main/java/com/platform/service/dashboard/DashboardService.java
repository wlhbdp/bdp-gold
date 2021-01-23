package com.platform.service.dashboard;

import com.platform.bean.enumeration.shop.OrderEnum;
import com.platform.service.shop.CartService;
import com.platform.service.shop.OrderService;
import com.platform.service.shop.ShopUserService;
import com.platform.utils.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wlhbdp
 * @date ：Created in 1/7/2020 2:25 PM
 */
@Service
public class DashboardService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private CartService cartService;
    public Map getDashboardData(){
        long orderCount = orderService.count();
        long userCount = shopUserService.count();
        long cartCount = cartService.count();

        Map orderSumPrice = orderService.getMapBySql("select sum(real_price) as realPrice from t_shop_order where status<>"+ OrderEnum.OrderStatusEnum.UN_PAY.getId());
        Map result = Maps.newHashMap(
                "orderCount",orderCount,
                "userCount",userCount,
                "cartCount",cartCount,
                "orderSumPrice", orderSumPrice!=null?(Double.valueOf(orderSumPrice.get("realPrice").toString())/100):"0"
        );
        return result;
    }
}
