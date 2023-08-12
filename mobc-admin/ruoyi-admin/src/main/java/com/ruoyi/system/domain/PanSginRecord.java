package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 签到记录对象 pan_sgin_record
 *
 * @author ruoyi
 * @date 2023-06-29
 */
public class PanSginRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long signId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 签到时间 */
    @Excel(name = "签到时间")
    private String sginTime;

    /** 年份 */
    @Excel(name = "年份")
    private int sginYear;

    /** 月份 */
    @Excel(name = "月份")
    private int sginMonth;

    /** 日期 */
    @Excel(name = "日期")
    private int sginDay;

    /** 连续天数 */
    @Excel(name = "连续天数")
    private int continuousDay;

    private Long topId;
    private Long managerId;
    private Long agentId;
    private String userName;
    private String topName;
    private String managerName;
    private String agentName;

    public void setSignId(Long signId)
    {
        this.signId = signId;
    }

    public Long getSignId()
    {
        return signId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setSginTime(String sginTime)
    {
        this.sginTime = sginTime;
    }

    public String getSginTime()
    {
        return sginTime;
    }
    public void setSginYear(int sginYear)
    {
        this.sginYear = sginYear;
    }

    public int getSginYear()
    {
        return sginYear;
    }
    public void setSginMonth(int sginMonth)
    {
        this.sginMonth = sginMonth;
    }

    public int getSginMonth()
    {
        return sginMonth;
    }
    public void setSginDay(int sginDay)
    {
        this.sginDay = sginDay;
    }

    public int getSginDay()
    {
        return sginDay;
    }
    public void setContinuousDay(int continuousDay)
    {
        this.continuousDay = continuousDay;
    }

    public int getContinuousDay()
    {
        return continuousDay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("signId", getSignId())
            .append("userId", getUserId())
            .append("sginTime", getSginTime())
            .append("sginYear", getSginYear())
            .append("sginMonth", getSginMonth())
            .append("sginDay", getSginDay())
            .append("continuousDay", getContinuousDay())
            .toString();
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
