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
import com.ruoyi.system.domain.PanHome;
import com.ruoyi.system.domain.PanProduct;
import com.ruoyi.system.service.IPanHomeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 主页Controller
 * 
 * @author ruoyi
 * @date 2023-04-21
 */
@RestController
@RequestMapping("/system/home")
public class PanHomeController extends BaseController
{
    @Autowired
    private IPanHomeService panHomeService;

    /**
     * 查询主页列表
     */
    @PreAuthorize("@ss.hasPermi('system:home:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanHome panHome)
    {
        startPage();
        List<PanHome> list = panHomeService.selectPanHomeList(panHome);
        return getDataTable(list);
    }
    
    @GetMapping("/getHomeList")
    public AjaxResult getHomeItemList()
    {
        PanHome panHome = new PanHome();
        List<PanHome> list = panHomeService.selectPanHomeList(panHome);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", list);
        return ajax;
    }

    /**
     * 导出主页列表
     */
    @PreAuthorize("@ss.hasPermi('system:home:export')")
    @Log(title = "主页", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanHome panHome)
    {
        List<PanHome> list = panHomeService.selectPanHomeList(panHome);
        ExcelUtil<PanHome> util = new ExcelUtil<PanHome>(PanHome.class);
        util.exportExcel(response, list, "主页数据");
    }

    /**
     * 获取主页详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:home:query')")
    @GetMapping(value = "/{homeId}")
    public AjaxResult getInfo(@PathVariable("homeId") Long homeId)
    {
        return success(panHomeService.selectPanHomeByHomeId(homeId));
    }

    /**
     * 新增主页
     */
    @PreAuthorize("@ss.hasPermi('system:home:add')")
    @Log(title = "主页", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PanHome panHome)
    {
        return toAjax(panHomeService.insertPanHome(panHome));
    }

    /**
     * 修改主页
     */
    @PreAuthorize("@ss.hasPermi('system:home:edit')")
    @Log(title = "主页", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanHome panHome)
    {
        return toAjax(panHomeService.updatePanHome(panHome));
    }

    /**
     * 删除主页
     */
    @PreAuthorize("@ss.hasPermi('system:home:remove')")
    @Log(title = "主页", businessType = BusinessType.DELETE)
	@DeleteMapping("/{homeIds}")
    public AjaxResult remove(@PathVariable Long[] homeIds)
    {
        return toAjax(panHomeService.deletePanHomeByHomeIds(homeIds));
    }
}
