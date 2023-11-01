package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 产品对象 pan_product
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public class PanProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 产品名称（印度） */
    @Excel(name = "产品名称", readConverterExp = "印=度")
    private String nameIn;

    /** 产品名称（英文） */
    @Excel(name = "产品名称", readConverterExp = "英=文")
    private String nameEn;

    /** 产品名称（俄语） */
    @Excel(name = "产品名称", readConverterExp = "俄=语")
    private String nameRu;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String coverImages;

    /** 标识 */
    @Excel(name = "标识")
    private String category;

    /** 分类 */
    @Excel(name = "分类")
    private String type;

    /** 奖励产品 */
    @Excel(name = "奖励产品")
    private Long rewardProduct;

    /** 日利率 */
    @Excel(name = "日利率")
    private Double dailyInterest;

    /** 周期 */
    @Excel(name = "周期")
    private Integer cycle;

    /** 会员充值最小金额 */
    @Excel(name = "会员充值最小金额")
    private Double minimumBuy;

    /** 会员充值最大金额 */
    @Excel(name = "会员充值最大金额")
    private Double maximumBuy;

    /** 金额类型 */
    @Excel(name = "金额类型")
    private String fundType;

    /** 产品进度 */
    @Excel(name = "产品进度")
    private Double progress;

    /** 是否显示数字进度条 */
    @Excel(name = "是否显示数字进度条")
    private String showProgressBar;

    /** 发售时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sellingTimestamp;

    /** 配额 */
    @Excel(name = "配额")
    private Double totalFund;

    /** 当前配额 */
    @Excel(name = "当前配额")
    private Double currFund;

    /** 份数 */
    @Excel(name = "份数")
    private Integer copies;
    private Long userId;
    private Integer  buyCount;

    /** 是否显示 */
    @Excel(name = "是否显示")
    private String isVisible;

    /** 是否发售 */
    @Excel(name = "是否发售")
    private String onSale;

    /** 是否支持团购 */
    @Excel(name = "是否支持团购")
    private String hasGroupBuyOption;

    /** 幸运收入区间开始值 */
    @Excel(name = "幸运收入区间开始值")
    private Integer luckyNumberRangeStart;

    /** 幸运收入区间终止值 */
    @Excel(name = "幸运收入区间终止值")
    private Integer luckyNumberRangeEnd;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

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

    private String isDraws;

    private String isVoucher;

    private Integer voucherObtainStart;

    private Integer voucherObtainEnd;

    private Integer voucherStart;

    private Integer voucherEnd;

    private Integer voucherCycle;

    public Integer getVoucherCycle() {
        return voucherCycle;
    }

    public void setVoucherCycle(Integer voucherCycle) {
        this.voucherCycle = voucherCycle;
    }

    public String getIsDraws() {
        return isDraws;
    }

    public void setIsDraws(String isDraws) {
        this.isDraws = isDraws;
    }

    public String getIsVoucher() {
        return isVoucher;
    }

    public void setIsVoucher(String isVoucher) {
        this.isVoucher = isVoucher;
    }

    public Integer getVoucherStart() {
        return voucherStart;
    }

    public void setVoucherStart(Integer voucherStart) {
        this.voucherStart = voucherStart;
    }

    public Integer getVoucherEnd() {
        return voucherEnd;
    }

    public void setVoucherEnd(Integer voucherEnd) {
        this.voucherEnd = voucherEnd;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setNameIn(String nameIn)
    {
        this.nameIn = nameIn;
    }

    public String getNameIn()
    {
        return nameIn;
    }
    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getNameEn()
    {
        return nameEn;
    }
    public void setNameRu(String nameRu)
    {
        this.nameRu = nameRu;
    }

    public String getNameRu()
    {
        return nameRu;
    }
    public void setCoverImages(String coverImages)
    {
        this.coverImages = coverImages;
    }

    public String getCoverImages()
    {
        return coverImages;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setRewardProduct(Long rewardProduct)
    {
        this.rewardProduct = rewardProduct;
    }

    public Long getRewardProduct()
    {
        return rewardProduct;
    }
    public void setDailyInterest(Double dailyInterest)
    {
        this.dailyInterest = dailyInterest;
    }

    public Double getDailyInterest()
    {
        return dailyInterest;
    }
    public void setCycle(Integer cycle)
    {
        this.cycle = cycle;
    }

    public Integer getCycle()
    {
        return cycle;
    }
    public void setMinimumBuy(Double minimumBuy)
    {
        this.minimumBuy = minimumBuy;
    }

    public Double getMinimumBuy()
    {
        return minimumBuy;
    }
    public void setMaximumBuy(Double maximumBuy)
    {
        this.maximumBuy = maximumBuy;
    }

    public Double getMaximumBuy()
    {
        return maximumBuy;
    }
    public void setFundType(String fundType)
    {
        this.fundType = fundType;
    }

    public String getFundType()
    {
        return fundType;
    }
    public void setProgress(Double progress)
    {
        this.progress = progress;
    }

    public Double getProgress()
    {
        return progress;
    }
    public void setShowProgressBar(String showProgressBar)
    {
        this.showProgressBar = showProgressBar;
    }

    public String getShowProgressBar()
    {
        return showProgressBar;
    }
    public void setSellingTimestamp(Date sellingTimestamp)
    {
        this.sellingTimestamp = sellingTimestamp;
    }

    public Date getSellingTimestamp()
    {
        return sellingTimestamp;
    }
    public void setTotalFund(Double totalFund)
    {
        this.totalFund = totalFund;
    }

    public Double getTotalFund()
    {
        return totalFund;
    }
    public void setCopies(Integer copies)
    {
        this.copies = copies;
    }

    public Integer getCopies()
    {
        return copies;
    }
    public void setIsVisible(String isVisible)
    {
        this.isVisible = isVisible;
    }

    public String getIsVisible()
    {
        return isVisible;
    }
    public void setOnSale(String onSale)
    {
        this.onSale = onSale;
    }

    public String getOnSale()
    {
        return onSale;
    }
    public void setHasGroupBuyOption(String hasGroupBuyOption)
    {
        this.hasGroupBuyOption = hasGroupBuyOption;
    }

    public String getHasGroupBuyOption()
    {
        return hasGroupBuyOption;
    }
    public void setLuckyNumberRangeStart(Integer luckyNumberRangeStart)
    {
        this.luckyNumberRangeStart = luckyNumberRangeStart;
    }

    public Integer getLuckyNumberRangeStart()
    {
        return luckyNumberRangeStart;
    }
    public void setLuckyNumberRangeEnd(Integer luckyNumberRangeEnd)
    {
        this.luckyNumberRangeEnd = luckyNumberRangeEnd;
    }

    public Integer getLuckyNumberRangeEnd()
    {
        return luckyNumberRangeEnd;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
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

    public Double getCurrFund() {
        return currFund;
    }

    public void setCurrFund(Double currFund) {
        this.currFund = currFund;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getVoucherObtainStart() {
        return voucherObtainStart;
    }

    public void setVoucherObtainStart(Integer voucherObtainStart) {
        this.voucherObtainStart = voucherObtainStart;
    }

    public Integer getVoucherObtainEnd() {
        return voucherObtainEnd;
    }

    public void setVoucherObtainEnd(Integer voucherObtainEnd) {
        this.voucherObtainEnd = voucherObtainEnd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("nameIn", getNameIn())
                .append("nameEn", getNameEn())
                .append("nameRu", getNameRu())
                .append("coverImages", getCoverImages())
                .append("category", getCategory())
                .append("type", getType())
                .append("rewardProduct", getRewardProduct())
                .append("dailyInterest", getDailyInterest())
                .append("cycle", getCycle())
                .append("minimumBuy", getMinimumBuy())
                .append("maximumBuy", getMaximumBuy())
                .append("fundType", getFundType())
                .append("progress", getProgress())
                .append("showProgressBar", getShowProgressBar())
                .append("sellingTimestamp", getSellingTimestamp())
                .append("currFund", getCurrFund())
                .append("totalFund", getTotalFund())
                .append("copies", getCopies())
                .append("isVisible", getIsVisible())
                .append("onSale", getOnSale())
                .append("hasGroupBuyOption", getHasGroupBuyOption())
                .append("luckyNumberRangeStart", getLuckyNumberRangeStart())
                .append("luckyNumberRangeEnd", getLuckyNumberRangeEnd())
                .append("status", getStatus())
                .append("description", getDescription())
                .append("descriptionEn", getDescriptionEn())
                .append("descriptionIn", getDescriptionIn())
                .append("descriptionRu", getDescriptionRu())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
