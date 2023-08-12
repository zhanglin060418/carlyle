package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanTransactionHistory;
import com.ruoyi.system.service.IPanTransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2023-04-10
 */
@RestController
@RequestMapping("/system/history")
public class PanTransactionHistoryController extends BaseController
{
    @Autowired
    private IPanTransactionHistoryService panTransactionHistoryService;

    /**
 * 查询【请填写功能名称】列表
 */
@PreAuthorize("@ss.hasPermi('system:history:list')")
@GetMapping("/list")
public TableDataInfo list(PanTransactionHistory panTransactionHistory)
{
    LoginUser currentUser = getLoginUser();
    if(currentUser.getUserType().equals("01")){
        panTransactionHistory.setTopId(currentUser.getUserId());
    }
    if (currentUser.getUserType().equals("02")) {
        panTransactionHistory.setManagerId(currentUser.getUserId());
    }
    if (currentUser.getUserType().equals("03")) {
        panTransactionHistory.setAgentId(currentUser.getUserId());
    }
    startPage();
    List<PanTransactionHistory> list = panTransactionHistoryService.selectPanTransactionHistoryList(panTransactionHistory);
    Long amountCount = panTransactionHistoryService.selectPanTransactionHistoryListCount(panTransactionHistory);
    return getDataTable(list,amountCount);
}


    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:listInterest')")
    @GetMapping("/listInterest")
    public TableDataInfo listInterest(PanTransactionHistory panTransactionHistory)
    {
        LoginUser currentUser = getLoginUser();
        if(currentUser.getUserType().equals("01")){
            panTransactionHistory.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panTransactionHistory.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panTransactionHistory.setAgentId(currentUser.getUserId());
        }
        panTransactionHistory.setTransactionType(TransType.Product_Daily_Interest.name());
        startPage();
        List<PanTransactionHistory> list = panTransactionHistoryService.selectPanTransByInterestList(panTransactionHistory);
        Long amountCount = panTransactionHistoryService.selectPanTransByInterestListCount(panTransactionHistory);
        return getDataTable(list,amountCount);
    }

    @PreAuthorize("@ss.hasPermi('system:history:listCommission')")
    @GetMapping("/listCommission")
    public TableDataInfo listCommission(PanTransactionHistory panTransactionHistory)
    {
        LoginUser currentUser = getLoginUser();
        if(currentUser.getUserType().equals("01")){
            panTransactionHistory.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panTransactionHistory.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panTransactionHistory.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanTransactionHistory> list = panTransactionHistoryService.selectPanTransByCommissionList(panTransactionHistory);
        Long amountCount = panTransactionHistoryService.selectPanTransByCommissionListCount(panTransactionHistory);
        return getDataTable(list,amountCount);
    }

    @GetMapping("/today")
    public AjaxResult getTodayTransaction(@RequestParam Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        List<PanTransactionHistory> list = panTransactionHistoryService.selectTodayPanTransactionHistoryList(userId);
        ajax.put("list", list);
        return ajax;
    }

    @GetMapping("/yesterday")
    public AjaxResult getYesterdayTransaction(@RequestParam Long userId)
    {
    	AjaxResult ajax = AjaxResult.success();
        List<PanTransactionHistory> list = panTransactionHistoryService.selectYesterdayPanTransactionHistoryList(userId);
        ajax.put("list", list);
        return ajax;
    }

    @GetMapping("/lastweek")
    public AjaxResult getLast7Transaction(@RequestParam Long userId)
    {
    	AjaxResult ajax = AjaxResult.success();
        List<PanTransactionHistory> list = panTransactionHistoryService.selectLastWeekPanTransactionHistoryList(userId);
        ajax.put("list", list);
        return ajax;
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanTransactionHistory panTransactionHistory)
    {
        List<PanTransactionHistory> list = panTransactionHistoryService.selectPanTransactionHistoryList(panTransactionHistory);
        ExcelUtil<PanTransactionHistory> util = new ExcelUtil<PanTransactionHistory>(PanTransactionHistory.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:history:query')")
    @GetMapping(value = "/{transactionHistoryId}")
    public AjaxResult getInfo(@PathVariable("transactionHistoryId") Long transactionHistoryId)
    {
        return success(panTransactionHistoryService.selectPanTransactionHistoryByTransactionHistoryId(transactionHistoryId));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:history:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanTransactionHistory panTransactionHistory)
    {
        return toAjax(panTransactionHistoryService.updatePanTransactionHistory(panTransactionHistory));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:history:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{transactionHistoryIds}")
    public AjaxResult remove(@PathVariable Long[] transactionHistoryIds)
    {
        return toAjax(panTransactionHistoryService.deletePanTransactionHistoryByTransactionHistoryIds(transactionHistoryIds));
    }
}
