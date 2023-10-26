package com.ctsi.ssdc.mybatisplus.domain;

import com.ctsi.ssdc.annocation.AutoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Employee {
    @AutoId(primaryKey = "id")
    private Long id;
    private String name;
    private Integer age;
    private Date bir;
}
