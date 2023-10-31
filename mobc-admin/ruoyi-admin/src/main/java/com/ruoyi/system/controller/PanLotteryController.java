package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.PanDrawsDetail;
import com.ruoyi.system.domain.PanLottery;
import com.ruoyi.system.domain.PanSignRecord;
import com.ruoyi.system.domain.PanUserAsset;
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
@RequestMapping("/system/lottery")
public class PanLotteryController extends BaseController {

    @Autowired
    private IPanLotteryService panLotteryService;

    @Autowired
    private IPanUserAssetService panUserAssetService;

    @Autowired
    private ITransService iTransService;

    /**
     * 查询产品列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PanLottery PanLottery) {
        startPage();
        List<PanLottery> list = panLotteryService.selectPanLotteryList(PanLottery);
        return getDataTable(list);
    }

    /**
     * 查询产品列表
     */
    @GetMapping("/getDrawsList")
    public TableDataInfo drawsList(PanDrawsDetail drawsDetail) {
        startPage();
        List<PanDrawsDetail> list = panLotteryService.selectPanDrawsList(drawsDetail);
        return getDataTable(list);
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(panLotteryService.selectPanLotteryById(id));
    }


    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PanLottery PanLottery) {
        PanLottery.setUpdateBy(getUsername());
        return toAjax(panLotteryService.updatePanLottery(PanLottery));
    }


    @GetMapping("/getLotteryList")
    public AjaxResult getLotteryList() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        List<PanLottery> list = panLotteryService.getLotteryListByCategory();
        PanUserAsset panUserAsset = panUserAssetService.getDrawsNumberByUserId(user.getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("lotteryList", list);
        ajax.put("data", panUserAsset);
        return ajax;
    }




    @PostMapping("/addLotteryMove")
    public AjaxResult addLotteryMove(@RequestBody PanLottery panLottery) {
        logger.info("***抽奖-start****" + JSONObject.toJSONString(panLottery));

        AjaxResult ajax = AjaxResult.error();
        if(panLottery.getUserId() == null || panLottery.getUserId().longValue() == 0){
            ajax.put("msg", "User cannot be empty!");
            return ajax;
        }
        PanUserAsset panUserAsset = panUserAssetService.getDrawsNumberByUserId(panLottery.getUserId());

        if(panUserAsset.getDrawsNumber()<1){
            ajax.put("msg", "Lottery opportunities have been exhausted!");
            return ajax;
        }
        try {
            List<PanLottery> list = panLotteryService.getLotteryListByCategory();
            Map<String, Double> prizes = new HashMap<>();
            for (int i = 0, len = list.size(); i < len; i++) {
                prizes.put(list.get(i).getId().toString(),list.get(i).getProbability());
            }
            Long id = Long.parseLong(DateUtils.getRandomForLottery(prizes));
            PanLottery  resultPanLottery =  panLotteryService.selectPanLotteryById(id);
            resultPanLottery.setUserId(panLottery.getUserId());
            String result = iTransService.addLotteryMove(resultPanLottery);
            if (result.equals(MessageStatus.SUCCESS)) {
                ajax = AjaxResult.success();
                ajax.put("data",resultPanLottery);
            } else {
                ajax.put("msg", "Network exception, please try again later");
            }
        }catch (Exception e) {
            ajax.put("msg", "Network is abnormal, please try again later!");
        }
        return ajax;
    }
}
