package com.platform.service.api;

import lombok.Data;

import java.util.List;

/**
 * @author wlhbdp
 * @date ：Created in 2020/6/1 0:22
 */
@Data
public class KdniaoResponse {
    private Boolean Success;
    private String State;
    private String StateEx;
    private String Location;
    private List<Trace> Traces;
    @Data
    class Trace{
        private String AcceptTime;
        private String AcceptStation;

    }
}
