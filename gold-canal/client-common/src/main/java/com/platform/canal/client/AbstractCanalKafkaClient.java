package com.platform.canal.client;


import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @author wlhbdp
 * @ClassName: AbstractCanalKafkaClient
 * @Description: canal kafka client
 */
@Log4j2
public abstract class AbstractCanalKafkaClient {

    /**
     *  发送失败或者服务器处理数据失败单独打印
     */


    @Resource
    private KafkaCanalConnector kafkaCanalConnector;

    private static volatile boolean         running = false;


    private Thread.UncaughtExceptionHandler handler = (t, e) -> log.error("parse events has an error", e);


    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("canal-client-pool-%d").setUncaughtExceptionHandler(handler).build();

    ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    public void start() {
        Assert.notNull(kafkaCanalConnector, "connector is null");
        singleThreadPool.execute(()-> process());
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (singleThreadPool != null) {
            try {
                singleThreadPool.shutdown();

                if (!singleThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("Timeout.... Ignore for this case");
                }
            } catch (InterruptedException e) {
                log.error("thread join error!",e);
                Thread.currentThread().interrupt();
            }
        }
    }


    private void process() {
        while (running) {
            try {
                kafkaCanalConnector.connect();
                //开始订阅
                kafkaCanalConnector.subscribe();
                while (running) {
                    try {
                        //获取message
                        List<FlatMessage> messages = kafkaCanalConnector.getFlatListWithoutAck(100L, TimeUnit.MILLISECONDS);
                        Map<String,List<FlatMessage>> dbTableListMap = new HashMap<>();
                        if (messages == null) {
                            continue;
                        }
                        for (FlatMessage message : messages){
                            //创建、删除、修改数据库、表(忽略)
                            if (message.getIsDdl()){
                                continue;
                            }

                            long batchId = message.getId();
                            int size = message.getData().size();
                            if (batchId == -1 || size == 0) {
                                // 没有数据，暂停
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    log.warn("canal process thread sleep error:",e);
                                }
                            } else {
                                    processMessageToList(dbTableListMap,message);
                            }
                        }

                        processMessage(dbTableListMap);
                        kafkaCanalConnector.ack(); // 提交确认
                    } catch (Exception e) {
                        log.error("message", e);
                    }
                }
            } catch (Exception e) {
                log.error("process running exception:", e);
            }
        }
        try {
            kafkaCanalConnector.unsubscribe();
        } catch (Exception e) {
            log.warn("connector unsubscribe error:",e);
        }
        kafkaCanalConnector.disconnect();
    }


    public abstract void processMessage(Map<String,List<FlatMessage>> dbTableListMap);

    public void processMessageToList(Map<String, List<FlatMessage>> dbTableListMap, FlatMessage message) {
        String dbTable = new StringBuffer(message.getDatabase()).append(".").append(message.getTable()).toString();
        if (dbTableListMap.containsKey(dbTable)) {
            List<FlatMessage> messageList = (List)dbTableListMap.get(dbTable);
            messageList.add(message);
        } else {
            List<FlatMessage> messageList = new ArrayList();
            messageList.add(message);
            dbTableListMap.put(dbTable, messageList);
        }

    }

}
