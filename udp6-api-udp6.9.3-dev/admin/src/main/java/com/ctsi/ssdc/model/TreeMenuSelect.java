package com.ctsi.ssdc.model;

import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 
 * @author hx
 */
public class TreeMenuSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeMenuSelect> children;


    public TreeMenuSelect(CscpMenus menus)
    {
        this.id = menus.getId();
        this.label = menus.getTitle();
        this.children = menus.getChildren().stream().map(TreeMenuSelect::new).collect(Collectors.toList());
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<TreeMenuSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeMenuSelect> children)
    {
        this.children = children;
    }
}
