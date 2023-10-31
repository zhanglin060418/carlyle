package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.PanUserAsset;
import com.ruoyi.system.mapper.PanUserAssetMapper;
import com.ruoyi.system.service.IPanUserAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 增值宝Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-14
 */
@Service
public class PanUserAssetServiceImpl implements IPanUserAssetService
{
    @Autowired
    private PanUserAssetMapper panUserAssetMapper;

    /**
     * 查询增值宝
     *
     * @param userAssetId 增值宝主键
     * @return 增值宝
     */
    @Override
    public PanUserAsset selectPanUserAssetByUserAssetId(Long userAssetId)
    {
        return panUserAssetMapper.selectPanUserAssetByUserAssetId(userAssetId);
    }
    public List<PanUserAsset> selectUserAssetList(){
        return panUserAssetMapper.selectUserAssetList();
    }

    @Override
    public List<PanUserAsset> selectUserAssetListByJob() {
        return panUserAssetMapper.selectUserAssetListByJob();
    }

    /**
     * 查询增值宝列表
     *
     * @param panUserAsset 增值宝
     * @return 增值宝
     */
    @Override
    public List<PanUserAsset> selectPanUserAssetList(PanUserAsset panUserAsset)
    {
        return panUserAssetMapper.selectPanUserAssetList(panUserAsset);
    }

    /**
     * 新增增值宝
     *
     * @param panUserAsset 增值宝
     * @return 结果
     */
    @Override
    public int insertPanUserAsset(PanUserAsset panUserAsset)
    {
        return panUserAssetMapper.insertPanUserAsset(panUserAsset);
    }

    /**
     * 修改增值宝
     *
     * @param panUserAsset 增值宝
     * @return 结果
     */
    @Override
    public int updatePanUserAsset(PanUserAsset panUserAsset)
    {
        return panUserAssetMapper.updatePanUserAsset(panUserAsset);
    }

    /**
     * 批量删除增值宝
     *
     * @param userAssetIds 需要删除的增值宝主键
     * @return 结果
     */
    @Override
    public int deletePanUserAssetByUserAssetIds(Long[] userAssetIds)
    {
        return panUserAssetMapper.deletePanUserAssetByUserAssetIds(userAssetIds);
    }

    /**
     * 删除增值宝信息
     *
     * @param userAssetId 增值宝主键
     * @return 结果
     */
    @Override
    public int deletePanUserAssetByUserAssetId(Long userAssetId)
    {
        return panUserAssetMapper.deletePanUserAssetByUserAssetId(userAssetId);
    }

	@Override
	public PanUserAsset selectPanUserAssetByUserId(Long userId) {
		return panUserAssetMapper.selectPanUserAssetByUserId(userId);
	}

	@Override
	public Long assetTotalBalance() {
		return panUserAssetMapper.getAssetTotalBalance();
	}

    @Override
    public Long assetTotalRebate() {
        return panUserAssetMapper.assetTotalRebate();
    }

    @Override
    public PanUserAsset getDrawsNumberByUserId(Long userId) {
        return panUserAssetMapper.getDrawsNumberByUserId(userId);
    }
}
