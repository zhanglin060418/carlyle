package com.indo.payment;

import com.alibaba.fastjson2.JSONObject;

import java.math.BigDecimal;

/**
 * 银行名单Service接口
 *
 * @author ruoyi
 * @date 2023-04-09
 */
public interface IndoPaymentService
{

	/**
     * 查询银行名单
     *
     * @param amount 银行名单主键
     * @param currencyCode 银行名单主键
     * @param requestNo 银行名单主键
     * @param name 银行名单主键
     * @param mobile 银行名单主键
     * @param email 银行名单主键
     * @param  domain  创建订单的域名信息因为前端可能来自不同的域名来的
     * @return 银行名单
     */
    public IndoRechargeResult recharge(BigDecimal amount, String requestNo, String name, String mobile, String email,String domain);


    public IndoRechargeResult recharge(BigDecimal amount, String requestNo, String name, String mobile, String email,String domain,String channel);
    /**
     * 查询不同通道的余额 慢慢修改吧
     * @param channelId
     * @return
     * @throws Exception
     */
    public JSONObject queryBalance(String channelId) throws Exception;
    /**
     * 查询银行名单
     * @return 银行名单
     */
    public IndoWithdrawResult withdraw(String requestNo, String currencyCode, BigDecimal amount, String bankCode, String bankName, String cardNo, String beneficiaryNo, String beneficiaryName, String beneficiaryMobile, String beneficiaryEmail,String domain);
}
