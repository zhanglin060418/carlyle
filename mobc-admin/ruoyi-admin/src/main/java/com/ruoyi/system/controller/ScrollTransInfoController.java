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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.ScrollTransInfo;
import com.ruoyi.system.service.IScrollTransInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 滚屏交易信息Controller
 * 
 * @author ruoyi
 * @date 2023-06-05
 */
@RestController
@RequestMapping("/system/scrollTransInfo")
public class ScrollTransInfoController extends BaseController
{
    @Autowired
    private IScrollTransInfoService scrollTransInfoService;

    /**
     * 查询滚屏交易信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:scrollTransInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScrollTransInfo scrollTransInfo)
    {
        startPage();
        List<ScrollTransInfo> list = scrollTransInfoService.selectScrollTransInfoList(scrollTransInfo);
        return getDataTable(list);
    }

    /**
     * 导出滚屏交易信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:scrollTransInfo:export')")
    @Log(title = "滚屏交易信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScrollTransInfo scrollTransInfo)
    {
        List<ScrollTransInfo> list = scrollTransInfoService.selectScrollTransInfoList(scrollTransInfo);
        ExcelUtil<ScrollTransInfo> util = new ExcelUtil<ScrollTransInfo>(ScrollTransInfo.class);
        util.exportExcel(response, list, "滚屏交易信息数据");
    }

    /**
     * 获取滚屏交易信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:scrollTransInfo:query')")
    @GetMapping(value = "/{transId}")
    public AjaxResult getInfo(@PathVariable("transId") Long transId)
    {
        return success(scrollTransInfoService.selectScrollTransInfoByTransId(transId));
    }

    /**
     * 新增滚屏交易信息
     */
    @PreAuthorize("@ss.hasPermi('system:scrollTransInfo:add')")
    @Log(title = "滚屏交易信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScrollTransInfo scrollTransInfo)
    {
        return toAjax(scrollTransInfoService.insertScrollTransInfo(scrollTransInfo));
    }

    /**
     * 修改滚屏交易信息
     */
    @PreAuthorize("@ss.hasPermi('system:scrollTransInfo:edit')")
    @Log(title = "滚屏交易信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScrollTransInfo scrollTransInfo)
    {
        return toAjax(scrollTransInfoService.updateScrollTransInfo(scrollTransInfo));
    }

    /**
     * 删除滚屏交易信息
     */
    @PreAuthorize("@ss.hasPermi('system:scrollTransInfo:remove')")
    @Log(title = "滚屏交易信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{transIds}")
    public AjaxResult remove(@PathVariable Long[] transIds)
    {
        return toAjax(scrollTransInfoService.deleteScrollTransInfoByTransIds(transIds));
    }
}
