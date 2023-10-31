package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.IPanLotteryService;
import com.ruoyi.system.service.IPanUserAssetService;
import com.ruoyi.system.service.ITransService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽奖Controller
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@Api
@RestController
@RequestMapping("/system/draws")
public class PanDrawsController extends BaseController {

    @Autowired
    private IPanLotteryService panLotteryService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('system:draws:getDrawsDetailList')")
    @GetMapping("/getDrawsDetailList")
    public TableDataInfo getDrawsDetailList(PanDrawsDetail drawsDetail) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            drawsDetail.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            drawsDetail.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            drawsDetail.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanDrawsDetail> list = panLotteryService.selectPanDrawsList(drawsDetail);
        return getDataTable(list);
    }

    @GetMapping("/getDrawsList")
    public AjaxResult getDrawsList(@RequestParam Long userId) {
        AjaxResult ajax = AjaxResult.success();
        List<PanDrawsDetail> drawsList =  panLotteryService.getDrawsList(userId);
        ajax.put("data", drawsList);
        return ajax;
    }

    @GetMapping("/getVoucherList")
    public AjaxResult getVoucherList(@RequestParam Long productId) {
        LoginUser currentUser = getLoginUser();
        PanDrawsDetail drawsDetail = new PanDrawsDetail();
        drawsDetail.setUserId(currentUser.getUserId());
        drawsDetail.setProductId(productId);
        List<PanDrawsDetail> drawsList = panLotteryService.getVoucherList(drawsDetail);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, drawsList);
        return ajax;
    }

}
