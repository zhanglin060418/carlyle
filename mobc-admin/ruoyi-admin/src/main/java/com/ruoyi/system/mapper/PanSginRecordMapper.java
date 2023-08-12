package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PanSginRecord;

/**
 * 签到记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-06-29
 */
public interface PanSginRecordMapper
{
    /**
     * 查询签到记录列表
     *
     * @param panSginRecord 签到记录
     * @return 签到记录集合
     */
    public List<PanSginRecord> selectpanSginRecordList(PanSginRecord panSginRecord);

    /**
     * 新增签到记录
     *
     * @param panSginRecord 签到记录
     * @return 结果
     */
    public int insertpanSginRecord(PanSginRecord panSginRecord);

    public List<PanSginRecord> getSginRecordListByUser(PanSginRecord user);
}
