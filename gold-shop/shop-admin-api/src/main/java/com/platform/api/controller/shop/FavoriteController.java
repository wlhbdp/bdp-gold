package com.platform.api.controller.shop;

import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.entity.shop.Favorite;
import com.platform.bean.vo.front.Rets;
import com.platform.service.shop.FavoriteService;
import com.platform.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop/favorite")
public class FavoriteController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private FavoriteService favoriteService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
		Page<Favorite> page = new PageFactory<Favorite>().defaultPage();
		page = favoriteService.queryPage(page);
		return Rets.success(page);
	}
}
