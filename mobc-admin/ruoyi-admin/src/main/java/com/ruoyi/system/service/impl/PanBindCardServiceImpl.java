package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PanBindCard;
import com.ruoyi.system.mapper.PanBindCardMapper;
import com.ruoyi.system.service.IPanBindCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户银行卡Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-09
 */
@Service
public class PanBindCardServiceImpl implements IPanBindCardService
{
    @Autowired
    private PanBindCardMapper panBindCardMapper;

    /**
     * 查询用户银行卡
     *
     * @param cardId 用户银行卡主键
     * @return 用户银行卡
     */
    @Override
    public PanBindCard selectPanBindCardByCardId(Long cardId)
    {
        return panBindCardMapper.selectPanBindCardByCardId(cardId);
    }

    /**
     * 查询用户银行卡列表
     *
     * @param panBindCard 用户银行卡
     * @return 用户银行卡
     */
    @Override
    public List<PanBindCard> selectPanBindCardList(PanBindCard panBindCard)
    {
        return panBindCardMapper.selectPanBindCardList(panBindCard);
    }

    /**
     * 新增用户银行卡
     *
     * @param panBindCard 用户银行卡
     * @return 结果
     */
    @Override
    public int insertPanBindCard(PanBindCard panBindCard)
    {
        return panBindCardMapper.insertPanBindCard(panBindCard);
    }

    /**
     * 修改用户银行卡
     *
     * @param panBindCard 用户银行卡
     * @return 结果
     */
    @Override
    public int updatePanBindCard(PanBindCard panBindCard)
    {
        return panBindCardMapper.updatePanBindCard(panBindCard);
    }

    /**
     * 批量删除用户银行卡
     *
     * @param cardIds 需要删除的用户银行卡主键
     * @return 结果
     */
    @Override
    public int deletePanBindCardByCardIds(Long[] cardIds)
    {
        return panBindCardMapper.deletePanBindCardByCardIds(cardIds);
    }

    /**
     * 删除用户银行卡信息
     *
     * @param cardId 用户银行卡主键
     * @return 结果
     */
    @Override
    public int deletePanBindCardByCardId(Long cardId)
    {
        return panBindCardMapper.deletePanBindCardByCardId(cardId);
    }

    @Override
    public List<PanBindCard> selectCardInfoByCardNo(String cardNo) {
        return panBindCardMapper.selectCardInfoByCardNo(cardNo);
    }

    @Override
    public int insertPanBindCardRecord(PanBindCard panBindCardRecord) {
        return panBindCardMapper.insertPanBindCardRecord(panBindCardRecord);
    }
    @Override
    public List<PanBindCard> selectPanBindCardRecordList(PanBindCard panBindCard){
        return panBindCardMapper.selectPanBindCardRecordList(panBindCard);
    }
}
