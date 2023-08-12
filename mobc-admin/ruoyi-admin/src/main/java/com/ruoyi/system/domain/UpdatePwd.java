package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户余额对象 pan_user_balance
 * 
 * @author ruoyi
 * @date 2023-03-28
 */
public class UpdatePwd extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "Raw")
    private String rawPwd;

    /** 余额 */
    @Excel(name = "New")
    private String newPwd;
    
    
    @Excel(name = "UserName")
    private String userName;
    
    
    public void setRawPwd(String rawPwd) 
    {
        this.rawPwd = rawPwd;
    }

    public String getRawPwd() 
    {
        return rawPwd;
    }
    
    public void setNewPwd(String newPwd) 
    {
        this.newPwd = newPwd;
    }

    public String getNewPwd() 
    {
        return newPwd;
    }
    
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("rawPwd", getRawPwd())
            .append("newPwd", getNewPwd())
            .append("userName", getUserName())
            .toString();
    }
}
