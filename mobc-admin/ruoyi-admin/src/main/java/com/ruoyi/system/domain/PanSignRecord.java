package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 签到记录对象 pan_sign_record
 *
 * @author ruoyi
 * @date 2023-06-30
 */
public class PanSignRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    private Long signId;
    /** 用户编号 */
    private Long userId;
    private String signTime;
    private int signYear;
    private int signMonth;
    private int signDay;
    private int continuousDay;
    private Long topId;
    private Long managerId;
    private Long agentId;
    private String userName;
    private String topName;
    private String managerName;
    private String agentName;

    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public int getSignYear() {
        return signYear;
    }

    public void setSignYear(int signYear) {
        this.signYear = signYear;
    }

    public int getSignMonth() {
        return signMonth;
    }

    public void setSignMonth(int signMonth) {
        this.signMonth = signMonth;
    }

    public int getSignDay() {
        return signDay;
    }

    public void setSignDay(int signDay) {
        this.signDay = signDay;
    }

    public int getContinuousDay() {
        return continuousDay;
    }

    public void setContinuousDay(int continuousDay) {
        this.continuousDay = continuousDay;
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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
