package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.PanUserBalance;

import java.util.List;

/**
 * 用户余额Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public interface PanUserBalanceMapper
{
    /**
     * 查询用户余额
     *
     * @param userBalanceId 用户余额主键
     * @return 用户余额
     */
    public PanUserBalance selectPanUserBalanceByUserBalanceId(Long userBalanceId);

    public PanUserBalance getPanUserBalanceByUserId(Long userId);

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
     * 删除用户余额
     *
     * @param userBalanceId 用户余额主键
     * @return 结果
     */
    public int deletePanUserBalanceByUserBalanceId(Long userBalanceId);

    /**
     * 批量删除用户余额
     *
     * @param userBalanceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanUserBalanceByUserBalanceIds(Long[] userBalanceIds);

	public List<PanUserBalance> selectPanUserBalanceListDetail(PanUserBalance panUserBalance);

    public Long getUserBalanceCountAmt(PanUserBalance user);

    public Long selectPanUserBalanceListDetailCount(PanUserBalance panUserBalance);

    public PanUserBalance getUserBalanceCount(PanUserBalance userBalance);
}
