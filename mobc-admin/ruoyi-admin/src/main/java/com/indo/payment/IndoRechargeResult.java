package com.indo.payment;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class IndoRechargeResult extends BaseEntity
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String requestNo;
	private String orderNo;
	private long orderDate;
	private String payInfoUrl;

    /** 银行业权 */
    @Excel(name = "银行业权")
    private String name;

    /** 介绍 */
    @Excel(name = "介绍")
    private String description;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String code;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("requestNo", getRequestNo())
            .append("orderNo", getOrderNo())
            .append("orderDate", getOrderDate())
            .append("payInfoUrl", getPayInfoUrl())
            .toString();
    }

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(long orderDate) {
		this.orderDate = orderDate;
	}

	public String getPayInfoUrl() {
		return payInfoUrl;
	}

	public void setPayInfoUrl(String payInfoUrl) {
		this.payInfoUrl = payInfoUrl;
	}
}
