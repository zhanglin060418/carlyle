package com.ruoyi.system.service;

import com.ruoyi.system.domain.PanDrawsDetail;
import com.ruoyi.system.domain.PanLottery;

import java.util.List;

/**
 * 产品Service接口
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public interface IPanLotteryService
{
    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    public PanLottery selectPanLotteryById(Long id);

    /**
     * 查询产品列表
     *
     * @param PanLottery 产品
     * @return 产品集合
     */
    public List<PanLottery> selectPanLotteryList(PanLottery PanLottery);


    /**
     * 修改产品
     *
     * @param PanLottery 产品
     * @return 结果
     */
    public int updatePanLottery(PanLottery PanLottery);

    public List<PanLottery> getLotteryListByCategory();

    public List<PanDrawsDetail> getDrawsList(Long userId);

    List<PanDrawsDetail> selectPanDrawsList(PanDrawsDetail drawsDetail);

    public List<PanDrawsDetail> getVoucherListByJob();

    public int updateVoucherEndDate();

    public List<PanDrawsDetail> getVoucherList(PanDrawsDetail drawsDetail);

    public PanDrawsDetail getDrawsById(Long id);
}
