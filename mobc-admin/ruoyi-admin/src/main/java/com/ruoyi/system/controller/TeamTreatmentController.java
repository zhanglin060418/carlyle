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
import com.ruoyi.system.domain.TeamTreatment;
import com.ruoyi.system.service.ITeamTreatmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 主页Controller
 * 
 * @author ruoyi
 * @date 2023-05-03
 */
@RestController
@RequestMapping("/system/treatment")
public class TeamTreatmentController extends BaseController
{
    @Autowired
    private ITeamTreatmentService teamTreatmentService;

    /**
     * 查询主页列表
     */
    @PreAuthorize("@ss.hasPermi('system:treatment:list')")
    @GetMapping("/list")
    public TableDataInfo list(TeamTreatment teamTreatment)
    {
        startPage();
        List<TeamTreatment> list = teamTreatmentService.selectTeamTreatmentList(teamTreatment);
        return getDataTable(list);
    }
    
    @GetMapping("/getList")
    public AjaxResult getList(TeamTreatment teamTreatment)
    {
    	AjaxResult ajax = AjaxResult.success();
        List<TeamTreatment> list = teamTreatmentService.selectTeamTreatmentList(teamTreatment);
        ajax.put("data", list);
        return ajax;
    }

    /**
     * 导出主页列表
     */
    @PreAuthorize("@ss.hasPermi('system:treatment:export')")
    @Log(title = "主页", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TeamTreatment teamTreatment)
    {
        List<TeamTreatment> list = teamTreatmentService.selectTeamTreatmentList(teamTreatment);
        ExcelUtil<TeamTreatment> util = new ExcelUtil<TeamTreatment>(TeamTreatment.class);
        util.exportExcel(response, list, "主页数据");
    }

    /**
     * 获取主页详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:treatment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(teamTreatmentService.selectTeamTreatmentById(id));
    }

    /**
     * 新增主页
     */
    @PreAuthorize("@ss.hasPermi('system:treatment:add')")
    @Log(title = "主页", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeamTreatment teamTreatment)
    {
        return toAjax(teamTreatmentService.insertTeamTreatment(teamTreatment));
    }

    /**
     * 修改主页
     */
    @PreAuthorize("@ss.hasPermi('system:treatment:edit')")
    @Log(title = "主页", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TeamTreatment teamTreatment)
    {
        return toAjax(teamTreatmentService.updateTeamTreatment(teamTreatment));
    }

    /**
     * 删除主页
     */
    @PreAuthorize("@ss.hasPermi('system:treatment:remove')")
    @Log(title = "主页", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(teamTreatmentService.deleteTeamTreatmentByIds(ids));
    }
}
