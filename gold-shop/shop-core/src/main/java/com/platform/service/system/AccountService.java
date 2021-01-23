package com.platform.service.system;

import com.platform.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountService
 *
 * @author wlhbdp
 * @version 2020/9/12 0012
 */
@Service
public class AccountService {
    @Autowired
    private TokenCache tokenCache;
    public void logout(String token) {
        tokenCache.remove(token);
    }

}
