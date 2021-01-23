package com.platform.api.controller.shop;

import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.entity.shop.OrderLog;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.shop.OrderLogService;
import com.platform.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop/order/log")
public class OrderLogController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private OrderLogService orderLogService;
	@RequestMapping(value = "/queryByIdOrder/{idOrder}",method = RequestMethod.GET)
	public Object queryByIdOrder(@PathVariable("idOrder") Long idOrder){
		List<OrderLog> logList = orderLogService.queryAll(SearchFilter.build("idOrder",idOrder));
		return Rets.success(logList);
	}
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
	Page<OrderLog> page = new PageFactory<OrderLog>().defaultPage();
		page = orderLogService.queryPage(page);
		return Rets.success(page);
	}
}
