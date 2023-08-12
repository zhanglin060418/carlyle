package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanAgentBalance;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 代理商余额
 *
 * @author ruoyi
 * @date 2023-03-28
 */
@RestController
@RequestMapping("/system/agentBalance")
public class PanAgentBalanceController extends BaseController {
    @Autowired
    private IPanAgentBalanceService panAgentBalanceService;
    @Autowired
    private ITransService iTransService;

    @Autowired
    private IPanWithdrawService panWithdrawService;

    /**
     * 查询余额列表
     */

    @PreAuthorize("@ss.hasPermi('system:agentBalance:index')")
    @GetMapping("/listDetail")
    public TableDataInfo listDetail(PanAgentBalance agentBalance) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("03")) {
            agentBalance.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanAgentBalance> list = panAgentBalanceService.selectPanAgentBalanceList(agentBalance);

        list.stream().forEach(userT -> {
            userT.setAvailableAmt(panWithdrawService.getAgentBalance(userT.getAgentId()));
        });
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:agentBalance:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(panAgentBalanceService.selectPanAgentBalanceById(id));
    }

    /**
     * 修改代理商预充值金额
     */
    @PreAuthorize("@ss.hasPermi('system:agentBalance:edit')")
    @Log(title = "修改代理商预充值金额", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanAgentBalance requestAgentBalance) {
        BigDecimal currAmount = requestAgentBalance.getAmount().multiply(new BigDecimal(100));

        requestAgentBalance.setUpdateAt(String.valueOf(getLoginUser().getUserId()));
        logger.info("********PanAgentBalanceController Edit Balance Request:{}", JSONObject.toJSONString(requestAgentBalance));
        AjaxResult ajax = AjaxResult.success();

        if(currAmount.compareTo(new BigDecimal(0))<0){
            ajax = AjaxResult.error();
            ajax.put("msg", "金额异常");
            return ajax;
        }
        try {

            String result = iTransService.editAgentBalance(requestAgentBalance);
            if (!result.equals(MessageStatus.SUCCESS)) {
                ajax = AjaxResult.error();
                ajax.put("msg", "网络异常，请稍后重试！");
            }
        } catch (Exception e) {
            ajax = AjaxResult.error();
            ajax.put("msg", "网络异常，请稍后重试!");
            e.printStackTrace();
        }
        return ajax;
    }

}
