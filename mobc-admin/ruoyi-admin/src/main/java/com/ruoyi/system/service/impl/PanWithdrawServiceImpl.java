package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.PanAgentBalance;
import com.ruoyi.system.domain.PanWithdraw;
import com.ruoyi.system.domain.TeamOverview;
import com.ruoyi.system.mapper.PanAgentBalanceMapper;
import com.ruoyi.system.mapper.PanTransactionHistoryMapper;
import com.ruoyi.system.mapper.PanWithdrawMapper;
import com.ruoyi.system.service.IPanWithdrawService;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提款Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-10
 */
@Service
public class PanWithdrawServiceImpl implements IPanWithdrawService
{
    @Autowired
    private PanWithdrawMapper panWithdrawMapper;

    @Autowired
    private PanAgentBalanceMapper agentBalanceMapper;

    @Autowired
    private PanTransactionHistoryMapper transHistoryMapper;

    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 查询提款
     *
     * @param withdrawId 提款主键
     * @return 提款
     */
    @Override
    public PanWithdraw selectPanWithdrawByWithdrawId(Long withdrawId)
    {
        return panWithdrawMapper.selectPanWithdrawByWithdrawId(withdrawId);
    }

    /**
     * 查询提款列表
     *
     * @param panWithdraw 提款
     * @return 提款
     */
    @Override
    public List<PanWithdraw> selectPanWithdrawList(PanWithdraw panWithdraw)
    {
        return panWithdrawMapper.selectPanWithdrawList(panWithdraw);
    }

    /**
     * 新增提款
     *
     * @param panWithdraw 提款
     * @return 结果
     */
    @Override
    public int insertPanWithdraw(PanWithdraw panWithdraw)
    {
        panWithdraw.setCreateTime(DateUtils.getNowDate());
        return panWithdrawMapper.insertPanWithdraw(panWithdraw);
    }

    /**
     * 修改提款
     *
     * @param panWithdraw 提款
     * @return 结果
     */
    @Override
    public int updatePanWithdraw(PanWithdraw panWithdraw)
    {
        return panWithdrawMapper.updatePanWithdraw(panWithdraw);
    }

    /**
     * 批量删除提款
     *
     * @param withdrawIds 需要删除的提款主键
     * @return 结果
     */
    @Override
    public int deletePanWithdrawByWithdrawIds(Long[] withdrawIds)
    {
        return panWithdrawMapper.deletePanWithdrawByWithdrawIds(withdrawIds);
    }

    /**
     * 删除提款信息
     *
     * @param withdrawId 提款主键
     * @return 结果
     */
    @Override
    public int deletePanWithdrawByWithdrawId(Long withdrawId)
    {
        return panWithdrawMapper.deletePanWithdrawByWithdrawId(withdrawId);
    }

    @Override
    public Long getTotalWithdraw()
    {
    	return panWithdrawMapper.getTotalWithdraw();
    }

    @Override
    public Long getTodayWithdraw()
    {
    	return panWithdrawMapper.getTodayWithdraw();
    }

	@Override
	public PanWithdraw selectPanWithdrawByOrderNo(String orderNo) {
		return panWithdrawMapper.selectPanWithdrawByOrderNo(orderNo);
	}

	@Override
	public Long checkWithdrawStatus(Long userId) {
		return panWithdrawMapper.checkWithdrawStatus(userId);
	}

    @Override
    public Long getTotalWithdrawCountByUser(Long userId) {
        return panWithdrawMapper.getTotalWithdrawCountByUser(userId);
    }

    @Override
    public Long selectPanWithdrawListCount(PanWithdraw panWithdraw) {
        return panWithdrawMapper.selectPanWithdrawListCount(panWithdraw);
    }

    @Override
    public PanWithdraw getTransitAmtByUser(Long agentId) {
        return panWithdrawMapper.getTransitAmtByUser(agentId);
    }


    @Override
    public Long getUserWithdrawInfoByIds(String withdrawIds) {
        return panWithdrawMapper.getUserWithdrawInfoByIds(withdrawIds);
    }

    @Override
    public PanWithdraw getTodayWithdrawCountByUserId(Long userId) {
        return panWithdrawMapper.getTodayWithdrawCountByUserId(userId);
    }

    @Override
    public BigDecimal getAgentBalance(Long agentId) {

        BigDecimal agentBalance = new BigDecimal(0);
        PanAgentBalance panAgentBalance = agentBalanceMapper.selectPanAgentBalanceByAgentId(agentId);
        // 预充值
        BigDecimal prechargeAmt = panAgentBalance.getPrechargeAmt();

        List<TeamOverview> totalTransCountlist = transHistoryMapper.getAgentTransInfo(agentId);
        Long rechargeAmt = 0L;
        Long withdrawAmt = 0L;
        Long withdrawCount = 0L;
        for (TeamOverview teamUser : totalTransCountlist) {
            if (teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name())) {
                rechargeAmt = teamUser.getTransAmt();
            } else if (teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name())) {
                withdrawCount = teamUser.getTotalPeopleCount();
                withdrawAmt = teamUser.getTransAmt();
            }
        }
        // 在途金额
        PanWithdraw transitInfo = panWithdrawMapper.getTransitAmtByUser(agentId);
        BigDecimal transitAmt = transitInfo.getAmount();
        BigDecimal transitCount = new BigDecimal(transitInfo.getWithdrawId());

        String channel_recharge_withdrawRate = sysConfigService.selectConfigByKey("channel_recharge_withdrawRate");
        Double withdrawal_fee = Double.parseDouble(sysConfigService.selectConfigByKey("withdrawal_fee"));

        String[] strArr = channel_recharge_withdrawRate.split(",");
        Double channelRechargeRate = Double.parseDouble(strArr[0]);
        Double channelWithdrawRate = Double.parseDouble(strArr[1]);
        Double channelWithdrawSingefee = Double.parseDouble(strArr[2]);

        // 充值手续费
        BigDecimal channelRechargeFee = new BigDecimal(rechargeAmt).multiply(new BigDecimal(channelRechargeRate)).divide(new BigDecimal(100));

        // 已提现金额
        BigDecimal channelWithdarwAmt = new BigDecimal(withdrawAmt).subtract(new BigDecimal(withdrawAmt).multiply(new BigDecimal(withdrawal_fee)).divide(new BigDecimal(100)));
        // 已提现手续费
        BigDecimal channelWithdrawFee = channelWithdarwAmt.multiply(new BigDecimal(channelWithdrawRate)).divide(new BigDecimal(100));
        // 已提现单笔费用
        BigDecimal channelSingefee = new BigDecimal(withdrawCount).multiply(new BigDecimal(channelWithdrawSingefee)).multiply(new BigDecimal(100));

        // 在途手续费
        BigDecimal channelTransitFee = transitAmt.multiply(new BigDecimal(channelWithdrawRate)).divide(new BigDecimal(100));
        // 在途单笔费用
        BigDecimal channelTransitSingefee = transitCount.multiply(new BigDecimal(channelWithdrawSingefee)).multiply(new BigDecimal(100));

        agentBalance = prechargeAmt.add(new BigDecimal(rechargeAmt)).subtract(channelRechargeFee).
                subtract(channelWithdarwAmt).subtract(channelWithdrawFee).subtract(channelSingefee).
                subtract(transitAmt).subtract(channelTransitFee).subtract(channelTransitSingefee);

        return agentBalance;
    }
}
