package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.IsIncome;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanProduct;
import com.ruoyi.system.domain.PanTransactionHistory;
import com.ruoyi.system.domain.PanUserAsset;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 增值宝Controller
 *
 * @author ruoyi
 * @date 2023-04-14
 */
@Api
@RestController
@RequestMapping("/system/asset")
public class PanUserAssetController extends BaseController {
    @Autowired
    private IPanUserAssetService panUserAssetService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IPanUserBalanceService panUserBalanceService;

    @Autowired
    private IPanTransactionHistoryService panTransactionHistoryService;

    @Autowired
    private IPanProductService panProductService;

    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysUserService sysUserService;

    private static final Logger logger = LoggerFactory.getLogger(PanUserAssetController.class);

    /**
     * 查询增值宝列表
     */
    @PreAuthorize("@ss.hasPermi('system:asset:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanTransactionHistory panUserAsset) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panUserAsset.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panUserAsset.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panUserAsset.setAgentId(currentUser.getUserId());
        }
        panUserAsset.setTransactionType(TransType.Treasure_Reward.name());
        startPage();
        List<PanTransactionHistory> list = panTransactionHistoryService.selectPanTransactionHistoryList(panUserAsset);
        Long amountCount = panTransactionHistoryService.selectPanTransactionHistoryListCount(panUserAsset);
        return getDataTable(list, amountCount);
    }

    @ApiOperation("get daily interest")
    @GetMapping("/interest_rate")
    public Double getInterestRate() {
        return Double.parseDouble(sysConfigService.selectConfigByKey("treasure_interest_rate").toString());
    }

    @GetMapping("/getAsset")
    public AjaxResult getAsset() {
        PanProduct panProduct = panProductService.getAsset();
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, panProduct);
        return ajax;
    }

    @GetMapping("/getBalance")
    public AjaxResult getAssetBalance(@RequestParam Long userId) {
        PanUserAsset userAsset = panUserAssetService.selectPanUserAssetByUserId(userId);
        BigDecimal assetBalance = new BigDecimal(0);
        if (userAsset != null)
            assetBalance = userAsset.getBalance();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("assetBalance", assetBalance);
        return ajax;
    }

    /**
     * 导出增值宝列表
     */
    @PreAuthorize("@ss.hasPermi('system:asset:export')")
    @Log(title = "增值宝", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanUserAsset panUserAsset) {
        List<PanUserAsset> list = panUserAssetService.selectPanUserAssetList(panUserAsset);
        ExcelUtil<PanUserAsset> util = new ExcelUtil<PanUserAsset>(PanUserAsset.class);
        util.exportExcel(response, list, "增值宝数据");
    }

    /**
     * 获取增值宝详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:asset:query')")
    @GetMapping(value = "/{userAssetId}")
    public AjaxResult getInfo(@PathVariable("userAssetId") Long userAssetId) {
        return success(panUserAssetService.selectPanUserAssetByUserAssetId(userAssetId));
    }

    @GetMapping(value = "/user/{userId}")
    public AjaxResult selectUserAssetByUserId(@PathVariable("userId") Long userId) {
        return success(panUserAssetService.selectPanUserAssetByUserId(userId));
    }

    /**
     * 增值宝转入
     */
    @ApiOperation("Transfer In")
    @Log(title = "增值宝转入", businessType = BusinessType.UPDATE)
    @PostMapping("/transferIn")
    public AjaxResult transferIn(@RequestBody PanUserAsset panUserAsset) {
        logger.info("********PanUserAssetController transferIn UserAsset Info:" + JSONObject.toJSONString(panUserAsset));
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
            if (sysUser.getStatus().equals("1")) {
                ajax.put("msg", "Account has been disabled");
                return ajax;
            }
            PanUserBalance userBalance = panUserBalanceService.getPanUserBalanceByUserId(panUserAsset.getUserId());
            //判断交易金额
            if (panUserAsset.getAmount().compareTo(userBalance.getAvailableAmt()) > 0) {
                throw new ServiceException("The user does not have enough balance");
            }
            try {
                String result = iTransService.transferIn(panUserAsset);
                if (result.equals(MessageStatus.SUCCESS)) {
                    ajax = AjaxResult.success();
                } else {
                    ajax.put("msg", "Network is abnormal, please try again!");
                }
            } catch (Exception e) {
                ajax.put("msg", "Network is abnormal, please try again!");
            }
        }
        return ajax;
    }

    /**
     * 增值宝转出
     */
    @ApiOperation("Transfer Out")
    @Log(title = "增值宝转出", businessType = BusinessType.UPDATE)
    @PostMapping("/transferOut")

    public AjaxResult transferOut(@RequestBody PanUserAsset panUserAsset) {

        logger.info("********PanUserAssetController transferOut UserAsset Info:" + JSONObject.toJSONString(panUserAsset));
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
            if (sysUser.getStatus().equals("1")) {
                ajax.put("msg", "Account has been disabled");
                return ajax;
            }
            PanUserBalance userBalance = panUserBalanceService.getPanUserBalanceByUserId(panUserAsset.getUserId());
            //判断交易金额
            if (panUserAsset.getAmount().compareTo(userBalance.getAssetBalance()) > 0) {
                ajax.put("msg", "The user does not have enough asset balance");
            }
            try {
                String result = iTransService.transferOut(panUserAsset);
                if (result.equals(MessageStatus.SUCCESS)) {
                    ajax = AjaxResult.success();
                }else{
                    ajax.put("msg", "网络异常，请稍后重试！");
                }
            } catch (Exception e) {
                ajax.put("msg", "网络异常，请稍后重试!");
            }
        }
        return ajax;
    }

}
