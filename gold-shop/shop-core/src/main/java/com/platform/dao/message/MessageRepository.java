package com.platform.dao.message;


import com.platform.bean.entity.message.Message;
import com.platform.dao.BaseRepository;

import java.util.ArrayList;


public interface MessageRepository extends BaseRepository<Message,Long> {
    void deleteAllByIdIn(ArrayList<String> list);
}

