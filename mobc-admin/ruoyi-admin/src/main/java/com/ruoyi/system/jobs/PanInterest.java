package com.ruoyi.system.jobs;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.IsIncome;
import com.ruoyi.common.enums.TaskStatus;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component("PanInterest")
public class PanInterest {
    @Autowired
    private IPanUserBalanceService iPanUserBalanceService;

    @Autowired
    private IPurchaseService iPurchaseService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IPanTransactionHistoryService iPanTransactionHistoryService;

    @Autowired
    private ITaskRecordService iTaskRecordService;

    @Autowired
    private ISysConfigService iSysConfigService;

    @Autowired
    private IPanLotteryService lotteryService;

    @Autowired
    private IPanUserAssetService iPanUserAssetService;

    private static final Logger log = LoggerFactory.getLogger(PanInterest.class);

    public void ryNoParams() {
        log.info("****任务调度-ryNoParams Strat****");
        /*
        查看当前任务Id
         */
        PanTaskRecord jobInfo = iTaskRecordService.selectTaskJobByTarget("PanInterest.ryNoParams");
        if (jobInfo != null) {
            PanTaskRecord taskRecord = new PanTaskRecord();
            String todayDate = DateUtils.getSomeDayLaterDate(0);
            taskRecord.setTaskDate(todayDate);
            taskRecord.setInvokeTarget("PanInterest.ryNoParams");
            List<PanTaskRecord> taskList = iTaskRecordService.selectTaskList(taskRecord);
            log.info("****任务调度-TaskList-Size:" + taskList.size());
            if (taskList.size() > 0) {
                log.info("****任务调度今天已经执行****");
            } else {
                String rid = DateUtils.createOrderId("T");
                taskRecord.setrId(rid);
                taskRecord.setJobId(jobInfo.getJobId());
                taskRecord.setJobName(jobInfo.getJobName());
                taskRecord.setTaskDate(todayDate);
                log.info("****任务调度 Task Record:" + JSONObject.toJSONString(taskRecord));
                iTaskRecordService.insertTaskJobRecord(taskRecord);
                String vipLevelAmt = iSysConfigService.selectConfigByKey("VIP_LEVEL_AMT");
                JSONObject vipLevelObject = JSON.parseObject(vipLevelAmt);

                /**
                 * 优惠卷过期
                 */
                List<PanDrawsDetail> drawsList = lotteryService.getVoucherListByJob();
                if (drawsList.size() > 0) {
                    lotteryService.updateVoucherEndDate();
                }

                /**
                 * 计算购买产品利息
                 */
                List<Purchase> purchaseList = iPurchaseService.selectPurchaseJobList();
                log.info("****任务调度产品列表Size：" + purchaseList.size());
                if (purchaseList.size() > 0) {
                    for (int i = 0; i < purchaseList.size(); i++) {
                        try {
                            generatePurchaseInterest(purchaseList.get(i), todayDate, vipLevelObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                /**
                 * 计算增值宝利息
                 */
                List<PanUserAsset> userAssetList = iPanUserAssetService.selectUserAssetListByJob();
                Double treasureRate = Double.parseDouble(iSysConfigService.selectConfigByKey("treasure_interest_rate").toString());
                log.info("****增值宝利率:" + treasureRate);
                if (userAssetList.size() > 0) {
                    for (int i = 0; i < userAssetList.size(); i++) {
                        try {
                            generateTreasureInterest(userAssetList.get(i), treasureRate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                taskRecord.setTaskStatus(TaskStatus.SUCCESS.toString());
                log.info("****任务调度-TaskRecord-Info:" + JSONObject.toJSONString(taskRecord));
                iTaskRecordService.updateTaskJobRecord(taskRecord);
            }
        }
        log.info("****任务调度-End****");
    }

    /***
     * 产品利息处理
     */
    @Transactional
    public void generatePurchaseInterest(Purchase currPurchase, String todayDate, JSONObject vipLevelObject) {
        log.info("****产品利息处理开始****" + currPurchase.getBuyer());
        PanUserBalance userBalance = iPanUserBalanceService.getUserBalanceByUserId(currPurchase.getBuyer());
        log.info("****任务调度产品利息处理更新前余额-userBalance:" + JSONObject.toJSONString(userBalance));
        if (userBalance.getIsRebate().equals("0")) {
            //今日收益
            BigDecimal dailyReward = new BigDecimal(currPurchase.getDailyInterest()).multiply(currPurchase.getAmount()).divide(new BigDecimal(100));
            BigDecimal beforeBalance = userBalance.getBalance();
            BigDecimal afterBalance = userBalance.getBalance().add(dailyReward);
            // 更新今日收益
            BigDecimal currTotalInterest  = currPurchase.getTotalInterest().add(dailyReward);
            currPurchase.setTotalInterest(currTotalInterest);

            PanTransactionHistory transHistory = new PanTransactionHistory();
            transHistory.setAmount(dailyReward);
            transHistory.setUserId(currPurchase.getBuyer());
            transHistory.setOrUserId(currPurchase.getBuyer());
            transHistory.setOrOrderId(currPurchase.getOrderNo());
            transHistory.setTransactionType(TransType.Product_Daily_Interest.toString());
            transHistory.setIsIncome(IsIncome.Y.toString());
            transHistory.setBillType(BillType.IN.toString());
            transHistory.setRemark(currPurchase.getProductName() + "日收益");
            transHistory.setAmountBefore(beforeBalance);
            transHistory.setAmountAfter(afterBalance);

            log.info("****任务调度-TransInfo:" + JSONObject.toJSONString(transHistory));
            iPanTransactionHistoryService.insertPanTransactionHistory(transHistory);
            BigDecimal currBalance = userBalance.getBalance().add(dailyReward);
            BigDecimal currAvailableAmt = userBalance.getAvailableAmt().add(dailyReward);
            userBalance.setBalance(currBalance);
            userBalance.setAvailableAmt(currAvailableAmt);
            String isInit = vipLevelObject.get("is_init").toString();
            iPurchaseService.updatePurchase(currPurchase);
            if (isInit.equals("N")) {
                Purchase purchaseAmount = iPurchaseService.selectPurchaseAmtByVip(currPurchase.getBuyer());
                SysUser userBean = new SysUser();
                userBean.setVipLevel(DateUtils.getVipLevel(vipLevelObject, purchaseAmount.getAmount()));
                userBean.setUserId(currPurchase.getBuyer());
                userService.updateUser(userBean);
                log.info("****任务调度-userVip:" + JSONObject.toJSONString(userBean) + "purchaseAmt:" + purchaseAmount.getAmount());
            }
        }
        if (currPurchase.getEndDate().equals(todayDate) && currPurchase.getPayBack().equals("0")) {
            log.info("****任务调度 本金返还 Start****");
            //返息完成
            currPurchase.setPayBack("1");
            PanTransactionHistory transHistoryToday = new PanTransactionHistory();
            BigDecimal amount = currPurchase.getAmount();
            BigDecimal beforeReturnBalance = userBalance.getBalance();
            BigDecimal afterReturnBalance = userBalance.getBalance().add(amount);

            transHistoryToday.setOrUserId(currPurchase.getBuyer());
            transHistoryToday.setOrOrderId(currPurchase.getOrderNo());
            transHistoryToday.setUserId(currPurchase.getBuyer());
            transHistoryToday.setAmount(amount);
            transHistoryToday.setTransactionType(TransType.Principal_Return.toString());
            transHistoryToday.setIsIncome(IsIncome.N.toString());
            transHistoryToday.setBillType(BillType.IN.toString());
            transHistoryToday.setRemark(currPurchase.getProductName() + "本金返还");
            transHistoryToday.setAmountBefore(beforeReturnBalance);
            transHistoryToday.setAmountAfter(afterReturnBalance);

            log.info("****任务调度 Principal Return Trans Info:" + JSONObject.toJSONString(transHistoryToday));
            iPanTransactionHistoryService.insertPanTransactionHistory(transHistoryToday);
            //可提现增加
            userBalance.setAvailableAmt(userBalance.getAvailableAmt().add(amount));
            //减少基金金额
            userBalance.setLockBalance(userBalance.getLockBalance().subtract(amount));
            iPurchaseService.updatePurchase(currPurchase);

            Purchase purchaseAmount = iPurchaseService.selectPurchaseAmtByVip(currPurchase.getBuyer());
            SysUser userBean = new SysUser();
            userBean.setVipLevel(DateUtils.getVipLevel(vipLevelObject, purchaseAmount.getAmount()));
            userBean.setUserId(currPurchase.getBuyer());
            userService.updateUser(userBean);
            log.info("****任务调度-userVip:" + JSONObject.toJSONString(userBean) + "purchaseAmt:" + purchaseAmount.getAmount());

        }

        log.info("****任务调度-UserBalance:" + JSONObject.toJSONString(userBalance));
        iPanUserBalanceService.updatePanUserBalance(userBalance);

    }

    /***
     * 增值宝收益处理
     */
    @Transactional
    public void generateTreasureInterest(PanUserAsset curr, Double treasureRate) {
        log.info("****增值宝收益任务开始****" + curr.getUserId());
        if (curr.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            PanUserBalance panUserBalance = iPanUserBalanceService.getUserBalanceByUserId(curr.getUserId());
            log.info("****任务调度增值宝收益处理前余额-userBalance:" + JSONObject.toJSONString(panUserBalance));
            // 增值宝今日收益
            BigDecimal todyReward = new BigDecimal(treasureRate).multiply(curr.getBalance()).divide(new BigDecimal(100));

            BigDecimal beforeBalance = panUserBalance.getBalance();
            BigDecimal afterBalance = panUserBalance.getBalance();

            PanTransactionHistory transInfo = new PanTransactionHistory();
            transInfo.setAmount(todyReward);
            transInfo.setOrUserId(curr.getUserId());
            transInfo.setUserId(curr.getUserId());
            transInfo.setTransactionType(TransType.Treasure_Reward.toString());
            transInfo.setIsIncome(IsIncome.Y.toString());
            transInfo.setRemark("增值宝利息");
            transInfo.setBillType(BillType.BAL.toString());
            transInfo.setAmountBefore(beforeBalance);
            transInfo.setAmountAfter(afterBalance);

            log.info("****记录增值宝收益交易记录:" + JSONObject.toJSONString(transInfo));
            iPanTransactionHistoryService.insertPanTransactionHistory(transInfo);

            panUserBalance.setBalance(panUserBalance.getBalance().add(todyReward));
            panUserBalance.setAssetBalance(panUserBalance.getAssetBalance().add(todyReward));

            log.info("****更新余额 :{}", JSONObject.toJSONString(panUserBalance));
            iPanUserBalanceService.updatePanUserBalance(panUserBalance);

            log.info("****更新增值宝 :{}", JSONObject.toJSONString(curr));
            curr.setBalance(curr.getBalance().add(todyReward));

            iPanUserAssetService.updatePanUserAsset(curr);

        }
        log.info("****增值宝收益任务结束****");
    }

}
