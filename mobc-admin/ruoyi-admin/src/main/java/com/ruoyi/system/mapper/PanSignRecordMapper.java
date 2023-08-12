package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PanSignRecord;

/**
 * 签到记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-06-30
 */
public interface PanSignRecordMapper
{
    /**
     * 查询签到记录
     * @param userId
     * @return 签到记录
     */
    public PanSignRecord getMaxSignRecordByUser(Long userId);

    /**
     * 查询签到记录列表
     *
     * @param panSignRecord 签到记录
     * @return 签到记录集合
     */
    public List<PanSignRecord> selectPanSignRecordList(PanSignRecord panSignRecord);

    /**
     * 新增签到记录
     *
     * @param panSignRecord 签到记录
     * @return 结果
     */
    public int insertPanSignRecord(PanSignRecord panSignRecord);




    public  List<PanSignRecord> getSignRecordListByUser(PanSignRecord signRecord);
}
