package com.platform.core.factory;

import com.platform.bean.exception.ApplicationException;
import com.platform.bean.exception.ApplicationExceptionEnum;
import com.platform.service.system.IConstantFactory;
import com.platform.service.system.impl.ConstantFactory;

import java.lang.reflect.Method;

/**
 * 字段的包装创建工厂
 *
 * @author wlhbdp
 * @date 2020-05-06 15:12
 */
public class DictFieldWarpperFactory {

    public static Object createFieldWarpper(Object field, String methodName) {
        IConstantFactory me = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, field.getClass());
            Object result = method.invoke(me, field);
            return result;
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Long.class);
                Object result = method.invoke(me, Long.valueOf(field.toString()));
                return result;
            } catch (Exception e1) {
                try {
                    Method method = IConstantFactory.class.getMethod(methodName, Integer.class);
                    Object result = method.invoke(me, Integer.parseInt(field.toString()));
                    return result;
                } catch (Exception e2) {
                    throw new ApplicationException(ApplicationExceptionEnum.ERROR_WRAPPER_FIELD);
                }
            }
        }
    }

}
