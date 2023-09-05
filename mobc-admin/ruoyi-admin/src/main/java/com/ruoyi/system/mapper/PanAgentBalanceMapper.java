package com.ruoyi.system.mapper;

        import com.ruoyi.system.domain.PanAgentBalance;
        import com.ruoyi.system.domain.PanAgentBalanceDetail;

        import java.util.List;

/**
 * 用户余额Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-28
 */
public interface PanAgentBalanceMapper {

    public int updatePanAgentBalance(PanAgentBalance agentBalance);

    public int insertPanAgentBalance(PanAgentBalance agentBalance);

    public List<PanAgentBalance> selectPanAgentBalanceList(PanAgentBalance agentBalance);

    public PanAgentBalance selectPanAgentBalanceById(Long id);

    public PanAgentBalance selectPanAgentBalanceByAgentId(Long agentId);

    public int insertAgentBalanceDetail(PanAgentBalanceDetail trans);
}
