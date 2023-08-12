package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.system.domain.PanBank;
import com.ruoyi.system.service.IPanBankService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 银行名单Controller
 * 
 * @author ruoyi
 * @date 2023-04-09
 */
@RestController
@RequestMapping("/system/bank")
public class PanBankController extends BaseController
{
    @Autowired
    private IPanBankService panBankService;

    /**
     * 查询银行名单列表
     */
    @PreAuthorize("@ss.hasPermi('system:bank:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanBank panBank)
    {
        startPage();
        List<PanBank> list = panBankService.selectPanBankList(panBank);
        return getDataTable(list);
    }
    
	@GetMapping("/getBankList")
	public AjaxResult getBankList() {
		PanBank panBank = new PanBank();
        List<PanBank> list = panBankService.selectPanBankList(panBank);
        AjaxResult ajax = AjaxResult.success();
		ajax.put(AjaxResult.DATA_TAG, list);
		return ajax;
	}

    /**
     * 导出银行名单列表
     */
    @PreAuthorize("@ss.hasPermi('system:bank:export')")
    @Log(title = "银行名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanBank panBank)
    {
        List<PanBank> list = panBankService.selectPanBankList(panBank);
        ExcelUtil<PanBank> util = new ExcelUtil<PanBank>(PanBank.class);
        util.exportExcel(response, list, "银行名单数据");
    }

    /**
     * 获取银行名单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bank:query')")
    @GetMapping(value = "/{bankId}")
    public AjaxResult getInfo(@PathVariable("bankId") Long bankId)
    {
        return success(panBankService.selectPanBankByBankId(bankId));
    }

    /**
     * 新增银行名单
     */
    @PreAuthorize("@ss.hasPermi('system:bank:add')")
    @Log(title = "银行名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PanBank panBank)
    {
        return toAjax(panBankService.insertPanBank(panBank));
    }

    /**
     * 修改银行名单
     */
    @PreAuthorize("@ss.hasPermi('system:bank:edit')")
    @Log(title = "银行名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanBank panBank)
    {
        return toAjax(panBankService.updatePanBank(panBank));
    }

    /**
     * 删除银行名单
     */
    @PreAuthorize("@ss.hasPermi('system:bank:remove')")
    @Log(title = "银行名单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bankIds}")
    public AjaxResult remove(@PathVariable Long[] bankIds)
    {
        return toAjax(panBankService.deletePanBankByBankIds(bankIds));
    }
}
