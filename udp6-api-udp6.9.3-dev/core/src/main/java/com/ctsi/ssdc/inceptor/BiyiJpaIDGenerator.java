package com.ctsi.ssdc.inceptor;


import com.ctsi.ssdc.util.SnowIdUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.springframework.data.mapping.MappingException;

import java.io.Serializable;

/**
 * Spring data JPA 主键自动生成器
 * @author zhaoliangliang
 * @date 2021/9/16
 */
public class BiyiJpaIDGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws MappingException {
               Object id = SnowIdUtils.uniqueLong();
                 if (id != null) {
                         return (Serializable) id;
                     }
                 return super.generate(session, object);
             }
}
