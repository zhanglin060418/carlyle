package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品对象 pan_product
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public class PanLottery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 产品名称（英文） */
    @Excel(name = "产品名称", readConverterExp = "英=文")
    private String nameEn;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String coverImages;

    /** 标识 */
    @Excel(name = "标识")
    private Integer category;

    /** 分类 */
    @Excel(name = "分类")
    private String type;

    /** 日利率 */
    @Excel(name = "概率率")
    private Double probability;

    /** 周期 */
    @Excel(name = "周期")
    private Integer cycle;

    /** 会员充值最小金额 */
    @Excel(name = "会员充值最小金额")
    private BigDecimal amount;

    private Long userId;

    /** 介绍 */
    @Excel(name = "介绍")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCoverImages() {
        return coverImages;
    }

    public void setCoverImages(String coverImages) {
        this.coverImages = coverImages;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("nameEn", getNameEn())
                .append("coverImages", getCoverImages())
                .append("category", getCategory())
                .append("type", getType())
                .append("probability", getProbability())
                .append("cycle", getCycle())
                .append("amount", getAmount())
                .append("description", getDescription())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
