package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ScrollTransInfoMapper;
import com.ruoyi.system.domain.ScrollTransInfo;
import com.ruoyi.system.service.IScrollTransInfoService;

/**
 * 滚屏交易信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-05
 */
@Service
public class ScrollTransInfoServiceImpl implements IScrollTransInfoService 
{
    @Autowired
    private ScrollTransInfoMapper scrollTransInfoMapper;

    /**
     * 查询滚屏交易信息
     * 
     * @param transId 滚屏交易信息主键
     * @return 滚屏交易信息
     */
    @Override
    public ScrollTransInfo selectScrollTransInfoByTransId(Long transId)
    {
        return scrollTransInfoMapper.selectScrollTransInfoByTransId(transId);
    }

    /**
     * 查询滚屏交易信息列表
     * 
     * @param scrollTransInfo 滚屏交易信息
     * @return 滚屏交易信息
     */
    @Override
    public List<ScrollTransInfo> selectScrollTransInfoList(ScrollTransInfo scrollTransInfo)
    {
        return scrollTransInfoMapper.selectScrollTransInfoList(scrollTransInfo);
    }

    /**
     * 新增滚屏交易信息
     * 
     * @param scrollTransInfo 滚屏交易信息
     * @return 结果
     */
    @Override
    public int insertScrollTransInfo(ScrollTransInfo scrollTransInfo)
    {
        scrollTransInfo.setCreateTime(DateUtils.getNowDate());
        return scrollTransInfoMapper.insertScrollTransInfo(scrollTransInfo);
    }

    /**
     * 修改滚屏交易信息
     * 
     * @param scrollTransInfo 滚屏交易信息
     * @return 结果
     */
    @Override
    public int updateScrollTransInfo(ScrollTransInfo scrollTransInfo)
    {
        scrollTransInfo.setUpdateTime(DateUtils.getNowDate());
        return scrollTransInfoMapper.updateScrollTransInfo(scrollTransInfo);
    }

    /**
     * 批量删除滚屏交易信息
     * 
     * @param transIds 需要删除的滚屏交易信息主键
     * @return 结果
     */
    @Override
    public int deleteScrollTransInfoByTransIds(Long[] transIds)
    {
        return scrollTransInfoMapper.deleteScrollTransInfoByTransIds(transIds);
    }

    /**
     * 删除滚屏交易信息信息
     * 
     * @param transId 滚屏交易信息主键
     * @return 结果
     */
    @Override
    public int deleteScrollTransInfoByTransId(Long transId)
    {
        return scrollTransInfoMapper.deleteScrollTransInfoByTransId(transId);
    }
}
