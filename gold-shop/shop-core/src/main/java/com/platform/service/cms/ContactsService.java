package com.platform.service.cms;

import com.platform.bean.entity.cms.Contacts;
import com.platform.dao.cms.ContactsRepository;
import com.platform.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ContactsService extends BaseService<Contacts,Long,ContactsRepository> {
}
