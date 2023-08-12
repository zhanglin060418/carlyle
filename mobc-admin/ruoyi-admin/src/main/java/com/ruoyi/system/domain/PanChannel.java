package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付通道对象 pan_channel
 *
 * @author ruoyi
 * @date 2023-04-24
 */
public class PanChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Channel ID */
    private Long channelId;

    /** 商户号 */
    @Excel(name = "商户号")
    private String channelMerno;

    /** 通道名称 */
    @Excel(name = "通道名称")
    private String channelName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 充值手续费 */
    @Excel(name = "充值手续费")
    private Long inFeeRate;

    /** 提现手续费 */
    @Excel(name = "提现手续费")
    private Long outFeeRate;

    /** 最小充值金额 */
    @Excel(name = "最小充值金额")
    private Long inMinAmount;

    /** 最大充值金额 */
    @Excel(name = "最大充值金额")
    private Long inMaxAmount;

    /** 最小提现金额 */
    @Excel(name = "最小提现金额")
    private Long outMinAmount;

    /** 调用产生json */
    @Excel(name = "调用产生json")
    private String jsonParam;


    /** 页面显示 */
    @Excel(name = "页面显示")
    private String displayName;

    /** 显示logo */
    @Excel(name = "显示logo")
    private String displayLogo;

    /** 可能出现的营销信息 */
    @Excel(name = "可能出现的营销信息")
    private String displayTips;

    @Excel(name = "可提现余额")
    private String balance; //不保存数据库


    @Excel(name = "是否代付")
    private String isProxy;//只设定一个可以做为代付通道


    public String getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(String isProxy) {
        this.isProxy = isProxy;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
    public void setDisplayLogo(String displayLogo)
    {
        this.displayLogo = displayLogo;
    }

    public String getDisplayLogo()
    {
        return displayLogo;
    }
    public void setDisplayTips(String displayTips)
    {
        this.displayTips = displayTips;
    }

    public String getDisplayTips()
    {
        return displayTips;
    }




    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }
    public void setChannelMerno(String channelMerno)
    {
        this.channelMerno = channelMerno;
    }

    public String getChannelMerno()
    {
        return channelMerno;
    }
    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelName()
    {
        return channelName;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setInFeeRate(Long inFeeRate)
    {
        this.inFeeRate = inFeeRate;
    }

    public Long getInFeeRate()
    {
        return inFeeRate;
    }
    public void setOutFeeRate(Long outFeeRate)
    {
        this.outFeeRate = outFeeRate;
    }

    public Long getOutFeeRate()
    {
        return outFeeRate;
    }
    public void setInMinAmount(Long inMinAmount)
    {
        this.inMinAmount = inMinAmount;
    }

    public Long getInMinAmount()
    {
        return inMinAmount;
    }
    public void setInMaxAmount(Long inMaxAmount)
    {
        this.inMaxAmount = inMaxAmount;
    }

    public Long getInMaxAmount()
    {
        return inMaxAmount;
    }
    public void setOutMinAmount(Long outMinAmount)
    {
        this.outMinAmount = outMinAmount;
    }

    public Long getOutMinAmount()
    {
        return outMinAmount;
    }
    public void setJsonParam(String jsonParam)
    {
        this.jsonParam = jsonParam;
    }

    public String getJsonParam()
    {
        return jsonParam;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("channelId", getChannelId())
            .append("channelMerno", getChannelMerno())
            .append("channelName", getChannelName())
            .append("status", getStatus())
            .append("inFeeRate", getInFeeRate())
            .append("outFeeRate", getOutFeeRate())
            .append("inMinAmount", getInMinAmount())
            .append("inMaxAmount", getInMaxAmount())
            .append("outMinAmount", getOutMinAmount())
            .append("jsonParam", getJsonParam())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())

            .append("displayName", getDisplayName())
            .append("displayLogo", getDisplayLogo())
            .append("displayTips", getDisplayTips())
            .toString();
    }
}
