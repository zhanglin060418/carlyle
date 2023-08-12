package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户余额对象 pan_user_balance
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public class TeamOverview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long userId;
	private String userName;

	private Long parentId;
	private String parentName;

    private Long childCount;
    private Long grandCount;
    private Long greatGrandCount;

	//今日新增人数
    private Long todayChildCount;

    private Long childIncome;
    private Long grandIncome;
    private Long greatGrandIncome;

	//今日总收益
    private Long dailyIncome;
	//团队总收益
	private Long childTotalIncome;

	private Double childCrowdRatio;
	private Double grandCrowdRatio;
	private Double greatGrandCrowdRatio;

	// 首次购买产品获取奖励金额比例
	private Double firstPurchaseCommissionRatio;
	// 产品购买赠送产品比例
	private Double purchaseTreasureRate;
	// 赠送产品日利率
	private Double  rewardProductDailyInterest;

	private Long rechargeCount;
	private Long withdrawCount;
	private Long purchaseCount;

	private Long totalRechargeCount;
	private Long totalWithdrawCount;
	private Long totalPurchaseCount;
	private Long totalPeopleCount;
	private Long totalTeamReward;
	private Long totalTeamBuyCount;


	private String transType;
	private Long  transAmt;

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Long getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(Long transAmt) {
		this.transAmt = transAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Long rechargeCount) {
		this.rechargeCount = rechargeCount;
	}

	public Long getWithdrawCount() {
		return withdrawCount;
	}

	public void setWithdrawCount(Long withdrawCount) {
		this.withdrawCount = withdrawCount;
	}

	public Long getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(Long purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

	public Long getTotalPurchaseCount() {
		return totalPurchaseCount;
	}

	public void setTotalPurchaseCount(Long totalPurchaseCount) {
		this.totalPurchaseCount = totalPurchaseCount;
	}

	public Long getTotalPeopleCount() {
		return totalPeopleCount;
	}

	public void setTotalPeopleCount(Long totalPeopleCount) {
		this.totalPeopleCount = totalPeopleCount;
	}

	public Long getTotalTeamReward() {
		return totalTeamReward;
	}

	public void setTotalTeamReward(Long totalTeamReward) {
		this.totalTeamReward = totalTeamReward;
	}

	public Long getTotalTeamBuyCount() {
		return totalTeamBuyCount;
	}

	public void setTotalTeamBuyCount(Long totalTeamBuyCount) {
		this.totalTeamBuyCount = totalTeamBuyCount;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Long getTodayChildCount() {
		return todayChildCount;
	}
	public void setTodayChildCount(Long todayChildCount) {
		this.todayChildCount = todayChildCount;
	}
	public Long getChildIncome() {
		return childIncome;
	}
	public void setChildIncome(Long childIncome) {
		this.childIncome = childIncome;
	}
	public Long getGrandIncome() {
		return grandIncome;
	}
	public void setGrandIncome(Long grandIncome) {
		this.grandIncome = grandIncome;
	}
	public Long getGreatGrandIncome() {
		return greatGrandIncome;
	}
	public void setGreatGrandIncome(Long greatGrandIncome) {
		this.greatGrandIncome = greatGrandIncome;
	}
	public Long getDailyIncome() {
		return dailyIncome;
	}
	public void setDailyIncome(Long dailyIncome) {
		this.dailyIncome = dailyIncome;
	}

	public Double getChildCrowdRatio() {
		return childCrowdRatio;
	}

	public void setChildCrowdRatio(Double childCrowdRatio) {
		this.childCrowdRatio = childCrowdRatio;
	}

	public Double getGrandCrowdRatio() {
		return grandCrowdRatio;
	}

	public void setGrandCrowdRatio(Double grandCrowdRatio) {
		this.grandCrowdRatio = grandCrowdRatio;
	}

	public Double getGreatGrandCrowdRatio() {
		return greatGrandCrowdRatio;
	}

	public void setGreatGrandCrowdRatio(Double greatGrandCrowdRatio) {
		this.greatGrandCrowdRatio = greatGrandCrowdRatio;
	}

	public Long getTotalRechargeCount() {
		return totalRechargeCount;
	}

	public void setTotalRechargeCount(Long totalRechargeCount) {
		this.totalRechargeCount = totalRechargeCount;
	}

	public Long getTotalWithdrawCount() {
		return totalWithdrawCount;
	}

	public void setTotalWithdrawCount(Long totalWithdrawCount) {
		this.totalWithdrawCount = totalWithdrawCount;
	}

	public Double getFirstPurchaseCommissionRatio() {
		return firstPurchaseCommissionRatio;
	}

	public void setFirstPurchaseCommissionRatio(Double firstPurchaseCommissionRatio) {
		this.firstPurchaseCommissionRatio = firstPurchaseCommissionRatio;
	}

	public Double getPurchaseTreasureRate() {
		return purchaseTreasureRate;
	}

	public void setPurchaseTreasureRate(Double purchaseTreasureRate) {
		this.purchaseTreasureRate = purchaseTreasureRate;
	}

	public Double getRewardProductDailyInterest() {
		return rewardProductDailyInterest;
	}

	public void setRewardProductDailyInterest(Double rewardProductDailyInterest) {
		this.rewardProductDailyInterest = rewardProductDailyInterest;
	}

	public Long getChildTotalIncome() {
		return childTotalIncome;
	}

	public void setChildTotalIncome(Long childTotalIncome) {
		this.childTotalIncome = childTotalIncome;
	}
}
