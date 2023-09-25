package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.indo.payment.IndoRechargeResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TransStatus;
import com.ruoyi.system.domain.PanRecharge;
import com.ruoyi.system.domain.PanTransactionHistory;
import com.ruoyi.system.domain.RechargeCreate;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.PanMessage;
import com.ruoyi.system.service.IPanMessageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 社区信息Controller
 *
 * @author ruoyi
 * @date 2023-09-23
 */
@RestController
@RequestMapping("/system/message")
public class PanMessageController extends BaseController
{
    @Autowired
    private IPanMessageService panMessageService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询社区信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanMessage panMessage)
    {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panMessage.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panMessage.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panMessage.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanMessage> list = panMessageService.selectPanMessageList(panMessage);
        return getDataTable(list);
    }

    /**
     * 导出社区信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:message:export')")
    @Log(title = "社区信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanMessage panMessage)
    {
        List<PanMessage> list = panMessageService.selectPanMessageList(panMessage);
        ExcelUtil<PanMessage> util = new ExcelUtil<PanMessage>(PanMessage.class);
        util.exportExcel(response, list, "社区信息数据");
    }

    /**
     * 获取社区信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:message:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(panMessageService.selectPanMessageByMessageId(messageId));
    }


    /**
     * 新增社区信息
     */
    @PostMapping("/createMessage")
    public AjaxResult createMessage(@RequestBody PanMessage panMessage, HttpServletRequest request) {
        logger.info("********社区信息-新增 rechargeCreate:" + JSONObject.toJSONString(panMessage));
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
            if (sysUser.getStatus().equals("1")) {
                ajax.put("msg", "Account has been disabled");
                return ajax;
            }
            try {
                panMessageService.insertPanMessage(panMessage);
                ajax = AjaxResult.success();
            } catch (Exception e) {
                ajax.put("msg", "Network is abnormal, please try again!");
                e.printStackTrace();
            }
        } else {
            ajax.put("msg", "Invalid session or session expired, please log in again.");
        }
        return ajax;
    }


    @GetMapping("/getMessageList")
    public AjaxResult getMessageList(@RequestParam Long userId, @RequestParam int currentPage, @RequestParam int pageSize) {
        PanMessage panMessage = new PanMessage();
        panMessage.setCurrentPage(currentPage);
        panMessage.setSize(pageSize);
        panMessage.setUserId(userId);
        List<PanTransactionHistory> purchaseInterestList = panMessageService.getMessageList(panMessage);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, purchaseInterestList);
        return ajax;
    }
    /**
     * 修改社区信息
     */
    @PreAuthorize("@ss.hasPermi('system:message:edit')")
    @Log(title = "社区信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanMessage panMessage)
    {
        return toAjax(panMessageService.updatePanMessage(panMessage));
    }

    /**
     * 删除社区信息
     */
    @PreAuthorize("@ss.hasPermi('system:message:remove')")
    @Log(title = "社区信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(panMessageService.deletePanMessageByMessageIds(messageIds));
    }

    @PostMapping("/createLikes")
    public AjaxResult createRecharge(@RequestBody PanMessage panMessage, HttpServletRequest request) {
        logger.info("********社区信息-点赞 rechargeCreate:" + JSONObject.toJSONString(panMessage));
        LoginUser loginUser = getLoginUser();
        AjaxResult ajax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            try {
               panMessageService.createLikes(panMessage);
               ajax = AjaxResult.success();
            } catch (Exception e) {
                ajax.put("msg", "Network is abnormal, please try again!");
                e.printStackTrace();
            }
        } else {
            ajax.put("msg", "Invalid session or session expired, please log in again.");
        }
        return ajax;
    }
}
