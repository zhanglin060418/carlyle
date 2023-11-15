package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户余额对象 pan_user_balance
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public class PanUserBalance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 总余额(Total balance )
     *  */
    @Excel(name = "总余额")
    private BigDecimal balance;
    /**
     * 可用余额(available balance)
     */
    private BigDecimal availableAmt;
    /**
     * 在途金额(Transferring)
     */
    private BigDecimal transitAmt;
    /**
     * 不可提现金额(lock Balance)
     */
    private BigDecimal lockBalance;

    private BigDecimal rewardAmt;

    /**
     * 增值宝(asset balance)
     */
    private BigDecimal assetBalance;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户余额ID */
    private Long userBalanceId;
    private Long topId;
    private Long managerId;
    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
    }

    private String username;

    private String updateAt;

    private BigDecimal amount;

    public int getDrawsNumber() {
        return drawsNumber;
    }

    public void setDrawsNumber(int drawsNumber) {
        this.drawsNumber = drawsNumber;
    }

    private int drawsNumber;

    private String  billType;
    private String transType;
    private String isRebate;
    private Integer vipLevel;
    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }
    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("username", getUsername())
                .append("balance", getBalance())
                .append("availableAmt", getAvailableAmt())
                .append("transitAmt", getTransitAmt())
                .append("lockBalance", getLockBalance())
                .append("assetBalance", getAssetBalance())
                .toString();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(BigDecimal availableAmt) {
        this.availableAmt = availableAmt;
    }

    public BigDecimal getTransitAmt() {
        return transitAmt;
    }

    public void setTransitAmt(BigDecimal transitAmt) {
        this.transitAmt = transitAmt;
    }

    public BigDecimal getLockBalance() {
        return lockBalance;
    }

    public void setLockBalance(BigDecimal lockBalance) {
        this.lockBalance = lockBalance;
    }

    public BigDecimal getAssetBalance() {
        return assetBalance;
    }

    public void setAssetBalance(BigDecimal assetBalance) {
        this.assetBalance = assetBalance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserBalanceId() {
        return userBalanceId;
    }

    public void setUserBalanceId(Long userBalanceId) {
        this.userBalanceId = userBalanceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
    private String topName;
    private String managerName;

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

    private Long agentId;
    private String agentName;

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

    public String getIsRebate() {
        return isRebate;
    }

    public void setIsRebate(String isRebate) {
        this.isRebate = isRebate;
    }

    public BigDecimal getRewardAmt() {
        return rewardAmt;
    }

    public void setRewardAmt(BigDecimal rewardAmt) {
        this.rewardAmt = rewardAmt;
    }
}
