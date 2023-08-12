package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@Api
@RestController
public class SysRegisterController extends BaseController {

	@Autowired
	private SysRegisterService registerService;

	@Autowired
	private ISysConfigService configService;



	@ApiOperation(value = "Register")
	@PostMapping("/register")
	public AjaxResult register(@RequestBody RegisterBody user)
	{
//		if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
//		{
//			return error("当前系统没有开启注册功能！");
//		}
		String msg = registerService.register(user);
		return StringUtils.isEmpty(msg) ? success() : error(msg);
	}


	@GetMapping("/registerBonusValue")
	public AjaxResult registerBonusValue(){
		AjaxResult bonusAjax =AjaxResult.success();
		// 注册赠送最小金额 200
		Double registerbBonusMin = Double.parseDouble(configService.selectConfigByKey("sign_up_bonus_min"));
		//注册赠送最大金额 300
		Double registerbBonusMan = Double.parseDouble(configService.selectConfigByKey("sign_up_bonus_max"));
		bonusAjax.put("registerbBonusMin",registerbBonusMin);
		bonusAjax.put("registerbBonusMan",registerbBonusMan);
		return bonusAjax;
	}
}
