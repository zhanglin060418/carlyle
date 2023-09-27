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
        // 预充值
        PanAgentBalance panAgentBalance = agentBalanceMapper.selectPanAgentBalanceByAgentId(agentId);
        BigDecimal prechargeAmt = panAgentBalance.getPrechargeAmt();
        // 充值余额，已扣除充值手续费
        List<TeamOverview> totalTransCountlist = transHistoryMapper.getAgentTransInfo(agentId);
        Long rechargeBalance = 0L;
        if(totalTransCountlist.size()>0){
            if(totalTransCountlist.get(0)!=null){
                if(totalTransCountlist.get(0).getTransAmt()!=null) {
                    rechargeBalance = totalTransCountlist.get(0).getTransAmt();
                }
            }
        }

        // 代付金额+手续费+单笔费用
        BigDecimal withdraAmtAndFee = new BigDecimal(0);
        PanWithdraw transitInfo = panWithdrawMapper.getTransitAmtByUser(agentId);
        if(transitInfo!=null){
            if(transitInfo.getAmount()!=null) {
                withdraAmtAndFee =transitInfo.getAmount();
            }
        }
        agentBalance = prechargeAmt.add(new BigDecimal(rechargeBalance)).subtract(withdraAmtAndFee);
        return agentBalance;
    }
}
