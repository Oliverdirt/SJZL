package com.ctsi.flow.core.behavior;

import com.ctsi.flow.core.service.CandidateUsersService;
import com.ctsi.flow.server.rule.FlowRuleService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-25 15:53
 * @description ：
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Setter
    private FlowRuleService flowTaskRuleService;

    @Setter
    private CandidateUsersService candidateUsersService;

    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        FlowUserTaskActivityBehavior userTaskActivityBehavior = new FlowUserTaskActivityBehavior(userTask);
        userTaskActivityBehavior.setFlowTaskRuleService(flowTaskRuleService);
        userTaskActivityBehavior.setCandidateUsersService(candidateUsersService);
        return userTaskActivityBehavior;
    }


}
