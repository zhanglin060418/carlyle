package com.ruoyi.system.service;

import com.ruoyi.system.domain.PanTaskRecord;

import java.util.List;


/**
 * 银行名单Service接口
 *
 * @author ruoyi
 * @date 2023-04-09
 */
public interface ITaskRecordService
{
    public PanTaskRecord selectTaskJobByTarget(String invokeTarget);

    public List<PanTaskRecord> selectTaskList(PanTaskRecord panTaskRecord);

    public int insertTaskJobRecord(PanTaskRecord taskRecord);

    public int updateTaskJobRecord(PanTaskRecord taskRecord);


}
