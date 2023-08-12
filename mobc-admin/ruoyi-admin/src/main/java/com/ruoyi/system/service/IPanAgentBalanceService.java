package com.ruoyi.system.service;

import com.ruoyi.system.domain.PanAgentBalance;

import java.util.List;

/**
 * 用户余额Service接口
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public interface IPanAgentBalanceService {

    public List<PanAgentBalance> selectPanAgentBalanceList(PanAgentBalance agentBalance);

    public int insertPanAgentBalance(PanAgentBalance balance);

    public int updatePanAgentBalance(PanAgentBalance balance);

    public PanAgentBalance selectPanAgentBalanceById(Long id);

    public PanAgentBalance selectPanAgentBalanceByAgentId(Long agentId);
}
