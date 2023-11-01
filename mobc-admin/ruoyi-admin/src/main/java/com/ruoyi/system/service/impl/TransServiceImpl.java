package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.IsIncome;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ITransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by HFY on 2023/5/14
 */
@Service
public class TransServiceImpl implements ITransService {

    @Autowired
    private PanProductMapper panProductMapper;

    @Autowired
    private PanRechargeMapper panRechargeMapper;

    @Autowired
    private PanWithdrawMapper panWithdrawMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PanTransactionHistoryMapper transHistoryMapper;

    @Autowired
    private PanUserBalanceMapper userBalanceMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private PanBindCardMapper panBindCardMapper;

    @Autowired
    private PanAgentBalanceMapper agentBalanceMapper;

    @Autowired
    private PanUserAssetMapper userAssetMapper;

    @Autowired
    private PanSignRecordMapper panSignRecordMapper;

    @Autowired
    private PanLotteryMapper panLotteryMapper;

    private static final Logger logger = LoggerFactory.getLogger(TransServiceImpl.class);

    /**
     * 新增提现
     *
     * @param createWithdraw
     * @return
     */
    @Override
    @Transactional
    public String createWithdraw(PanWithdrawCreate createWithdraw) {
        logger.info("****交易管理-新增提现-Start****");
        String msg = MessageStatus.SUCCESS;
        Long outFeeRate = Long.parseLong(sysConfigService.selectConfigByKey("withdrawal_fee"));
        PanBindCard panBindCard = panBindCardMapper.selectPanBindCardByCardId(createWithdraw.getCardId());
        PanUserBalance panUserBalance = userBalanceMapper.getPanUserBalanceByUserId(createWithdraw.getUserId());

        PanWithdraw panWithdraw = new PanWithdraw();
        String requestNo = DateUtils.createOrderId("W");
        panWithdraw.setUserId(createWithdraw.getUserId());
        panWithdraw.setRequestNo(requestNo);
        BigDecimal bd = createWithdraw.getAmount().multiply(new BigDecimal(100).subtract(new BigDecimal(outFeeRate))).divide(new BigDecimal(10000));
        bd = bd.setScale(0, BigDecimal.ROUND_DOWN);
        panWithdraw.setAmount(bd.multiply(new BigDecimal(100)));
        panWithdraw.setFee(createWithdraw.getAmount().subtract(panWithdraw.getAmount()));
        panWithdraw.setCardId(createWithdraw.getCardId());
        panWithdraw.setBankCode(panBindCard.getBankCode());
        panWithdraw.setBankName(panBindCard.getBankName());
        panWithdraw.setCardName(panBindCard.getName());
        panWithdraw.setCardNo(panBindCard.getCardNo());
        panWithdraw.setBeneficiaryNo("0000");
        panWithdraw.setBeneficiaryName(panBindCard.getName());
        panWithdraw.setBeneficiaryMobile(panBindCard.getMobile());
        panWithdraw.setStatus(TransStatus.PENDING);

        panUserBalance.setAvailableAmt(panUserBalance.getAvailableAmt().subtract(createWithdraw.getAmount()));
        panUserBalance.setTransitAmt(panUserBalance.getTransitAmt().add(createWithdraw.getAmount()));

        logger.info("****交易管理-新增提现-userBalance:" + JSONObject.toJSONString(panUserBalance));
        userBalanceMapper.updatePanUserBalance(panUserBalance);

        logger.info("****交易管理-新增提现-Withdraw:" + JSONObject.toJSONString(panWithdraw));
        int i = panWithdrawMapper.insertPanWithdraw(panWithdraw);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }


