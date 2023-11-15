package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Purchase;

import java.util.List;

/**
 * 产品支付Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public interface PurchaseMapper
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
     * 删除产品支付
     *
     * @param id 产品支付主键
     * @return 结果
     */
    public int deletePurchaseById(Long id);

    /**
     * 批量删除产品支付
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePurchaseByIds(Long[] ids);

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

    public List<Purchase> selectPurchaseListForBuy(Purchase userPurchase);

    public List<Purchase> getTotalPurchase(Purchase userPurchase);

    public List<Purchase> selectPurchaseListByUser(Purchase purchase);

    public Purchase selectPurchaseAmtByVip(Long buyer);

}
