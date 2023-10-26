package com.ctsi.ssdc.admin.domain.dto;

import com.ctsi.ssdc.admin.domain.CscpUserPostExample.Criteria;
import com.ctsi.ssdc.criteria.LongCriteria;

import java.io.Serializable;

/**
 * Criteria for the entity CscpUserRole
 *
 * @author ctsi biyi generator
 *
 */

public class CscpUserPostCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongCriteria id;
    private LongCriteria userId;
    private LongCriteria postId;

    public LongCriteria getId() {
        return id;
    }

    public void setId(LongCriteria id) {
        this.id = id;
    }

    public LongCriteria getUserId() {
        return userId;
    }

    public void setUserId(LongCriteria userId) {
        this.userId = userId;
    }

    public LongCriteria getPostId() {
        return postId;
    }

    public void setPostId(LongCriteria postId) {
        this.postId = postId;
    }

    public Criteria buildCriteria(Criteria criteria) {
        if (criteria == null) {
            return criteria;
        }
        this.buildIdCriteria(criteria);
        this.buildUserIdCriteria(criteria);
        this.buildPostIdCriteria(criteria);

        return criteria;
    }
    private void buildIdCriteria(Criteria criteria) {

        if (this.getId() == null) {
            return;
        }

        if (this.getId().getEquals() != null) {
            criteria.andIdEqualTo(this.getId().getEquals());
        } else {

            if (this.getId().getGreaterOrEqualThan() != null) {
                criteria.andIdGreaterThanOrEqualTo(this.getId().getGreaterOrEqualThan());
            }

            if (this.getId().getLessOrEqualThan() != null) {
                criteria.andIdLessThanOrEqualTo(this.getId().getLessOrEqualThan());
            }
        }
    }

    private void buildUserIdCriteria(Criteria criteria) {

        if (this.getUserId() == null) {
            return;
        }

        if (this.getUserId().getEquals() != null) {
            criteria.andUserIdEqualTo(this.getUserId().getEquals());
        } else {

            if (this.getUserId().getGreaterOrEqualThan() != null) {
                criteria.andUserIdGreaterThanOrEqualTo(this.getUserId().getGreaterOrEqualThan());
            }

            if (this.getUserId().getLessOrEqualThan() != null) {
                criteria.andUserIdLessThanOrEqualTo(this.getUserId().getLessOrEqualThan());
            }
        }
    }

    private void buildPostIdCriteria(Criteria criteria) {

        if (this.getPostId() == null) {
            return;
        }

        if (this.getPostId().getEquals() != null) {
            criteria.andPostIdEqualTo(this.getPostId().getEquals());
        } else {

            if (this.getPostId().getGreaterOrEqualThan() != null) {
                criteria.andPostIdGreaterThanOrEqualTo(this.getPostId().getGreaterOrEqualThan());
            }

            if (this.getPostId().getLessOrEqualThan() != null) {
                criteria.andPostIdLessThanOrEqualTo(this.getPostId().getLessOrEqualThan());
            }
        }
    }
}
