package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.xss.Xss;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;
    private String  sortType ;
    private String propName;

    private Long parentId;
    /**
     *  经理
     */
    private Long managerId;
    private Long agentId;
    private String agentName;
    private String managerName;

    private Long grandId;
    private String grandUsername;

    private Long greatGrandId;
    private String greatGrandUsername;

    private Long topId;

    private String inviteCode;

    private String userType;

    private BigDecimal balance;

    private BigDecimal availableAmt;


    /** 部门ID */
    private Long deptId;

    /** 用户账号 */
    @Excel(name = "登录名称")
    private String userName;

    @Excel(name = "推荐人")
    private String parentUsername;
    /** 用户昵称 */
    private String nickName;

    @Excel(name = "业务员")
    private String topName;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    @Excel(name = "Whatsapp")
    private String whatsapp;

    @Excel(name = "Telegram")
    private String telegram;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    private String isWithdarw;
    private String isRebate;
    private String isInviteCode;

    /** 密码 */
    private String password;

    private String payPassword;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /** 最后登录时间 */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private String loginDate;

    private String firstDeposit;

    /** 部门对象 */
    private SysDept dept;

    /** 角色对象 */
    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;

    /** 角色ID */
    private Long roleId;

    private BigDecimal assetBalance;
    private Long rechargeAmt;
    private Long withdrawAmt;
    private Long purchaseAmt;
    private Long rewardAmt;
    private Long rewardProductAmt;
    private Long noBackProductAmt;
    private Integer vipLevel;
    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    private int size;
    private int currentPage;
    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
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

    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 5, max = 30, message = "用户账号长度在5-30个字符之间")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Size(min = 0, max = 50, message = "WhatsApp长度不能超过50个字符")
    public String getWhatsApp()
    {
        return whatsapp;
    }

    public void setWhatsApp(String whatsapp)
    {
        this.whatsapp= whatsapp;
    }


    @Size(min = 0, max = 50, message = "Telegram长度不能超过50个字符")
    public String getTelegram()
    {
        return telegram;
    }

    public void setTelegram(String telegram)
    {
        this.telegram= telegram;
    }
    @Size(min = 0, max = 15, message = "手机号码长度不能超过15个字符")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }



    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public String getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(String loginDate)
    {
        this.loginDate = loginDate;
    }

    public SysDept getDept()
    {
        return dept;
    }

    public void setDept(SysDept dept)
    {
        this.dept = dept;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getGrandId() {
		return grandId;
	}

	public void setGrandId(Long grandId) {
		this.grandId = grandId;
	}

	public Long getGreatGrandId() {
		return greatGrandId;
	}

	public void setGreatGrandId(Long greatGrandId) {
		this.greatGrandId = greatGrandId;
	}

	public Long getTopId() {
		return topId;
	}

	public void setTopId(Long topId) {
		this.topId = topId;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("email", getEmail())
            .append("whatsapp", getWhatsApp())
            .append("telegram", getTelegram())
            .append("phonenumber", getPhonenumber())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("payPassword", getPayPassword())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("dept", getDept())
            .append("userType", getUserType())
            .append("parentId", getParentId())
            .append("grandId", getGrandId())
            .append("greatGrandId", getGreatGrandId())
            .append("inviteCode", getInviteCode())
            .append("topId", getTopId())
            .append("roleIds", getRoleIds())
            .append("roles", getRoles())
            .toString();
    }

	public String getFirstDeposit() {
		return firstDeposit;
	}

	public void setFirstDeposit(String firstDeposit) {
		this.firstDeposit = firstDeposit;
	}

	public String getParentUsername() {
		return parentUsername;
	}

	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}

	public String getGrandUsername() {
		return grandUsername;
	}

	public void setGrandUsername(String grandUsername) {
		this.grandUsername = grandUsername;
	}

	public String getGreatGrandUsername() {
		return greatGrandUsername;
	}

	public void setGreatGrandUsername(String greatGrandUsername) {
		this.greatGrandUsername = greatGrandUsername;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(BigDecimal availableAmt) {
        this.availableAmt = availableAmt;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }


    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

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

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getIsWithdarw() {
        return isWithdarw;
    }

    public void setIsWithdarw(String isWithdarw) {
        this.isWithdarw = isWithdarw;
    }

    public String getIsRebate() {
        return isRebate;
    }

    public void setIsRebate(String isRebate) {
        this.isRebate = isRebate;
    }

    public String getIsInviteCode() {
        return isInviteCode;
    }

    public void setIsInviteCode(String isInviteCode) {
        this.isInviteCode = isInviteCode;
    }

    public BigDecimal getAssetBalance() {
        return assetBalance;
    }

    public void setAssetBalance(BigDecimal assetBalance) {
        this.assetBalance = assetBalance;
    }

    public Long getRechargeAmt() {
        return rechargeAmt;
    }

    public void setRechargeAmt(Long rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
    }

    public Long getWithdrawAmt() {
        return withdrawAmt;
    }

    public void setWithdrawAmt(Long withdrawAmt) {
        this.withdrawAmt = withdrawAmt;
    }

    public Long getPurchaseAmt() {
        return purchaseAmt;
    }

    public void setPurchaseAmt(Long purchaseAmt) {
        this.purchaseAmt = purchaseAmt;
    }

    public Long getRewardAmt() {
        return rewardAmt;
    }

    public void setRewardAmt(Long rewardAmt) {
        this.rewardAmt = rewardAmt;
    }

    public Long getRewardProductAmt() {
        return rewardProductAmt;
    }

    public void setRewardProductAmt(Long rewardProductAmt) {
        this.rewardProductAmt = rewardProductAmt;
    }

    public Long getNoBackProductAmt() {
        return noBackProductAmt;
    }

    public void setNoBackProductAmt(Long noBackProductAmt) {
        this.noBackProductAmt = noBackProductAmt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
