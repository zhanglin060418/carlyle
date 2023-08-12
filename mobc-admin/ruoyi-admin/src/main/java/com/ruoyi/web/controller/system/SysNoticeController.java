package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取登录后弹出通知内容
     * @return
     */
    @GetMapping("/getHomeAnnouncement")
    public AjaxResult getLoginAnnouncement() {

        AjaxResult ajax = AjaxResult.success();
        List<SysNotice> loginAnnouncementList = noticeService.selectNoticeListByhomeAnnouncement();
        SysNotice loginAnnouncement = new SysNotice();
        if (loginAnnouncementList.size()>0) {
            loginAnnouncement = loginAnnouncementList.get(0);
        }
        ajax.put("loginAnnouncement", loginAnnouncement);
        return ajax;
    }

    /**
     * 获取通知公告列表
     */
//    @PreAuthorize("@ss.hasPermi('system:notice:list')")

    @GetMapping("/list")
public TableDataInfo list(SysNotice notice)
{
    startPage();
    notice.setNoticeType("1");
    List<SysNotice> list = noticeService.selectNoticeList(notice);
    return getDataTable(list);
}
    @GetMapping("/listAnnouncement")
    public TableDataInfo listAnnouncement(SysNotice notice)
    {
        startPage();
        notice.setNoticeType("2");
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    @GetMapping("/listActive")
    public AjaxResult listActive(SysNotice notice)
    {
    	AjaxResult ajax = new AjaxResult();
        SysNotice sysNotice = new SysNotice();
        sysNotice.setStatus("0");
        List<SysNotice> list = noticeService.selectActiveList(sysNotice);
        ajax.put("list", list);
        return ajax;
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }


}