    /**
     * 购买产品账务处理
     *
     * @param purchaseBalance
     * @return
     */
    @Override
    @Transactional
    public String purchaseBalance(Purchase purchaseBean) {
        logger.info("****交易管理-余额购买产品-Start********");
        String result = MessageStatus.SUCCESS;
        if (purchaseBean.getStatus().equals(TransStatus.SUCCESS)) {
            purchaseBean.setBeginDate(DateUtils.getTomorrowDate());
            purchaseBean.setEndDate(DateUtils.getSomeDayLaterDate(purchaseBean.getCycle()));
            PanUserBalance panUserBalance = userBalanceMapper.getPanUserBalanceByUserId(purchaseBean.getBuyer());

            if(purchaseBean.getDrawsId()>0 && purchaseBean.getVoucherAmount().compareTo(BigDecimal.ZERO)>0){
                BigDecimal cashBefore = panUserBalance.getBalance();
                BigDecimal cash  = purchaseBean.getAmount().subtract(purchaseBean.getVoucherAmount());
                BigDecimal cashAfter = panUserBalance.getBalance().subtract(cash);
                PanTransactionHistory cashTrans = new PanTransactionHistory();
                cashTrans.setAmount(cash);
                cashTrans.setOrOrderId(purchaseBean.getOrderNo());
                cashTrans.setOrUserId(purchaseBean.getBuyer());
                cashTrans.setUserId(purchaseBean.getBuyer());
                cashTrans.setTransactionType(TransType.Buy_Product_Balance.toString());
                cashTrans.setIsIncome(IsIncome.N.toString());
                cashTrans.setRemark("余额购买产品");
                cashTrans.setBillType(BillType.OUT.toString());
                cashTrans.setAmountBefore(cashBefore);
                cashTrans.setAmountAfter(cashAfter);

                logger.info("****交易管理-余额购买产品-TransInfo:{}", JSONObject.toJSONString(cashTrans));
                transHistoryMapper.insertPanTransactionHistory(cashTrans);


                BigDecimal voucherBefore = cashAfter;
                BigDecimal voucherAfter = cashAfter;
                PanTransactionHistory voucherTrans = new PanTransactionHistory();
                voucherTrans.setAmount(purchaseBean.getVoucherAmount());
                voucherTrans.setOrOrderId(purchaseBean.getOrderNo());
                voucherTrans.setOrUserId(purchaseBean.getBuyer());
                voucherTrans.setUserId(purchaseBean.getBuyer());
                voucherTrans.setTransactionType(TransType.Coupon_Deals.toString());
                voucherTrans.setIsIncome(IsIncome.N.toString());
                voucherTrans.setRemark("使用优惠券");
                voucherTrans.setBillType(BillType.IN.toString());
                voucherTrans.setAmountBefore(voucherBefore);
                voucherTrans.setAmountAfter(voucherAfter);

                logger.info("****交易管理-使用优惠券-TransInfo:{}", JSONObject.toJSONString(voucherTrans));
                transHistoryMapper.insertPanTransactionHistory(voucherTrans);
                PanDrawsDetail drawsDetail =new PanDrawsDetail();
                drawsDetail.setStatus(PrizeStatus.COMPLETED.trim());
                drawsDetail.setId(purchaseBean.getDrawsId());
                logger.info("****交易管理-更新优惠券:{}", JSONObject.toJSONString(drawsDetail));
                panLotteryMapper.updateDrawsDetail(drawsDetail);

                panUserBalance.setBalance(panUserBalance.getBalance().add(purchaseBean.getVoucherAmount()));
                //购买成功，可用余额减少
                panUserBalance.setAvailableAmt(panUserBalance.getAvailableAmt().subtract(purchaseBean.getAmount()).add(purchaseBean.getVoucherAmount()));
                //购买成功，锁定金额增加，
                panUserBalance.setLockBalance(panUserBalance.getLockBalance().add(purchaseBean.getAmount()));
                logger.info("****交易管理-余额优惠卷购买产品-UserBalance:{},Voucher:{}", JSONObject.toJSONString(panUserBalance),purchaseBean.getVoucherAmount());
            }else{
                BigDecimal balanceBefore = panUserBalance.getBalance();
                BigDecimal balanceAfter = panUserBalance.getBalance().subtract(purchaseBean.getAmount());
                PanTransactionHistory panTransactionHistory = new PanTransactionHistory();
                panTransactionHistory.setAmount(purchaseBean.getAmount());
                panTransactionHistory.setOrOrderId(purchaseBean.getOrderNo());
                panTransactionHistory.setOrUserId(purchaseBean.getBuyer());
                panTransactionHistory.setUserId(purchaseBean.getBuyer());
                panTransactionHistory.setTransactionType(TransType.Buy_Product_Balance.toString());
                panTransactionHistory.setIsIncome(IsIncome.N.toString());
                panTransactionHistory.setRemark("余额购买产品");
                panTransactionHistory.setBillType(BillType.OUT.toString());
                panTransactionHistory.setAmountBefore(balanceBefore);
                panTransactionHistory.setAmountAfter(balanceAfter);

                logger.info("****交易管理-余额购买产品-TransInfo:{}", JSONObject.toJSONString(panTransactionHistory));
                transHistoryMapper.insertPanTransactionHistory(panTransactionHistory);

                //购买成功，可用余额减少
                panUserBalance.setAvailableAmt(panUserBalance.getAvailableAmt().subtract(purchaseBean.getAmount()));
                //购买成功，锁定金额增加，总额不变
                panUserBalance.setLockBalance(panUserBalance.getLockBalance().add(purchaseBean.getAmount()));
                logger.info("****交易管理-余额购买产品-UserBalance:{}", JSONObject.toJSONString(panUserBalance));
            }

            userBalanceMapper.updatePanUserBalance(panUserBalance);

            SysUser sysUser = sysUserMapper.selectUserById(purchaseBean.getBuyer());
            logger.info("****交易管理-余额购买产品-UserInfo", JSONObject.toJSONString(sysUser));
            if (purchaseBean.getIsLucky().equals("0")) {
                PanUserBalance luckyUserBalance = userBalanceMapper.getPanUserBalanceByUserId(purchaseBean.getBuyer());
                BigDecimal luckyBalanceBefore = luckyUserBalance.getBalance();
                BigDecimal luckyBalanceAfter = luckyUserBalance.getBalance().add(purchaseBean.getLuckyAmt());
                PanTransactionHistory transactionHistory = new PanTransactionHistory();
                transactionHistory.setAmount(purchaseBean.getLuckyAmt());
                transactionHistory.setOrOrderId(purchaseBean.getOrderNo());
                transactionHistory.setOrUserId(purchaseBean.getBuyer());
                transactionHistory.setUserId(purchaseBean.getBuyer());
                transactionHistory.setTransactionType(TransType.Lucky_Income.toString());
                transactionHistory.setIsIncome(IsIncome.N.toString());
                transactionHistory.setRemark("购买产品-幸运收益");
                transactionHistory.setBillType(BillType.IN.toString());
                transactionHistory.setAmountBefore(luckyBalanceBefore);
                transactionHistory.setAmountAfter(luckyBalanceAfter);

                logger.info("****交易管理-余额购买产品-幸运收益-TransInfo:{}", JSONObject.toJSONString(transactionHistory));
                transHistoryMapper.insertPanTransactionHistory(transactionHistory);

                luckyUserBalance.setAvailableAmt(luckyUserBalance.getAvailableAmt().add(purchaseBean.getLuckyAmt()));
                luckyUserBalance.setBalance(luckyUserBalance.getBalance().add(purchaseBean.getLuckyAmt()));

                logger.info("****交易管理-余额购买产品-幸运收益-UserBalance:{}", JSONObject.toJSONString(luckyUserBalance));
                userBalanceMapper.updatePanUserBalance(luckyUserBalance);
            }else if(purchaseBean.getIsVoucher().equals("0")){
                PanDrawsDetail drawsDetail =new PanDrawsDetail();
                drawsDetail.setType(LotteryType.Voucher.trim());
                drawsDetail.setUserId(purchaseBean.getBuyer());
                drawsDetail.setLotteryId(purchaseBean.getProduct());
                drawsDetail.setName("优惠券 "+purchaseBean.getVoucherObainAmount().divide(new BigDecimal(100)));
                drawsDetail.setNameEn("Coupon "+purchaseBean.getVoucherObainAmount().divide(new BigDecimal(100)));
                drawsDetail.setAmount(purchaseBean.getVoucherObainAmount());
                drawsDetail.setStatus(PrizeStatus.TO_BE_USED);
                drawsDetail.setPrizeMode(PrizeMode.BUY_PROD);
                drawsDetail.setEndDate(DateUtils.getSomeDayLaterDateByToday(purchaseBean.getVoucherCycle()));
                int i =  panLotteryMapper.addDrawsDetail(drawsDetail);
            }
            if(purchaseBean.getIsDraws().equals("0")){
                PanUserAsset panUserAsset = new PanUserAsset();
                panUserAsset.setUserId(purchaseBean.getBuyer());
                userAssetMapper.updateDrawsNumberAdd(panUserAsset);
            }

            if (sysUser.getParentId() != null && sysUser.getParentId() > 0) {
                commission(purchaseBean, sysUser);
            }
        }
        int i = purchaseMapper.insertPurchase(purchaseBean);
        if (i < 1) {
            result = MessageStatus.ERROR;
        }
        return result;
    }

