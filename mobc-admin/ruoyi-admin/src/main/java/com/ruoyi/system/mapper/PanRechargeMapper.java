package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PanRecharge;

import java.util.List;

/**
 * 充值Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-02
 */
public interface PanRechargeMapper
{
    /**
     * 查询充值
     *
     * @param rechargeId 充值主键
     * @return 充值
     */
    public PanRecharge selectPanRechargeByRechargeId(Long rechargeId);

    public PanRecharge selectPanRechargeByRequestNo(String requestNo);

    public PanRecharge selectPanRechargeByOrderNo(String orderNo);

    /**
     * 查询充值列表
     *
     * @param panRecharge 充值
     * @return 充值集合
     */
    public List<PanRecharge> selectPanRechargeList(PanRecharge panRecharge);

    /**
     * 新增充值
     *
     * @param panRecharge 充值
     * @return 结果
     */
    public int insertPanRecharge(PanRecharge panRecharge);

    /**
     * 修改充值
     *
     * @param panRecharge 充值
     * @return 结果
     */
    public int updatePanRecharge(PanRecharge panRecharge);

    /**
     * 删除充值
     *
     * @param rechargeId 充值主键
     * @return 结果
     */
    public int deletePanRechargeByRechargeId(Long rechargeId);

    /**
     * 批量删除充值
     *
     * @param rechargeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanRechargeByRechargeIds(Long[] rechargeIds);

    public Long getTotalRecharge();

    public Long getTotalRechargeUsers();

    public Long getTodayRecharge();

    public Long getTodayRechargeUsers();

    public Long setTotalRechargeCountByUser(Long userId);

    public Long selectPanRechargeListCount(PanRecharge panRecharge);
}
