package com.platform.service.shop;


import com.platform.bean.entity.shop.Order;
import com.platform.bean.entity.shop.OrderItem;
import com.platform.bean.entity.shop.OrderLog;
import com.platform.bean.enumeration.shop.OrderEnum;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.dao.shop.OrderItemRepository;
import com.platform.dao.shop.OrderLogRepository;
import com.platform.dao.shop.OrderRepository;
import com.platform.security.JwtUtil;
import com.platform.service.BaseService;
import com.platform.utils.DateUtil;
import com.platform.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService extends BaseService<Order, Long, OrderRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderLogRepository orderLogRepository;

    /**
     * 获取唯一订单号
     * 时间戳+随机数<br>
     * 建议生产环境使用redis获取唯一订单号
     *
     * @return
     */
    public String getOrderSn() {
        return DateUtil.getAllTime() + RandomUtil.getRandomNumber(6);
    }

    public void save(Order order, List<OrderItem> itemList) {
        order.setOrderSn(getOrderSn());
        insert(order);
        for(OrderItem item:itemList){
            item.setIdOrder(order.getId());
        }
        orderItemRepository.saveAll(itemList);
        OrderLog orderLog = new OrderLog();
        orderLog.setIdOrder(order.getId());
        orderLog.setDescript("生成订单");
        orderLogRepository.save(orderLog);

    }

    public void updateOrder(Order order){
        update(order);
    }
    /**
     * 用户取消订单
     * @param orderSn
     */
    public void cancel(String orderSn) {
        Order order = getByOrderSn(orderSn);
        order.setStatus(OrderEnum.OrderStatusEnum.CANCEL.getId());
        String descript  = "用户取消订单";
        saveOrderLog(order,descript);;
        updateOrder(order);

    }

    public Order getByOrderSn(String orderSn) {
        return get(SearchFilter.build("orderSn", SearchFilter.Operator.EQ,orderSn));
    }

    /**
     * 确认收货
     * @param orderSn
     */
    public Order confirmReceive(String orderSn) {
        Order order = getByOrderSn(orderSn);
        order.setStatus(OrderEnum.OrderStatusEnum.FINISHED.getId());
        String descript = "客户确认收货";
        saveOrderLog(order,descript);
        updateOrder(order);
        return order;
    }

    public void startPay(Order order){
        order.setPayStatus(OrderEnum.PayStatusEnum.PAYING.getId());
        saveOrderLog(order,"客户发起支付");
        updateOrder(order);

    }

    private void saveOrderLog(Order order,String descript){
        OrderLog orderLog = new OrderLog();
        orderLog.setIdOrder(order.getId());
        orderLog.setDescript(descript);
        orderLogRepository.save(orderLog);
    }


    /**
     * 管理员添加备注信息
     * @param order
     * @param message
     */
    public void addComment(Order order, String message) {
        order.setAdminMessage(message);
        update(order);
        OrderLog orderLog = new OrderLog();
        orderLog.setIdOrder(order.getId());
        orderLog.setDescript("管理员("+ JwtUtil.getUsername() +")添加备注："+message);
        orderLogRepository.save(orderLog);
    }

    /**
     * 支付成功，更新订单数据
     * @param order
     * @param  payType
     */
    public void paySuccess(Order order,String payType) {
             order.setPayTime(new Date());
            order.setPayStatus(OrderEnum.PayStatusEnum.UN_SEND.getId());
            order.setStatus(OrderEnum.OrderStatusEnum.UN_SEND.getId());
            order.setRealPrice(order.getTotalPrice());
            order.setPayType(payType);
            updateOrder(order);
            String descript = "用户付款成功";
            saveOrderLog(order,descript);

    }

    public void send(Order order) {
        String descript = "管理员("+JwtUtil.getUsername()+")已发货";
        updateOrder(order);
        saveOrderLog(order,descript);
    }
}

