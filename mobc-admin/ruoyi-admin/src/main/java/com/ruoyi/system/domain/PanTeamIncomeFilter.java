package com.ruoyi.system.domain;

import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

public class PanTeamIncomeFilter extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    
    private List<String> transactionType;
    
    private List<String> transactionRemark;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(List<String> transactionType) {
		this.transactionType = transactionType;
	}

	public List<String> getTransactionRemark() {
		return transactionRemark;
	}

	public void setTransactionRemark(List<String> transactionRemark) {
		this.transactionRemark = transactionRemark;
	}
}