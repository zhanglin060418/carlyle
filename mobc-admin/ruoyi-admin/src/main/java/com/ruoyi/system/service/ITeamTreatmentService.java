package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TeamTreatment;

/**
 * 主页Service接口
 * 
 * @author ruoyi
 * @date 2023-05-03
 */
public interface ITeamTreatmentService 
{
    /**
     * 查询主页
     * 
     * @param id 主页主键
     * @return 主页
     */
    public TeamTreatment selectTeamTreatmentById(Long id);

    /**
     * 查询主页列表
     * 
     * @param teamTreatment 主页
     * @return 主页集合
     */
    public List<TeamTreatment> selectTeamTreatmentList(TeamTreatment teamTreatment);

    /**
     * 新增主页
     * 
     * @param teamTreatment 主页
     * @return 结果
     */
    public int insertTeamTreatment(TeamTreatment teamTreatment);

    /**
     * 修改主页
     * 
     * @param teamTreatment 主页
     * @return 结果
     */
    public int updateTeamTreatment(TeamTreatment teamTreatment);

    /**
     * 批量删除主页
     * 
     * @param ids 需要删除的主页主键集合
     * @return 结果
     */
    public int deleteTeamTreatmentByIds(Long[] ids);

    /**
     * 删除主页信息
     * 
     * @param id 主页主键
     * @return 结果
     */
    public int deleteTeamTreatmentById(Long id);
}
