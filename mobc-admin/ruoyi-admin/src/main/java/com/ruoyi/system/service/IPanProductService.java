package com.ruoyi.system.service;

import com.ruoyi.system.domain.PanProduct;

import java.util.List;

/**
 * 产品Service接口
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public interface IPanProductService
{
    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    public PanProduct selectPanProductById(Long id);

    /**
     * 查询产品列表
     *
     * @param panProduct 产品
     * @return 产品集合
     */
    public List<PanProduct> selectPanProductList(PanProduct panProduct);

    /**
     * 新增产品
     *
     * @param panProduct 产品
     * @return 结果
     */
    public int insertPanProduct(PanProduct panProduct);

    public PanProduct selectPanProductByName(String nameIn);

    /**
     * 修改产品
     *
     * @param panProduct 产品
     * @return 结果
     */
    public int updatePanProduct(PanProduct panProduct);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的产品主键集合
     * @return 结果
     */
    public int deletePanProductByIds(Long[] ids);

    /**
     * 删除产品信息
     *
     * @param id 产品主键
     * @return 结果
     */
    public int deletePanProductById(Long id);

	public PanProduct getAsset();

	public List<String> getRewardProductId();

    public List<PanProduct> selectPanProductBuyUser(PanProduct panProduct);
}
