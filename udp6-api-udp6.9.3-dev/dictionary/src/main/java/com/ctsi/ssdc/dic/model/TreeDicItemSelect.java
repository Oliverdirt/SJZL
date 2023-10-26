package com.ctsi.ssdc.dic.model;

import com.ctsi.ssdc.dic.domain.CscpHxDicItem;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeDicItemSelect树结构实体类
 * 
 * @author hx
 */
public class TreeDicItemSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeDicItemSelect> children;

    public TreeDicItemSelect()
    {

    }

    public TreeDicItemSelect(CscpHxDicItem item)
    {
        this.id = item.getItemId();
        this.label = item.getItemValue();
        this.children = item.getChildren().stream().map(TreeDicItemSelect::new).collect(Collectors.toList());
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

    public List<TreeDicItemSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeDicItemSelect> children)
    {
        this.children = children;
    }
}
