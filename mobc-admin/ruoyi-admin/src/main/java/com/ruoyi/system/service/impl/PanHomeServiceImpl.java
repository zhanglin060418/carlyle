package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PanHomeMapper;
import com.ruoyi.system.domain.PanHome;
import com.ruoyi.system.service.IPanHomeService;

/**
 * 主页Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-04-21
 */
@Service
public class PanHomeServiceImpl implements IPanHomeService 
{
    @Autowired
    private PanHomeMapper panHomeMapper;

    /**
     * 查询主页
     * 
     * @param homeId 主页主键
     * @return 主页
     */
    @Override
    public PanHome selectPanHomeByHomeId(Long homeId)
    {
        return panHomeMapper.selectPanHomeByHomeId(homeId);
    }

    /**
     * 查询主页列表
     * 
     * @param panHome 主页
     * @return 主页
     */
    @Override
    public List<PanHome> selectPanHomeList(PanHome panHome)
    {
        return panHomeMapper.selectPanHomeList(panHome);
    }

    /**
     * 新增主页
     * 
     * @param panHome 主页
     * @return 结果
     */
    @Override
    public int insertPanHome(PanHome panHome)
    {
        panHome.setCreateTime(DateUtils.getNowDate());
        return panHomeMapper.insertPanHome(panHome);
    }

    /**
     * 修改主页
     * 
     * @param panHome 主页
     * @return 结果
     */
    @Override
    public int updatePanHome(PanHome panHome)
    {
        panHome.setUpdateTime(DateUtils.getNowDate());
        return panHomeMapper.updatePanHome(panHome);
    }

    /**
     * 批量删除主页
     * 
     * @param homeIds 需要删除的主页主键
     * @return 结果
     */
    @Override
    public int deletePanHomeByHomeIds(Long[] homeIds)
    {
        return panHomeMapper.deletePanHomeByHomeIds(homeIds);
    }

    /**
     * 删除主页信息
     * 
     * @param homeId 主页主键
     * @return 结果
     */
    @Override
    public int deletePanHomeByHomeId(Long homeId)
    {
        return panHomeMapper.deletePanHomeByHomeId(homeId);
    }
}
