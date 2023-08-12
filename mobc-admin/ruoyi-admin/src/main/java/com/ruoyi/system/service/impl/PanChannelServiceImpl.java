package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PanChannelMapper;
import com.ruoyi.system.domain.PanChannel;
import com.ruoyi.system.service.IPanChannelService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付通道Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-24
 */
@Service
public class PanChannelServiceImpl implements IPanChannelService
{
    @Autowired
    private PanChannelMapper panChannelMapper;

    /**
     * 查询支付通道
     *
     * @param channelId 支付通道主键
     * @return 支付通道
     */
    @Override
    public PanChannel selectPanChannelByChannelId(Long channelId)
    {
        return panChannelMapper.selectPanChannelByChannelId(channelId);
    }

    @Override
    public PanChannel selectPanChannelByStatus()
    {
        return panChannelMapper.selectPanChannelByStatus();
    }

    @Override
    public PanChannel selectPanProxyChannelByStatus()
    {
        return panChannelMapper.selectPanProxyChannelByStatus();
    }

    @Override
    @Transactional
    public int setOnlyOneProxy(Long channelId) {
        panChannelMapper.resetAllProxy();
        return panChannelMapper.setProxy(channelId);
    }
    @Override
    @Transactional
    public int resetAllProxy(){
        return panChannelMapper.resetAllProxy();
    }

    /**
     * 查询支付通道列表
     *
     * @param panChannel 支付通道
     * @return 支付通道
     */
    @Override
    public List<PanChannel> selectPanChannelList(PanChannel panChannel)
    {
        return panChannelMapper.selectPanChannelList(panChannel);
    }

    /**
     * 新增支付通道
     *
     * @param panChannel 支付通道
     * @return 结果
     */
    @Override
    public int insertPanChannel(PanChannel panChannel)
    {
        panChannel.setCreateTime(DateUtils.getNowDate());
        return panChannelMapper.insertPanChannel(panChannel);
    }






    /**
     * 修改支付通道
     *
     * @param panChannel 支付通道
     * @return 结果
     */
    @Override
    public int updatePanChannel(PanChannel panChannel)
    {
        panChannel.setUpdateTime(DateUtils.getNowDate());
        return panChannelMapper.updatePanChannel(panChannel);
    }

    /**
     * 批量删除支付通道
     *
     * @param channelIds 需要删除的支付通道主键
     * @return 结果
     */
    @Override
    public int deletePanChannelByChannelIds(Long[] channelIds)
    {
        return panChannelMapper.deletePanChannelByChannelIds(channelIds);
    }

    /**
     * 删除支付通道信息
     *
     * @param channelId 支付通道主键
     * @return 结果
     */
    @Override
    public int deletePanChannelByChannelId(Long channelId)
    {
        return panChannelMapper.deletePanChannelByChannelId(channelId);
    }
}
