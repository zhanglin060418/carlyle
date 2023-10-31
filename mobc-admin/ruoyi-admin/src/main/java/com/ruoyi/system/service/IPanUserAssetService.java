package com.ruoyi.system.service;

import com.ruoyi.system.domain.PanUserAsset;

import java.util.List;

/**
 * 增值宝Service接口
 *
 * @author ruoyi
 * @date 2023-04-14
 */
public interface IPanUserAssetService
{
    /**
     * 查询增值宝
     *
     * @param userAssetId 增值宝主键
     * @return 增值宝
     */
    public PanUserAsset selectPanUserAssetByUserAssetId(Long userAssetId);

    /**
     * 查询增值宝列表
     *
     * @param panUserAsset 增值宝
     * @return 增值宝集合
     */
    public List<PanUserAsset> selectPanUserAssetList(PanUserAsset panUserAsset);

    /**
     * 查询增值宝列表
     *
     * @param panUserAsset 增值宝
     * @return 增值宝集合
     */
    public List<PanUserAsset> selectUserAssetList();


    public List<PanUserAsset> selectUserAssetListByJob();

    /**
     * 新增增值宝
     *
     * @param panUserAsset 增值宝
     * @return 结果
     */
    public int insertPanUserAsset(PanUserAsset panUserAsset);

    /**
     * 修改增值宝
     *
     * @param panUserAsset 增值宝
     * @return 结果
     */
    public int updatePanUserAsset(PanUserAsset panUserAsset);

    /**
     * 批量删除增值宝
     *
     * @param userAssetIds 需要删除的增值宝主键集合
     * @return 结果
     */
    public int deletePanUserAssetByUserAssetIds(Long[] userAssetIds);

    /**
     * 删除增值宝信息
     *
     * @param userAssetId 增值宝主键
     * @return 结果
     */
    public int deletePanUserAssetByUserAssetId(Long userAssetId);

	public PanUserAsset selectPanUserAssetByUserId(Long userId);

	public Long assetTotalBalance();

    public Long assetTotalRebate();

    public PanUserAsset getDrawsNumberByUserId(Long userId);
}
