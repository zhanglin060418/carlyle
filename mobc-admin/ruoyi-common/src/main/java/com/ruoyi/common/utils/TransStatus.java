package com.ruoyi.common.utils;

/**
 * Created by HFY on 2023/6/2
 */
public class TransStatus {
    /**
     * 购买产品成功
     */
    public static String SUCCESS = "Success";
    /**
     * 充值/提现待处理
     */
    public static String PROGRESS = "In progress";
    /**
     * 充值/提现处理中
     */
    public static String PENDING = "Pending";
    /**
     * 充值/提现完成
     */
    public static String COMPLETED = "Completed";
    /**
     * 提现异常
     */
    public static String FAILED = "Failed";
    /**
     * 提现审核不通过
     */
    public static String DECLINED = "Declined";
}
