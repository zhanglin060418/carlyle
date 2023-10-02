package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户余额对象 pan_user_balance
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public class TeamIncomeOverview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userName;
    private String userAvatar;
    private Long income;
    private Long recharge;

	public Long getRecharge() {
		return recharge;
	}

	public void setRecharge(Long recharge) {
		this.recharge = recharge;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public Long getIncome() {
		return income;
	}
	public void setIncome(Long income) {
		this.income = income;
	}


}
