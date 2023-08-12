package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PanTaskRecord;
import com.ruoyi.system.mapper.PanTaskRecordMapper;
import com.ruoyi.system.service.ITaskRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ruoyi
 * @date 2023-04-09
 */
@Service
public class PanTaskRecordServiceImpl implements ITaskRecordService {
    @Autowired
    private PanTaskRecordMapper panTaskRecordMapper;

    @Override
    public PanTaskRecord selectTaskJobByTarget(String invokeTarget) {
        return panTaskRecordMapper.selectTaskJobByTarget(invokeTarget);
    }

    public List<PanTaskRecord> selectTaskList(PanTaskRecord panTaskRecord){
        return panTaskRecordMapper.selectTaskList(panTaskRecord);
    };

    @Override
    public int insertTaskJobRecord(PanTaskRecord taskRecord) {
        return panTaskRecordMapper.insertTaskJobRecord(taskRecord);
    }

    @Override
    public int updateTaskJobRecord(PanTaskRecord taskRecord) {
        return panTaskRecordMapper.updateTaskJobRecord(taskRecord);
    }
}
