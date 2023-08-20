package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.indo.payment.IndoPaymentService;
import com.indo.payment.IndoRechargeResult;
import com.indo.payment.IndoWithdrawResult;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TransStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.PanRecharge;
import com.ruoyi.system.domain.PanWithdraw;
import com.ruoyi.system.domain.RechargeCreate;
import com.ruoyi.system.service.IPanRechargeService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ITransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 充值Controller
 *
 * @author ruoyi
 * @date 2023-04-02
 */

@RestController
@RequestMapping("/system/recharge")
public class PanRechargeController extends BaseController {
    @Autowired
    private IPanRechargeService panRechargeService;

    @Autowired
    private IndoPaymentService indoPaymentService;

    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询充值列表
     */
    private static final Logger logger = LoggerFactory.getLogger(PanRechargeController.class);

    @GetMapping("getRechargeHistory")
    public AjaxResult getRechargeHistory(@RequestParam Long userId) {
        PanRecharge recharge = new PanRecharge();
        recharge.setUserId(userId);
        List<PanRecharge> list = panRechargeService.selectPanRechargeList(recharge);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }


    @PostMapping("/createRecharge")
    public AjaxResult createRecharge(@RequestBody RechargeCreate rechargeCreate, HttpServletRequest request) {
        logger.info("********用户充值-新增 rechargeCreate:" + JSONObject.toJSONString(rechargeCreate));
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
            if (sysUser.getStatus().equals("1")) {
                ajax.put("msg", "Account has been disabled");
                return ajax;
            }
            PanRecharge panRecharge = new PanRecharge();
            BigDecimal minAmount = new BigDecimal(sysConfigService.selectConfigByKey("Minimun_recharge_amount")).multiply(new BigDecimal(100));
            BigDecimal maxAmount = new BigDecimal(sysConfigService.selectConfigByKey("maxium_recharge_amount")).multiply(new BigDecimal(100));

            if (rechargeCreate.getAmount() == null) {
                ajax.put("msg", "Network exception, please try again later");
                return ajax;
            }
            if (rechargeCreate.getAmount().compareTo(maxAmount) > 0) {
                ajax.put("msg", "Recharge amount cannot be greater than" + maxAmount);
                return ajax;
            }
            if (minAmount.compareTo(rechargeCreate.getAmount()) > 0) {
                ajax.put("msg", "Recharge amount cannot be less than" + minAmount);
                return ajax;
            }

            if (StringUtils.isEmpty(rechargeCreate.getSelectChannel())) {
                ajax.put("msg", "Please select Recharge Channel");
                return ajax;
            }

            if (!StringUtils.isNumeric(rechargeCreate.getSelectChannel())) {
                ajax.put("msg", "Invlid Channel Paramter");
                return ajax;
            }


            panRecharge.setUserId(loginUser.getUserId());
            panRecharge.setAmount(rechargeCreate.getAmount());

            try {
                String requestNo = DateUtils.createOrderId("R");
                panRecharge.setRequestNo(requestNo);
                String userEmail = sysUser.getEmail();

                if (sysUser.getEmail().equals("") || StringUtils.isEmpty(sysUser.getEmail())) {
                    userEmail = sysUser.getPhonenumber() + "@gmail.com";
                }
                logger.info("********用户充值-新增 requestInfo：" + JSONObject.toJSONString(panRecharge));



                IndoRechargeResult indoRechargeResult = indoPaymentService.recharge(rechargeCreate.getAmount(), requestNo,
                        loginUser.getUsername(), sysUser.getPhonenumber(), userEmail, rechargeCreate.getDomain(),rechargeCreate.getSelectChannel());
                if(StringUtils.isEmpty(indoRechargeResult.getPayInfoUrl())){
                    ajax.put("msg", "Failed to obtain payment link. Please try again later or choose a different recharge channel");
                    return ajax;
                }



                panRecharge.setOrderNo(indoRechargeResult.getOrderNo());
                panRecharge.setPayInfoUrl(indoRechargeResult.getPayInfoUrl());
                panRecharge.setStatus(TransStatus.PENDING);
                panRecharge.setChannelId(Long.parseLong(rechargeCreate.getSelectChannel()));
                logger.info("********用户充值-新增 responseInfo:" + JSONObject.toJSONString(panRecharge));
                panRechargeService.insertPanRecharge(panRecharge);
                ajax = AjaxResult.success();
                ajax.put("recharge", panRecharge);
            } catch (Exception e) {
                ajax.put("msg", "Network is abnormal, please try again!");
                e.printStackTrace();
            }
        } else {
            ajax.put("msg", "Invalid session or session expired, please log in again.");
        }
        return ajax;
    }


    @RequestMapping("/rechargeSuccess")
    public AjaxResult successRecharge(@RequestParam(name = "requestNo", required = false) String requestNo) {
        PanRecharge panRecharge = panRechargeService.selectPanRechargeByRequestNo(requestNo);
        panRecharge.setStatus(TransStatus.PROGRESS);
        return toAjax(panRechargeService.updatePanRecharge(panRecharge));
    }

    @PreAuthorize("@ss.hasPermi('system:recharge:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanRecharge panRecharge) {

        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panRecharge.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panRecharge.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panRecharge.setAgentId(currentUser.getUserId());
        }
        startPage();

        List<PanRecharge> list = panRechargeService.selectPanRechargeList(panRecharge);
        String currEnvironment = sysConfigService.selectConfigByKey("Current_Environment");
        Long amountCount = panRechargeService.selectPanRechargeListCount(panRecharge);
        return getDataTable(list, amountCount,currEnvironment);
    }

    /**
     * 导出充值列表
     */
    @PreAuthorize("@ss.hasPermi('system:recharge:export')")
    @Log(title = "充值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanRecharge panRecharge) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panRecharge.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panRecharge.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panRecharge.setAgentId(currentUser.getUserId());
        }
        List<PanRecharge> list = panRechargeService.selectPanRechargeList(panRecharge);
        ExcelUtil<PanRecharge> util = new ExcelUtil<PanRecharge>(PanRecharge.class);
        util.exportExcel(response, list, "充值数据");
    }

    /**
     * 获取充值详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:recharge:query')")
    @GetMapping(value = "/{rechargeId}")
    public AjaxResult getInfo(@PathVariable("rechargeId") Long rechargeId) {
        return success(panRechargeService.selectPanRechargeByRechargeId(rechargeId));
    }

    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody PanRecharge panRecharge) {
        logger.info("****用户充值-回调 start****");
        PanRecharge curr = panRechargeService.selectPanRechargeByRechargeId(panRecharge.getRechargeId());
        LoginUser currentUser = getLoginUser();
        panRecharge.setUpdateBy(currentUser.getUsername());
        AjaxResult ajax = AjaxResult.success();
        try {
            if (!curr.getStatus().equals(TransStatus.COMPLETED)) {
                curr.setStatus(TransStatus.COMPLETED);//Completed
                iTransService.recharge(curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }

        return ajax;
    }

}
