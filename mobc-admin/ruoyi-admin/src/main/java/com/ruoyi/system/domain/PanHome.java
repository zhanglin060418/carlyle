package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 主页对象 pan_home
 * 
 * @author ruoyi
 * @date 2023-04-21
 */
public class PanHome extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主页ID */
    private Long homeId;

    /** 影像地址 */
    @Excel(name = "影像地址")
    private String coverImage;

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

    /** 公告状态（0正常 1关闭） */
    @Excel(name = "公告状态", readConverterExp = "0=正常,1=关闭")
    private String status;

    public void setHomeId(Long homeId) 
    {
        this.homeId = homeId;
    }

    public Long getHomeId() 
    {
        return homeId;
    }
    public void setCoverImage(String coverImage) 
    {
        this.coverImage = coverImage;
    }

    public String getCoverImage() 
    {
        return coverImage;
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
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("homeId", getHomeId())
            .append("coverImage", getCoverImage())
            .append("description", getDescription())
            .append("descriptionEn", getDescriptionEn())
            .append("descriptionIn", getDescriptionIn())
            .append("descriptionRu", getDescriptionRu())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
