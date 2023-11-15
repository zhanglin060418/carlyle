package com.ruoyi.system.service;

import com.ruoyi.system.domain.Purchase;

import java.util.List;

/**
 * 产品支付Service接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface IPurchaseService
{
    /**
     * 查询产品支付
     *
     * @param id 产品支付主键
     * @return 产品支付
     */
    public Purchase selectPurchaseById(Long id);

    public boolean isFirst(Long userId);
    /**
     * 查询产品支付列表
     *
     * @param purchase 产品支付
     * @return 产品支付集合
     */
    public List<Purchase> selectPurchaseList(Purchase purchase);

    public List<Purchase> selectLast3Purchase(Long userId);

    /**
     * 新增产品支付
     *
     * @param purchase 产品支付
     * @return 结果
     */
    public int insertPurchase(Purchase purchase);

    /**
     * 修改产品支付
     *
     * @param purchase 产品支付
     * @return 结果
     */
    public int updatePurchase(Purchase purchase);

    /**
     * 批量删除产品支付
     *
     * @param ids 需要删除的产品支付主键集合
     * @return 结果
     */
    public int deletePurchaseByIds(Long[] ids);

    /**
     * 删除产品支付信息
     *
     * @param id 产品支付主键
     * @return 结果
     */
    public int deletePurchaseById(Long id);

    public Long getTotalPurchase();

    public Long getTotalPurchaseCount();

    public Long getTotalPurchaseRebate();

    public Long getTotalPurchaseRebateCount();

    public Long getTodayPurchase();

    public Long getTodayPurchaseCount();

    public Long getTodayPurchaseRebate();

    public Long getTodayPurchaseRebateCount();

    public Long getOrderRebateNow();

    public Long getOrderRebateNowCount();

	public Purchase selectPanPurchaseByOrderNo(String orderNo);

	public Long getUserRewardProductAmount(Long userId);

    public List<Purchase> selectPurchaseJobList();

    public List<Purchase> selectPurchaseCountByUser(Purchase currPurchase);

    public Long selectPurchaseListCount(Purchase purchase);

    public Purchase getPurchaseCount(Purchase purchase);

    public List<Purchase> getTotalPurchase(Purchase purchase);

    public  List<Purchase> selectPurchaseListByUser(Purchase purchase);

    public List<Purchase> selectPurchaseListForBuy(Purchase userPurchase);

    public Purchase selectPurchaseAmtByVip(Long buyer);
}
