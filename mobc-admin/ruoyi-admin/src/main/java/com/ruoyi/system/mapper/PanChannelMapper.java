package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PanChannel;

/**
 * 支付通道Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-24
 */
public interface PanChannelMapper
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


    public int resetAllProxy();
    public int setProxy(Long channelId);
    /**
     * 删除支付通道
     *
     * @param channelId 支付通道主键
     * @return 结果
     */
    public int deletePanChannelByChannelId(Long channelId);

    /**
     * 批量删除支付通道
     *
     * @param channelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanChannelByChannelIds(Long[] channelIds);

	public PanChannel selectPanChannelByStatus();


    public PanChannel selectPanProxyChannelByStatus();

    public List<PanChannel> selectPanChannelListByRecharge(PanChannel panChannel);


}
