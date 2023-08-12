package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PanBindCard;

import java.util.List;

/**
 * 用户银行卡Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-09
 */
public interface PanBindCardMapper
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



    public List<PanBindCard> selectCardInfoByCardNo(String cardNo);

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
     * 删除用户银行卡
     *
     * @param cardId 用户银行卡主键
     * @return 结果
     */
    public int deletePanBindCardByCardId(Long cardId);

    /**
     * 批量删除用户银行卡
     *
     * @param cardIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanBindCardByCardIds(Long[] cardIds);

    public int insertPanBindCardRecord(PanBindCard panBindCardRecord);

    public List<PanBindCard> selectPanBindCardRecordList(PanBindCard panBindCard);
}
