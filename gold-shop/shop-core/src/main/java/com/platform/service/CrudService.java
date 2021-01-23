package com.platform.service;

/**
 *
 * @author wlhbdp
 * @date ：Created in 2019/6/29 22:31
 */
public interface CrudService <T, ID> extends
        InsertService<T, ID>,
        UpdateService<T,ID>,
        DeleteService<T,ID>,
        SelectService<T, ID> {
}
