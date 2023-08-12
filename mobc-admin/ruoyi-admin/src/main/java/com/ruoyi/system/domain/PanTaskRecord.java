package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 执行任务记录表
 * Created by HFY on 2023/5/10
 */
public class PanTaskRecord extends BaseEntity {
    /**
     * 编号
     */
    private String rId;
    /**
     * 任务编号
     */
    private String jobId;
    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务目标方法
     */
    private String invokeTarget;
    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 执行日期
     */
    private String taskDate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("rId", getrId())
                .append("jobId", getJobId())
                .append("jobName", getJobName())
                .append("invokeTarget", getInvokeTarget())
                .append("taskStatus", getTaskStatus())
                .append("taskDate", getTaskDate())
                .toString();
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

}
