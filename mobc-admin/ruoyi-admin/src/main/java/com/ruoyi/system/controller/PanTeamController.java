package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品Controller
 *
 * @author ruoyi
 * @date 2023-03-22
 */

@Api
@RestController
@RequestMapping("/system/team")
public class PanTeamController extends BaseController {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IPanTransactionHistoryService panTransactionHistoryService;

    @Autowired
    private IPanRechargeService panRechargeService;

    @Autowired
    private IPanProductService panProductService;

    @Autowired
    private IPanWithdrawService panWithdrawService;

    @Autowired
    private IPanUserBalanceService panUserBalanceService;


    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysConfigService sysConfigService;

    @ApiOperation("overview")
    @GetMapping("/overview")
    public AjaxResult getTeamOverview(@RequestParam Long userId) {

        TeamOverview requestTeam =  panTransactionHistoryService.getTeamIncomeInfo(userId);
        requestTeam.setUserId(userId);
        //        //今日新增团队成员
        requestTeam.setTodayChildCount(sysUserService.getTodayChildCount(userId));
        requestTeam.setChildTotalIncome(panTransactionHistoryService.getChildTotalIncome(userId));
        //今日团队总收益
        requestTeam.setDailyIncome(panTransactionHistoryService.getChildDailyIncome(userId));
        requestTeam.setRewardAmt(panUserBalanceService.getPanUserBalanceByUserId(userId).getRewardAmt());
        List<SysConfig> configList = sysConfigService.selectConfigListByKey();
        for(SysConfig conf : configList){
            if(conf.getConfigKey().equals("commission_A_share_ratio") ){
                requestTeam.setChildCrowdRatio(Double.parseDouble(conf.getConfigValue()));
            }else if(conf.getConfigKey().equals("commission_B_share_ratio") ){
                requestTeam.setGrandCrowdRatio(Double.parseDouble(conf.getConfigValue()));
            }else  if(conf.getConfigKey().equals("commission_C_share_ratio") ){
                requestTeam.setGreatGrandCrowdRatio(Double.parseDouble(conf.getConfigValue()));
            }else if(conf.getConfigKey().equals("first_purchase_commission_A_ratio") ){
                requestTeam.setFirstPurchaseCommissionRatio(Double.parseDouble(conf.getConfigValue()));
            }else  if(conf.getConfigKey().equals("purchase_treasure_rate") ){
                requestTeam.setPurchaseTreasureRate(Double.parseDouble(conf.getConfigValue()));
            }
        }

//        PanProduct product = panProductService.selectPanProductByName("Reward Product");
//        requestTeam.setTotalRechargeCount(panRechargeService.setTotalRechargeCountByUser(userId));
//        requestTeam.setTotalWithdrawCount(panWithdrawService.getTotalWithdrawCountByUser(userId));
//        requestTeam.setRewardProductDailyInterest(product.getDailyInterest());

        AjaxResult ajax = AjaxResult.success();
        ajax.put("teamOverview", requestTeam);
        return ajax;
    }

    @GetMapping("/incomeOverview")
    public AjaxResult getTeamIncomeOverview(@RequestParam Long userId, @RequestParam Long teamLevel) {
        AjaxResult ajax = AjaxResult.success();
        PanTeamIncomeFilter teamIncomeFilter = new PanTeamIncomeFilter();
        teamIncomeFilter.setUserId(userId);
        List<TeamIncomeOverview> teamIncomeOverview = new ArrayList<TeamIncomeOverview>();
        if (teamLevel == 0) {
            teamIncomeOverview = panTransactionHistoryService.getChildIncomeOverview(userId);
        } else if (teamLevel == 1) {
            teamIncomeOverview = panTransactionHistoryService.getGrandIncomOverview(userId);
        } else if (teamLevel == 2) {
            teamIncomeOverview = panTransactionHistoryService.getGreatGrandIncomOverview(userId);
        }
        ajax.put("teamIncomeOverview", teamIncomeOverview);
        return ajax;
    }

    /**
     * 用户     userName,
     * 金额     amount,
     * 交易类型  transactionType,
     * 交易时间  transactionDate
     */
    @GetMapping("/rewardDetail")
    public AjaxResult getRewardHistory(@RequestParam Long userId, @RequestParam Long teamLevel) {

        AjaxResult ajax = AjaxResult.success();
        List<PanTransactionHistory> teamIncomeList = new ArrayList<PanTransactionHistory>();

        if (teamLevel == 0) {
            teamIncomeList = panTransactionHistoryService.getChildIncomeListByUser(userId);
        } else if (teamLevel == 1) {
            teamIncomeList = panTransactionHistoryService.getGrandIncomListByUser(userId);
        } else if (teamLevel == 2) {
            teamIncomeList = panTransactionHistoryService.getGreatGrandIncomListByUser(userId);
        }
        ajax.put("teamIncomeList", teamIncomeList);
        return ajax;
    }


