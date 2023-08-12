package com.ruoyi.system.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.ScenesVerifyCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.PhoneVerifyCode;
import com.ruoyi.system.service.IPhoneVerifyCodeService;

import cn.hutool.core.date.DateTime;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2023-04-30
 */
@RestController
@RequestMapping("/system/code")
public class PhoneVerifyCodeController extends BaseController
{
    @Autowired
    private IPhoneVerifyCodeService phoneVerifyCodeService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(PhoneVerifyCode phoneVerifyCode)
    {
        startPage();
        List<PhoneVerifyCode> list = phoneVerifyCodeService.selectPhoneVerifyCodeList(phoneVerifyCode);
        return getDataTable(list);
    }

//    @PreAuthorize("@ss.hasPermi('system:code:list')")
    @GetMapping("/send")
    public AjaxResult sendCode(@RequestParam String phoneNo)
    {
    	// generate code
    	Random rnd = new Random();
    	int number = rnd.nextInt(999999);
    	String digitCode = String.format("%06d", number);

    	// send the code to the phone no

    	// create / update verify code
    	PhoneVerifyCode phoneVerifyCode = phoneVerifyCodeService.selectPhoneVerifyCodeByPhoneNo(phoneNo);
    	if (phoneVerifyCode != null) {
    		phoneVerifyCode.setVerifyCode(digitCode);
        	phoneVerifyCode.setExpiredAt(System.currentTimeMillis() + 10 * 60 * 1000L);
        	phoneVerifyCodeService.updatePhoneVerifyCode(phoneVerifyCode);
    	} else {
    		phoneVerifyCode = new PhoneVerifyCode();
    		phoneVerifyCode.setPhoneNo(phoneNo);
    		phoneVerifyCode.setVerifyCode(digitCode);
        	phoneVerifyCode.setExpiredAt(System.currentTimeMillis() + 10 * 60 * 1000L);
        	phoneVerifyCodeService.insertPhoneVerifyCode(phoneVerifyCode);
    	}

    	AjaxResult ajax = AjaxResult.success();
		ajax.put("phoneVerifyCode", phoneVerifyCode);

		return ajax;
    }

    @GetMapping("/verify")
    public AjaxResult verifyCode(@RequestParam String phoneNo, @RequestParam String verifyCode) {
    	AjaxResult ajax = AjaxResult.success();

		// query verify code
		PhoneVerifyCode phoneVerifyCode = phoneVerifyCodeService.selectPhoneVerifyCodeByPhoneNo(phoneNo);
		if(phoneVerifyCode == null) {
			ajax = AjaxResult.error();
			ajax.put("msg", "Please click phone verify button and receive the verify code");
		}
		else if (!phoneVerifyCode.getVerifyCode().equals(verifyCode)) {
			System.out.println(phoneVerifyCode.getVerifyCode());
			System.out.println(verifyCode);
			ajax = AjaxResult.error();
			ajax.put("msg", "Phone verify code invalid!");
		} else if (phoneVerifyCode.getExpiredAt() <= System.currentTimeMillis()) {
			ajax = AjaxResult.error();
			ajax.put("msg", "Verify code expired. Please receive verify code!");
		}
		return ajax;
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PhoneVerifyCode phoneVerifyCode)
    {
        List<PhoneVerifyCode> list = phoneVerifyCodeService.selectPhoneVerifyCodeList(phoneVerifyCode);
        ExcelUtil<PhoneVerifyCode> util = new ExcelUtil<PhoneVerifyCode>(PhoneVerifyCode.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:code:query')")
    @GetMapping(value = "/{phoneNo}")
    public AjaxResult getInfo(@PathVariable("phoneNo") String phoneNo)
    {
        return success(phoneVerifyCodeService.selectPhoneVerifyCodeByPhoneNo(phoneNo));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:code:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PhoneVerifyCode phoneVerifyCode)
    {
        return toAjax(phoneVerifyCodeService.insertPhoneVerifyCode(phoneVerifyCode));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:code:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PhoneVerifyCode phoneVerifyCode)
    {
        return toAjax(phoneVerifyCodeService.updatePhoneVerifyCode(phoneVerifyCode));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:code:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{phoneNos}")
    public AjaxResult remove(@PathVariable String[] phoneNos)
    {
        return toAjax(phoneVerifyCodeService.deletePhoneVerifyCodeByPhoneNos(phoneNos));
    }


    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:scenesVerifyCodeList')")
    @GetMapping("/scenesVerifyCodeList")
    public TableDataInfo scenesVerifyCodeList(ScenesVerifyCode phoneVerifyCode)
    {
        startPage();
        List<ScenesVerifyCode> list = phoneVerifyCodeService.selectScenesVerifyCodeList(phoneVerifyCode);
        return getDataTable(list);
    }
}
