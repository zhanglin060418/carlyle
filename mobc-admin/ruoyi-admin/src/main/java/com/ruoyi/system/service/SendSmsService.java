package com.ruoyi.system.service;

/**
 * 主页Service接口
 *
 * @author ruoyi
 * @date 2023-05-09
 */
public interface SendSmsService
{
	public String sendSms(String phoneNo, String digitCode);
	public String sendSmsBuka(String phoneNo, String digitCode);
	public String verifyIsPhone(String phoneNo);
}
