package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 用户余额对象 pan_user_balance
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public class RechargeCreate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 余额 */
    @Excel(name = "余额")
    private BigDecimal amount;

    @Excel(name = "调用域名")
    private String domain;//产生调用的域名信息 主要为了完成前台跳转使用 因为前台请求可能来自于不同的域名

    @Excel(name="通道名字")
    private String selectChannel;//选择的渠道信息


    public String getSelectChannel() {
        return selectChannel;
    }

    public void setSelectChannel(String selectChannel) {
        this.selectChannel = selectChannel;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount= amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("amount", getAmount())
                .append("domain", getDomain())
                .append("selectChannel", getSelectChannel())
            .toString();
    }
}
