package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Purchase;
import com.ruoyi.system.mapper.PurchaseMapper;
import com.ruoyi.system.service.IPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品支付Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@Service
public class PurchaseServiceImpl implements IPurchaseService
{
    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public  List<Purchase> selectPurchaseCountByUser(Purchase currPurchase){
        return purchaseMapper.selectPurchaseCountByUser(currPurchase);
    }

    @Override
    public Long selectPurchaseListCount(Purchase purchase) {
        return purchaseMapper.selectPurchaseListCount(purchase);
    }

    @Override
    public Purchase getPurchaseCount(Purchase purchase) {
        return purchaseMapper.getPurchaseCount(purchase);
    }

    @Override
    public List<Purchase> getTotalPurchase(Purchase purchase){
        return purchaseMapper.getTotalPurchase(purchase);
    }

    @Override
    public Purchase selectPurchaseById(Long id)
    {
        return purchaseMapper.selectPurchaseById(id);
    }

    @Override
    public boolean isFirst(Long userId)
    {
    	int count = 0;
    	Purchase purchase = new Purchase();
    	List<Purchase> purchsaeList = selectPurchaseList(purchase);
    	for(int i=0; i<purchsaeList.size();i++) {
    		Purchase temp = purchsaeList.get(i);
    		{
    			if (userId == temp.getBuyer())
    				count++;
    		}
    	}
    	if(count == 1)
    		return true;
    	return false;
    }
    /**
     * 查询产品支付列表
     *
     * @param purchase 产品支付
     * @return 产品支付
     */
    @Override
    public List<Purchase> selectPurchaseList(Purchase purchase)
    {
        return purchaseMapper.selectPurchaseList(purchase);
    }

    /**
     * 新增产品支付
     *
     * @param purchase 产品支付
     * @return 结果
     */
    @Override
    public int insertPurchase(Purchase purchase)
    {
        return purchaseMapper.insertPurchase(purchase);
    }

    /**
     * 修改产品支付
     *
     * @param purchase 产品支付
     * @return 结果
     */
    @Override
    public int updatePurchase(Purchase purchase)
    {
        return purchaseMapper.updatePurchase(purchase);
    }

    /**
     * 批量删除产品支付
     *
     * @param ids 需要删除的产品支付主键
     * @return 结果
     */
    @Override
    public int deletePurchaseByIds(Long[] ids)
    {
        return purchaseMapper.deletePurchaseByIds(ids);
    }

    /**
     * 删除产品支付信息
     *
     * @param id 产品支付主键
     * @return 结果
     */
    @Override
    public int deletePurchaseById(Long id)
    {
        return purchaseMapper.deletePurchaseById(id);
    }

	@Override
    public Long getTotalPurchase()
    {
    	return purchaseMapper.getTotalPurchase();
    }

    @Override
    public Long getTotalPurchaseCount()
    {
    	return purchaseMapper.getTotalPurchaseCount();
    }

	@Override
    public Long getTotalPurchaseRebate()
    {
    	return purchaseMapper.getTotalPurchaseRebate();
    }

    @Override
    public Long getTotalPurchaseRebateCount()
    {
    	return purchaseMapper.getTotalPurchaseRebateCount();
    }


    @Override
    public Long getTodayPurchase()
    {
    	return purchaseMapper.getTodayPurchase();
    }

    @Override
    public Long getTodayPurchaseCount()
    {
    	return purchaseMapper.getTodayPurchaseCount();
    }

    @Override
    public Long getTodayPurchaseRebate()
    {
    	return purchaseMapper.getTodayPurchaseRebate();
    }

    @Override
    public Long getTodayPurchaseRebateCount()
    {
    	return purchaseMapper.getTodayPurchaseRebateCount();
    }

    @Override
    public Long getOrderRebateNow()
    {
    	return purchaseMapper.getOrderRebateNow();
    }

    @Override
    public Long getOrderRebateNowCount()
    {
    	return purchaseMapper.getOrderRebateNowCount();
    }

	@Override
	public Purchase selectPanPurchaseByOrderNo(String orderNo) {
		return purchaseMapper.selectPanPurchaseByOrderNo(orderNo);
	}

	@Override
	public List<Purchase> selectLast3Purchase(Long userId) {
		return purchaseMapper.selectLast3Purchase(userId);
	}

	@Override
	public Long getUserRewardProductAmount(Long userId) {
		return purchaseMapper.getUserRewardProductAmount(userId);
	}
    @Override
    public List<Purchase> selectPurchaseJobList(){
        return purchaseMapper.selectPurchaseJobList();
    }

    @Override
    public List<Purchase> selectPurchaseListByUser(Purchase purchase) {
        return purchaseMapper.selectPurchaseListByUser(purchase);
    }

    @Override
    public List<Purchase> selectPurchaseListForBuy(Purchase purchase) {
        return purchaseMapper.selectPurchaseListForBuy(purchase);
    }
}
