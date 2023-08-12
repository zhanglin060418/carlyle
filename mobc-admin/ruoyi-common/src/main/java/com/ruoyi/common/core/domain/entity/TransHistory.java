package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
public class TransHistory extends BaseEntity
{
    /**
     * 交易记录ID
     */
    private Long transactionHistoryId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 交易类型
     */
    @Excel(name = "类型")
    private String transactionType;

    /**
     * 账单类型
     * IN：进账
     * OUT：出账
     * BAL：平账
     */
    private String billType;


    /**
     * 交易金额
     */
    @Excel(name = "数量")
    private BigDecimal amount;

    /**
     * 交易前总额
     */
    private BigDecimal amountBefore;

    /**
     * 交易后总额
     */
    private BigDecimal amountAfter;
    /**
     * 是否收益
     */
    private String isIncome;

    // 原订单用户编号
    private Long  orUserId;

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public void setTransactionHistoryId(Long transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }

    public Long getTransactionHistoryId() {
        return transactionHistoryId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getAmountBefore() {
        return amountBefore;
    }

    public void setAmountBefore(BigDecimal amountBefore) {
        this.amountBefore = amountBefore;
    }

    public BigDecimal getAmountAfter() {
        return amountAfter;
    }

    public void setAmountAfter(BigDecimal amountAfter) {
        this.amountAfter = amountAfter;
    }

    public String getIsIncome() {
        return isIncome;
    }

    public void setIsIncome(String isIncome) {
        this.isIncome = isIncome;
    }

    public Long getOrUserId() {
        return orUserId;
    }

    public void setOrUserId(Long orUserId) {
        this.orUserId = orUserId;
    }

}
