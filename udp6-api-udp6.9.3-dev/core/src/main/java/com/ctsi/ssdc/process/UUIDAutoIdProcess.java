package com.ctsi.ssdc.process;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-10-13 16:48
 **/
public class UUIDAutoIdProcess extends BaseAutoIdProcess {
    public UUIDAutoIdProcess(Field field) {
        super(field);
    }

    @Override
    public void setFieldValue(Object entity) throws Exception {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        this.field.set(entity, uuid);
    }
}
