package com.ctsi.flow.config;

import com.ctsi.flow.core.behavior.FlowActivityBehaviorFactory;
import com.ctsi.flow.core.identity.EmptyUserGroupManager;
import com.ctsi.flow.core.service.CandidateUsersService;
import com.ctsi.flow.server.rule.FlowRuleService;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.activiti.spring.boot.ProcessEngineAutoConfiguration.BEHAVIOR_FACTORY_MAPPING_CONFIGURER;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-06-21 15:45
 */

@Configuration
public class ActivitiConfiguration {

    /**
     * description: 注入流程图的生成器，通过他绘制流程图
     */
    @Bean
    public ProcessDiagramGenerator processDiagramGenerator() {
        return new DefaultProcessDiagramGenerator();
    }

    /**
     * description: 配置空用户组
     */
    @Bean
    public UserGroupManager userGroupManager() {
        return new EmptyUserGroupManager();
    }

    @Bean(name = BEHAVIOR_FACTORY_MAPPING_CONFIGURER)
    public ProcessEngineConfigurationConfigurer defaultActivityBehaviorFactoryMappingConfigurer(
            FlowActivityBehaviorFactory flowActivityBehaviorFactory) {
        return configuration -> {
            // 设置 ActivityBehaviorFactory 实现类，用于流程任务的审核人的自定义
            configuration.setActivityBehaviorFactory(flowActivityBehaviorFactory);
        };
    }



    @Bean
    public FlowActivityBehaviorFactory bpmActivityBehaviorFactory(FlowRuleService flowRuleService,
                                                                  CandidateUsersService candidateUsersService
    ) {
        FlowActivityBehaviorFactory bpmActivityBehaviorFactory = new FlowActivityBehaviorFactory();
        bpmActivityBehaviorFactory.setFlowTaskRuleService(flowRuleService);
        bpmActivityBehaviorFactory.setCandidateUsersService(candidateUsersService);
        return bpmActivityBehaviorFactory;
    }

    @Bean("flow-extra-executor")
    public ThreadPoolTaskExecutor flowExtraExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("flow-extra-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(10);
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setQueueCapacity(200);
        return taskExecutor;
    }

}
