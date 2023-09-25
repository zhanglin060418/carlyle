package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PanMessage;
import com.ruoyi.system.domain.PanTransactionHistory;

/**
 * 社区信息Service接口
 *
 * @author ruoyi
 * @date 2023-09-23
 */
public interface IPanMessageService
{
    /**
     * 查询社区信息
     *
     * @param messageId 社区信息主键
     * @return 社区信息
     */
    public PanMessage selectPanMessageByMessageId(Long messageId);

    /**
     * 查询社区信息列表
     *
     * @param panMessage 社区信息
     * @return 社区信息集合
     */
    public List<PanMessage> selectPanMessageList(PanMessage panMessage);

    /**
     * 新增社区信息
     *
     * @param panMessage 社区信息
     * @return 结果
     */
    public int insertPanMessage(PanMessage panMessage);

    /**
     * 修改社区信息
     *
     * @param panMessage 社区信息
     * @return 结果
     */
    public int updatePanMessage(PanMessage panMessage);

    /**
     * 批量删除社区信息
     *
     * @param messageIds 需要删除的社区信息主键集合
     * @return 结果
     */
    public int deletePanMessageByMessageIds(Long[] messageIds);

    /**
     * 删除社区信息信息
     *
     * @param messageId 社区信息主键
     * @return 结果
     */
    public int deletePanMessageByMessageId(Long messageId);

    public List<PanTransactionHistory> getMessageList(PanMessage panMessage);

    public int createLikes(PanMessage panMessage);
}
