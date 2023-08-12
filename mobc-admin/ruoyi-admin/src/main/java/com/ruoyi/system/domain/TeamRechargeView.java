package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public class TeamRechargeView extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long userId;
	private String userName;

	private Long agentId;
	private String agentName;

	private Long topId;
	private String topName;

	private Long managerId;
	private String managerName;

    private Long childCount;
    private Long grandCount;
    private Long greatGrandCount;

	private Long childRechargeCount;
	private Long grandRechargeCount;
	private Long greatGrandRechargeCount;

	private Long childRechargeAmt;
	private Long grandRechargeAmt;
	private Long greatGrandRechargeAmt;

	private Long childPurcherCount;
	private Long grandPurcherCount;
	private Long greatGrandPurcherCount;

	private Long childPurcherAmt;
	private Long grandPurcherAmt;
	private Long greatGrandPurcherAmt;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Long getTopId() {
		return topId;
	}

	public void setTopId(Long topId) {
		this.topId = topId;
	}

	public String getTopName() {
		return topName;
	}

	public void setTopName(String topName) {
		this.topName = topName;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}

	public Long getGrandCount() {
		return grandCount;
	}

	public void setGrandCount(Long grandCount) {
		this.grandCount = grandCount;
	}

	public Long getGreatGrandCount() {
		return greatGrandCount;
	}

	public void setGreatGrandCount(Long greatGrandCount) {
		this.greatGrandCount = greatGrandCount;
	}

	public Long getChildRechargeCount() {
		return childRechargeCount;
	}

	public void setChildRechargeCount(Long childRechargeCount) {
		this.childRechargeCount = childRechargeCount;
	}

	public Long getGrandRechargeCount() {
		return grandRechargeCount;
	}

	public void setGrandRechargeCount(Long grandRechargeCount) {
		this.grandRechargeCount = grandRechargeCount;
	}

	public Long getGreatGrandRechargeCount() {
		return greatGrandRechargeCount;
	}

	public void setGreatGrandRechargeCount(Long greatGrandRechargeCount) {
		this.greatGrandRechargeCount = greatGrandRechargeCount;
	}

	public Long getChildRechargeAmt() {
		return childRechargeAmt;
	}

	public void setChildRechargeAmt(Long childRechargeAmt) {
		this.childRechargeAmt = childRechargeAmt;
	}

	public Long getGrandRechargeAmt() {
		return grandRechargeAmt;
	}

	public void setGrandRechargeAmt(Long grandRechargeAmt) {
		this.grandRechargeAmt = grandRechargeAmt;
	}

	public Long getGreatGrandRechargeAmt() {
		return greatGrandRechargeAmt;
	}

	public void setGreatGrandRechargeAmt(Long greatGrandRechargeAmt) {
		this.greatGrandRechargeAmt = greatGrandRechargeAmt;
	}

	public Long getChildPurcherCount() {
		return childPurcherCount;
	}

	public void setChildPurcherCount(Long childPurcherCount) {
		this.childPurcherCount = childPurcherCount;
	}

	public Long getGrandPurcherCount() {
		return grandPurcherCount;
	}

	public void setGrandPurcherCount(Long grandPurcherCount) {
		this.grandPurcherCount = grandPurcherCount;
	}

	public Long getGreatGrandPurcherCount() {
		return greatGrandPurcherCount;
	}

	public void setGreatGrandPurcherCount(Long greatGrandPurcherCount) {
		this.greatGrandPurcherCount = greatGrandPurcherCount;
	}

	public Long getChildPurcherAmt() {
		return childPurcherAmt;
	}

	public void setChildPurcherAmt(Long childPurcherAmt) {
		this.childPurcherAmt = childPurcherAmt;
	}

	public Long getGrandPurcherAmt() {
		return grandPurcherAmt;
	}

	public void setGrandPurcherAmt(Long grandPurcherAmt) {
		this.grandPurcherAmt = grandPurcherAmt;
	}

	public Long getGreatGrandPurcherAmt() {
		return greatGrandPurcherAmt;
	}

	public void setGreatGrandPurcherAmt(Long greatGrandPurcherAmt) {
		this.greatGrandPurcherAmt = greatGrandPurcherAmt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
