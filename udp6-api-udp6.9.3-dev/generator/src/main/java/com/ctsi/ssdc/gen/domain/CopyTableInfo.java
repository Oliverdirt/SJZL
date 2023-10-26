package com.ctsi.ssdc.gen.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-07-18 19:27
 **/
@ApiModel(description = "CopyTableInfo")
@Data
public class CopyTableInfo implements Serializable {
    String newTableName;
    String newTableComment;
    String copyTableName;
}