    /**
     * 用户团队充值数据
     */
    @PreAuthorize("@ss.hasPermi('system:team:rechargeList')")
    @GetMapping("/rechargeList")
    public TableDataInfo rechargeList(TeamRechargeView teamTreatment) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            teamTreatment.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            teamTreatment.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            teamTreatment.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<TeamRechargeView> list = panTransactionHistoryService.selectTeamRechargeListByUser(teamTreatment);
        return getDataTable(list);
    }

    /**
     * 业务员团队充值数据
     */
    @PreAuthorize("@ss.hasPermi('system:team:rechargeListByClerk')")
    @GetMapping("/rechargeListByClerk")
    public TableDataInfo rechargeListByClerk(TeamRechargeView teamTreatment) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("02")) {
            teamTreatment.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            teamTreatment.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<TeamRechargeView> list = panTransactionHistoryService.selectTeamRechargeListByClerk(teamTreatment);
        return getDataTable(list);
    }


    @RequestMapping("/getUserTeamTrans")
    public AjaxResult getUserTeamTrans(@RequestParam Long userId) {
        AjaxResult ajax = AjaxResult.success();
        TeamRechargeView userTeamTrans = panTransactionHistoryService.selectTeamTransByUserId(userId);
        ajax.put("data", userTeamTrans);
        return ajax;
    }


    /**
     * 业务员团队交易数据
     */
    @PreAuthorize("@ss.hasPermi('system:team:getClerkTeamTransInfo')")
    @GetMapping("/getClerkTeamTransInfo")
    public TableDataInfo getClerkTeamTransInfo(PanTransactionHistory transInfo) {
        startPage();
        List<PanTransactionHistory> list = new ArrayList<PanTransactionHistory>();
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            if (StringUtils.isBlank(transInfo.getTransactionType())) {
                transInfo.setTransactionType(TransType.Register_Reward.name());
            }
            if (StringUtils.isBlank(transInfo.getLevel())) {
                transInfo.setParentId(currentUser.getUserId());
            } else if (transInfo.getLevel().equals("A")) {
                transInfo.setParentId(currentUser.getUserId());
            } else if (transInfo.getLevel().equals("B")) {
                transInfo.setGrandId(currentUser.getUserId());
            } else if (transInfo.getLevel().equals("C")) {
                transInfo.setGreatGrandId(currentUser.getUserId());
            }
            list = panTransactionHistoryService.getClerkTeamTransInfo(transInfo);
        }
        return getDataTable(list);
    }

    @ApiOperation("Export")
    @Log(title = "业务员团队交易", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:team:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanTransactionHistory transInfo) {
        List<PanTransactionHistory> list = new ArrayList<PanTransactionHistory>();;
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            if (StringUtils.isBlank(transInfo.getTransactionType())) {
                transInfo.setTransactionType(TransType.Register_Reward.name());
            }
            if (StringUtils.isBlank(transInfo.getLevel())) {
                transInfo.setParentId(currentUser.getUserId());
            } else if (transInfo.getLevel().equals("A")) {
                transInfo.setParentId(currentUser.getUserId());
            } else if (transInfo.getLevel().equals("B")) {
                transInfo.setGrandId(currentUser.getUserId());
            } else if (transInfo.getLevel().equals("C")) {
                transInfo.setGreatGrandId(currentUser.getUserId());
            }
            list = panTransactionHistoryService.getClerkTeamTransInfo(transInfo);
        }

        ExcelUtil<PanTransactionHistory> util = new ExcelUtil<PanTransactionHistory>(PanTransactionHistory.class);
        util.exportExcel(response, list, "业务员团队交易");
    }

    /**
     * 返佣余额转出
     */
    @ApiOperation("Reward Transfer Out")
    @Log(title = "基金收益余额转出", businessType = BusinessType.UPDATE)
    @PostMapping("/rewardTransferOut")
    public AjaxResult rewardTransferOut(@RequestBody PanUserBalance rewardBalance) {
        logger.info("********PanTeamController rewardTransferOut rewardBalance Info:" + JSONObject.toJSONString(rewardBalance));
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
            if (sysUser.getStatus().equals("1")) {
                ajax.put("msg", "Account has been disabled");
                return ajax;
            }
            PanUserBalance userBalance = panUserBalanceService.getPanUserBalanceByUserId(rewardBalance.getUserId());
            //判断交易金额
            if (rewardBalance.getAmount().compareTo(userBalance.getRewardAmt()) > 0) {
                ajax.put("msg", "The user does not have enough balance");
            }
            try {
                String result = iTransService.rewardTransferOut(rewardBalance);
                if (result.equals(MessageStatus.SUCCESS)) {
                    ajax = AjaxResult.success();
                } else {
                    ajax.put("msg", "Network exception, please try again later");
                }
            } catch (Exception e) {
                ajax.put("msg", "Network exception, please try again later");
            }
        }
        return ajax;
    }
}
