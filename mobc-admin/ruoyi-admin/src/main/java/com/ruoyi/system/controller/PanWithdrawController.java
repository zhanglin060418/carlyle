package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.indo.payment.IndoPaymentService;
import com.indo.payment.IndoWithdrawResult;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TransStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 提款Controller
 *
 * @author ruoyi
 * @date 2023-04-10
 */
@Api
@RestController
@RequestMapping("/system/withdraw")
public class PanWithdrawController extends BaseController {
    @Autowired
    private IPanWithdrawService panWithdrawService;

    @Autowired
    private IPanUserBalanceService panUserBalanceService;

    @Autowired
    private IndoPaymentService indoPaymentService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysUserService sysUserService;


    @Autowired
    private IPanAgentBalanceService panAgentBalanceService;

    @Autowired
    private IPanTransactionHistoryService IPanTransHistoryService;


    @Autowired
    private RedisCache redisCache;

    private static final Logger logger = LoggerFactory.getLogger(PanWithdrawController.class);


    /**
     * 查询提款列表
     */

    @GetMapping("getWithdrawHistory")
    public AjaxResult getWithdrawHistory(@RequestParam Long userId) {
        PanWithdraw panWithraw = new PanWithdraw();
        panWithraw.setUserId(userId);
        List<PanWithdraw> list = panWithdrawService.selectPanWithdrawList(panWithraw);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('system:withdraw:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanWithdraw panWithdraw) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panWithdraw.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panWithdraw.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panWithdraw.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanWithdraw> list = panWithdrawService.selectPanWithdrawList(panWithdraw);
        Long amountCount = panWithdrawService.selectPanWithdrawListCount(panWithdraw);
        return getDataTable(list, amountCount);
    }

    /**
     * 导出提款列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:export')")
    @Log(title = "提款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanWithdraw panWithdraw) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panWithdraw.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panWithdraw.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panWithdraw.setAgentId(currentUser.getUserId());
        }
        List<PanWithdraw> list = panWithdrawService.selectPanWithdrawList(panWithdraw);
        ExcelUtil<PanWithdraw> util = new ExcelUtil<PanWithdraw>(PanWithdraw.class);
        util.exportExcel(response, list, "提款数据");
    }

    /**
     * 获取提款详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:query')")
    @GetMapping(value = "/{withdrawId}")
    public AjaxResult getInfo(@PathVariable("withdrawId") Long withdrawId) {
        return success(panWithdrawService.selectPanWithdrawByWithdrawId(withdrawId));
    }

    /**
     * 新增提款
     */
    @ApiOperation("Withraw")
    @Log(title = "新增提款", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PanWithdrawCreate panWithdrawCreate) {
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            try {
                SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
                if (sysUser.getStatus().equals("1")) {
                    ajax.put("msg", "Account has been disabled");
                    return ajax;
                }
                if (sysUser.getIsWithdarw().equals("1")) {
                    ajax.put("msg", "Account No withdrawal permission");
                    return ajax;
                }
                if (iswithdrawalTime()) {
                    if (panWithdrawCreate.getAmount() == null) {
                        ajax.put("msg", "amount can not be empty, please try again later");
                        return ajax;
                    }
                    BigDecimal minAmount = new BigDecimal(sysConfigService.selectConfigByKey("min_withddrawal_amount")).multiply(new BigDecimal(100));
                    BigDecimal maxAmount = new BigDecimal(sysConfigService.selectConfigByKey("max_withdrawal_amount")).multiply(new BigDecimal(100));
                    if (panWithdrawCreate.getAmount().compareTo(maxAmount) > 0) {
                        ajax.put("msg", "Withdrawal amount cannot be greater than" + maxAmount);
                        return ajax;
                    }
                    if (minAmount.compareTo(panWithdrawCreate.getAmount()) > 0) {
                        ajax.put("msg", "Withdrawal amount cannot be less than" + minAmount);
                        return ajax;
                    }
                    PanUserBalance panUserBalance = panUserBalanceService.getPanUserBalanceByUserId(panWithdrawCreate.getUserId());
                    if (panWithdrawCreate.getAmount().compareTo(panUserBalance.getAvailableAmt()) > 0) {
                        ajax.put("msg", "Not enough balance");
                        return ajax;
                    }

                    PanWithdraw  todayWithdraw =  panWithdrawService.getTodayWithdrawCountByUserId(panWithdrawCreate.getUserId());
                    int maxNumWithdrawalsDay = Integer.parseInt(sysConfigService.selectConfigByKey("max_num_withdrawals_day"));
                    if(maxNumWithdrawalsDay-todayWithdraw.getWithdrawCount()<=0){
                        ajax.put("msg", "The maximum number of withdrawals per day is "+maxNumWithdrawalsDay);
                        return ajax;
                    }

                    String result = iTransService.createWithdraw(panWithdrawCreate);

                    if (result.equals(MessageStatus.SUCCESS)) {
                        ajax = AjaxResult.success();
                    } else {
                        ajax.put("msg", "Network exception, please try again later");
                    }
                } else {
                    ajax.put("msg", "The current time is not available for withdrawal!");
                }
            } catch (Exception e) {
                ajax.put("msg", "Network is abnormal, please try again!");
            }
        }
        return ajax;
    }

