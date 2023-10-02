package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.system.domain.PanTransactionHistory;
import com.ruoyi.system.domain.TeamIncomeOverview;
import com.ruoyi.system.domain.TeamOverview;
import com.ruoyi.system.domain.TeamRechargeView;
import com.ruoyi.system.mapper.PanTransactionHistoryMapper;
import com.ruoyi.system.service.IPanTransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-10
 */
@Service
public class PanTransactionHistoryServiceImpl implements IPanTransactionHistoryService {
	@Autowired
	private PanTransactionHistoryMapper transMapper;

	/**
	 * 查询【请填写功能名称】
	 *
	 * @param transactionHistoryId 【请填写功能名称】主键
	 * @return 【请填写功能名称】
	 */
	@Override
	public PanTransactionHistory selectPanTransactionHistoryByTransactionHistoryId(Long transactionHistoryId) {
		return transMapper.selectPanTransactionHistoryByTransactionHistoryId(transactionHistoryId);
	}

	/**
	 * 查询【请填写功能名称】列表
	 *
	 * @param panTransactionHistory 【请填写功能名称】
	 * @return 【请填写功能名称】
	 */
	@Override
	public List<PanTransactionHistory> selectPanTransactionHistoryList(PanTransactionHistory panTransactionHistory) {
		return transMapper.selectPanTransactionHistoryList(panTransactionHistory);
	}

	/**
	 * 新增【请填写功能名称】
	 *
	 * @param panTransactionHistory 【请填写功能名称】
	 * @return 结果
	 */
	@Override
	public int insertPanTransactionHistory(PanTransactionHistory panTransactionHistory) {
		return transMapper.insertPanTransactionHistory(panTransactionHistory);
	}

	/**
	 * 修改【请填写功能名称】
	 *
	 * @param panTransactionHistory 【请填写功能名称】
	 * @return 结果
	 */
	@Override
	public int updatePanTransactionHistory(PanTransactionHistory panTransactionHistory) {
		return transMapper.updatePanTransactionHistory(panTransactionHistory);
	}

	/**
	 * 批量删除【请填写功能名称】
	 *
	 * @param transactionHistoryIds 需要删除的【请填写功能名称】主键
	 * @return 结果
	 */
	@Override
	public int deletePanTransactionHistoryByTransactionHistoryIds(Long[] transactionHistoryIds) {
		return transMapper.deletePanTransactionHistoryByTransactionHistoryIds(transactionHistoryIds);
	}

	/**
	 * 删除【请填写功能名称】信息
	 *
	 * @param transactionHistoryId 【请填写功能名称】主键
	 * @return 结果
	 */
	@Override
	public int deletePanTransactionHistoryByTransactionHistoryId(Long transactionHistoryId) {
		return transMapper.deletePanTransactionHistoryByTransactionHistoryId(transactionHistoryId);
	}

	@Override
	public Long getDailyIncome(Long userId) {
		return transMapper.getDailyIncome(userId);
	}

	@Override
	public Long getTotalIncome(Long userId) {
		return transMapper.getTotalIncome(userId);
	}

	@Override
	public Long getChildIncome(Long userId) {
		return transMapper.getChildIncome(userId);
	}

	@Override
	public Long getGrandIncome(Long userId) {
		return transMapper.getGrandIncome(userId);
	}

	@Override
	public Long getGreatGrandIncome(Long userId) {
		return transMapper.getGreatGrandIncome(userId);
	}

	@Override
	public Long getChildDailyIncome(Long userId) {
		return transMapper.getChildDailyIncome(userId);
	}

	@Override
	public Long getChildTotalIncome(Long userId) {
		return transMapper.getChildTotalIncome(userId);
	}

	@Override
	public List<PanTransactionHistory> selectTodayPanTransactionHistoryList(
			Long userId) {
		return transMapper.getTodayTransaction(userId);
	}

	@Override
	public List<PanTransactionHistory> selectYesterdayPanTransactionHistoryList(
			Long userId) {
		return transMapper.getYesterdayTransaction(userId);
	}

	@Override
	public List<PanTransactionHistory> selectLastWeekPanTransactionHistoryList(
			Long userId) {
		return transMapper.getLastWeekTransaction(userId);
	}

