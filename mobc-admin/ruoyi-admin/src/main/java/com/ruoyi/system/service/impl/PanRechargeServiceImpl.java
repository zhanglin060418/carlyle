package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.PanRecharge;
import com.ruoyi.system.mapper.PanRechargeMapper;
import com.ruoyi.system.service.IPanRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 充值Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-02
 */
@Service
public class PanRechargeServiceImpl implements IPanRechargeService
{
    @Autowired
    private PanRechargeMapper panRechargeMapper;

    /**
     * 查询充值
     *
     * @param rechargeId 充值主键
     * @return 充值
     */
    @Override
    public PanRecharge selectPanRechargeByRechargeId(Long rechargeId)
    {
        return panRechargeMapper.selectPanRechargeByRechargeId(rechargeId);
    }

    /**
     * 查询充值列表
     *
     * @param panRecharge 充值
     * @return 充值
     */
    @Override
    public List<PanRecharge> selectPanRechargeList(PanRecharge panRecharge)
    {
        return panRechargeMapper.selectPanRechargeList(panRecharge);
    }


    /**
     * 新增充值
     *
     * @param panRecharge 充值
     * @return 结果
     */
    @Override
    public int insertPanRecharge(PanRecharge panRecharge)
    {
        panRecharge.setCreateTime(DateUtils.getNowDate());
        return panRechargeMapper.insertPanRecharge(panRecharge);
    }

    /**
     * 修改充值
     *
     * @param panRecharge 充值
     * @return 结果
     */
    @Override
    public int updatePanRecharge(PanRecharge panRecharge)
    {
        return panRechargeMapper.updatePanRecharge(panRecharge);
    }

    /**
     * 批量删除充值
     *
     * @param rechargeIds 需要删除的充值主键
     * @return 结果
     */
    @Override
    public int deletePanRechargeByRechargeIds(Long[] rechargeIds)
    {
        return panRechargeMapper.deletePanRechargeByRechargeIds(rechargeIds);
    }

    /**
     * 删除充值信息
     *
     * @param rechargeId 充值主键
     * @return 结果
     */
    @Override
    public int deletePanRechargeByRechargeId(Long rechargeId)
    {
        return panRechargeMapper.deletePanRechargeByRechargeId(rechargeId);
    }

    @Override
    public Long getTotalRecharge()
    {
    	return panRechargeMapper.getTotalRecharge();
    }

    @Override
    public Long getTotalRechargeUsers()
    {
    	return panRechargeMapper.getTotalRechargeUsers();
    }

    @Override
    public Long getTodayRecharge()
    {
    	return panRechargeMapper.getTodayRecharge();
    }

    @Override
    public Long getTodayRechargeUsers()
    {
    	return panRechargeMapper.getTodayRechargeUsers();
    }

	@Override
	public PanRecharge selectPanRechargeByRequestNo(String requestNo) {
		return panRechargeMapper.selectPanRechargeByRequestNo(requestNo);
	}

	public PanRecharge selectPanRechargeByOrderNo(String orderNo) {
		return panRechargeMapper.selectPanRechargeByOrderNo(orderNo);
	}

    @Override
    public Long setTotalRechargeCountByUser(Long userId) {
        return panRechargeMapper.setTotalRechargeCountByUser(userId);
    }

    @Override
    public Long selectPanRechargeListCount(PanRecharge panRecharge) {
        return panRechargeMapper.selectPanRechargeListCount(panRecharge);
    }
}
