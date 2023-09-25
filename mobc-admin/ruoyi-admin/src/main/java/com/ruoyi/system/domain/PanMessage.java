package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 社区信息对象 pan_message
 *
 * @author ruoyi
 * @date 2023-09-23
 */
public class PanMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long messageId;


    private Long userId;

    private Long topId;

    private Long managerId;

    private Long agentId;

    /** 用户编号 */
    @Excel(name = "用户账号")
    private String userName;

    private String topName;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String coverImage;

    private String headImg;

    /** 评论数 */
    @Excel(name = "评论数")
    private int commentNum;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private int likesNum;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    private int size;

    private int currentPage;

    private boolean likesbefore;

    public boolean isLikesbefore() {
        return likesbefore;
    }

    public void setLikesbefore(boolean likesbefore) {

        this.likesbefore = likesbefore;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public Long getMessageId()
    {
        return messageId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setCoverImage(String coverImage)
    {
        this.coverImage = coverImage;
    }

    public String getCoverImage()
    {
        return coverImage;
    }
    public void setCommentNum(int commentNum)
    {
        this.commentNum = commentNum;
    }

    public int getCommentNum()
    {
        return commentNum;
    }
    public void setLikesNum(int likesNum)
    {
        this.likesNum = likesNum;
    }

    public int getLikesNum()
    {
        return likesNum;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("userId", getUserId())
            .append("content", getContent())
            .append("coverImage", getCoverImage())
            .append("createTime", getCreateTime())
            .append("commentNum", getCommentNum())
            .append("likesNum", getLikesNum())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("status", getStatus())
            .toString();
    }
}
