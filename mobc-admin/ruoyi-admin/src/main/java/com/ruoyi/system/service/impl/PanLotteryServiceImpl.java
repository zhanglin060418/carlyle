package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PanDrawsDetail;
import com.ruoyi.system.domain.PanLottery;
import com.ruoyi.system.mapper.PanLotteryMapper;
import com.ruoyi.system.service.IPanLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@Service
public class PanLotteryServiceImpl implements IPanLotteryService
{
    @Autowired
    private PanLotteryMapper panLotteryMapper;

    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public PanLottery selectPanLotteryById(Long id)
    {
        return panLotteryMapper.selectPanLotteryById(id);
    }

    /**
     * 查询产品列表
     *
     * @param PanLottery 产品
     * @return 产品
     */
    @Override
    public List<PanLottery> selectPanLotteryList(PanLottery PanLottery)
    {
        return panLotteryMapper.selectPanLotteryList(PanLottery);
    }


    /**
     * 修改产品
     *
     * @param PanLottery 产品
     * @return 结果
     */
    @Override
    public int updatePanLottery(PanLottery PanLottery)
    {
        return panLotteryMapper.updatePanLottery(PanLottery);
    }

    @Override
    public List<PanLottery> getLotteryListByCategory() {
        return panLotteryMapper.getLotteryListByCategory();
    }

    @Override
    public List<PanDrawsDetail> getDrawsList(Long userId) {
        return panLotteryMapper.getDrawsList(userId);
    }

    @Override
    public List<PanDrawsDetail> selectPanDrawsList(PanDrawsDetail drawsDetail) {
       return panLotteryMapper.selectPanDrawsList(drawsDetail);
    }

    @Override
    public List<PanDrawsDetail> getVoucherListByJob() {
        return panLotteryMapper.getVoucherListByJob();
    }

    @Override
    public int updateVoucherEndDate() {
        return panLotteryMapper.updateVoucherEndDate();
    }

    @Override
    public List<PanDrawsDetail> getVoucherList(PanDrawsDetail drawsDetail) {
        return panLotteryMapper.getVoucherList(drawsDetail);
    }

    @Override
    public PanDrawsDetail getDrawsById(Long id) {
        return panLotteryMapper.getDrawsById(id);
    }

    @Override
    public List<PanDrawsDetail> getCouponList(PanDrawsDetail drawsDetail) {
        return panLotteryMapper.getCouponList(drawsDetail);
    }


}
