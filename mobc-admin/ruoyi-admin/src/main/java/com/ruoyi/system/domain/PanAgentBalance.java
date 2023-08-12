package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 用户余额对象 pan_user_balance
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public class PanAgentBalance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 总余额(Total balance )
     */
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
     * 预充值金额
     */
    private BigDecimal prechargeAmt;

    private String updateAt;

    private Long agentId;

    private String agentName;

    private BigDecimal amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPrechargeAmt() {
        return prechargeAmt;
    }

    public void setPrechargeAmt(BigDecimal prechargeAmt) {
        this.prechargeAmt = prechargeAmt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
