package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 【请填写功能名称】对象 pan_transaction_history
 *
 * @author ruoyi
 * @date 2023-04-10
 */
public class PanTransactionHistory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 交易记录ID
     */
    private Long transactionHistoryId;

    /**
     * 用户ID
     */

    private Long userId;
    private Long topId;
    private Long managerId;
    /**
     * 用户名
     */
    @Excel(name = "用户")
    private String username;
    /**
     * 交易金额
     */
    @Excel(name = "金额")
    private BigDecimal amount;


    @Excel(name = "笔数")
    private int transCount;
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
     * 交易时间
     */
    private String transactionDate;

    private int currentPage;

    private BigDecimal productAmount;

    /**
     * 交易前总额
     */
    private BigDecimal amountBefore;

    private int size;

    private Long parentId;
    private Long grandId;
    private Long greatGrandId;


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
    // 原订单编号
    private String orOrderId;
    private Long agentId;
    private String agentName;
    private String orUserName;
    private String topName;
    private String managerName;
    private String productName;

    private String level;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getGrandId() {
        return grandId;
    }

    public void setGrandId(Long grandId) {
        this.grandId = grandId;
    }

    public Long getGreatGrandId() {
        return greatGrandId;
    }

    public void setGreatGrandId(Long greatGrandId) {
        this.greatGrandId = greatGrandId;
    }

    public int getTransCount() {
        return transCount;
    }

    public void setTransCount(int transCount) {
        this.transCount = transCount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
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

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDate() {
        return transactionDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("transactionType", getTransactionType())
                .append("amount", getAmount())
                .append("isIncome", getIsIncome())
                .append("amountBefore", getAmountBefore())
                .append("amountAfter", getAmountAfter())
                .append("billType", getBillType())
                .toString();
    }

    public String getIsIncome() {
        return isIncome;
    }

    public void setIsIncome(String isIncome) {
        this.isIncome = isIncome;
    }

    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
    }

    public String getOrOrderId() {
        return orOrderId;
    }

    public void setOrOrderId(String orOrderId) {
        this.orOrderId = orOrderId;
    }

    public Long getOrUserId() {
        return orUserId;
    }

    public void setOrUserId(Long orUserId) {
        this.orUserId = orUserId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }



    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }


    public String getOrUserName() {
        return orUserName;
    }

    public void setOrUserName(String orUserName) {
        this.orUserName = orUserName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
