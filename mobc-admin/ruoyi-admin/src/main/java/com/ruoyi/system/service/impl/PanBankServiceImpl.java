package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PanBankMapper;
import com.ruoyi.system.domain.PanBank;
import com.ruoyi.system.service.IPanBankService;

/**
 * 银行名单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-04-09
 */
@Service
public class PanBankServiceImpl implements IPanBankService 
{
    @Autowired
    private PanBankMapper panBankMapper;

    /**
     * 查询银行名单
     * 
     * @param bankId 银行名单主键
     * @return 银行名单
     */
    @Override
    public PanBank selectPanBankByBankId(Long bankId)
    {
        return panBankMapper.selectPanBankByBankId(bankId);
    }

    /**
     * 查询银行名单列表
     * 
     * @param panBank 银行名单
     * @return 银行名单
     */
    @Override
    public List<PanBank> selectPanBankList(PanBank panBank)
    {
        return panBankMapper.selectPanBankList(panBank);
    }

    /**
     * 新增银行名单
     * 
     * @param panBank 银行名单
     * @return 结果
     */
    @Override
    public int insertPanBank(PanBank panBank)
    {
        return panBankMapper.insertPanBank(panBank);
    }

    /**
     * 修改银行名单
     * 
     * @param panBank 银行名单
     * @return 结果
     */
    @Override
    public int updatePanBank(PanBank panBank)
    {
        return panBankMapper.updatePanBank(panBank);
    }

    /**
     * 批量删除银行名单
     * 
     * @param bankIds 需要删除的银行名单主键
     * @return 结果
     */
    @Override
    public int deletePanBankByBankIds(Long[] bankIds)
    {
        return panBankMapper.deletePanBankByBankIds(bankIds);
    }

    /**
     * 删除银行名单信息
     * 
     * @param bankId 银行名单主键
     * @return 结果
     */
    @Override
    public int deletePanBankByBankId(Long bankId)
    {
        return panBankMapper.deletePanBankByBankId(bankId);
    }
}
