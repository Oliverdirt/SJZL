package com.ctsi.ssdc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * mybatisPlus 条件查询between条件构建实体类
 *
 * <p>The code change the world !!!
 *
 * @author guochui
 * @create 2022-11-30 15:16
 */
@Data
public class BetweenDO implements Serializable {

  private static final long serialVersionUID = 1L;

  // 数据库字段属性
  String betweenFeild;

  // 上边界
  Object upperBoundary;

  // 下边界
  Object lowerBoundary;
}
