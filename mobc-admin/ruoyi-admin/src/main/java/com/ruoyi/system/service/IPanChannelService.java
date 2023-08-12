package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PanChannel;

/**
 * 支付通道Service接口
 *
 * @author ruoyi
 * @date 2023-04-24
 */
public interface IPanChannelService
{
    /**
     * 查询支付通道
     *
     * @param channelId 支付通道主键
     * @return 支付通道
     */
    public PanChannel selectPanChannelByChannelId(Long channelId);

    /**
     * 查询支付通道列表
     *
     * @param panChannel 支付通道
     * @return 支付通道集合
     */
    public List<PanChannel> selectPanChannelList(PanChannel panChannel);

    /**
     * 新增支付通道
     *
     * @param panChannel 支付通道
     * @return 结果
     */
    public int insertPanChannel(PanChannel panChannel);

    /**
     * 修改支付通道
     *
     * @param panChannel 支付通道
     * @return 结果
     */
    public int updatePanChannel(PanChannel panChannel);

    /**
     * 批量删除支付通道
     *
     * @param channelIds 需要删除的支付通道主键集合
     * @return 结果
     */
    public int deletePanChannelByChannelIds(Long[] channelIds);

    /**
     * 删除支付通道信息
     *
     * @param channelId 支付通道主键
     * @return 结果
     */
    public int deletePanChannelByChannelId(Long channelId);

	public PanChannel selectPanChannelByStatus();

    public  PanChannel selectPanProxyChannelByStatus();

    /**
     * 设置指定一个可以代付
     * @param channelId
     * @return
     */
    public int setOnlyOneProxy(Long channelId);

    /**
     * 重置所有都不能代付
     * @return
     */
    public int resetAllProxy();
}
