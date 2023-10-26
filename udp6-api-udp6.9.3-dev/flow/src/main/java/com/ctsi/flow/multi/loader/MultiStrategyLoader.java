package com.ctsi.flow.multi.loader;

import com.ctsi.flow.multi.annotation.Multi;
import com.ctsi.flow.multi.strategy.IMulti;
import com.ctsi.flow.multi.utils.FlowMultiUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author gyp
 */
@Order(1000)
@Component
public class MultiStrategyLoader implements ApplicationRunner {



    private List<MultiStrategyDetail> multiInstanceStrategyList = new ArrayList<>();

    private MultiStrategyDetail defaultMultiInstanceStrategy = null;

    public List<MultiStrategyDetail> getMultiInstanceStrategyList() {
        return multiInstanceStrategyList;
    }

    public void setMultiInstanceStrategyList(List<MultiStrategyDetail> multiInstanceStrategyList) {
        this.multiInstanceStrategyList = multiInstanceStrategyList;
    }

    public MultiStrategyDetail getDefaultMultiInstanceStrategy() {
        return defaultMultiInstanceStrategy;
    }

    public void setDefaultMultiInstanceStrategy(MultiStrategyDetail defaultMultiInstanceStrategy) {
        this.defaultMultiInstanceStrategy = defaultMultiInstanceStrategy;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MultiStrategyDetail> datas = new ArrayList<>();
        ApplicationContext applicationContext = FlowMultiUtils.springContext().getApplicationContext();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Multi.class);
        MultiStrategyDetail detail = null;
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            IMulti strategy = (IMulti) applicationContext.getBean(entry.getKey());
            Multi annotation = strategy.getClass().getAnnotation(Multi.class);
            if (annotation.off()) {
                continue;
            }
            // 未注销的加入列表
            detail = new MultiStrategyDetail();
            detail.setKey(strategy.getClass().getName());
            detail.setName(annotation.name());
            detail.setSort(annotation.sort());
            datas.add(detail);
        }
        // 排个序
        if (datas.size() > 0) {
            List<MultiStrategyDetail> collect = datas.stream().sorted(Comparator.comparing(MultiStrategyDetail::getSort)).collect(Collectors.toList());
            multiInstanceStrategyList.addAll(collect);
            defaultMultiInstanceStrategy = multiInstanceStrategyList.get(0);
        }

    }
}
