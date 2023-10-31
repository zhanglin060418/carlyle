package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ITransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.PanSignRecord;
import com.ruoyi.system.service.IPanSignRecordService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 签到记录Controller
 *
 * @author ruoyi
 * @date 2023-06-30
 */
@RestController
@RequestMapping("/system/signRecord")
public class PanSignRecordController extends BaseController
{
    @Autowired
    private IPanSignRecordService panSignRecordService;

    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysConfigService sysConfigService;

    private static final Logger logger = LoggerFactory.getLogger(PanSignRecordController.class);

    /**
     * 查询签到记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:signRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanSignRecord panSignRecord)
    {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panSignRecord.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panSignRecord.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panSignRecord.setAgentId(currentUser.getUserId());
        }
        if (StringUtils.isBlank(panSignRecord.getPropName())) {
            panSignRecord.setPropName("signTime");
            //descending  ascending
            panSignRecord.setSortType("descending");
        }
        startPage();
        List<PanSignRecord> list = panSignRecordService.selectPanSignRecordList(panSignRecord);
        return getDataTable(list);
    }


    @RequestMapping("/getSignRecordListByUser")
    public AjaxResult getSginRecordListByUser(@RequestParam Long userId , @RequestParam int signYear, @RequestParam int signMonth ){
        AjaxResult ajax = AjaxResult.success();
        PanSignRecord signRecord = new PanSignRecord();
        signRecord.setUserId(userId);
        signRecord.setSignYear(signYear);
        signRecord.setSignMonth(signMonth);
        List <PanSignRecord> signRecordList = panSignRecordService.getSignRecordListByUser(signRecord);
        ajax.put("data", signRecordList);
        return ajax;
    }

    @RequestMapping("/getMaxSignRecordByUser")
    public AjaxResult getMaxSignRecordByUser(@RequestParam Long userId){
        AjaxResult ajax = AjaxResult.success();
        PanSignRecord signRecord = panSignRecordService.getMaxSignRecordByUser(userId);
        ajax.put("data", signRecord);
        return ajax;
    }

    @RequestMapping("/getDailySignInAmount")
    public AjaxResult getDailySignInAmount(){
        AjaxResult ajax = AjaxResult.success();
        Double dailySignInAmount = Double.parseDouble(sysConfigService.selectConfigByKey("dailySignInAmount"));
        Double dailySignInAmountMax = Double.parseDouble(sysConfigService.selectConfigByKey("dailySignInAmountMax"));
        ajax.put("dailySignInAmount", dailySignInAmount);
        ajax.put("dailySignInAmountMax", dailySignInAmountMax);
        return ajax;
    }

    /**
     * 签到
     */
    @PostMapping("/addSignRecord")
    public AjaxResult addSignRecord(@RequestBody PanSignRecord panSignRecord) {
        logger.info("****签到-start****" + JSONObject.toJSONString(panSignRecord));

        AjaxResult ajax = AjaxResult.error();
        if(panSignRecord.getUserId() == null || panSignRecord.getUserId().longValue() == 0){
            ajax.put("msg", "User cannot be empty!");
            return ajax;
        }
        if(panSignRecord.getSignYear() <1||panSignRecord.getSignMonth() <1||panSignRecord.getSignDay() <1){
            ajax.put("msg", "Date exception!");
            return ajax;
        }
        List <PanSignRecord> signRecordList = panSignRecordService.getSignRecordListByUser(panSignRecord);
        if(signRecordList.size()>0){
            ajax.put("msg", "Signed in today!");
            return ajax;
        }
        String fristDay = panSignRecord.getSignYear()+"-"+panSignRecord.getSignMonth()+"-"+panSignRecord.getSignDay();
        if(!DateUtils.isToday(fristDay)){
            ajax.put("msg", "Date exception!");
            return ajax;
        }
        try {
            String result = iTransService.toSgin(panSignRecord);
            if (result.equals(MessageStatus.SUCCESS)) {
                ajax = AjaxResult.success();
            } else {
                ajax.put("msg", "Network exception, please try again later");
            }
        }catch (Exception e) {
            ajax.put("msg", "Network is abnormal, please try again later!");
        }
        return ajax;
    }




}
