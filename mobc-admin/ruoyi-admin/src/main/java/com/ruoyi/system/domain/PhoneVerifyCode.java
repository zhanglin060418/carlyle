package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 phone_verify_code
 * 
 * @author ruoyi
 * @date 2023-04-30
 */
public class PhoneVerifyCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNo;

    /** 验证码 */
    @Excel(name = "验证码")
    private String verifyCode;

    /** 失效时间 */
    private Long expiredAt;

    public void setPhoneNo(String phoneNo) 
    {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() 
    {
        return phoneNo;
    }
    public void setVerifyCode(String verifyCode) 
    {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCode() 
    {
        return verifyCode;
    }
    public void setExpiredAt(Long expiredAt) 
    {
        this.expiredAt = expiredAt;
    }

    public Long getExpiredAt() 
    {
        return expiredAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("phoneNo", getPhoneNo())
            .append("verifyCode", getVerifyCode())
            .append("expiredAt", getExpiredAt())
            .toString();
    }
}