	@Override
	public Long getTotayTreasureRewardById(Long userId) {
		return transMapper.getTodayTreasureRewardById(userId);
	}

	@Override
	public Long getTotalTreasureRewardById(Long userId) {
		return transMapper.getTotalTreasureRewardById(userId);
	}

	@Override
	public List<TeamIncomeOverview> getGrandIncomOverview(Long userId) {
		return transMapper.getGrandIncomOverview(userId);
	}

	@Override
	public List<TeamIncomeOverview> getGreatGrandIncomOverview(Long userId) {
		return transMapper.getGreatGrandIncomOverview(userId);
	}

	@Override
	public List<TeamIncomeOverview> getChildIncomeOverview(Long userId) {
		return transMapper.getChildIncomeOverview(userId);
	}

	@Override
	public List<PanTransactionHistory> getGreatGrandIncomListByUser(Long userId) {
		return transMapper.getGreatGrandIncomListByUser(userId);
	}

	@Override
	public List<PanTransactionHistory> getGrandIncomListByUser(Long userId) {
		return transMapper.getGrandIncomListByUser(userId);
	}

	@Override
	public List<PanTransactionHistory> getChildIncomeListByUser(Long userId) {
		return transMapper.getChildIncomeListByUser(userId);
	}

	@Override
	public Long selectPanTransactionHistoryListCount(PanTransactionHistory panTransactionHistory) {
		return transMapper.selectPanTransactionHistoryListCount(panTransactionHistory);
	}

