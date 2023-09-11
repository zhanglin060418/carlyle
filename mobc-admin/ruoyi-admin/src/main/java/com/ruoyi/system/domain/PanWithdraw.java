package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提款对象 pan_withdraw
 *
 * @author ruoyi
 * @date 2023-04-10
 */
public class PanWithdraw extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 提款ID */
    private Long withdrawId;
    @Excel(name = "编号",width = 30)
    private String requestNo;

    private String orderNo;

    /** 用户ID */
    private Long userId;

    private Long topId;
    private Long managerId;
    @Excel(name = "用户账号")
    private String userName;

    /** 银行卡ID */
    private Long cardId;
    @Excel(name = "业务员")
    private String topName;

    /** 代付金额 */
    @Excel(name = "到账金额")
    private BigDecimal amount;
    @Excel(name = "手续费")
    private BigDecimal fee;

    /** 代付银行卡号 */
    private String bankCode;

    /** 持卡人姓名 */
    private String beneficiaryName;
    /** 受益人姓名 */
    @Excel(name = "银行名称")
    private String bankName;
    @Excel(name = "账户名称")
    private String cardName;

    /** 代付收款账户 */
    @Excel(name = "收款账户")
    private String cardNo;

    /** 收益号码 */
    private String beneficiaryNo;

    /** 受益人电话 */
    private String beneficiaryMobile;

    /** 受益人邮件 */
    private String beneficiaryEmail;

    @Excel(name = "状态",readConverterExp = "Pending=待审核,In progress=进行中,Completed=已完成,Failed=异常,Declined=审核不通过")
    private String status;

    private String remark;

    private String respDesc;
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
    @Excel(name = "完成时间",width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private String orderTime;

    private String domain;//产生调用的域名信息 主要为了完成前台跳转使用 因为前台请求可能来自于不同的域名

    private  Long channelId;

    private int withdrawCount;

    public int getWithdrawCount() {
        return withdrawCount;
    }

    public void setWithdrawCount(int withdrawCount) {
        this.withdrawCount = withdrawCount;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public void setRemark(String remark)
    {
    	this.remark = remark;
    }

    public String getRemark()
    {
    	return remark;
    }

    public void setWithdrawId(Long withdrawId)
    {
        this.withdrawId = withdrawId;
    }

    public Long getWithdrawId()
    {
        return withdrawId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setCardId(Long cardId)
    {
        this.cardId = cardId;
    }

    public Long getCardId()
    {
        return cardId;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }

    public String getBankCode()
    {
        return bankCode;
    }
    public void setBeneficiaryName(String beneficiaryName)
    {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryName()
    {
        return beneficiaryName;
    }
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getBankName()
    {
        return bankName;
    }
    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public String getCardNo()
    {
        return cardNo;
    }
    public void setBeneficiaryNo(String beneficiaryNo)
    {
        this.beneficiaryNo = beneficiaryNo;
    }

    public String getBeneficiaryNo()
    {
        return beneficiaryNo;
    }
    public void setBeneficiaryMobile(String beneficiaryMobile)
    {
        this.beneficiaryMobile = beneficiaryMobile;
    }

    public String getBeneficiaryMobile()
    {
        return beneficiaryMobile;
    }
    public void setBeneficiaryEmail(String beneficiaryEmail)
    {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    public String getBeneficiaryEmail()
    {
        return beneficiaryEmail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("withdrawId", getWithdrawId())
            .append("userId", getUserId())
            .append("amount", getAmount())
            .append("free", getFee())
            .append("bankCode", getBankCode())
            .append("cardId", getCardId())
            .append("bankName", getBankName())
            .append("cardNo", getCardNo())
            .append("remark", getRemark())
                .append("domain", getDomain())
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
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

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
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

}
