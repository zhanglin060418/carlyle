package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PanProduct;

import java.util.List;

/**
 * 产品Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-22
 */
public interface PanProductMapper
{
    /**
     * 查询产品
     *
     * @param id 产品主键
     * @return 产品
     */
    public PanProduct selectPanProductById(Long id);


    public PanProduct selectPanProductByName(String nameIn);

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

    /**
     * 修改产品
     *
     * @param panProduct 产品
     * @return 结果
     */
    public int updatePanProduct(PanProduct panProduct);

    /**
     * 删除产品
     *
     * @param id 产品主键
     * @return 结果
     */
    public int deletePanProductById(Long id);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanProductByIds(Long[] ids);

	public PanProduct getAsset();

	public List<String> getRewardProductId();

    public List<PanProduct> selectPanProductBuyUser(PanProduct panProduct);
}
