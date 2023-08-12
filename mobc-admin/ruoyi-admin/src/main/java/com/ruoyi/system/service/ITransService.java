package com.ruoyi.system.service;

import com.ruoyi.system.domain.*;

/**
 * 主页Service接口
 *
 * @author ruoyi
 * @date 2023-05-09
 */
public interface ITransService
{
    public String createWithdraw(PanWithdrawCreate panWithdrawCreate);

    public String recharge(PanRecharge recharge);

    public String withdraw(PanWithdraw withdraw);

    public String purchaseBalance(Purchase purchase);

    public String editBalance(PanUserBalance requestUserBalance);

    public String transferIn(PanUserAsset panUserAsset);

    public String transferOut(PanUserAsset panUserAsset);

    public String toSgin(PanSignRecord panSignRecord);

    public String editAgentBalance(PanAgentBalance requestUserBalance);

    public String productReward(PanUserBalance requestUserBalance);
}
