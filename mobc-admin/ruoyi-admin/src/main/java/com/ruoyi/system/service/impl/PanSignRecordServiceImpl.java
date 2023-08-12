package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PanSignRecordMapper;
import com.ruoyi.system.domain.PanSignRecord;
import com.ruoyi.system.service.IPanSignRecordService;

/**
 * 签到记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-30
 */
@Service
public class PanSignRecordServiceImpl implements IPanSignRecordService
{
    @Autowired
    private PanSignRecordMapper panSignRecordMapper;


    /**
     * 查询签到记录列表
     *
     * @param panSignRecord 签到记录
     * @return 签到记录
     */
    @Override
    public List<PanSignRecord> selectPanSignRecordList(PanSignRecord panSignRecord)
    {
        return panSignRecordMapper.selectPanSignRecordList(panSignRecord);
    }

    /**
     * 新增签到记录
     *
     * @param panSignRecord 签到记录
     * @return 结果
     */
    @Override
    public int insertPanSignRecord(PanSignRecord panSignRecord)
    {
        return panSignRecordMapper.insertPanSignRecord(panSignRecord);
    }

    @Override
    public List<PanSignRecord> getSignRecordListByUser(PanSignRecord signRecord) {
        return panSignRecordMapper.getSignRecordListByUser(signRecord);
    }

    @Override
    public PanSignRecord getMaxSignRecordByUser(Long userId) {
        return panSignRecordMapper.getMaxSignRecordByUser(userId);
    }


}
