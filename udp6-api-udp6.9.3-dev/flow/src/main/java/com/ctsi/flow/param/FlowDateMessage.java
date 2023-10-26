package com.ctsi.flow.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-09-06 9:57
 * @description ：
 */

@Data
public class FlowDateMessage {

    private String date;

    private String key;

    private Date startTime;

    private Date endTime;

}
