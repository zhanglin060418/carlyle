package com.ruoyi.framework.web.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.TransHistory;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.IsIncome;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class SysRegisterService {
	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private RedisCache redisCache;

	private static final Logger logger = LoggerFactory.getLogger(SysRegisterService.class);
	/**
	 * 注册
	 */
	@Transactional
	public String register(RegisterBody registerBody) {
		logger.info("****** SysRegisterService  start registerBody:"+ JSONObject.toJSONString(registerBody));
		String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
		String inviteCode = registerBody.getInviteCode();
		SysUser sysUser = new SysUser();
		sysUser.setUserName(username);
		// 验证码开关
		boolean captchaEnabled = configService.selectCaptchaEnabled();
		if (captchaEnabled) {
			validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
		}

		if (StringUtils.isEmpty(username)) {
			msg = "Name can not be empty";
		} else if (StringUtils.isEmpty(password)) {
			msg = "Password can not be empty";
		}else if (StringUtils.isEmpty(inviteCode)) {
			msg = "InviteCode can not be empty";
		} else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
				|| username.length() > UserConstants.USERNAME_MAX_LENGTH) {
			msg = "Account length must be between 5 and 20 characters";
		} else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
				|| password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
			msg = "Password length must be between 5 and 20 characters";
		} else if (!userService.checkUserNameUnique(sysUser)) {
			msg = "Save user'" + username + "'fail,account already exists";
		} else {
			SysUser parentUser = userService.selectUserByInviteCode(inviteCode);
			if (parentUser == null) {
				return "Invalid invite code";
			}
			if(parentUser.getIsInviteCode().equals("1")){
				return  "Invite code not available";
			}
			sysUser.setTopId(parentUser.getTopId() == null ? parentUser.getUserId() : parentUser.getTopId());
			sysUser.setAgentId(parentUser.getAgentId());
			sysUser.setManagerId(parentUser.getManagerId());
			sysUser.setParentId(parentUser.getUserId());
			sysUser.setGrandId(parentUser.getParentId());
			sysUser.setGreatGrandId(parentUser.getGrandId());
			sysUser.setNickName(username);
			sysUser.setPhonenumber(username);
			sysUser.setPassword(SecurityUtils.encryptPassword(password));

			sysUser.setInviteCode(RandomStringUtils.randomAlphabetic(10));
			sysUser.setUserType("10");
			sysUser.setRoleIds(new Long[]{2L});
			sysUser.setDeptId((long) 105);
			logger.info("******SysRegisterService User Info:"+ JSONObject.toJSONString(sysUser));
			 userService.registerUser(sysUser);

			/**
			 * 新增user_asset表记录
			 *
			 */
			SysUser currUser = userService.selectUserByUserName(username);
			SysUser userAsset = new SysUser();
			userAsset.setUserId(currUser.getUserId());
			userService.insertPanUserAsset(userAsset);

			int registerbBonusMin = Integer.parseInt(configService.selectConfigByKey("sign_up_bonus_min"));
			int registerbBonusMan = Integer.parseInt(configService.selectConfigByKey("sign_up_bonus_max"));
			int registerUpBonusRate = Integer.parseInt(configService.selectConfigByKey("sign_up_bonus_rate"));

			/**
			 * 新增 user_balance
			 */
			//int ax = (int) (registerbBonusMin + Math.random() * ((registerbBonusMan-registerbBonusMin)+1));
			int ax  = DateUtils.getProportionRandom(registerbBonusMin,registerbBonusMan,registerUpBonusRate);
			BigDecimal amount =  new BigDecimal(ax).multiply(new BigDecimal(100));
			SysUser userBalance = new SysUser();
			userBalance.setUserId(currUser.getUserId());
			userBalance.setBalance(amount);
			userBalance.setAvailableAmt(amount);
			logger.info("******SysRegisterService UserBalance Info:"+ JSONObject.toJSONString(userBalance));
			userService.insertPanUserBalance(userBalance);

			/**
			 * 新增交易记录
			 */
			TransHistory transHistory = new TransHistory();
			transHistory.setOrUserId(currUser.getUserId());
			transHistory.setAmount(amount);
			transHistory.setUserId(currUser.getUserId());
			transHistory.setTransactionType(TransType.Register_Reward.toString());
			transHistory.setIsIncome(IsIncome.Y.toString());
			transHistory.setBillType(BillType.IN.toString());
			transHistory.setRemark("注册奖励");
			transHistory.setAmountBefore(new BigDecimal(0));
			transHistory.setAmountAfter(amount);
			logger.info("******SysRegisterService TransHistory Info:"+ JSONObject.toJSONString(transHistory));
			int regFlag = 	userService.insertTransHistory(transHistory);
			if (regFlag<1) {
				msg = "Registration failed, please try again later";
			} else {
				AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
						MessageUtils.message("user.register.success")));
			}
		}
		return msg;
	}

		/**
         * 校验验证码
         *
         * @param username 用户名
         * @param code     验证码
         * @param uuid     唯一标识
         * @return 结果
         */
	public void validateCaptcha(String username, String code, String uuid) {
		String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
		String captcha = redisCache.getCacheObject(verifyKey);
		redisCache.deleteObject(verifyKey);
		if (captcha == null) {
			throw new CaptchaExpireException();
		}
		if (!code.equalsIgnoreCase(captcha)) {
			throw new CaptchaException();
		}
	}
}
