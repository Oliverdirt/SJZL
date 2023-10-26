package com.ctsi.flow.multi.strategy;

import com.ctsi.flow.multi.model.MultiInstanceDecisionParam;
import com.ctsi.flow.multi.model.MultiInstanceDecisionResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author gyp
 */
public interface IMulti {

    default MultiInstanceDecisionResult validate(MultiInstanceDecisionParam param) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        param.setVoter(username);

        MultiInstanceDecisionResult result = run(param);

        return result;
    }


    MultiInstanceDecisionResult run(MultiInstanceDecisionParam param);


}
