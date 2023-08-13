package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.PanTransactionHistory;
import com.ruoyi.system.domain.TeamIncomeOverview;
import com.ruoyi.system.domain.TeamOverview;
import com.ruoyi.system.domain.TeamRechargeView;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-10
 */
public interface PanTransactionHistoryMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param transactionHistoryId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public PanTransactionHistory selectPanTransactionHistoryByTransactionHistoryId(Long transactionHistoryId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param panTransactionHistory 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<PanTransactionHistory> selectPanTransactionHistoryList(PanTransactionHistory panTransactionHistory);

    /**
     * 新增【请填写功能名称】
     *
     * @param panTransactionHistory 【请填写功能名称】
     * @return 结果
     */
    public int insertPanTransactionHistory(PanTransactionHistory panTransactionHistory);

    /**
     * 修改【请填写功能名称】
     *
     * @param panTransactionHistory 【请填写功能名称】
     * @return 结果
     */
    public int updatePanTransactionHistory(PanTransactionHistory panTransactionHistory);

    /**
     * 删除【请填写功能名称】
     *
     * @param transactionHistoryId 【请填写功能名称】主键
     * @return 结果
     */
    public int deletePanTransactionHistoryByTransactionHistoryId(Long transactionHistoryId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param transactionHistoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanTransactionHistoryByTransactionHistoryIds(Long[] transactionHistoryIds);

    public Long getDailyIncome(Long UserId);

    public Long getTotalIncome(Long userId);

	public Long getChildIncome(Long userId);

	public Long getGrandIncome(Long userId);

	public Long getGreatGrandIncome(Long userId);

	public Long getChildDailyIncome(Long userId);

	public List<PanTransactionHistory> getTodayTransaction(Long userId);

	public List<PanTransactionHistory> getYesterdayTransaction(Long userId);

	public List<PanTransactionHistory> getLastWeekTransaction(Long userId);

    public Long getTodayTreasureRewardById(Long userId);

    public Long getTotalTreasureRewardById(Long userId);

    public Long getChildTotalIncome(Long userId);
    public List<PanTransactionHistory> getGreatGrandIncomListByUser(Long userId);

    public List<PanTransactionHistory> getGrandIncomListByUser(Long userId);

    public List<PanTransactionHistory> getChildIncomeListByUser(Long userId);

    public List<TeamIncomeOverview> getGrandIncomOverview(Long userId);

    public List<TeamIncomeOverview> getGreatGrandIncomOverview(Long userId);

    public List<TeamIncomeOverview> getChildIncomeOverview(Long userId);

    public Long selectPanTransactionHistoryListCount(PanTransactionHistory panTransactionHistory);




    public List<TeamOverview> selectUserTransCount(Long userId);

    public List<TeamOverview> selectTeamTransCount(Long userId);

    public Long totalTeamBuyCount(Long userId);

    public TeamOverview selectManagerTeam(Long userId);

    public TeamOverview selectUserTeam(Long userId);

    public TeamOverview selectPanUserTeam(Long userId);

    public List<TeamOverview> selectUserTeamList(SysUser user);
    public List<TeamOverview> selectUserTeamByManagerList(SysUser user);
    public Long totalPeopleCount(Long userId);
    public List<TeamOverview> selectTeamTransCountBySys(PanTransactionHistory currUser);
    public Long totalPeopleCountBySys(Long userId);

    public List<PanTransactionHistory> selectPanTransByInterestList(PanTransactionHistory panTransactionHistory);

    public Long selectPanTransByInterestListCount(PanTransactionHistory panTransactionHistory);

    public Long selectPanTransByCommissionListCount(PanTransactionHistory panTransactionHistory);

    public List<PanTransactionHistory> selectPanTransByCommissionList(PanTransactionHistory panTransactionHistory);

    public  List<TeamOverview> getTotalTransInfo(SysUser sysUser);

    public List<TeamOverview> getTotalTransInfoByDay(SysUser sysUser);

    public List<TeamOverview> getToatlTransUsers(SysUser sysUser);

    public List<TeamOverview> getTodayTransUsers(SysUser sysUser);

    public List<TeamOverview> totalTransCountlist(SysUser sysUser);

    public List<TeamOverview> todayTransCountlist(SysUser sysUser);

    public Long todayActiveUsers(SysUser sysUser);

    public Long todayFristRechargeUsers(SysUser sysUser);

    public List<TeamOverview> getUserTransInfo(Long userId);


    public List<TeamOverview> getUserTransInfoByIds(String userIds);

    public List<PanTransactionHistory> getPurchaseInterestList(PanTransactionHistory orderNo);

    public List<TeamRechargeView> selectTeamRechargeListByClerk(TeamRechargeView teamRecharge);

    public List<TeamRechargeView> selectTeamRechargeListByUser(TeamRechargeView teamRecharge);

    public TeamRechargeView selectTeamTransByUserId(Long userId);

    public List<PanTransactionHistory> getClerkTeamTransInfo(PanTransactionHistory transInfo);


    public List<TeamOverview> getAgentTransInfo(Long agentId);

    public TeamOverview getTeamIncomeInfo(Long userId);
}
