package com.ctsi.ssdc.process;


import com.ctsi.ssdc.util.SnowIdUtils;
import java.lang.reflect.Field;

/**
 * @author zhaoliangliang
 * @date 2021/9/9
 */
public class SnowFlakeAutoIdProcess extends BaseAutoIdProcess {



    public SnowFlakeAutoIdProcess(Field field) {
        super(field);
    }

    @Override
    void setFieldValue(Object entity) throws Exception{
        long value = SnowIdUtils.uniqueLong();
        field.set(entity,value);
    }
}
