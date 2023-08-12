package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 产品支付对象 purchase
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public class PurchaseNews extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String userName;

    private BigDecimal amount;



    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userName", getUserName())
            .append("amount", getAmount())
            .toString();
    }
}
