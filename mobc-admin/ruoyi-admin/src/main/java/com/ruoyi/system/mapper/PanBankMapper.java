package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PanBank;

/**
 * 银行名单Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-09
 */
public interface PanBankMapper
{
    /**
     * 查询银行名单
     *
     * @param bankId 银行名单主键
     * @return 银行名单
     */
    public PanBank selectPanBankByBankId(Long bankId);

    /**
     * 查询银行名单列表
     *
     * @param panBank 银行名单
     * @return 银行名单集合
     */
    public List<PanBank> selectPanBankList(PanBank panBank);

    /**
     * 新增银行名单
     *
     * @param panBank 银行名单
     * @return 结果
     */
    public int insertPanBank(PanBank panBank);

    /**
     * 修改银行名单
     *
     * @param panBank 银行名单
     * @return 结果
     */
    public int updatePanBank(PanBank panBank);

    /**
     * 删除银行名单
     *
     * @param bankId 银行名单主键
     * @return 结果
     */
    public int deletePanBankByBankId(Long bankId);

    /**
     * 批量删除银行名单
     *
     * @param bankIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanBankByBankIds(Long[] bankIds);

    public List<PanBank> getBankList();
}
