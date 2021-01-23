package com.platform.mobile.controller;

import com.platform.bean.constant.CfgKey;
import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.entity.shop.Address;
import com.platform.bean.entity.shop.Cart;
import com.platform.bean.entity.shop.Order;
import com.platform.bean.entity.shop.OrderItem;
import com.platform.bean.enumeration.shop.OrderEnum;
import com.platform.bean.exception.ApplicationException;
import com.platform.bean.exception.ApplicationExceptionEnum;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.api.KdniaoResponse;
import com.platform.service.api.KdniaoService;
import com.platform.service.shop.AddressService;
import com.platform.service.shop.CartService;
import com.platform.service.shop.OrderService;
import com.platform.service.system.CfgService;
import com.platform.utils.HttpUtil;
import com.platform.utils.Lists;
import com.platform.utils.Maps;
import com.platform.utils.StringUtil;
import com.platform.utils.factory.Page;
import com.platform.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wlhbdp
 * @date ：Created in 11/6/2020 5:07 PM
 */
@RestController
@RequestMapping("/user/order")
public class OrderController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CfgService cfgService;
    @Autowired
    private KdniaoService kdniaoService;

    @RequestMapping(value = "{orderSn}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "orderSn") String orderSn) {
        Order order = orderService.getByOrderSn(orderSn);
        return Rets.success(order);
    }

    @RequestMapping(value = "getOrders", method = RequestMethod.GET)
    public Object getOrders(@RequestParam(value = "status", required = false) Integer status) {
        Long idUser = getIdUser(HttpUtil.getRequest());
        Page<Order> page = new PageFactory<Order>().defaultPage();
        page.addFilter(SearchFilter.build("idUser", SearchFilter.Operator.EQ, idUser));
        page.setSort(Sort.by(Sort.Direction.DESC, "id"));
        if (status != null && status != 0) {
            page.addFilter(SearchFilter.build("status", SearchFilter.Operator.EQ, status));
        }
        page = orderService.queryPage(page);
        return Rets.success(page);
    }

    @RequestMapping(value = "prepareCheckout", method = RequestMethod.GET)
    public Object prepareCheckout(@RequestParam(value = "chosenAddressId", required = false) Long chosenAddressId,
                                  @RequestParam(value = "idCarts") String idCarts) {
        Long idUser = getIdUser(HttpUtil.getRequest());
        List<SearchFilter> filters = Lists.newArrayList(
                SearchFilter.build("idUser", SearchFilter.Operator.EQ, idUser),
                SearchFilter.build("id", SearchFilter.Operator.IN, StringUtil.splitForLong(idCarts, ","))
        );
        List<Cart> list = cartService.queryAll(filters);
        Address address = null;
        logger.info("chosenAddressId：{}", chosenAddressId);
        if (chosenAddressId == null || chosenAddressId == 0) {
            address = addressService.getDefaultAddr(idUser);
        } else {
            address = addressService.get(chosenAddressId);
        }
        return Rets.success(Maps.newHashMap(
                "list", list, "addr", address
        ));
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Object save(
            @RequestParam("idAddress") Long idAddress,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "idCarts") String idCarts
    ) {

        Long idUser = getIdUser();
        List<SearchFilter> filters = Lists.newArrayList(
                SearchFilter.build("idUser", SearchFilter.Operator.EQ, idUser),
                SearchFilter.build("id", SearchFilter.Operator.IN, StringUtil.splitForLong(idCarts, ","))
        );
        List<Cart> cartList = cartService.queryAll(filters);

        Order order = new Order();
        order.setIdUser(idUser);
        Address address = addressService.get(idAddress);
        order.setIdAddress(idAddress);
        order.setConsignee(address.getName());
        order.setConsigneeAddress(address.getWholeAddressInfo());
        order.setMobile(address.getTel());
        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderItem> itemList = Lists.newArrayList();
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setIdGoods(cart.getIdGoods());
            orderItem.setIdSku(cart.getIdSku());
            orderItem.setPrice(cart.getPrice());
            orderItem.setCount(cart.getCount());
            orderItem.setTotalPrice(orderItem.getPrice().multiply(orderItem.getCount()));
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
            itemList.add(orderItem);
        }
        order.setMessage(message);
        order.setTotalPrice(totalPrice);
        order.setRealPrice(totalPrice);
        order.setStatus(OrderEnum.OrderStatusEnum.UN_PAY.getId());
        order.setPayStatus(OrderEnum.PayStatusEnum.UN_PAY.getId());
        orderService.save(order, itemList);
        cartService.deleteAll(cartList);
        return Rets.success(order);
    }

    @RequestMapping(value = "cancel/{orderSn}", method = RequestMethod.POST)
    public Object cancel(@PathVariable("orderSn") String orderSn) {
        orderService.cancel(orderSn);
        return Rets.success();
    }

    @RequestMapping(value = "confirm/{orderSn}", method = RequestMethod.POST)
    public Object confirm(@PathVariable("orderSn") String orderSn) {
        Order order = orderService.confirmReceive(orderSn);
        return Rets.success(order);
    }

    @RequestMapping(value = "getExpressInfo/{orderSn}", method = RequestMethod.GET)
    public Object getExpressInfo(@PathVariable(value = "orderSn") String orderSn) {
        Order order = orderService.getByOrderSn(orderSn);
        String apiKdniaoUserid = cfgService.getCfgValue(CfgKey.API_KDNIAO_USERID);
        if (StringUtil.isEmpty(apiKdniaoUserid)) {
            //该商城暂无配置物流查询服务
            throw new ApplicationException(ApplicationExceptionEnum.SERVER_ERROR);
        }
        KdniaoResponse response = kdniaoService.realTimeQuery(order.getShippingSn(), order.getExpress().getCode());
        Map data = Maps.newHashMap(
                "order", order,
                "expressInfo", response
        );
        return Rets.success(data);
    }
}
