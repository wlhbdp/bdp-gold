package com.platform.service;

import com.platform.BaseApplicationStartTest;
import com.platform.service.api.KdniaoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wlhbdp
 * @date ：Created in 2020/5/31 22:06
 */
public class KdniaoServiceTest extends BaseApplicationStartTest {
    @Autowired
    private KdniaoService kdniaoService;
    @Test
    public void realTimeQuery() {
        kdniaoService.realTimeQuery("YT4544755661648","YTO");

    }
}
