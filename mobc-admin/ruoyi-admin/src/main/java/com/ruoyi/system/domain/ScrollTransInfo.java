package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 滚屏交易信息对象 pan_scroll_trans_info
 * 
 * @author ruoyi
 * @date 2023-06-05
 */
public class ScrollTransInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 交易编号 */
    private Long transId;

    /** 用户账户 */
    @Excel(name = "用户账户")
    private String userName;

    /** 交易类型 */
    @Excel(name = "交易类型")
    private String transType;

    /** 交易金额 */
    @Excel(name = "交易金额")
    private BigDecimal amount;

    public void setTransId(Long transId) 
    {
        this.transId = transId;
    }

    public Long getTransId() 
    {
        return transId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setTransType(String transType) 
    {
        this.transType = transType;
    }

    public String getTransType() 
    {
        return transType;
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
            .append("transId", getTransId())
            .append("userName", getUserName())
            .append("transType", getTransType())
            .append("amount", getAmount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
