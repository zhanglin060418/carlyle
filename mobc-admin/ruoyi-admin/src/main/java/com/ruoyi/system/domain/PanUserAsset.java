package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 增值宝对象 pan_user_asset
 *
 * @author ruoyi
 * @date 2023-04-14
 */
public class PanUserAsset extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 增值宝ID
     */
    private Long userAssetId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 余额
     */
    @Excel(name = "余额")
    private BigDecimal balance;

    /**
     * 交易
     */
    @Excel(name = "金额")
    private BigDecimal amount;

    private int drawsNumber;

    /*
    交易金额
     */

    public void setUserAssetId(Long userAssetId) {
        this.userAssetId = userAssetId;
    }

    public Long getUserAssetId() {
        return userAssetId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDrawsNumber() {
        return drawsNumber;
    }

    public void setDrawsNumber(int drawsNumber) {
        this.drawsNumber = drawsNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userAssetId", getUserAssetId())
                .append("userId", getUserId())
                .append("balance", getBalance())
                .append("amount", getAmount())
                .toString();
    }

}
