package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.PanUserBalance;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户余额Service接口
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public interface IPanUserBalanceService
{
    /**
     * 查询用户余额
     *
     * @param userBalanceId 用户余额主键
     * @return 用户余额
     */
    public PanUserBalance selectPanUserBalanceByUserBalanceId(Long userBalanceId);

    public PanUserBalance getPanUserBalanceByUserId(Long userId);

    public Long getUserBalanceCountAmt(PanUserBalance userBalance);

    /**
     * 用于任务调度
     * @param userId
     * @return
     */
    public PanUserBalance getUserBalanceByUserId(Long userId);

    /**
     * 查询用户余额列表
     *
     * @param panUserBalance 用户余额
     * @return 用户余额集合
     */
    public List<PanUserBalance> selectPanUserBalanceList(PanUserBalance panUserBalance);

    /**
     * 新增用户余额
     *
     * @param panUserBalance 用户余额
     * @return 结果
     */
    public int insertPanUserBalance(PanUserBalance panUserBalance);

    /**
     * 修改用户余额
     *
     * @param panUserBalance 用户余额
     * @return 结果
     */
    public int updatePanUserBalance(PanUserBalance panUserBalance);

    /**
     * 批量删除用户余额
     *
     * @param userBalanceIds 需要删除的用户余额主键集合
     * @return 结果
     */
    public int deletePanUserBalanceByUserBalanceIds(Long[] userBalanceIds);

    /**
     * 删除用户余额信息
     *
     * @param userBalanceId 用户余额主键
     * @return 结果
     */
    public int deletePanUserBalanceByUserBalanceId(Long userBalanceId);

	public List<PanUserBalance> selectPanUserBalanceListDetail(PanUserBalance panUserBalance);

    public Long selectPanUserBalanceListDetailCount(PanUserBalance panUserBalance);

    public  PanUserBalance getUserBalanceCount(PanUserBalance userBalance);
}
