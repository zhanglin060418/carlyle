package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TransStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.service.IPanTransactionHistoryService;
import com.ruoyi.system.service.IPanUserBalanceService;
import com.ruoyi.system.service.ITransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户余额Controller
 *
 * @author ruoyi
 * @date 2023-03-28
 */
@RestController
@RequestMapping("/system/balance")
public class PanUserBalanceController extends BaseController {
    @Autowired
    private IPanUserBalanceService panUserBalanceService;
    @Autowired
    private IPanTransactionHistoryService panTransactionService;
    @Autowired
    private ITransService iTransService;

    /**
     * 查询用户余额列表
     */

    @GetMapping("/getUserBalance")
    public AjaxResult getUserBalance(@RequestParam Long userId) {
        PanUserBalance userBalance = panUserBalanceService.getPanUserBalanceByUserId(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("balance", userBalance.getBalance());
        ajax.put("availableAmt", userBalance.getAvailableAmt());
        ajax.put("transitAmt", userBalance.getTransitAmt());
        ajax.put("lockBalance", userBalance.getLockBalance());
        ajax.put("assetBalance", userBalance.getAssetBalance());
        ajax.put("rewardAmt", userBalance.getRewardAmt());
        Long totayTreasureReward = panTransactionService.getTotayTreasureRewardById(userId);
        Long totalTreasureReward = panTransactionService.getTotalTreasureRewardById(userId);
        if (totayTreasureReward == null) {
            totayTreasureReward = 0L;
        }
        if (totalTreasureReward == null) {
            totalTreasureReward = 0L;
        }

        // 增值宝今日利息
        ajax.put("todayTreasureReward", totayTreasureReward);
        // 增值宝总利息
        ajax.put("totalTreasureReward", totalTreasureReward);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('system:balance:index')")
    @GetMapping("/listDetail")
    public TableDataInfo listDetail(PanUserBalance panUserBalance) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panUserBalance.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panUserBalance.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panUserBalance.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanUserBalance> list = panUserBalanceService.selectPanUserBalanceListDetail(panUserBalance);
        Long amountCount = panUserBalanceService.selectPanUserBalanceListDetailCount(panUserBalance);
        return getDataTable(list, amountCount);
    }

    /**
     * 导出用户余额列表
     */
    @PreAuthorize("@ss.hasPermi('system:balance:export')")
    @Log(title = "用户余额", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanUserBalance panUserBalance) {
        List<PanUserBalance> list = panUserBalanceService.selectPanUserBalanceList(panUserBalance);
        ExcelUtil<PanUserBalance> util = new ExcelUtil<PanUserBalance>(PanUserBalance.class);
        util.exportExcel(response, list, "用户余额数据");
    }

    /**
     * 获取用户余额详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:balance:query')")
    @GetMapping(value = "/{userBalanceId}")
    public AjaxResult getInfo(@PathVariable("userBalanceId") Long userBalanceId) {
        return success(panUserBalanceService.selectPanUserBalanceByUserBalanceId(userBalanceId));
    }

    /**
     * 新增用户余额
     */
    @PreAuthorize("@ss.hasPermi('system:balance:add')")
    @Log(title = "用户余额", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PanUserBalance panUserBalance) {
        return toAjax(panUserBalanceService.insertPanUserBalance(panUserBalance));
    }

    /**
     * 修改用户余额
     */
    @PreAuthorize("@ss.hasPermi('system:balance:edit')")
    @Log(title = "修改用户余额", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanUserBalance requestUserBalance) {
        BigDecimal currAmount = requestUserBalance.getAmount().multiply(new BigDecimal(100));
        requestUserBalance.setUpdateAt(String.valueOf(getLoginUser().getUserId()));
        logger.info("********PanUserBalanceController Edit Balance Request:{}", JSONObject.toJSONString(requestUserBalance));
        AjaxResult ajax = AjaxResult.success();
        PanUserBalance currUserBalance = panUserBalanceService.selectPanUserBalanceByUserBalanceId(requestUserBalance.getUserBalanceId());

        if(requestUserBalance.getBillType().equals(BillType.OUT.toString())){
            if(currAmount.compareTo(currUserBalance.getAvailableAmt())>0){
                ajax = AjaxResult.error();
                ajax.put("msg", "减少金额不能大于可用余额");
                return ajax;
            }
        }
        if(requestUserBalance.getBillType().equals(BillType.IN.toString())){
            if(StringUtils.isBlank(requestUserBalance.getTransType())){
                ajax = AjaxResult.error();
                ajax.put("msg", "请输入交易类型");
                return ajax;
            }
        }
        try {

            String result = iTransService.editBalance(requestUserBalance);
            if (!result.equals(MessageStatus.SUCCESS)) {
                ajax = AjaxResult.error();
                ajax.put("msg", "网络异常，请稍后重试！");
            }
        } catch (Exception e) {
            ajax = AjaxResult.error();
            ajax.put("msg", "网络异常，请稍后重试!");
            e.printStackTrace();
        }
        return ajax;
    }

    /**
     * 赠送产品
     */
    @PreAuthorize("@ss.hasPermi('system:balance:productReward')")
    @RequestMapping("/productReward")
    public AjaxResult productReward(@RequestBody PanUserBalance requestUserBalance) {
        requestUserBalance.setUpdateAt(String.valueOf(getLoginUser().getUserId()));
        logger.info("********PanUserBalanceController productReward Request:{}", JSONObject.toJSONString(requestUserBalance));
        AjaxResult ajax = AjaxResult.success();
        if(requestUserBalance.getAmount().compareTo(new BigDecimal(0))<0) {
            ajax = AjaxResult.error();
            ajax.put("msg", "金额异常");
        }
        try {

            String result = iTransService.productReward(requestUserBalance);
            if (!result.equals(MessageStatus.SUCCESS)) {
                ajax = AjaxResult.error();
                ajax.put("msg", "网络异常，请稍后重试！");
            }
        } catch (Exception e) {
            ajax = AjaxResult.error();
            ajax.put("msg", "网络异常，请稍后重试!");
            e.printStackTrace();
        }
        return ajax;
    }

    /**
     * 删除用户余额
     */
    @PreAuthorize("@ss.hasPermi('system:balance:remove')")
    @Log(title = "用户余额", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userBalanceIds}")
    public AjaxResult remove(@PathVariable Long[] userBalanceIds) {
        return toAjax(panUserBalanceService.deletePanUserBalanceByUserBalanceIds(userBalanceIds));
    }
}
