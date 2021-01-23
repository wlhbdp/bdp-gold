package com.platform.service.message;


import com.platform.bean.entity.message.MessageSender;
import com.platform.bean.entity.message.MessageTemplate;
import com.platform.dao.message.MessagesenderRepository;
import com.platform.dao.message.MessagetemplateRepository;
import com.platform.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * MessagesenderService
 *
 * @author wlhbdp
 * @version 2019/05/17 0017
 */
@Service
public class MessagesenderService extends BaseService<MessageSender,Long,MessagesenderRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MessagesenderRepository messageSenderRepository;
    @Autowired
    private MessagetemplateRepository messagetemplateRepository;

    public void save(MessageSender messageSender){
        messageSenderRepository.save(messageSender);
    }
    @Override
    public void  deleteById(Long id){
        List<MessageTemplate> templateList = messagetemplateRepository.findByIdMessageSender(id);
        if(templateList.isEmpty()) {
            messageSenderRepository.deleteById(id);
        }else {
            throw new RuntimeException("有模板使用该发送器，无法删除");
        }
    }

}

