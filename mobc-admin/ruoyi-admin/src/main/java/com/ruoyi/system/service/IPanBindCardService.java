package com.ruoyi.system.service;

import com.ruoyi.system.domain.PanBindCard;

import java.util.List;

/**
 * 用户银行卡Service接口
 *
 * @author ruoyi
 * @date 2023-04-09
 */
public interface IPanBindCardService
{
    /**
     * 查询用户银行卡
     *
     * @param cardId 用户银行卡主键
     * @return 用户银行卡
     */
    public PanBindCard selectPanBindCardByCardId(Long cardId);

    /**
     * 查询用户银行卡列表
     *
     * @param panBindCard 用户银行卡
     * @return 用户银行卡集合
     */
    public List<PanBindCard> selectPanBindCardList(PanBindCard panBindCard);

    /**
     * 新增用户银行卡
     *
     * @param panBindCard 用户银行卡
     * @return 结果
     */
    public int insertPanBindCard(PanBindCard panBindCard);

    /**
     * 修改用户银行卡
     *
     * @param panBindCard 用户银行卡
     * @return 结果
     */
    public int updatePanBindCard(PanBindCard panBindCard);

    /**
     * 批量删除用户银行卡
     *
     * @param cardIds 需要删除的用户银行卡主键集合
     * @return 结果
     */
    public int deletePanBindCardByCardIds(Long[] cardIds);

    /**
     * 删除用户银行卡信息
     *
     * @param cardId 用户银行卡主键
     * @return 结果
     */
    public int deletePanBindCardByCardId(Long cardId);

    public List<PanBindCard> selectCardInfoByCardNo(String cardNo);

    public int  insertPanBindCardRecord(PanBindCard panBindCardRecord);

    public List<PanBindCard> selectPanBindCardRecordList(PanBindCard panBindCard);
}
