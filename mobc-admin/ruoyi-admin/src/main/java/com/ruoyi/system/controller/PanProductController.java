package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.TransStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanProduct;
import com.ruoyi.system.domain.PhoneVerifyCode;
import com.ruoyi.system.domain.ScenesVerifyCode;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 产品Controller
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@Api
@RestController
@RequestMapping("/system/product")
public class PanProductController extends BaseController {

    @Autowired
    private IPanProductService panProductService;

    @Autowired
    private IPhoneVerifyCodeService phoneVerifyCodeService;

    @Autowired
    private SendSmsService sendSmsService;

    @Autowired
    private ISysConfigService sysConfigService;

    @GetMapping("/getRewardProduct")
    public AjaxResult getRewardProduct() {
        AjaxResult ajax = AjaxResult.success();
        List<String> rewardProductIds = new ArrayList<String>();
        rewardProductIds = panProductService.getRewardProductId();
        ajax.put("list", rewardProductIds);
        return ajax;
    }

    @GetMapping("/verify")
    public AjaxResult verifyCode(@RequestParam String phoneNo, @RequestParam String verifyCode,@RequestParam String useMethod) {
        AjaxResult ajax = AjaxResult.success();

        // query verify code
        PhoneVerifyCode phoneVerifyCode = phoneVerifyCodeService.selectPhoneVerifyCodeByPhoneNo(phoneNo);
        if (phoneVerifyCode == null) {
            ajax = AjaxResult.error();
            ajax.put("msg", "Please click phone verify button and receive the verify code");
            return ajax;
        } else if (!phoneVerifyCode.getVerifyCode().equals(verifyCode)) {
            ajax = AjaxResult.error();
            ajax.put("msg", "Phone verify code invalid!");
            return ajax;
        } else if (phoneVerifyCode.getExpiredAt() <= System.currentTimeMillis()) {
            ScenesVerifyCode scenesVerifyCode = new ScenesVerifyCode();
            scenesVerifyCode.setPhoneNo(phoneNo);
            scenesVerifyCode.setVerifyCode(verifyCode);
            scenesVerifyCode.setUseScenes(useMethod);
            scenesVerifyCode.setStatus(TransStatus.FAILED);
            scenesVerifyCode.setRemark("Verify code expired");
            phoneVerifyCodeService.updateScenesVerifyCode(scenesVerifyCode);
            ajax = AjaxResult.error();
            ajax.put("msg", "Verify code expired. Please receive verify code!");
            return ajax;
        }
        ScenesVerifyCode scenesVerifyCode = new ScenesVerifyCode();
        scenesVerifyCode.setPhoneNo(phoneNo);
        scenesVerifyCode.setVerifyCode(verifyCode);
        scenesVerifyCode.setUseScenes(useMethod);
        scenesVerifyCode.setStatus(TransStatus.SUCCESS);
        phoneVerifyCodeService.updateScenesVerifyCode(scenesVerifyCode);
        return ajax;
    }

    @GetMapping("/send")
    public AjaxResult sendCode(@RequestParam String phoneNo,@RequestParam String useMethod) {
        AjaxResult ajax = AjaxResult.success();
        String digitCode = "";
        String currEnvironment = sysConfigService.selectConfigByKey("Current_Environment");

        String result ="1";
        /**
         * 判断是不是测试环境
         */
        if (currEnvironment.equals("TEST")) {
            digitCode = "123456";
            result ="0";
        } else {
            Random rnd = new Random();
            int number = rnd.nextInt(9999);
            digitCode = String.format("%04d", number);
        }
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
        String msg = "";
        if (!currEnvironment.equals("TEST")) {
            result = sendSmsService.sendSmsBuka(phoneNo, digitCode);
            if (!result.equals("0")) {
                if(result.equals("-8")){
                    ajax = AjaxResult.error();
                    msg = "number is empty";
                    ajax.put("msg",msg);
                    return ajax;
                }
                else if(result.equals("-9")){
                    ajax = AjaxResult.error();
                    msg = "nabnormal number";
                    ajax.put("msg",msg);
                    return ajax;
                }
                else if (result.equals("-18")){
                    ajax = AjaxResult.error();
                    msg = "Interface exception, please try again later";
                    ajax.put("msg",msg);
                    return ajax;
                } else{
                    ajax = AjaxResult.error();
                    msg = "Network anomaly, please try again later";
                    ajax.put("msg",msg);
                    return ajax;
                }
            }
        }
        ScenesVerifyCode scenesVerifyCode = new ScenesVerifyCode();
        scenesVerifyCode.setPhoneNo(phoneNo);
        scenesVerifyCode.setVerifyCode(digitCode);
        scenesVerifyCode.setUseScenes(useMethod);
        phoneVerifyCodeService.insertScenesVerifyCode(scenesVerifyCode);
        ajax.put("msg",msg);
        return ajax;
    }

    @GetMapping("getProductDetail")
    public AjaxResult getProductDetail(@RequestParam Long productId) {
        PanProduct product = panProductService.selectPanProductById(productId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("name", product.getName());
        ajax.put("nameIn", product.getNameIn());
        ajax.put("nameEn", product.getNameEn());
        ajax.put("nameRu", product.getNameRu());
        ajax.put("coverImage", product.getCoverImages());
        return ajax;
    }

    @ApiOperation("Get product list")
    @GetMapping("/getProductList")
    public AjaxResult getProductList() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        PanProduct panProduct = new PanProduct();
        panProduct.setUserId(user.getUserId());
        List<PanProduct> list = panProductService.selectPanProductBuyUser(panProduct);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("productList", list);
        return ajax;
    }

    /**
     * 查询产品列表
     */
//    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanProduct panProduct) {
        startPage();
        List<PanProduct> list = panProductService.selectPanProductList(panProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanProduct panProduct) {
        List<PanProduct> list = panProductService.selectPanProductList(panProduct);
        ExcelUtil<PanProduct> util = new ExcelUtil<PanProduct>(PanProduct.class);
        util.exportExcel(response, list, "产品数据");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(panProductService.selectPanProductById(id));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PanProduct panProduct) {
        panProduct.setCreateBy(getUsername());
        return toAjax(panProductService.insertPanProduct(panProduct));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PanProduct panProduct) {
        panProduct.setUpdateBy(getUsername());
        return toAjax(panProductService.updatePanProduct(panProduct));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(panProductService.deletePanProductByIds(ids));
    }
}
