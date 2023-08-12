package com.ruoyi.common.core.domain.entity;

public class SysUserDetail extends SysUser {
	private String parentUsername;
	private String grandUsername;
	private String greatGrandUsername;
	private String topUsername;
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
	public String getTopUsername() {
		return topUsername;
	}
	public void setTopUsername(String topUsername) {
		this.topUsername = topUsername;
	}
}