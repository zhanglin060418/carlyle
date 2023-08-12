package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PanTaskRecord;

import java.util.List;

/**
 *
 * @author ruoyi
 * @date 2023-04-10
 */
public interface PanTaskRecordMapper
{
    public PanTaskRecord selectTaskJobByTarget(String invokeTarget);

    public List<PanTaskRecord> selectTaskList(PanTaskRecord panTaskRecord);

    public int insertTaskJobRecord(PanTaskRecord taskRecord);

    public int updateTaskJobRecord(PanTaskRecord taskRecord);
}
