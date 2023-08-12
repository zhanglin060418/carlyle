package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 ScenesVerifyCode
 *
 * @author ruoyi
 */
public class ScenesVerifyCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 手机号码 */
    private String phoneNo;

    /** 验证码 */
    private String verifyCode;

    /** 使用场景 */
    private String useScenes;

    private String useTime;

    private String sendTime;

    private String status;

    private String remark;


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUseScenes() {
        return useScenes;
    }

    public void setUseScenes(String useScenes) {
        this.useScenes = useScenes;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