    @Override
    @Transactional
    public String editBalance(PanUserBalance requestUserBalance) {
        logger.info("****交易管理-修改用户余额-Start********");
        String msg = MessageStatus.SUCCESS;
        PanUserBalance currUserBalance = userBalanceMapper.selectPanUserBalanceByUserBalanceId(requestUserBalance.getUserBalanceId());
        BigDecimal currAmount = requestUserBalance.getAmount().multiply(new BigDecimal(100));

        PanTransactionHistory panTransactionHistory = new PanTransactionHistory();
        panTransactionHistory.setAmount(currAmount);
        panTransactionHistory.setUserId(currUserBalance.getUserId());
        panTransactionHistory.setOrUserId(Long.parseLong(requestUserBalance.getUpdateAt()));


        if (requestUserBalance.getBillType().equals(BillType.IN.toString())) {
            BigDecimal inBalanceBefore = currUserBalance.getBalance();
            BigDecimal inBalanceAfter = currUserBalance.getBalance().add(currAmount);
            panTransactionHistory.setIsIncome(IsIncome.Y.toString());
            panTransactionHistory.setBillType(BillType.IN.toString());
            panTransactionHistory.setAmountBefore(inBalanceBefore);
            panTransactionHistory.setAmountAfter(inBalanceAfter);
            panTransactionHistory.setTransactionType(requestUserBalance.getTransType());
            panTransactionHistory.setRemark("");
            logger.info("****交易管理-修改用户余额-TransInfo:{}", JSONObject.toJSONString(panTransactionHistory));

            currUserBalance.setAvailableAmt(currUserBalance.getAvailableAmt().add(currAmount));
            currUserBalance.setBalance(currUserBalance.getBalance().add(currAmount));
            logger.info("****交易管理-修改用户余额-UserBalance:{}", JSONObject.toJSONString(currUserBalance));
        } else if (requestUserBalance.getBillType().equals(BillType.OUT.toString())) {
            BigDecimal outBalanceBefore = currUserBalance.getBalance();
            BigDecimal outBalanceAfter = currUserBalance.getBalance().subtract(currAmount);
            panTransactionHistory.setIsIncome(IsIncome.N.toString());
            panTransactionHistory.setBillType(BillType.OUT.toString());
            panTransactionHistory.setTransactionType(TransType.Manual_Adjustment.toString());
            panTransactionHistory.setRemark("手工调账");
            panTransactionHistory.setAmountBefore(outBalanceBefore);
            panTransactionHistory.setAmountAfter(outBalanceAfter);
            logger.info("****交易管理-修改用户余额-TransInfo:{}", JSONObject.toJSONString(panTransactionHistory));

            currUserBalance.setAvailableAmt(currUserBalance.getAvailableAmt().subtract(currAmount));
            currUserBalance.setBalance(currUserBalance.getBalance().subtract(currAmount));
            logger.info("****交易管理-修改用户余额-UserBalance:{}", JSONObject.toJSONString(currUserBalance));
        }
        transHistoryMapper.insertPanTransactionHistory(panTransactionHistory);
        int i = userBalanceMapper.updatePanUserBalance(currUserBalance);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }


