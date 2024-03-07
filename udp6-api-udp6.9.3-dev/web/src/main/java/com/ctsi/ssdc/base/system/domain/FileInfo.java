package com.ctsi.ssdc.base.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctsi.ssdc.annocation.AutoId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
@TableName("FILE_INFO")
@Data
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 文件id
     */
      @AutoId(primaryKey = "id")
      private Long id;

      /**
     * 文件名称
     */
      @TableField("NAME")
    private String name;

      /**
     * 文件地址
     */
      @TableField("FILE_URL")
    private String fileUrl;

      /**
     * 文件大小
     */
      @TableField("SIZE")
    private String size;

      /**
     * 文件后缀
     */
      @TableField("SUFFIX")
    private String suffix;

      /**
     * 创建者
     */
      @TableField("CREATE_ID")
    private BigInteger createId;

      /**
     * 创建时间
     */
      @TableField("CREATE_TIME")
    private Date createTime;

      /**
     * 修改者
     */
      @TableField("UPDATE_ID")
    private BigInteger updateId;

      /**
     * 修改时间
     */
      @TableField("UPDATE_TIME")
    private Date updateTime;

      /**
     * 删除标记：0：有效；1：无效
     */
      @TableField("DEL_FLAG")
    private Integer delFlag;
}
