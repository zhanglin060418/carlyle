package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PanDrawsDetail;
import com.ruoyi.system.domain.PanLottery;

import java.util.List;

/**
 * 产品Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public interface PanLotteryMapper
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

    public int addDrawsDetail(PanDrawsDetail drawsDetail);

    public int updateDrawsDetail(PanDrawsDetail drawsDetail);

    public List<PanDrawsDetail> getDrawsList(Long userId);

    public List<PanDrawsDetail> selectPanDrawsList(PanDrawsDetail drawsDetail);
}