    /**
     * 充值账务处理
     *
     * @param recharge
     * @return
     */
    @Override
    @Transactional
    public String recharge(PanRecharge recharge) {
        logger.info("****交易管理-充值 Start********");
        String msg = MessageStatus.SUCCESS;
        if (recharge.getStatus().equals(TransStatus.COMPLETED)) {
            PanUserBalance panUserBalance = userBalanceMapper.getPanUserBalanceByUserId(recharge.getUserId());

            BigDecimal balanceBefore = panUserBalance.getBalance();
            BigDecimal balanceAfter = panUserBalance.getBalance().add(recharge.getAmount());
            PanTransactionHistory panTransactionHistory = new PanTransactionHistory();
            panTransactionHistory.setOrOrderId(recharge.getRequestNo());
            panTransactionHistory.setOrUserId(recharge.getUserId());
            panTransactionHistory.setAmount(recharge.getAmount());
            panTransactionHistory.setUserId(recharge.getUserId());
            panTransactionHistory.setTransactionType(TransType.Recharge.toString());
            panTransactionHistory.setIsIncome(IsIncome.N.toString());
            panTransactionHistory.setBillType(BillType.IN.toString());
            panTransactionHistory.setRemark("充值");
            panTransactionHistory.setAmountBefore(balanceBefore);
            panTransactionHistory.setAmountAfter(balanceAfter);
            logger.info("****交易管理-充值-TransInfo:{}", JSONObject.toJSONString(panTransactionHistory));
            transHistoryMapper.insertPanTransactionHistory(panTransactionHistory);

            panUserBalance.setAvailableAmt(panUserBalance.getAvailableAmt().add(recharge.getAmount()));
            panUserBalance.setBalance(panUserBalance.getBalance().add(recharge.getAmount()));

            logger.info("****交易管理-充值-UserBalance:{}", JSONObject.toJSONString(panUserBalance));
            userBalanceMapper.updatePanUserBalance(panUserBalance);

        }
        int i = panRechargeMapper.updatePanRecharge(recharge);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    /**
     * 提现账务处理
     *
     * @param withdraw
     * @return
     */
    @Override
    @Transactional
    public String withdraw(PanWithdraw withdraw) {
        logger.info("****交易管理-提现-Start********");
        String msg = MessageStatus.SUCCESS;
        PanUserBalance panUserBalance = userBalanceMapper.getPanUserBalanceByUserId(withdraw.getUserId());
        if (withdraw.getStatus().equals(TransStatus.COMPLETED)) {
            // 完成
            BigDecimal withdrawBefore = panUserBalance.getBalance();
            BigDecimal withdrawAfter = panUserBalance.getBalance().subtract(withdraw.getAmount()).subtract(withdraw.getFee());
            PanTransactionHistory panTransactionHistory = new PanTransactionHistory();
            panTransactionHistory.setOrOrderId(withdraw.getRequestNo());
            panTransactionHistory.setOrUserId(withdraw.getUserId());
            panTransactionHistory.setAmount(withdraw.getAmount().add(withdraw.getFee()));
            panTransactionHistory.setUserId(withdraw.getUserId());
            panTransactionHistory.setTransactionType(TransType.Withdraw.toString());
            panTransactionHistory.setIsIncome(IsIncome.N.toString());
            panTransactionHistory.setBillType(BillType.BAL.toString());
            panTransactionHistory.setRemark("提现");
            panTransactionHistory.setAmountBefore(withdrawBefore);
            panTransactionHistory.setAmountAfter(withdrawAfter);

            logger.info("****交易管理-提现-完成-TransInfo:{}", JSONObject.toJSONString(panTransactionHistory));
            transHistoryMapper.insertPanTransactionHistory(panTransactionHistory);
            panUserBalance.setBalance(panUserBalance.getBalance().subtract(withdraw.getAmount()).subtract(withdraw.getFee()));
            panUserBalance.setTransitAmt(panUserBalance.getTransitAmt().subtract(withdraw.getAmount()).subtract(withdraw.getFee()));
            logger.info("****交易管理-提现-完成-UserBalance:" + JSONObject.toJSONString(panUserBalance));
            userBalanceMapper.updatePanUserBalance(panUserBalance);
        } else if (withdraw.getStatus().equals(TransStatus.DECLINED)) {
            // 审核不通过
            panUserBalance.setAvailableAmt(panUserBalance.getAvailableAmt().add(withdraw.getAmount()).add(withdraw.getFee()));
            panUserBalance.setTransitAmt(panUserBalance.getTransitAmt().subtract(withdraw.getAmount()).subtract(withdraw.getFee()));
            logger.info("****交易管理-提现-审核不通过-UserBalance:" + JSONObject.toJSONString(panUserBalance));
            userBalanceMapper.updatePanUserBalance(panUserBalance);
        }

        int i = panWithdrawMapper.updatePanWithdraw(withdraw);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    /**
     * 返佣账务处理
     *
     * @param panPurchase
     * @param currUser
     */
    public void commission(Purchase panPurchase, SysUser currUser) {
        logger.info("****交易管理-返佣-Start********");
        if (currUser.getParentId() != null) {
            logger.info("****交易管理-上级编号:" + currUser.getParentId());
            Purchase userPurchase = new Purchase();
            userPurchase.setBuyer(currUser.getUserId());
            userPurchase.setStatus(TransStatus.SUCCESS);
            List<Purchase> userPurchaseList = purchaseMapper.selectPurchaseListForBuy(userPurchase);
            if (userPurchaseList.size() < 1) {
                //首单，赠送金额
                logger.info("****交易管理-首单-Strat********");
                // 首单赠送比例
                Double firstPurchaseCommissionARate = Double.parseDouble(sysConfigService.selectConfigByKey("first_purchase_commission_A_ratio"));
                BigDecimal firstPurchaseCommission = panPurchase.getAmount().multiply(new BigDecimal(firstPurchaseCommissionARate)).divide(new BigDecimal(100));
                PanUserBalance parentUser = userBalanceMapper.getUserBalanceByUserId(currUser.getParentId());
                BigDecimal firstPurchaseBefore = parentUser.getBalance();
                BigDecimal firstPurchaseAfter = parentUser.getBalance().add(firstPurchaseCommission);

                PanTransactionHistory panParentTransactionHistory = new PanTransactionHistory();
                panParentTransactionHistory.setAmount(firstPurchaseCommission);
                panParentTransactionHistory.setUserId(currUser.getParentId());
                panParentTransactionHistory.setOrOrderId(panPurchase.getOrderNo());
                panParentTransactionHistory.setOrUserId(panPurchase.getBuyer());
                panParentTransactionHistory.setTransactionType(TransType.Child_First_Purchase_Reward.toString());
                panParentTransactionHistory.setIsIncome(IsIncome.Y.toString());
                panParentTransactionHistory.setBillType(BillType.IN.toString());
                panParentTransactionHistory.setRemark("A级首单奖金");
                panParentTransactionHistory.setAmountBefore(firstPurchaseBefore);
                panParentTransactionHistory.setAmountAfter(firstPurchaseAfter);
                logger.info("****交易管理-首单-TransInfo:{}", JSONObject.toJSONString(panParentTransactionHistory));
                transHistoryMapper.insertPanTransactionHistory(panParentTransactionHistory);

                parentUser.setBalance(parentUser.getBalance().add(firstPurchaseCommission));
                parentUser.setRewardAmt(parentUser.getRewardAmt().add(firstPurchaseCommission));
                logger.info("****交易管理-首单-UserBalance :{}", JSONObject.toJSONString(parentUser));
                userBalanceMapper.updatePanUserBalance(parentUser);

                /**
                 * 赠送抽奖1次
                 */
                PanUserAsset panUserAsset = new PanUserAsset();
                panUserAsset.setUserId(currUser.getParentId());
                userAssetMapper.updateDrawsNumberAdd(panUserAsset);
            }

            /**
             * 赠送产品
             */
            //赠送比例
            Double rewardRate = Double.parseDouble(sysConfigService.selectConfigByKey("purchase_treasure_rate"));
            PanProduct rewardProduct = panProductMapper.selectPanProductByName("Reward Product");

            PanUserBalance currParentUser = userBalanceMapper.getUserBalanceByUserId(currUser.getParentId());
            //产品日费率
            Double rewardProductDailyRate = rewardProduct.getDailyInterest();
            BigDecimal rewardAmt = panPurchase.getAmount().multiply(new BigDecimal(rewardRate)).divide(new BigDecimal(100));

            Purchase rewardPurchase = new Purchase();
            String orderNo = DateUtils.createOrderId("P");
            rewardPurchase.setOrderNo(orderNo);
            rewardPurchase.setBuyer(currUser.getParentId());
            rewardPurchase.setProduct(rewardProduct.getId());
            rewardPurchase.setProductName(rewardProduct.getName());
            rewardPurchase.setAmount(rewardAmt);
            rewardPurchase.setCycle(rewardProduct.getCycle());
            rewardPurchase.setDailyInterest(rewardProductDailyRate);
            rewardPurchase.setTotalInterest(new BigDecimal(0));
            rewardPurchase.setPayBack("0");
            rewardPurchase.setStatus(TransStatus.SUCCESS);
            rewardPurchase.setBeginDate(DateUtils.getTomorrowDate());
            rewardPurchase.setEndDate(DateUtils.getSomeDayLaterDate(rewardProduct.getCycle()));
            logger.info("****交易管理-赠送产品-Purchase：{}", JSONObject.toJSONString(rewardPurchase));
            purchaseMapper.insertPurchase(rewardPurchase);

            BigDecimal balanceBefore = currParentUser.getBalance();
            BigDecimal balanceAfter = currParentUser.getBalance().add(rewardAmt);

            PanTransactionHistory panTransHistory = new PanTransactionHistory();
            panTransHistory.setAmount(rewardAmt);
            panTransHistory.setUserId(currParentUser.getUserId());
            panTransHistory.setOrOrderId(panPurchase.getOrderNo());
            panTransHistory.setOrUserId(panPurchase.getBuyer());
            panTransHistory.setTransactionType(TransType.Reward_Product.toString());
            panTransHistory.setIsIncome(IsIncome.Y.toString());
            panTransHistory.setBillType(BillType.BAL.toString());
            panTransHistory.setRemark("赠送产品");
            panTransHistory.setAmountBefore(balanceBefore);
            panTransHistory.setAmountAfter(balanceAfter);
            logger.info("****交易管理-赠送产品-TransInfo:{}", JSONObject.toJSONString(panTransHistory));
            transHistoryMapper.insertPanTransactionHistory(panTransHistory);

            currParentUser.setBalance(currParentUser.getBalance().add(rewardAmt));
            currParentUser.setLockBalance(currParentUser.getLockBalance().add(rewardAmt));
            logger.info("****交易管理-赠送产品-UserBalance:{}", JSONObject.toJSONString(currParentUser));
            userBalanceMapper.updatePanUserBalance(currParentUser);

            // A级返佣
            PanUserBalance parentUser = userBalanceMapper.getUserBalanceByUserId(currUser.getParentId());
            Double commissionAShareRatio = Double.parseDouble(sysConfigService.selectConfigByKey("commission_A_share_ratio"));
            // 返佣金额
            BigDecimal commssionA = panPurchase.getAmount().multiply(new BigDecimal(commissionAShareRatio)).divide(new BigDecimal(100));

            BigDecimal parentCurrBefore = parentUser.getBalance();
            BigDecimal parentCurrAfter = parentUser.getBalance().add(commssionA);

            PanTransactionHistory transactionHistory = new PanTransactionHistory();
            transactionHistory.setAmount(commssionA);
            transactionHistory.setUserId(parentUser.getUserId());
            transactionHistory.setOrOrderId(panPurchase.getOrderNo());
            transactionHistory.setOrUserId(panPurchase.getBuyer());
            transactionHistory.setAmountBefore(parentCurrBefore);
            transactionHistory.setAmountAfter(parentCurrAfter);
            transactionHistory.setBillType(BillType.IN.toString());
            transactionHistory.setTransactionType(TransType.Commission_A_Reward.toString());
            transactionHistory.setIsIncome(IsIncome.Y.toString());
            transactionHistory.setRemark("A级返佣");

            logger.info("****交易管理-A级返佣-TransInfo:{}", JSONObject.toJSONString(transactionHistory));
            transHistoryMapper.insertPanTransactionHistory(transactionHistory);

            parentUser.setBalance(parentUser.getBalance().add(commssionA));
            parentUser.setRewardAmt(parentUser.getRewardAmt().add(commssionA));

            logger.info("****交易管理-A级返佣-UserBalance:{}", JSONObject.toJSONString(parentUser));
            userBalanceMapper.updatePanUserBalance(parentUser);
        }

        if (currUser.getGrandId() != null) {
            logger.info("****交易管理-上上级编号:" + currUser.getGrandId());
            PanUserBalance grandUser = userBalanceMapper.getUserBalanceByUserId(currUser.getGrandId());
            // B级返佣
            Double commissionBShareRatio = Double.parseDouble(sysConfigService.selectConfigByKey("commission_B_share_ratio"));
            BigDecimal commssionB = panPurchase.getAmount().multiply(new BigDecimal(commissionBShareRatio)).divide(new BigDecimal(100));

            BigDecimal grandBefore = grandUser.getBalance();
            BigDecimal grandAfter = grandUser.getBalance().add(commssionB);

            PanTransactionHistory panGrandTransactionHistory = new PanTransactionHistory();
            panGrandTransactionHistory.setAmount(commssionB);
            panGrandTransactionHistory.setUserId(grandUser.getUserId());
            panGrandTransactionHistory.setOrOrderId(panPurchase.getOrderNo());
            panGrandTransactionHistory.setOrUserId(panPurchase.getBuyer());
            panGrandTransactionHistory.setTransactionType(TransType.Commission_B_Reward.toString());
            panGrandTransactionHistory.setIsIncome(IsIncome.Y.toString());
            panGrandTransactionHistory.setBillType(BillType.IN.toString());
            panGrandTransactionHistory.setRemark("B级返佣");
            panGrandTransactionHistory.setAmountBefore(grandBefore);
            panGrandTransactionHistory.setAmountAfter(grandAfter);

            logger.info("****交易管理-B级返佣-TransInfo:{}", JSONObject.toJSONString(panGrandTransactionHistory));
            transHistoryMapper.insertPanTransactionHistory(panGrandTransactionHistory);

            grandUser.setBalance(grandUser.getBalance().add(commssionB));
            grandUser.setRewardAmt(grandUser.getRewardAmt().add(commssionB));

            logger.info("****交易管理-B级返佣-UserBalance:{}", JSONObject.toJSONString(grandUser));
            userBalanceMapper.updatePanUserBalance(grandUser);
        }
        if (currUser.getGreatGrandId() != null) {

            logger.info("****交易管理-上上上级编号：" + currUser.getGreatGrandId());
            PanUserBalance greatGrandUser = userBalanceMapper.getUserBalanceByUserId(currUser.getGreatGrandId());
            // C级返佣
            Double commissionCShareRatio = Double.parseDouble(sysConfigService.selectConfigByKey("commission_C_share_ratio"));
            BigDecimal commssionC = panPurchase.getAmount().multiply(new BigDecimal(commissionCShareRatio)).divide(new BigDecimal(100));

            BigDecimal reatGrandBefore = greatGrandUser.getBalance();
            BigDecimal reatGrandAfter = greatGrandUser.getBalance().add(commssionC);

            PanTransactionHistory greatGrandTrans = new PanTransactionHistory();
            greatGrandTrans.setAmount(commssionC);
            greatGrandTrans.setUserId(greatGrandUser.getUserId());
            greatGrandTrans.setOrOrderId(panPurchase.getOrderNo());
            greatGrandTrans.setOrUserId(panPurchase.getBuyer());
            greatGrandTrans.setTransactionType(TransType.Commission_C_Reward.toString());
            greatGrandTrans.setIsIncome(IsIncome.Y.toString());
            greatGrandTrans.setBillType(BillType.IN.toString());
            greatGrandTrans.setRemark("C级返佣");
            greatGrandTrans.setAmountBefore(reatGrandBefore);
            greatGrandTrans.setAmountAfter(reatGrandAfter);

            logger.info("****交易管理-C级返佣-TransInfo:{}", JSONObject.toJSONString(greatGrandTrans));
            transHistoryMapper.insertPanTransactionHistory(greatGrandTrans);

            greatGrandUser.setBalance(greatGrandUser.getBalance().add(commssionC));
            greatGrandUser.setRewardAmt(greatGrandUser.getRewardAmt().add(commssionC));

            logger.info("****交易管理-C级返佣-UserBalance :{}", JSONObject.toJSONString(greatGrandUser));
            userBalanceMapper.updatePanUserBalance(greatGrandUser);
        }
    }


    /**
     * 增值宝转入
     *
     * @param panUserAsset
     * @return
     */
    @Override
    @Transactional
    public String transferIn(PanUserAsset panUserAsset) {
        logger.info("****交易管理-增值宝转入-Start********");
        String msg = MessageStatus.SUCCESS;
        PanUserBalance userBalance = userBalanceMapper.getPanUserBalanceByUserId(panUserAsset.getUserId());
        PanUserAsset currAssetBalance = userAssetMapper.selectPanUserAssetByUserId(panUserAsset.getUserId());

        BigDecimal balanceBefore = userBalance.getBalance();
        BigDecimal balanceAfter = userBalance.getBalance();

        PanTransactionHistory transHistory = new PanTransactionHistory();
        transHistory.setAmount(panUserAsset.getAmount());
        transHistory.setUserId(panUserAsset.getUserId());
        transHistory.setTransactionType(TransType.Treasure_Transfer_In.toString());
        transHistory.setIsIncome(IsIncome.N.toString());
        transHistory.setBillType(BillType.OUT.toString());
        transHistory.setAmountBefore(balanceBefore);
        transHistory.setAmountAfter(balanceAfter);
        transHistory.setRemark("增值宝转入");

        logger.info("****交易管理-增值宝转入-TransInfo:" + JSONObject.toJSONString(transHistory));
        transHistoryMapper.insertPanTransactionHistory(transHistory);

        userBalance.setAvailableAmt(userBalance.getAvailableAmt().subtract(panUserAsset.getAmount()));
        userBalance.setAssetBalance(userBalance.getAssetBalance().add(panUserAsset.getAmount()));

        logger.info("****交易管理-增值宝转入-UserBalance:" + JSONObject.toJSONString(userBalance));
        userBalanceMapper.updatePanUserBalance(userBalance);
        currAssetBalance.setBalance(currAssetBalance.getBalance().add(panUserAsset.getAmount()));
        logger.info("****交易管理-增值宝转入-AssetBalance:" + JSONObject.toJSONString(currAssetBalance));
        int i = userAssetMapper.updatePanUserAsset(currAssetBalance);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    /**
     * 增值宝转出
     *
     * @param panUserAsset
     * @return
     */
    @Override
    @Transactional
    public String transferOut(PanUserAsset panUserAsset) {
        logger.info("****交易管理-增值宝转出-Start********");
        String msg = MessageStatus.SUCCESS;
        PanUserBalance userBalance = userBalanceMapper.getPanUserBalanceByUserId(panUserAsset.getUserId());
        PanUserAsset currAssetBalance = userAssetMapper.selectPanUserAssetByUserId(panUserAsset.getUserId());

        BigDecimal balanceBefore = userBalance.getBalance();
        BigDecimal balanceAfter = userBalance.getBalance();

        PanTransactionHistory transHistory = new PanTransactionHistory();
        transHistory.setAmount(panUserAsset.getAmount());
        transHistory.setUserId(panUserAsset.getUserId());
        transHistory.setTransactionType(TransType.Treasure_Transfer_Out.toString());
        transHistory.setIsIncome(IsIncome.N.toString());
        transHistory.setBillType(BillType.IN.toString());
        transHistory.setAmountBefore(balanceBefore);
        transHistory.setAmountAfter(balanceAfter);
        transHistory.setRemark("增值宝转出");

        logger.info("****交易管理-增值宝转出-TransInfo:" + JSONObject.toJSONString(transHistory));
        transHistoryMapper.insertPanTransactionHistory(transHistory);

        userBalance.setAvailableAmt(userBalance.getAvailableAmt().add(panUserAsset.getAmount()));
        userBalance.setAssetBalance(userBalance.getAssetBalance().subtract(panUserAsset.getAmount()));

        logger.info("****交易管理-增值宝转出-UserBalance:" + JSONObject.toJSONString(transHistory));
        userBalanceMapper.updatePanUserBalance(userBalance);

        currAssetBalance.setBalance(currAssetBalance.getBalance().subtract(panUserAsset.getAmount()));

        logger.info("****交易管理-增值宝转出-AssetBalance:" + JSONObject.toJSONString(currAssetBalance));
        int i = userAssetMapper.updatePanUserAsset(currAssetBalance);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    @Override
    @Transactional
    public String toSgin(PanSignRecord panSignRecord) {
        logger.info("****交易管理-签到-Start********");
        PanSignRecord maxSignRecord = panSignRecordMapper.getMaxSignRecordByUser(panSignRecord.getUserId());
        int continuousDay = 1;
        if (maxSignRecord != null) {
            String fristDay = maxSignRecord.getSignYear() + "-" + maxSignRecord.getSignMonth() + "-" + maxSignRecord.getSignDay();
            String secondDay = panSignRecord.getSignYear() + "-" + panSignRecord.getSignMonth() + "-" + panSignRecord.getSignDay();
            if (DateUtils.differenceDay(fristDay, secondDay) == 1) {
                continuousDay = maxSignRecord.getContinuousDay() + 1;
            }
        }
        PanUserBalance userBalance = userBalanceMapper.getPanUserBalanceByUserId(panSignRecord.getUserId());
        // 签到奖励金额
        int dailySignInAmount = Integer.parseInt(sysConfigService.selectConfigByKey("dailySignInAmount"));
        int dailySignInAmountMax = Integer.parseInt(sysConfigService.selectConfigByKey("dailySignInAmountMax"));
        int dailySignInAmountRate = Integer.parseInt(sysConfigService.selectConfigByKey("dailySignInAmountRate"));
        // int ax = (int) (dailySignInAmount + Math.random() * ((dailySignInAmountMax-dailySignInAmount)+1));
        int ax = DateUtils.getProportionRandom(dailySignInAmount, dailySignInAmountMax, dailySignInAmountRate);
        BigDecimal transAmount = new BigDecimal(ax).multiply(new BigDecimal(100));

        BigDecimal balanceBefore = userBalance.getBalance();
        BigDecimal balanceAfter = userBalance.getBalance().add(transAmount);

        PanTransactionHistory transHistory = new PanTransactionHistory();
        transHistory.setAmount(transAmount);
        transHistory.setUserId(panSignRecord.getUserId());
        transHistory.setTransactionType(TransType.SignIn_Reward.toString());
        transHistory.setIsIncome(IsIncome.Y.toString());
        transHistory.setBillType(BillType.IN.toString());
        transHistory.setAmountBefore(balanceBefore);
        transHistory.setAmountAfter(balanceAfter);
        transHistory.setRemark("签到奖励");

        logger.info("****交易管理-签到奖励-TransInfo:" + JSONObject.toJSONString(transHistory));
        transHistoryMapper.insertPanTransactionHistory(transHistory);

        userBalance.setAvailableAmt(userBalance.getAvailableAmt().add(transAmount));
        userBalance.setBalance(userBalance.getBalance().add(transAmount));

        logger.info("****交易管理-签到奖励-UserBalance:" + JSONObject.toJSONString(userBalance));
        userBalanceMapper.updatePanUserBalance(userBalance);
        panSignRecord.setContinuousDay(continuousDay);
        int i = panSignRecordMapper.insertPanSignRecord(panSignRecord);
        String msg = MessageStatus.SUCCESS;
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    @Override
    public String editAgentBalance(PanAgentBalance requestAgentBalance) {
        logger.info("****交易管理-修改代理余额-Start********");
        String msg = MessageStatus.SUCCESS;
        PanAgentBalance currAgentBalance = agentBalanceMapper.selectPanAgentBalanceById(requestAgentBalance.getId());
        if (requestAgentBalance.getTransType().equals(TransType.Prepaid_Recharge.toString())) {
            BigDecimal currAmount = requestAgentBalance.getAmount().multiply(new BigDecimal(100));
            BigDecimal inBalanceBefore = currAgentBalance.getBalance();
            BigDecimal inBalanceAfter = currAgentBalance.getBalance().add(currAmount);
            PanAgentBalanceDetail trans = new PanAgentBalanceDetail();
            trans.setAgentId(currAgentBalance.getAgentId());
            trans.setAmountBefore(inBalanceBefore);
            trans.setAmount(currAmount);
            trans.setAmountAfter(inBalanceAfter);
            trans.setTransType(TransType.Prepaid_Recharge.name());
            logger.info("****交易管理-新增代理商余额明细-agentBalanceDetail:{}", JSONObject.toJSONString(trans));
            agentBalanceMapper.insertAgentBalanceDetail(trans);

            currAgentBalance.setAvailableAmt(currAgentBalance.getAvailableAmt().add(currAmount));
            currAgentBalance.setBalance(currAgentBalance.getBalance().add(currAmount));
            currAgentBalance.setPrechargeAmt(currAgentBalance.getPrechargeAmt().add(currAmount));
            logger.info("****交易管理-修改代理商余额-AgentBalance:{}", JSONObject.toJSONString(currAgentBalance));
        } else if (requestAgentBalance.getTransType().equals(TransType.Manual_Adjustment.toString())) {

            BigDecimal currAmount = requestAgentBalance.getAmount().multiply(new BigDecimal(100));
            BigDecimal inBalanceBefore = currAgentBalance.getBalance();
            BigDecimal inBalanceAfter = currAgentBalance.getBalance().subtract(currAmount);
            PanAgentBalanceDetail trans = new PanAgentBalanceDetail();
            trans.setAgentId(currAgentBalance.getAgentId());
            trans.setAmountBefore(inBalanceBefore);
            trans.setAmount(currAmount);
            trans.setAmountAfter(inBalanceAfter);
            trans.setTransType(TransType.Manual_Adjustment.name());
            logger.info("****交易管理-减少代理商余额明细-agentBalanceDetail:{}", JSONObject.toJSONString(trans));
            agentBalanceMapper.insertAgentBalanceDetail(trans);

            currAgentBalance.setAvailableAmt(currAgentBalance.getAvailableAmt().subtract(currAmount));
            currAgentBalance.setBalance(currAgentBalance.getBalance().subtract(currAmount));
            currAgentBalance.setPrechargeAmt(currAgentBalance.getPrechargeAmt().subtract(currAmount));
            logger.info("****交易管理-减少代理商余额-AgentBalance:{}", JSONObject.toJSONString(currAgentBalance));
        }

        int i = agentBalanceMapper.updatePanAgentBalance(currAgentBalance);
        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    /**
     * 手工赠送产品账务处理
     *
     * @param purchaseBalance
     * @return
     */
    @Override
    @Transactional
    public String productReward(PanUserBalance purchaseBalance) {
        logger.info("****交易管理-手工赠送产品-Start********");
        String result = MessageStatus.SUCCESS;

        PanProduct rewardProduct = panProductMapper.selectPanProductByName("Reward Product");

        PanUserBalance currParentUser = userBalanceMapper.getUserBalanceByUserId(purchaseBalance.getUserId());
        //产品日费率
        BigDecimal currAmount = purchaseBalance.getAmount().multiply(new BigDecimal(100));

        Purchase rewardPurchase = new Purchase();
        String orderNo = DateUtils.createOrderId("P");
        rewardPurchase.setOrderNo(orderNo);
        rewardPurchase.setBuyer(purchaseBalance.getUserId());
        rewardPurchase.setProduct(rewardProduct.getId());
        rewardPurchase.setProductName(rewardProduct.getName());
        rewardPurchase.setAmount(currAmount);
        rewardPurchase.setCycle(rewardProduct.getCycle());
        rewardPurchase.setDailyInterest(rewardProduct.getDailyInterest());
        rewardPurchase.setTotalInterest(new BigDecimal(0));
        rewardPurchase.setPayBack("0");
        rewardPurchase.setStatus(TransStatus.SUCCESS);
        rewardPurchase.setBeginDate(DateUtils.getTomorrowDate());
        rewardPurchase.setEndDate(DateUtils.getSomeDayLaterDate(rewardProduct.getCycle()));

        rewardProduct.setCurrFund(new BigDecimal(rewardProduct.getCurrFund()).subtract(currAmount.divide(new BigDecimal(100))).doubleValue());
        rewardProduct.setProgress(((rewardProduct.getTotalFund() - rewardProduct.getCurrFund()) / rewardProduct.getTotalFund()) * 100);
        panProductMapper.updatePanProduct(rewardProduct);

        logger.info("****交易管理-人工赠送产品-Purchase：{}", JSONObject.toJSONString(rewardPurchase));
        purchaseMapper.insertPurchase(rewardPurchase);

        BigDecimal balanceBefore = currParentUser.getBalance();
        BigDecimal balanceAfter = currParentUser.getBalance().add(currAmount);

        PanTransactionHistory panTransHistory = new PanTransactionHistory();
        panTransHistory.setAmount(currAmount);
        panTransHistory.setUserId(currParentUser.getUserId());
        panTransHistory.setOrOrderId(orderNo);
        panTransHistory.setOrUserId(Long.parseLong(purchaseBalance.getUpdateAt()));
        panTransHistory.setTransactionType(TransType.Manual_Reward_Product.toString());
        panTransHistory.setIsIncome(IsIncome.Y.toString());
        panTransHistory.setBillType(BillType.BAL.toString());
        panTransHistory.setRemark("人工赠送产品");
        panTransHistory.setAmountBefore(balanceBefore);
        panTransHistory.setAmountAfter(balanceAfter);

        logger.info("****交易管理-人工赠送产品-TransInfo:{}", JSONObject.toJSONString(panTransHistory));
        transHistoryMapper.insertPanTransactionHistory(panTransHistory);

        currParentUser.setBalance(currParentUser.getBalance().add(currAmount));
        currParentUser.setLockBalance(currParentUser.getLockBalance().add(currAmount));

        logger.info("****交易管理-人工赠送产品-UserBalance:{}", JSONObject.toJSONString(currParentUser));
        int i = userBalanceMapper.updatePanUserBalance(currParentUser);

        if (i < 1) {
            result = MessageStatus.ERROR;
        }
        return result;
    }

    /***
     * 返佣余额转出
     * @param rewardBalance
     * @return
     */
    @Override
    @Transactional
    public String rewardTransferOut(PanUserBalance rewardBalance) {
        logger.info("****交易管理-返佣余额转出-Start********");
        String msg = MessageStatus.SUCCESS;
        PanUserBalance userBalance = userBalanceMapper.getPanUserBalanceByUserId(rewardBalance.getUserId());

        BigDecimal balanceBefore = userBalance.getBalance();
        BigDecimal balanceAfter = userBalance.getBalance();

        PanTransactionHistory transHistory = new PanTransactionHistory();
        transHistory.setAmount(rewardBalance.getAmount());
        transHistory.setUserId(rewardBalance.getUserId());
        transHistory.setTransactionType(TransType.Reward_Transfer_Out.toString());
        transHistory.setIsIncome(IsIncome.N.toString());
        transHistory.setBillType(BillType.IN.toString());
        transHistory.setAmountBefore(balanceBefore);
        transHistory.setAmountAfter(balanceAfter);
        transHistory.setRemark("返佣余额转出");

        logger.info("****交易管理-返佣余额转出-TransInfo:" + JSONObject.toJSONString(transHistory));
        transHistoryMapper.insertPanTransactionHistory(transHistory);

        userBalance.setAvailableAmt(userBalance.getAvailableAmt().add(rewardBalance.getAmount()));
        userBalance.setRewardAmt(userBalance.getRewardAmt().subtract(rewardBalance.getAmount()));

        logger.info("****交易管理-返佣余额转出-UserBalance:" + JSONObject.toJSONString(transHistory));
        int i = userBalanceMapper.updatePanUserBalance(userBalance);

        if (i < 1) {
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

    /***
     * 抽奖
     */
    @Override
    @Transactional
    public String addLotteryMove(PanLottery resultLottery) {
        logger.info("****交易管理-抽奖："+ JSONObject.toJSONString(resultLottery));
        String msg = MessageStatus.SUCCESS;

        PanUserAsset userAsset = new PanUserAsset();
        userAsset.setUserId(resultLottery.getUserId());
        userAssetMapper.updateDrawsNumberReduce(userAsset);
        PanDrawsDetail drawsDetail =new PanDrawsDetail();
        drawsDetail.setType(resultLottery.getType());
        drawsDetail.setUserId(resultLottery.getUserId());
        drawsDetail.setLotteryId(resultLottery.getId());
        drawsDetail.setName(resultLottery.getName());
        drawsDetail.setNameEn(resultLottery.getNameEn());
        drawsDetail.setAmount(resultLottery.getAmount());
        drawsDetail.setStatus(PrizeStatus.COMPLETED);
        drawsDetail.setPrizeMode(PrizeMode.DRAWS);
        if(resultLottery.getType().equals(LotteryType.Cash)){
            PanUserBalance userBalance = userBalanceMapper.getPanUserBalanceByUserId(resultLottery.getUserId());
            BigDecimal drawsBalanceBefore = userBalance.getBalance();
            BigDecimal drawsBalanceAfter = userBalance.getBalance().add(resultLottery.getAmount());
            PanTransactionHistory transactionHistory = new PanTransactionHistory();
            transactionHistory.setAmount(resultLottery.getAmount());
            transactionHistory.setOrUserId(resultLottery.getUserId());
            transactionHistory.setUserId(resultLottery.getUserId());
            transactionHistory.setTransactionType(TransType.Draws_Income.toString());
            transactionHistory.setIsIncome(IsIncome.N.toString());
            transactionHistory.setRemark("抽奖-现金收益");
            transactionHistory.setBillType(BillType.IN.toString());
            transactionHistory.setAmountBefore(drawsBalanceBefore);
            transactionHistory.setAmountAfter(drawsBalanceAfter);

            logger.info("****交易管理-抽奖现金-TransInfo:{}", JSONObject.toJSONString(transactionHistory));
            transHistoryMapper.insertPanTransactionHistory(transactionHistory);

            userBalance.setAvailableAmt(userBalance.getAvailableAmt().add(resultLottery.getAmount()));
            userBalance.setBalance(userBalance.getBalance().add(resultLottery.getAmount()));

            logger.info("****交易管理-抽奖现金-UserBalance:{}", JSONObject.toJSONString(userBalance));
            userBalanceMapper.updatePanUserBalance(userBalance);
        }else if(resultLottery.getType().equals(LotteryType.Voucher)){
            drawsDetail.setStatus(PrizeStatus.TO_BE_USED);
            drawsDetail.setEndDate(DateUtils.getSomeDayLaterDateByToday(resultLottery.getCycle()));
        }
        int i =  panLotteryMapper.addDrawsDetail(drawsDetail);

        if(i<1){
            msg = MessageStatus.ERROR;
        }
        return msg;
    }

}