	@Override
	public List<TeamOverview> selectUserTeamList(SysUser user) {
		List<TeamOverview> beforeTeam = new ArrayList<TeamOverview>();
		if (user.getUserType().equals("02")) {
			beforeTeam = transMapper.selectUserTeamByManagerList(user);
		} else {
			beforeTeam = transMapper.selectUserTeamList(user);
		}
		List<TeamOverview> afterTeam = new ArrayList<TeamOverview>();
		if (beforeTeam.size() > 0) {
			for (int i = 0; i < beforeTeam.size(); i++) {
				TeamOverview team = beforeTeam.get(i);
				Long rechargeCount = 0L;
				Long withdrawCount = 0L;
				Long purchaseCount = 0L;
				Long totalRechargeCount = 0L;
				Long totalWithdrawCount = 0L;
				Long totalPurchaseCount = 0L;
				Long totalTeamReward = 0L;
				List<TeamOverview> list = transMapper.selectUserTransCount(team.getUserId());
				if (list.size() > 0) {
					for (TeamOverview temp : list) {
						if (temp.getTransType().equalsIgnoreCase(TransType.Recharge.name())) {
							rechargeCount = temp.getTransAmt();
						} else if (temp.getTransType().equalsIgnoreCase(TransType.Withdraw.name())) {
							withdrawCount = temp.getTransAmt();
						} else if (temp.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name())) {
							purchaseCount = temp.getTransAmt();
						}
					}
				}
				List<TeamOverview> teamUserlist = new ArrayList<TeamOverview>();
				Long totalPeopleCount = 0L;
				if (user.getUserType().equals("02")) {
					PanTransactionHistory currUser = new PanTransactionHistory();
					currUser.setTopId(team.getUserId());
					teamUserlist = transMapper.selectTeamTransCountBySys(currUser);
					totalPeopleCount = transMapper.totalPeopleCountBySys(team.getUserId());
				} else {
					teamUserlist = transMapper.selectTeamTransCount(team.getUserId());
					totalPeopleCount = transMapper.totalPeopleCount(team.getUserId());
				}

				if (teamUserlist.size() > 0) {
					for (TeamOverview teamUser : teamUserlist) {
						if (teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name())) {
							totalRechargeCount = teamUser.getTransAmt();
						} else if (teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name())) {
							totalWithdrawCount = teamUser.getTransAmt();
						} else if (teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name())) {
							totalPurchaseCount = teamUser.getTransAmt();
						} else if (teamUser.getTransType().equalsIgnoreCase(TransType.Commission_A_Reward.name()) ||
								teamUser.getTransType().equalsIgnoreCase(TransType.Commission_B_Reward.name()) ||
								teamUser.getTransType().equalsIgnoreCase(TransType.Commission_C_Reward.name()) ||
								teamUser.getTransType().equalsIgnoreCase(TransType.Child_First_Purchase_Reward.name())) {
							totalTeamReward = totalTeamReward + teamUser.getTransAmt();
						}
					}
				}

				Long totalTeamBuyCount = transMapper.totalTeamBuyCount(team.getUserId());
				team.setRechargeCount(rechargeCount);
				team.setWithdrawCount(withdrawCount);
				team.setPurchaseCount(purchaseCount);
				team.setTotalRechargeCount(totalRechargeCount);
				team.setTotalWithdrawCount(totalWithdrawCount);
				team.setTotalPurchaseCount(totalPurchaseCount);
				team.setTotalTeamBuyCount(totalTeamBuyCount);
				team.setTotalTeamReward(totalTeamReward);
				team.setTotalPeopleCount(totalPeopleCount);
				afterTeam.add(team);
			}
		}

		return afterTeam;
	}

	@Override
	public TeamOverview selectUserTeam(SysUser user) {
		TeamOverview userInfo = new TeamOverview();

		List<TeamOverview> teamUserlist = new ArrayList<>();
		if (user.getUserType().equals("02")) {
			userInfo = transMapper.selectManagerTeam(user.getUserId());
			PanTransactionHistory currUser = new PanTransactionHistory();
			currUser.setManagerId(user.getUserId());
			teamUserlist = transMapper.selectTeamTransCountBySys(currUser);
		} else if (user.getUserType().equals("01")) {
			userInfo = transMapper.selectUserTeam(user.getUserId());
			PanTransactionHistory currUser = new PanTransactionHistory();
			currUser.setTopId(user.getUserId());
			teamUserlist = transMapper.selectTeamTransCountBySys(currUser);
		} else {
			userInfo = transMapper.selectPanUserTeam(user.getUserId());
			teamUserlist = transMapper.selectTeamTransCount(user.getUserId());
		}
		List<TeamOverview> list = transMapper.selectUserTransCount(user.getUserId());
		Long rechargeCount = 0L;
		Long withdrawCount = 0L;
		Long purchaseCount = 0L;
		Long totalRechargeCount = 0L;
		Long totalWithdrawCount = 0L;
		Long totalPurchaseCount = 0L;
		Long totalTeamReward = 0L;
		if (list.size() > 0) {
			for (TeamOverview temp : list) {
				if (temp.getTransType().equalsIgnoreCase(TransType.Recharge.name())) {
					rechargeCount = temp.getTransAmt();
				} else if (temp.getTransType().equalsIgnoreCase(TransType.Withdraw.name())) {
					withdrawCount = temp.getTransAmt();
				} else if (temp.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name())) {
					purchaseCount = temp.getTransAmt();
				}
			}
		}


		if (teamUserlist.size() > 0) {
			for (TeamOverview teamUser : teamUserlist) {
				if (teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name())) {
					totalRechargeCount = teamUser.getTransAmt();
				} else if (teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name())) {
					totalWithdrawCount = teamUser.getTransAmt();
				} else if (teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name())) {
					totalPurchaseCount = teamUser.getTransAmt();
				} else if (teamUser.getTransType().equalsIgnoreCase(TransType.Commission_A_Reward.name()) ||
						teamUser.getTransType().equalsIgnoreCase(TransType.Commission_B_Reward.name()) ||
						teamUser.getTransType().equalsIgnoreCase(TransType.Commission_C_Reward.name()) ||
						teamUser.getTransType().equalsIgnoreCase(TransType.Child_First_Purchase_Reward.name())) {
					totalTeamReward = totalTeamReward + teamUser.getTransAmt();
				}
			}
		}

		Long totalTeamBuyCount = transMapper.totalTeamBuyCount(user.getUserId());

		userInfo.setRechargeCount(rechargeCount);
		userInfo.setWithdrawCount(withdrawCount);
		userInfo.setPurchaseCount(purchaseCount);
		userInfo.setTotalRechargeCount(totalRechargeCount);
		userInfo.setTotalWithdrawCount(totalWithdrawCount);
		userInfo.setTotalPurchaseCount(totalPurchaseCount);
		userInfo.setTotalTeamBuyCount(totalTeamBuyCount);
		userInfo.setTotalTeamReward(totalTeamReward);

		return userInfo;
	}

	@Override
	public List<PanTransactionHistory> selectPanTransByCommissionList(PanTransactionHistory panTransactionHistory) {
		return transMapper.selectPanTransByCommissionList(panTransactionHistory);
	}

	@Override
	public Long selectPanTransByCommissionListCount(PanTransactionHistory panTransactionHistory) {
		return transMapper.selectPanTransByCommissionListCount(panTransactionHistory);
	}

	@Override
	public Long selectPanTransByInterestListCount(PanTransactionHistory panTransactionHistory) {
		return transMapper.selectPanTransByInterestListCount(panTransactionHistory);
	}

	@Override
	public List<PanTransactionHistory> selectPanTransByInterestList(PanTransactionHistory panTransactionHistory) {
		return transMapper.selectPanTransByInterestList(panTransactionHistory);
	}

	@Override
	public List<TeamOverview> getTotalTransInfo(SysUser sysUser) {
		return transMapper.getTotalTransInfo(sysUser);
	}

	@Override
	public List<TeamOverview> getTotalTransInfoByDay(SysUser sysUser) {
		return transMapper.getTotalTransInfoByDay(sysUser);
	}

	@Override
	public List<TeamOverview> getToatlTransUsers(SysUser sysUser) {
		return transMapper.getToatlTransUsers(sysUser);
	}

	@Override
	public List<TeamOverview> getTodayTransUsers(SysUser sysUser) {
		return transMapper.getTodayTransUsers(sysUser);
	}

	@Override
	public List<TeamOverview> totalTransCountlist(SysUser sysUser) {
		return transMapper.totalTransCountlist(sysUser);
	}

	@Override
	public List<TeamOverview> todayTransCountlist(SysUser sysUser) {
		return transMapper.todayTransCountlist(sysUser);
	}

	@Override
	public Long todayActiveUsers(SysUser sysUser) {
		return transMapper.todayActiveUsers(sysUser);
	}

	@Override
	public Long todayFristRechargeUsers(SysUser sysUser) {
		return transMapper.todayFristRechargeUsers(sysUser);
	}

	@Override
	public List<TeamOverview> getUserTransInfo(Long userId) {
		return transMapper.getUserTransInfo(userId) ;
	}

	@Override
	public List<TeamOverview> getUserTransInfoByIds(String userId) {
		return transMapper.getUserTransInfoByIds(userId) ;
	}

	@Override
	public List<PanTransactionHistory> getPurchaseInterestList(PanTransactionHistory orderNo) {
		return transMapper.getPurchaseInterestList(orderNo) ;
	}
	@Override
	public List<TeamRechargeView> selectTeamRechargeListByClerk(TeamRechargeView teamRecharge){
		return transMapper.selectTeamRechargeListByClerk(teamRecharge) ;
	}
	@Override
	public List<TeamRechargeView> selectTeamRechargeListByUser(TeamRechargeView teamRecharge){
		return transMapper.selectTeamRechargeListByUser(teamRecharge) ;
	}

	@Override
	public TeamRechargeView selectTeamTransByUserId(Long userId){
		return transMapper.selectTeamTransByUserId(userId) ;
	}

	@Override
	public List<PanTransactionHistory> getClerkTeamTransInfo(PanTransactionHistory transInfo) {
		return transMapper.getClerkTeamTransInfo(transInfo) ;
	}


	@Override
	public List<TeamOverview> getAgentTransInfo(Long agentId) {
		return transMapper.getAgentTransInfo(agentId) ;
	}


	@Override
	public TeamOverview getTeamIncomeInfo(Long userId){
		return transMapper.getTeamIncomeInfo(userId);

	}

	@Override
	public List<TeamOverview> getTeamTransInfoByUser(Long userId) {
		return transMapper.getTeamTransInfoByUser(userId);
	}
}
