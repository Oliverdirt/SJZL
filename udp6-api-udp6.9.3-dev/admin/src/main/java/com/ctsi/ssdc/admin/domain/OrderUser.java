package com.ctsi.ssdc.admin.domain;

import lombok.Data;

/**
 * @Description
 * @Author Len
 * @Date 2023/5/29 10:49
 */
@Data
public class OrderUser {

    /**
     * 被排序用户id
     */
    private Long id;

    /**
     * 被排序用户排序之后的上一个用户的id
     */
    private Long preId;

    /**
     * 被排序用户排序之后的下一个用户的id
     */
    private Long nextId;


}
