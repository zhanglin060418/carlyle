package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品支付对象 purchase
 *
 * @author ruoyi
 * @date 2023-03-27
 */
public class Purchase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * zhifuID
     */
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "编号",width = 30)
    private String orderNo;
    private Long buyer;

    private Long topId;

    private Long managerId;
    @Excel(name = "用户账号")
    private String userName;

    @Excel(name = "业务员")
    private String topName;

    /**
     * 产品ID
     */

    private Long product;

    @Excel(name = "产品")
    private String productName;

    /**
     * 周期
     */
    @Excel(name = "周期")
    private Integer cycle;

    /**
     * 日利率
     */
    @Excel(name = "日利率")
    private Double dailyInterest;

    /**
     * 总利率
     */
    @Excel(name = "总返利")
    private BigDecimal totalInterest;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal amount;

    private BigDecimal voucherAmount;

    private long drawsId;

    /**
     * 是否返息
     */
    @Excel(name = "是否返本")
    private String payBack;

    private String endAt;

    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
    }

    private BigDecimal luckyAmt;

    private BigDecimal voucherObainAmount;

    private String isLucky;

    private String isDraws;

    private String isVoucher;

    private Integer voucherStart;

    private Integer voucherEnd;



    /**
     * 状态
     */
    private String status;

    private PanProduct item;

    private Integer voucherCycle;

    public Integer getVoucherCycle() {
        return voucherCycle;
    }

    public void setVoucherCycle(Integer voucherCycle) {
        this.voucherCycle = voucherCycle;
    }

    public BigDecimal getVoucherObainAmount() {
        return voucherObainAmount;
    }

    public void setVoucherObainAmount(BigDecimal voucherObainAmount) {
        this.voucherObainAmount = voucherObainAmount;
    }

    public String getIsDraws() {
        return isDraws;
    }

    public void setIsDraws(String isDraws) {
        this.isDraws = isDraws;
    }

    public String getIsVoucher() {
        return isVoucher;
    }

    public void setIsVoucher(String isVoucher) {
        this.isVoucher = isVoucher;
    }

    public Integer getVoucherStart() {
        return voucherStart;
    }

    public void setVoucherStart(Integer voucherStart) {
        this.voucherStart = voucherStart;
    }

    public Integer getVoucherEnd() {
        return voucherEnd;
    }

    public void setVoucherEnd(Integer voucherEnd) {
        this.voucherEnd = voucherEnd;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getLuckyAmt() {
        return luckyAmt;
    }

    public void setLuckyAmt(BigDecimal luckyAmt) {
        this.luckyAmt = luckyAmt;
    }

    public String getIsLucky() {
        return isLucky;
    }

    public void setIsLucky(String isLucky) {
        this.isLucky = isLucky;
    }

    /**
     * 收益开始日期
     */
    @Excel(name = "收益开始日期")
    private String beginDate;

    /**
     * 收益结束日期
     */
    @Excel(name = "收益结束日期")
    private String endDate;

    /**
     * 结算时间
     */

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setBuyer(Long buyer) {
        this.buyer = buyer;
    }

    public Long getBuyer() {
        return buyer;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getProduct() {
        return product;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setDailyInterest(Double dailyInterest) {
        this.dailyInterest = dailyInterest;
    }

    public Double getDailyInterest() {
        return dailyInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setPayBack(String payBack) {
        this.payBack = payBack;
    }

    public String getPayBack() {
        return payBack;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getEndAt() {
        return endAt;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("buyer", getBuyer())
                .append("amount", getAmount())
                .append("product", getProduct())
                .append("cycle", getCycle())
                .append("dailyInterest", getDailyInterest())
                .append("totalInterest", getTotalInterest())
                .append("payBack", getPayBack())
                .append("orderNo", getOrderNo())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("beginDate", getBeginDate())
                .append("endDate", getEndDate())
                .append("endAt", getEndAt())
                .toString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public PanProduct getItem() {
        return item;
    }

    public void setItem(PanProduct item) {
        this.item = item;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }


    private String managerName;

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    private Long agentId;
    private String agentName;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public long getDrawsId() {
        return drawsId;
    }

    public void setDrawsId(long drawsId) {
        this.drawsId = drawsId;
    }
}
