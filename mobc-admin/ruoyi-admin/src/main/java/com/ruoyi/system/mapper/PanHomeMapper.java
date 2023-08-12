package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PanHome;

/**
 * 主页Mapper接口
 * 
 * @author ruoyi
 * @date 2023-04-21
 */
public interface PanHomeMapper 
{
    /**
     * 查询主页
     * 
     * @param homeId 主页主键
     * @return 主页
     */
    public PanHome selectPanHomeByHomeId(Long homeId);

    /**
     * 查询主页列表
     * 
     * @param panHome 主页
     * @return 主页集合
     */
    public List<PanHome> selectPanHomeList(PanHome panHome);

    /**
     * 新增主页
     * 
     * @param panHome 主页
     * @return 结果
     */
    public int insertPanHome(PanHome panHome);

    /**
     * 修改主页
     * 
     * @param panHome 主页
     * @return 结果
     */
    public int updatePanHome(PanHome panHome);

    /**
     * 删除主页
     * 
     * @param homeId 主页主键
     * @return 结果
     */
    public int deletePanHomeByHomeId(Long homeId);

    /**
     * 批量删除主页
     * 
     * @param homeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanHomeByHomeIds(Long[] homeIds);
}
