package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.mapper.PanUserBalanceMapper;
import com.ruoyi.system.service.IPanUserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户余额Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-28
 */
@Service
public class PanUserBalanceServiceImpl implements IPanUserBalanceService
{
    @Autowired
    private PanUserBalanceMapper panUserBalanceMapper;

    /**
     * 查询用户余额
     *
     * @param userBalanceId 用户余额主键
     * @return 用户余额
     */
    @Override
    public PanUserBalance selectPanUserBalanceByUserBalanceId(Long userBalanceId)
    {
        return panUserBalanceMapper.selectPanUserBalanceByUserBalanceId(userBalanceId);
    }

    @Override
    public PanUserBalance getPanUserBalanceByUserId(Long userId)
    {
        return panUserBalanceMapper.getPanUserBalanceByUserId(userId);
    }

    @Override
    public Long getUserBalanceCountAmt(PanUserBalance user) {
        return panUserBalanceMapper.getUserBalanceCountAmt(user);
    }

    @Override
    public PanUserBalance getUserBalanceByUserId(Long userId) {
        return panUserBalanceMapper.getUserBalanceByUserId(userId);
    }

    /**
     * 查询用户余额列表
     *
     * @param panUserBalance 用户余额
     * @return 用户余额
     */
    @Override
    public List<PanUserBalance> selectPanUserBalanceList(PanUserBalance panUserBalance)
    {
        return panUserBalanceMapper.selectPanUserBalanceList(panUserBalance);
    }

    /**
     * 新增用户余额
     *
     * @param panUserBalance 用户余额
     * @return 结果
     */
    @Override
    public int insertPanUserBalance(PanUserBalance panUserBalance)
    {
        return panUserBalanceMapper.insertPanUserBalance(panUserBalance);
    }

    /**
     * 修改用户余额
     *
     * @param panUserBalance 用户余额
     * @return 结果
     */
    @Override
    public int updatePanUserBalance(PanUserBalance panUserBalance)
    {
        return panUserBalanceMapper.updatePanUserBalance(panUserBalance);
    }

    /**
     * 批量删除用户余额
     *
     * @param userBalanceIds 需要删除的用户余额主键
     * @return 结果
     */
    @Override
    public int deletePanUserBalanceByUserBalanceIds(Long[] userBalanceIds)
    {
        return panUserBalanceMapper.deletePanUserBalanceByUserBalanceIds(userBalanceIds);
    }

    /**
     * 删除用户余额信息
     *
     * @param userBalanceId 用户余额主键
     * @return 结果
     */
    @Override
    public int deletePanUserBalanceByUserBalanceId(Long userBalanceId)
    {
        return panUserBalanceMapper.deletePanUserBalanceByUserBalanceId(userBalanceId);
    }

	@Override
	public List<PanUserBalance> selectPanUserBalanceListDetail(PanUserBalance panUserBalance) {
		return panUserBalanceMapper.selectPanUserBalanceListDetail(panUserBalance);
	}

    @Override
    public Long selectPanUserBalanceListDetailCount(PanUserBalance panUserBalance) {
        return panUserBalanceMapper.selectPanUserBalanceListDetailCount(panUserBalance);
    }

    @Override
    public PanUserBalance getUserBalanceCount(PanUserBalance userBalance) {
        return panUserBalanceMapper.getUserBalanceCount(userBalance);
    }


}
