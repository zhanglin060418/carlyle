package com.ruoyi.web.controller.system.domain;

public class ResetUserPwdPayload {
	
	private String phoneNo;
	
	private String newPwd;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
		
}
