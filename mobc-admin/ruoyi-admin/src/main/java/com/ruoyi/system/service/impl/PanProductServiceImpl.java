package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PanProduct;
import com.ruoyi.system.mapper.PanProductMapper;
import com.ruoyi.system.service.IPanProductService;
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
public class PanProductServiceImpl implements IPanProductService
{
    @Autowired
    private PanProductMapper panProductMapper;

    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public PanProduct selectPanProductById(Long id)
    {
        return panProductMapper.selectPanProductById(id);
    }

    /**
     * 查询产品列表
     *
     * @param panProduct 产品
     * @return 产品
     */
    @Override
    public List<PanProduct> selectPanProductList(PanProduct panProduct)
    {
        return panProductMapper.selectPanProductList(panProduct);
    }

    /**
     * 新增产品
     *
     * @param panProduct 产品
     * @return 结果
     */
    @Override
    public int insertPanProduct(PanProduct panProduct)
    {
        return panProductMapper.insertPanProduct(panProduct);
    }

    @Override
    public PanProduct selectPanProductByName(String nameIn) {
        return panProductMapper.selectPanProductByName(nameIn);
    }

    /**
     * 修改产品
     *
     * @param panProduct 产品
     * @return 结果
     */
    @Override
    public int updatePanProduct(PanProduct panProduct)
    {
        return panProductMapper.updatePanProduct(panProduct);
    }

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品主键
     * @return 结果
     */
    @Override
    public int deletePanProductByIds(Long[] ids)
    {
        return panProductMapper.deletePanProductByIds(ids);
    }

    /**
     * 删除产品信息
     *
     * @param id 产品主键
     * @return 结果
     */
    @Override
    public int deletePanProductById(Long id)
    {
        return panProductMapper.deletePanProductById(id);
    }

	@Override
	public PanProduct getAsset() {
		return panProductMapper.getAsset();
	}

	@Override
	public List<String> getRewardProductId() {
		return panProductMapper.getRewardProductId();
	}

    @Override
    public List<PanProduct> selectPanProductBuyUser(PanProduct panProduct) {
        return panProductMapper.selectPanProductBuyUser(panProduct);
    }

}
