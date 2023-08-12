package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户银行卡对象 pan_bind_card
 *
 * @author ruoyi
 * @date 2023-04-09
 */
public class PanBindCard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 卡ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    private String userName;

    /** 卡名 */
    @Excel(name = "卡名")
    private String name;

    /** 用户ID */
    @Excel(name = "银行ID")
    private String bankCode;

    private String bankName;


    /** 帐户 */
    @Excel(name = "帐户")
    private String cardNo;

    /** 手机 */
    @Excel(name = "手机")
    private String mobile;

    private PanBank bank;
    private Long topId;
    private Long managerId;

    private String oldCardNo;

    private String newCardNo;

    public Long getTopId() {
        return topId;
    }

    public void setTopId(Long topId) {
        this.topId = topId;
    }

    public PanBank getBank()
    {
        return bank;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }

    public String getBankCode()
    {
        return bankCode;
    }
    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public String getCardNo()
    {
        return cardNo;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile()
    {
        return mobile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("name", getName())
            .append("bankCode", getBankCode())
            .append("getBank", getBank())
            .append("cardNo", getCardNo())
            .append("mobile", getMobile())
            .append("panBank", getBank())
            .toString();
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    private String topName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getOldCardNo() {
        return oldCardNo;
    }

    public void setOldCardNo(String oldCardNo) {
        this.oldCardNo = oldCardNo;
    }

    public String getNewCardNo() {
        return newCardNo;
    }

    public void setNewCardNo(String newCardNo) {
        this.newCardNo = newCardNo;
    }
}
