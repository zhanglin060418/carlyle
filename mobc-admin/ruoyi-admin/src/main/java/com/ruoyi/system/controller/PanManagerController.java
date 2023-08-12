package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.SysUserController;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主页Controller
 *
 * @author ruoyi
 * @date 2023-04-21
 */
@RestController
@RequestMapping("/system/manager")
public class PanManagerController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;



    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    @ApiOperation("Get user list detail")
    @PreAuthorize("@ss.hasPermi('system:manager:list')")
    @GetMapping("/listDetail")
    public TableDataInfo listDetail(SysUser user) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("03")) {
            user.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<SysUser> list = userService.selectManagerList(user);
        return getDataTable(list);
    }
    /**
     * 新增经理
     */
    @PreAuthorize("@ss.hasPermi('system:manager:add')")
    @Log(title = "经理管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        LoginUser currentUser = getLoginUser();
        if(!currentUser.getUserType().equals("03")){
            return error("新增经理'" + user.getUserName() + "'失败，你可以联系代理商添加");
        }
        log.info("***********新增经理" + user);
        if (!userService.checkUserNameUnique(user)) {
            return error("新增经理'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("新增经理'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增经理'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUserId().toString());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUserType("02");
        user.setDeptId((long) 105);
        user.setRoleId(4L);
        user.setAgentId(currentUser.getUserId());
        int i = userService.insertUserSys(user);
        return toAjax(i);
    }

    /**
     * 修改经理
     */
    @PreAuthorize("@ss.hasPermi('system:manager:edit')")
    @Log(title = "经理管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return error("修改经理'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("修改经理'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改经理'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 重置密码
     */

    @PreAuthorize("@ss.hasPermi('system:manager:resetPwd')")
    @Log(title = "经理管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        System.out.println(user.getPassword());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }
    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:manager:edit')")
    @Log(title = "经理管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }


    /**
     * 根据经理编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:manager:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
        }
        return ajax;
    }


}
