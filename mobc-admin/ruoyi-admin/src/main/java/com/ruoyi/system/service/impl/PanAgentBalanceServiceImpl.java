package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PanAgentBalance;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.mapper.PanAgentBalanceMapper;
import com.ruoyi.system.mapper.PanUserBalanceMapper;
import com.ruoyi.system.service.IPanAgentBalanceService;
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
public class PanAgentBalanceServiceImpl implements IPanAgentBalanceService {

    @Autowired
    private PanAgentBalanceMapper panAgentBalanceMapper;

    @Override
    public List<PanAgentBalance> selectPanAgentBalanceList(PanAgentBalance agentBalance) {
        return panAgentBalanceMapper.selectPanAgentBalanceList(agentBalance);
    }

    @Override
    public int insertPanAgentBalance(PanAgentBalance agentBalance) {
        return panAgentBalanceMapper.insertPanAgentBalance(agentBalance);
    }

    @Override
    public int updatePanAgentBalance(PanAgentBalance agentBalance) {
        return panAgentBalanceMapper.updatePanAgentBalance(agentBalance);
    }

    @Override
    public PanAgentBalance selectPanAgentBalanceById(Long id){
        return panAgentBalanceMapper.selectPanAgentBalanceById(id);
    }

    @Override
    public PanAgentBalance selectPanAgentBalanceByAgentId(Long id){
        return panAgentBalanceMapper.selectPanAgentBalanceByAgentId(id);
    }
}
