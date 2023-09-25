package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.indo.payment.IndoPaymentService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanChannel;
import com.ruoyi.system.service.IPanChannelService;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;

/**
 * 支付通道Controller
 *
 * @author ruoyi
 * @date 2023-04-24
 */
@RestController
@RequestMapping("/system/channel")
public class PanChannelController extends BaseController
{
    @Autowired
    private IPanChannelService panChannelService;

	@Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IndoPaymentService indoPaymentService;

    /**
     * 查询支付通道列表
     */
    @GetMapping("/getPaymentRules")
    public AjaxResult getPayRules() {
    	AjaxResult ajax = AjaxResult.success();
    	//PanChannel panChannel = panChannelService.selectPanChannelByStatus();

    	Double inMinAmount = Double.parseDouble(sysConfigService.selectConfigByKey("Minimun_recharge_amount"));
    	Double inMaxAmount = Double.parseDouble(sysConfigService.selectConfigByKey("maxium_recharge_amount"));
    	Double outMinAmount = Double.parseDouble(sysConfigService.selectConfigByKey("min_withddrawal_amount"));
    	Double outMaxAmount = Double.parseDouble(sysConfigService.selectConfigByKey("max_withdrawal_amount"));
    	Double outFeeRate = Double.parseDouble(sysConfigService.selectConfigByKey("withdrawal_fee"));
    	//ajax.put("inFeeRate", panChannel.getInFeeRate());
    	ajax.put("outFeeRate", outFeeRate);
    	ajax.put("inMaxAmount", inMaxAmount*100);
    	ajax.put("inMinAmount", inMinAmount*100);
    	ajax.put("outMinAmount", outMinAmount*100);
    	ajax.put("outMaxAmount", outMaxAmount*100);
        //这里打算取一下不同的通道方式 需要能够支持多个
        PanChannel panChannel = new PanChannel();
        panChannel.setStatus(UserStatus.OK.getCode());
        List<PanChannel> list = panChannelService.selectPanChannelListByRecharge(panChannel);
        ajax.put("channelList", list);

        return ajax;
    }


    @GetMapping("/getChannelList")
    public AjaxResult getChannelList() {
        AjaxResult ajax = AjaxResult.success();
        PanChannel panChannel = new PanChannel();
        List<PanChannel> list = panChannelService.selectPanChannelListByRecharge(panChannel);
        ajax.put("data", list);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('system:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanChannel panChannel)
    {
        startPage();
        List<PanChannel> list = panChannelService.selectPanChannelList(panChannel);
        list.stream().forEach(panChannel1 -> {
            try {
                panChannel1.setBalance(indoPaymentService.queryBalance(panChannel1.getChannelId()+"").toJSONString());
                //panChannel1.setBalance(new JSONObject().toJSONString());
                panChannel1.setJsonParam(new JSONObject().toJSONString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return getDataTable(list);
    }



    @PreAuthorize("@ss.hasPermi('system:channel:queryBalance')")
    @PostMapping("/queryBalance")
    public AjaxResult queryBalance(PanChannel panChannel)
    {

        if(StringUtils.isEmpty(panChannel.getChannelId()+"")){
            return  error("错误参数");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject =  indoPaymentService.queryBalance(panChannel.getChannelId()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(jsonObject);
    }


    @PreAuthorize("@ss.hasPermi('system:channel:queryBalance')")
    @PostMapping("/setProxy")
    public AjaxResult setProxy(@RequestBody PanChannel panChannel)
    {
        if(StringUtils.isEmpty(panChannel.getChannelId()+"")){
            return  error("错误参数");
        }
        PanChannel panChannelT = panChannelService.selectPanChannelByChannelId(panChannel.getChannelId());
        //现状是1（关闭） 然后被点击了，增明要启用
        if(panChannelT.getIsProxy().equals("1")){
            return toAjax(this.panChannelService.setOnlyOneProxy(panChannel.getChannelId()));
        }
        //现状是0（启用） 然后被点击了，增明要关闭
        if(panChannelT.getIsProxy().equals("0")){
            return toAjax(this.panChannelService.resetAllProxy());
        }
       return toAjax(1);
    }



    /**
     * 导出支付通道列表
     */
    @PreAuthorize("@ss.hasPermi('system:channel:export')")
    @Log(title = "支付通道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanChannel panChannel)
    {
        List<PanChannel> list = panChannelService.selectPanChannelList(panChannel);
        ExcelUtil<PanChannel> util = new ExcelUtil<PanChannel>(PanChannel.class);
        util.exportExcel(response, list, "支付通道数据");
    }

    /**
     * 获取支付通道详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:channel:query')")
    @GetMapping(value = "/{channelId}")
    public AjaxResult getInfo(@PathVariable("channelId") Long channelId)
    {
        PanChannel panChannel = panChannelService.selectPanChannelByChannelId(channelId);
        return success(panChannel);
    }

    /**
     * 新增支付通道
     */
    @PreAuthorize("@ss.hasPermi('system:channel:add')")
    @Log(title = "支付通道", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PanChannel panChannel)
    {
        return toAjax(panChannelService.insertPanChannel(panChannel));
    }

    /**
     * 修改支付通道
     */
    @PreAuthorize("@ss.hasPermi('system:channel:edit')")
    @Log(title = "支付通道", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanChannel panChannel)
    {
        int id =  panChannelService.updatePanChannel(panChannel);
        return toAjax(id);
    }

    /**
     * 删除支付通道
     */
    @PreAuthorize("@ss.hasPermi('system:channel:remove')")
    @Log(title = "支付通道", businessType = BusinessType.DELETE)
	@DeleteMapping("/{channelIds}")
    public AjaxResult remove(@PathVariable Long[] channelIds)
    {
       int id =  panChannelService.deletePanChannelByChannelIds(channelIds);
        return toAjax(id);
    }
}
