package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 主页对象 team_treatment
 * 
 * @author ruoyi
 * @date 2023-05-03
 */
public class TeamTreatment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 介绍 */
    @Excel(name = "介绍")
    private String description;

    /** 介绍（英文） */
    @Excel(name = "介绍", readConverterExp = "英=文")
    private String descriptionEn;

    /** 介绍（印度语） */
    @Excel(name = "介绍", readConverterExp = "印=度语")
    private String descriptionIn;

    /** 介绍（俄文） */
    @Excel(name = "介绍", readConverterExp = "俄=文")
    private String descriptionRu;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setDescriptionEn(String descriptionEn) 
    {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionEn() 
    {
        return descriptionEn;
    }
    public void setDescriptionIn(String descriptionIn) 
    {
        this.descriptionIn = descriptionIn;
    }

    public String getDescriptionIn() 
    {
        return descriptionIn;
    }
    public void setDescriptionRu(String descriptionRu) 
    {
        this.descriptionRu = descriptionRu;
    }

    public String getDescriptionRu() 
    {
        return descriptionRu;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("description", getDescription())
            .append("descriptionEn", getDescriptionEn())
            .append("descriptionIn", getDescriptionIn())
            .append("descriptionRu", getDescriptionRu())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
