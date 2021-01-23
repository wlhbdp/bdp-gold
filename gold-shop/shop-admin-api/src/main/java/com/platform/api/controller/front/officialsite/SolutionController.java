package com.platform.api.controller.front.officialsite;

import com.platform.web.controller.BaseController;
import com.platform.bean.entity.cms.Article;
import com.platform.bean.enumeration.cms.BannerTypeEnum;
import com.platform.bean.enumeration.cms.ChannelEnum;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.offcialsite.BannerVo;
import com.platform.bean.vo.offcialsite.Solution;
import com.platform.service.cms.ArticleService;
import com.platform.service.cms.BannerService;
import com.platform.utils.Maps;
import com.platform.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offcialsite/solution")
public class SolutionController extends BaseController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public Object index() {
        Map<String, Object> dataMap =  Maps.newHashMap();

        BannerVo banner = bannerService.queryBanner(BannerTypeEnum.SOLUTION.getValue());
        dataMap.put("banner", banner);

        List<Solution> solutions = new ArrayList<>();
        Page<Article> articlePage = articleService.query(1, 10, ChannelEnum.SOLUTION.getId());
        for (Article article : articlePage.getRecords()) {
            solutions.add(new Solution(article.getId(), article.getTitle(), article.getImg()));
        }
        dataMap.put("solutionList", solutions);

        Map map =  Maps.newHashMap("data",dataMap);
        return Rets.success(map);

    }
}
