package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ScrollTransInfo;

/**
 * 滚屏交易信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-05
 */
public interface ScrollTransInfoMapper 
{
    /**
     * 查询滚屏交易信息
     * 
     * @param transId 滚屏交易信息主键
     * @return 滚屏交易信息
     */
    public ScrollTransInfo selectScrollTransInfoByTransId(Long transId);

    /**
     * 查询滚屏交易信息列表
     * 
     * @param scrollTransInfo 滚屏交易信息
     * @return 滚屏交易信息集合
     */
    public List<ScrollTransInfo> selectScrollTransInfoList(ScrollTransInfo scrollTransInfo);

    /**
     * 新增滚屏交易信息
     * 
     * @param scrollTransInfo 滚屏交易信息
     * @return 结果
     */
    public int insertScrollTransInfo(ScrollTransInfo scrollTransInfo);

    /**
     * 修改滚屏交易信息
     * 
     * @param scrollTransInfo 滚屏交易信息
     * @return 结果
     */
    public int updateScrollTransInfo(ScrollTransInfo scrollTransInfo);

    /**
     * 删除滚屏交易信息
     * 
     * @param transId 滚屏交易信息主键
     * @return 结果
     */
    public int deleteScrollTransInfoByTransId(Long transId);

    /**
     * 批量删除滚屏交易信息
     * 
     * @param transIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScrollTransInfoByTransIds(Long[] transIds);
}
