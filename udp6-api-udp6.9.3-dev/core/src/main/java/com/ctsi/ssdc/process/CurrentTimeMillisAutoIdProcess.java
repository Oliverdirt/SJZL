package com.ctsi.ssdc.process;



import java.lang.reflect.Field;

/**
 * @author zhaoliangliang
 * @date 2021/9/9
 */
public class CurrentTimeMillisAutoIdProcess extends BaseAutoIdProcess {



    public CurrentTimeMillisAutoIdProcess(Field field) {
        super(field);
    }

    @Override
    void setFieldValue(Object entity) throws Exception{
        long totalMilliSeconds = System.currentTimeMillis();
        field.set(entity,totalMilliSeconds);
    }
}
