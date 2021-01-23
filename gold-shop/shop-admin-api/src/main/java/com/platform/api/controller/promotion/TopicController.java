package com.platform.api.controller.promotion;

import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.core.BussinessLog;
import com.platform.bean.entity.promotion.Topic;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.exception.ApplicationException;
import com.platform.bean.exception.ApplicationExceptionEnum;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.promotion.TopicService;
import com.platform.utils.DateUtil;
import com.platform.utils.StringUtil;
import com.platform.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotion/topic")
public class TopicController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TopicService topicService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list(@RequestParam(value = "disabled",required = false) Boolean disabled,
					   @RequestParam(value = "startDate",required = false) String startDate,
					   @RequestParam(value = "endDate",required = false) String endDate) {
		Page<Topic> page = new PageFactory<Topic>().defaultPage();
		page.addFilter( "disabled", disabled);
		page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate,"yyyyMMddHHmmss"));
		page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate,"yyyyMMddHHmmss"));
		page = topicService.queryPage(page);
		return Rets.success(page);
	}

	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑专题", key = "name")
	@RequiresPermissions(value = {Permission.TOPIC_EDIT})
	public Object save(@ModelAttribute Topic topic){
		if(topic.getId()==null){
			topic.setPv(0L);
			topicService.insert(topic);
		}else {
			topicService.update(topic);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除专题", key = "id")
	@RequiresPermissions(value = {Permission.TOPIC_DEL})
	public Object remove(Long id){
		if (StringUtil.isEmpty(id)) {
			throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
		}
		topicService.deleteById(id);
		return Rets.success();
	}

	@RequestMapping(value="/changeDisabled",method = RequestMethod.POST)
	@RequiresPermissions(value = {Permission.TOPIC_EDIT})
	public Object changeIsOnSale(@RequestParam("id")  Long id,@RequestParam("disabled") Boolean disabled){
		if (id == null) {
			throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
		}
		topicService.changeDisabled(id,disabled);
		return Rets.success();
	}
}
