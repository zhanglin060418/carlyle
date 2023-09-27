package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.PanTransactionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PanMessageMapper;
import com.ruoyi.system.domain.PanMessage;
import com.ruoyi.system.service.IPanMessageService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 社区信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-23
 */
@Service
public class PanMessageServiceImpl implements IPanMessageService
{
    @Autowired
    private PanMessageMapper panMessageMapper;

    /**
     * 查询社区信息
     *
     * @param messageId 社区信息主键
     * @return 社区信息
     */
    @Override
    public PanMessage selectPanMessageByMessageId(Long messageId)
    {
        return panMessageMapper.selectPanMessageByMessageId(messageId);
    }

    /**
     * 查询社区信息列表
     *
     * @param panMessage 社区信息
     * @return 社区信息
     */
    @Override
    public List<PanMessage> selectPanMessageList(PanMessage panMessage)
    {
        return panMessageMapper.selectPanMessageList(panMessage);
    }

    /**
     * 新增社区信息
     *
     * @param panMessage 社区信息
     * @return 结果
     */
    @Override
    public int insertPanMessage(PanMessage panMessage)
    {
        panMessage.setCreateTime(DateUtils.getNowDate());
        return panMessageMapper.insertPanMessage(panMessage);
    }

    /**
     * 修改社区信息
     *
     * @param panMessage 社区信息
     * @return 结果
     */
    @Override
    public int updatePanMessage(PanMessage panMessage)
    {
        panMessage.setUpdateTime(DateUtils.getNowDate());
        return panMessageMapper.updatePanMessage(panMessage);
    }

    /**
     * 批量删除社区信息
     *
     * @param messageIds 需要删除的社区信息主键
     * @return 结果
     */
    @Override
    public int deletePanMessageByMessageIds(Long[] messageIds)
    {
        return panMessageMapper.deletePanMessageByMessageIds(messageIds);
    }

    /**
     * 删除社区信息信息
     *
     * @param messageId 社区信息主键
     * @return 结果
     */
    @Override
    public int deletePanMessageByMessageId(Long messageId)
    {
        return panMessageMapper.deletePanMessageByMessageId(messageId);
    }

    @Override
    public List<PanTransactionHistory> getMessageList(PanMessage panMessage)
    {
        return panMessageMapper.getMessageList(panMessage);
    }

    @Override
    @Transactional
    public int createLikes(PanMessage panMessage) {
        int i = 0;
        if (panMessage.isLikesbefore()) {
            i = panMessageMapper.deleteMessageLikes(panMessage);
            if (i > 0) {
                panMessageMapper.updatePanMessageByLikesNumReduce(panMessage);
            }

        } else {
            i = panMessageMapper.insertMessageLikes(panMessage);
            if (i > 0) {
                panMessageMapper.updatePanMessageByLikesNumAdd(panMessage);
            }

        }
        return i;
    }
}
