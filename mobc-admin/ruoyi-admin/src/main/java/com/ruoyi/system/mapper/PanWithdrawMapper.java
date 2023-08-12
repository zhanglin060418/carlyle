package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.PanWithdraw;

import java.util.List;

/**
 * 提款Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-10
 */
public interface PanWithdrawMapper
{
    /**
     * 查询提款
     *
     * @param withdrawId 提款主键
     * @return 提款
     */
    public PanWithdraw selectPanWithdrawByWithdrawId(Long withdrawId);

    /**
     * 查询提款列表
     *
     * @param panWithdraw 提款
     * @return 提款集合
     */
    public List<PanWithdraw> selectPanWithdrawList(PanWithdraw panWithdraw);

    /**
     * 新增提款
     *
     * @param panWithdraw 提款
     * @return 结果
     */
    public int insertPanWithdraw(PanWithdraw panWithdraw);

    /**
     * 修改提款
     *
     * @param panWithdraw 提款
     * @return 结果
     */
    public int updatePanWithdraw(PanWithdraw panWithdraw);

    /**
     * 删除提款
     *
     * @param withdrawId 提款主键
     * @return 结果
     */
    public int deletePanWithdrawByWithdrawId(Long withdrawId);

    /**
     * 批量删除提款
     *
     * @param withdrawIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePanWithdrawByWithdrawIds(Long[] withdrawIds);

    public Long getTotalWithdraw();

    public Long getTodayWithdraw();

	public PanWithdraw selectPanWithdrawByOrderNo(String orderNo);

	public Long checkWithdrawStatus(Long userId);

    public Long getTotalWithdrawCountByUser(Long userId);

    public Long selectPanWithdrawListCount(PanWithdraw panWithdraw);

    public  PanWithdraw getTransitAmtByUser(Long agentId);

    public Long getUserWithdrawInfoByIds(String withdrawIds);
}