    /**
     * 修改提款
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:edit')")
    @Log(title = "修改提款", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanWithdraw panWithdraw) {
        logger.info("****用户提现修改 start****");
        AjaxResult ajax = AjaxResult.success();
        PanWithdraw currWithdraw = panWithdrawService.selectPanWithdrawByWithdrawId(panWithdraw.getWithdrawId());
        panWithdraw.setAmount(currWithdraw.getAmount());
        panWithdraw.setFee(currWithdraw.getFee());
        LoginUser currentUser = getLoginUser();
        panWithdraw.setUpdateBy(currentUser.getUsername());
        String key = redisCache.getCacheObject(panWithdraw.getRequestNo());
        if (null == key) {
            redisCache.setCacheObject(panWithdraw.getRequestNo(), panWithdraw.getRequestNo());
            redisCache.expire(panWithdraw.getRequestNo(), 300L, TimeUnit.SECONDS);
        } else {
            ajax = AjaxResult.error();
            ajax.put("msg", "请勿重复提交");
            return ajax;
        }
        if (panWithdraw.getStatus().equals(currWithdraw.getStatus())) {
            return ajax;
        } else if (panWithdraw.getStatus().equals(TransStatus.PENDING)) {
            return ajax;
        } else if (panWithdraw.getStatus().equals(TransStatus.PROGRESS) && currWithdraw.getStatus().equals(TransStatus.PENDING)) {
            // 审核通过
            try {
                BigDecimal withdrawAmt = panWithdraw.getAmount().add(panWithdraw.getFee());
                if (withdrawAmt.compareTo(panWithdrawService.getAgentBalance(currWithdraw.getAgentId())) > 0) {
                    ajax = AjaxResult.error();
                    ajax.put("msg", "余额不足，请充值后再尝试");
                    return ajax;
                }
                IndoWithdrawResult indoWithdrawResult = indoPaymentService.withdraw(
                        currWithdraw.getRequestNo(), "NGN", currWithdraw.getAmount(), currWithdraw.getBankCode(), currWithdraw.getBankName(),
                        currWithdraw.getCardNo(), currWithdraw.getBeneficiaryNo(), currWithdraw.getBeneficiaryName(),
                        currWithdraw.getBeneficiaryMobile(), currWithdraw.getBeneficiaryEmail(), panWithdraw.getDomain());

                if (StringUtils.isNotEmpty(indoWithdrawResult.getRespCode())) {
                    if (indoWithdrawResult.getRespCode().equals("0000") || indoWithdrawResult.getRespCode().equals("P000")) {
                        currWithdraw.setOrderNo(indoWithdrawResult.getOrderNo());
                        currWithdraw.setStatus(TransStatus.PROGRESS);
                        currWithdraw.setChannelId(indoWithdrawResult.getChannelId());
                        panWithdrawService.updatePanWithdraw(currWithdraw);
                    }else if(indoWithdrawResult.getRespCode().equals("0023")){
                        ajax = AjaxResult.error();
                        ajax.put("msg", "订单已审核，请勿重复提交");
                        return ajax;
                    } else {
                        if (currWithdraw.getStatus().equals(TransStatus.PENDING)) {
                            currWithdraw.setRespDesc(indoWithdrawResult.getRespDesc());
                            currWithdraw.setStatus(TransStatus.FAILED);
                            panWithdrawService.updatePanWithdraw(currWithdraw);
                        }
                    }
                }
                logger.info("****用户提现-审核通过-info:" + JSONObject.toJSONString(currWithdraw));

            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        } else if (panWithdraw.getStatus().equals(TransStatus.COMPLETED) && !currWithdraw.getStatus().equals(TransStatus.COMPLETED) && !currWithdraw.getStatus().equals(TransStatus.DECLINED)) {
            //手工完成
            BigDecimal withdrawAmt = panWithdraw.getAmount().add(panWithdraw.getFee());
            if (withdrawAmt.compareTo(panWithdrawService.getAgentBalance(currWithdraw.getAgentId())) > 0) {
                ajax = AjaxResult.error();
                ajax.put("msg", "余额不足，请充值后再尝试");
                return ajax;
            }
            iTransService.withdraw(panWithdraw);

        } else if (panWithdraw.getStatus().equals(TransStatus.DECLINED) && !currWithdraw.getStatus().equals(TransStatus.COMPLETED) && !currWithdraw.getStatus().equals(TransStatus.DECLINED) && !currWithdraw.getStatus().equals(TransStatus.PROGRESS)) {
            //审核不通过
            iTransService.withdraw(panWithdraw);
        }
        return ajax;
    }

    @Log(title = "提现批准", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:withdraw:changeStatus')")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody PanWithdraw panWithdraw) {
        logger.info("****用户提现-批准 start****");
        PanWithdraw curr = panWithdrawService.selectPanWithdrawByWithdrawId(panWithdraw.getWithdrawId());
        LoginUser currentUser = getLoginUser();
        panWithdraw.setUpdateBy(currentUser.getUsername());
        AjaxResult ajax = AjaxResult.success();
        String requestNo = String.valueOf(curr.getRequestNo());
        String key = redisCache.getCacheObject(requestNo);
        if (null == key) {
            redisCache.setCacheObject(requestNo, requestNo);
            redisCache.expire(requestNo, 10L, TimeUnit.MINUTES);
        } else {
            ajax = AjaxResult.error();
            ajax.put("msg", "请勿重复提交");
            return ajax;
        }
        if (!curr.getStatus().equals(TransStatus.PENDING)) {
            ajax = AjaxResult.error();
            ajax.put("msg", "请勿重复提交");
            return ajax;
        }
        try {
            BigDecimal withdrawAmt = curr.getAmount().add(curr.getFee());
            if (withdrawAmt.compareTo(panWithdrawService.getAgentBalance(curr.getAgentId())) > 0) {
                ajax = AjaxResult.error();
                ajax.put("msg", "余额不足，请充值后再尝试");
                return ajax;
            }
            IndoWithdrawResult indoWithdrawResult = indoPaymentService.withdraw(
                    curr.getRequestNo(), "NGN", curr.getAmount(), curr.getBankCode(), curr.getBankName(),
                    curr.getCardNo(), curr.getBeneficiaryNo(), curr.getBeneficiaryName(),
                    curr.getBeneficiaryMobile(), curr.getBeneficiaryEmail(), panWithdraw.getDomain());
            if (StringUtils.isNotEmpty(indoWithdrawResult.getRespCode())) {
                if (indoWithdrawResult.getRespCode().equals("0000") || indoWithdrawResult.getRespCode().equals("P000")) {
                    curr.setOrderNo(indoWithdrawResult.getOrderNo());
                    curr.setStatus(TransStatus.PROGRESS);
                    curr.setChannelId(indoWithdrawResult.getChannelId());
                    panWithdrawService.updatePanWithdraw(curr);
                }else if(indoWithdrawResult.getRespCode().equals("0023")){
                    ajax = AjaxResult.error();
                    ajax.put("msg", "订单已审核，请勿重复提交");
                    return ajax;
                } else {
                    if (curr.getStatus().equals(TransStatus.PENDING)) {
                        curr.setRespDesc(indoWithdrawResult.getRespDesc());
                        curr.setStatus(TransStatus.FAILED);
                        panWithdrawService.updatePanWithdraw(curr);
                    }
                }
            }
            logger.info("****用户提现-批准 withdraw:" + JSONObject.toJSONString(panWithdraw));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }

        return ajax;
    }

    @Log(title = "重试", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatusRetry")
    public AjaxResult changeStatusRetry(@RequestBody PanWithdraw panWithdraw) {

        AjaxResult ajax = AjaxResult.success();
        if (panWithdraw != null) {
            PanWithdraw curr = panWithdrawService.selectPanWithdrawByWithdrawId(panWithdraw.getWithdrawId());

            BigDecimal withdrawAmt = curr.getAmount().add(curr.getFee());
            if (withdrawAmt.compareTo(panWithdrawService.getAgentBalance(curr.getAgentId())) > 0) {
                ajax = AjaxResult.error();
                ajax.put("msg", "余额不足，请充值后再尝试");
                return ajax;
            }
            String withdrawId = String.valueOf(panWithdraw.getWithdrawId());
            String key = redisCache.getCacheObject(withdrawId);
            if (null == key) {
                redisCache.setCacheObject(withdrawId, withdrawId);
                redisCache.expire(withdrawId, 20L, TimeUnit.MINUTES);
            } else {
                ajax = AjaxResult.error();
                ajax.put("msg", "请勿重复提交");
                return ajax;
            }

            if (!curr.getStatus().equals(TransStatus.FAILED)) {
                ajax = AjaxResult.error();
                ajax.put("msg", "处理中，请勿再次重试");
                return ajax;
            }

            if (curr.getRequestNo().contains("WRR")) {
                ajax = AjaxResult.error();
                ajax.put("msg", "此订单多次异常，请勿再次重试");
                return ajax;
            }
            String requestNo = curr.getRequestNo().replace("W", "WR");
            LoginUser currentUser = getLoginUser();
            panWithdraw.setUpdateBy(currentUser.getUsername());
            panWithdraw.setRequestNo(requestNo);
            panWithdrawService.updatePanWithdraw(panWithdraw);


            IndoWithdrawResult indoWithdrawResult = indoPaymentService.withdraw(
                    requestNo, "NGN", curr.getAmount(), curr.getBankCode(), curr.getBankName(),
                    curr.getCardNo(), curr.getBeneficiaryNo(), curr.getBeneficiaryName(),
                    curr.getBeneficiaryMobile(), curr.getBeneficiaryEmail(), panWithdraw.getDomain());

            if (StringUtils.isNotEmpty(indoWithdrawResult.getRespCode())) {
                if (indoWithdrawResult.getRespCode().equals("0000") || indoWithdrawResult.getRespCode().equals("P000")) {
                    panWithdraw.setOrderNo(indoWithdrawResult.getOrderNo());
                    panWithdraw.setStatus(TransStatus.PROGRESS);
                    panWithdraw.setChannelId(indoWithdrawResult.getChannelId());
                    panWithdrawService.updatePanWithdraw(panWithdraw);
                } else if(indoWithdrawResult.getRespCode().equals("0023")){
                    ajax = AjaxResult.error();
                    ajax.put("msg", "订单已审核，请勿重复提交");
                    return ajax;
                }else {
                    if (curr.getStatus().equals(TransStatus.FAILED)) {
                        panWithdraw.setRespDesc(indoWithdrawResult.getRespDesc());
                        panWithdrawService.updatePanWithdraw(panWithdraw);
                    }
                }
            }
            logger.info("****用户提现重试完成 changeStatusRetry withdraw:" + JSONObject.toJSONString(panWithdraw));

        }
        return ajax;
    }

    /***
     * 提现批量审核
     * @param ids
     * @return
     */
    @Log(title = "提现批量审核", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:withdraw:postBatchWithdraw')")
    @RequestMapping(value = "/postBatchWithdraw")
    @ResponseBody
    public AjaxResult postBatchWithdraw(@RequestBody JSONObject ids) {
        logger.info("****用户提现批量通过 JsonObject:" + JSONObject.toJSONString(ids));
        String withdrawIds = ids.get("ids").toString();
        logger.info("****用户提现批量通过 withdrawIds:" + withdrawIds);
        AjaxResult ajax = AjaxResult.success();
        /**
         * 判断数据是否大于一条
         */
        StringJoiner sj = new StringJoiner(",");

        if (withdrawIds.contains(",")) {
            String withIds = withdrawIds.substring(1, withdrawIds.length() - 1);
            String[] strs = withIds.split(",");
            PanWithdraw currWithdraw = panWithdrawService.selectPanWithdrawByWithdrawId(Long.parseLong(strs[0].trim()));
            for (int i = 0, len = strs.length; i < len; i++) {
                sj.add(Long.parseLong(strs[i].trim()) + "");
            }
            Long withdrawAmt = panWithdrawService.getUserWithdrawInfoByIds(sj.toString());
            if (new BigDecimal(withdrawAmt).compareTo(panWithdrawService.getAgentBalance(currWithdraw.getAgentId())) > 0) {
                ajax = AjaxResult.error();
                ajax.put("msg", "余额不足，请充值后再尝试");
                return ajax;
            } else {
                for (int i = 0, len = strs.length; i < len; i++) {
                    widhdrawhandle(Long.parseLong(strs[i].trim()));
                }
            }
        } else {
            String withId = withdrawIds.substring(1, withdrawIds.length() - 1);
            PanWithdraw currWithdraw = panWithdrawService.selectPanWithdrawByWithdrawId(Long.parseLong(withId.trim()));
            BigDecimal withdrawAmt = currWithdraw.getAmount().add(currWithdraw.getFee());
            if (withdrawAmt.compareTo(panWithdrawService.getAgentBalance(currWithdraw.getAgentId())) > 0) {
                ajax = AjaxResult.error();
                ajax.put("msg", "余额不足，请充值后再尝试");
                return ajax;
            }
            widhdrawhandle(Long.parseLong(withId.trim()));
        }
        return ajax;
    }

    /**
     * @param withId
     */
    public void widhdrawhandle(Long withId) {

        try {
            PanWithdraw curr = panWithdrawService.selectPanWithdrawByWithdrawId(withId);
            if (curr.getStatus().equals(TransStatus.PENDING)) {
                IndoWithdrawResult indoWithdrawResult = indoPaymentService.withdraw(
                        curr.getRequestNo(), "NGN", curr.getAmount(), curr.getBankCode(), curr.getBankName(),
                        curr.getCardNo(), curr.getBeneficiaryNo(), curr.getBeneficiaryName(),
                        curr.getBeneficiaryMobile(), curr.getBeneficiaryEmail(), curr.getDomain());

                if (StringUtils.isNotEmpty(indoWithdrawResult.getRespCode())) {
                    if (indoWithdrawResult.getRespCode().equals("0000") || indoWithdrawResult.getRespCode().equals("P000")) {
                        curr.setOrderNo(indoWithdrawResult.getOrderNo());
                        curr.setStatus(TransStatus.PROGRESS);
                        curr.setChannelId(indoWithdrawResult.getChannelId());
                        panWithdrawService.updatePanWithdraw(curr);
                    }else if(indoWithdrawResult.getRespCode().equals("0023")){
                        logger.info("****订单已审核，请勿重复提交******");
                    }  else {
                        if (curr.getStatus().equals(TransStatus.PENDING)) {
                            curr.setRespDesc(indoWithdrawResult.getRespDesc());
                            curr.setStatus(TransStatus.FAILED);
                            panWithdrawService.updatePanWithdraw(curr);
                        }
                    }
                }
            }
            logger.info("****用户提现批量审核 withdraw:" + JSONObject.toJSONString(curr));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }

    }

    /***
     * 判断当前时间是否可以提现
     * @return
     */
    public boolean iswithdrawalTime() {
        String withdrawal_working_days = sysConfigService.selectConfigByKey("withdrawal_working_days");
        String withdrawal_start_time = sysConfigService.selectConfigByKey("withdrawal_start_time");
        String withdrawal_end_time = sysConfigService.selectConfigByKey("withdrawal_end_time");
        boolean found = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String index = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        /**
         * 判断当前日期是否可以提现
         */
        logger.info("****提现判断日期=当前时间时星期" + index);

        if (withdrawal_working_days.contains(index)) {
            String today = DateUtils.getDate();
            StringBuffer stratDateTime = new StringBuffer(today);
            stratDateTime.append(" ").append(withdrawal_start_time).append(":00");
            StringBuffer endDateTime = new StringBuffer(today);
            endDateTime.append(" ").append(withdrawal_end_time).append(":00");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date stratWithdrawDateTime = sdf.parse(stratDateTime.toString());
                Date endWithdrawDateTime = sdf.parse(endDateTime.toString());
                Date currTime = sdf.parse(DateUtils.getTime());
                logger.info("***提现判断时间iswithdrawalTime：{},endDateTime:{},currTime{}", stratWithdrawDateTime, endWithdrawDateTime, currTime);
                if (endWithdrawDateTime.compareTo(currTime) > 0 && currTime.compareTo(stratWithdrawDateTime) > 0) {
                    found = true;
                } else {
                    found = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            found = false;
        }
        return found;

    }

}
