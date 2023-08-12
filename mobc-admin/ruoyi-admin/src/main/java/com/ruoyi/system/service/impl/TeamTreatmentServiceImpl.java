package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TeamTreatmentMapper;
import com.ruoyi.system.domain.TeamTreatment;
import com.ruoyi.system.service.ITeamTreatmentService;

/**
 * 主页Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-05-03
 */
@Service
public class TeamTreatmentServiceImpl implements ITeamTreatmentService 
{
    @Autowired
    private TeamTreatmentMapper teamTreatmentMapper;

    /**
     * 查询主页
     * 
     * @param id 主页主键
     * @return 主页
     */
    @Override
    public TeamTreatment selectTeamTreatmentById(Long id)
    {
        return teamTreatmentMapper.selectTeamTreatmentById(id);
    }

    /**
     * 查询主页列表
     * 
     * @param teamTreatment 主页
     * @return 主页
     */
    @Override
    public List<TeamTreatment> selectTeamTreatmentList(TeamTreatment teamTreatment)
    {
        return teamTreatmentMapper.selectTeamTreatmentList(teamTreatment);
    }

    /**
     * 新增主页
     * 
     * @param teamTreatment 主页
     * @return 结果
     */
    @Override
    public int insertTeamTreatment(TeamTreatment teamTreatment)
    {
        teamTreatment.setCreateTime(DateUtils.getNowDate());
        return teamTreatmentMapper.insertTeamTreatment(teamTreatment);
    }

    /**
     * 修改主页
     * 
     * @param teamTreatment 主页
     * @return 结果
     */
    @Override
    public int updateTeamTreatment(TeamTreatment teamTreatment)
    {
        teamTreatment.setUpdateTime(DateUtils.getNowDate());
        return teamTreatmentMapper.updateTeamTreatment(teamTreatment);
    }

    /**
     * 批量删除主页
     * 
     * @param ids 需要删除的主页主键
     * @return 结果
     */
    @Override
    public int deleteTeamTreatmentByIds(Long[] ids)
    {
        return teamTreatmentMapper.deleteTeamTreatmentByIds(ids);
    }

    /**
     * 删除主页信息
     * 
     * @param id 主页主键
     * @return 结果
     */
    @Override
    public int deleteTeamTreatmentById(Long id)
    {
        return teamTreatmentMapper.deleteTeamTreatmentById(id);
    }
}
