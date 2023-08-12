package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Api
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService sysUserService;

    private static final Logger logger = LoggerFactory.getLogger(SysLoginService.class);
    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation("Login")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {

        logger.info("*********SysLoginService login  loginBody:"+ JSONObject.toJSONString(loginBody));
        AjaxResult ajax = AjaxResult.success();
        String username = loginBody.getUsername();
        if (StringUtils.isNotEmpty(username)) {
            SysUser sysUser = sysUserService.selectUserByUserName(username);
            // 用户不存在
            if (sysUser == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
                throw new UserNotExistsException();
            }
            // 不是APP端用户
            if (sysUser.getUserType().equals("00")||sysUser.getUserType().equals("02")||sysUser.getUserType().equals("03")) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
                throw new UserNotExistsException();
            }
            // 状态已经停用
            if (sysUser.getStatus().equals("1")) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
                throw new UserNotExistsException();
            }
            String token = loginService.login(username, loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid());
            ajax.put(Constants.TOKEN, token);
        }

        return ajax;
    }

    @ApiOperation("sysLogin")
    @PostMapping("/sysLogin")
    public AjaxResult   sysLogin(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        String username = loginBody.getUsername();

        // 校验是不是后台用户
        if (StringUtils.isNotEmpty(username)) {
            SysUser sysUser = sysUserService.selectUserByUserName(username);
            if (sysUser == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
                throw new UserNotExistsException();
            }
            if (sysUser.getUserType().equals("10")) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
                throw new UserNotExistsException();
            }
            if (sysUser.getStatus().equals("1")) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
                throw new UserNotExistsException();
            }
            String token = loginService.login(username, loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid());
            ajax.put(Constants.TOKEN, token);

        }
        return ajax;
    }

    @PostMapping("/refreshToken")
    public AjaxResult refreshToken(String refreshToken) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        System.out.println(SecurityUtils.getLoginUser());
        AjaxResult ajax = AjaxResult.success();
        return ajax;
    }

    @GetMapping("getUserData")
    public AjaxResult getUserData() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        SysUser currUser = sysUserService.selectUserById(user.getUserId());
        //查找业务员信息
        SysUser clerkInfo = sysUserService.slectClerkByUserId(user.getUserId());
        AjaxResult ajax = AjaxResult.success();
        if (clerkInfo != null) {
            ajax.put("clerkWhatsApp", clerkInfo.getWhatsApp());
            ajax.put("clerkTelegram", clerkInfo.getTelegram());
        }
        ajax.put("user_id", currUser.getUserId());
        ajax.put("username", currUser.getUserName());
        ajax.put("inviteCode", currUser.getInviteCode());
        ajax.put("headImg", currUser.getAvatar());
        ajax.put("email", currUser.getEmail());
        ajax.put("telegram", currUser.getTelegram());
        ajax.put("whatsApp", currUser.getWhatsApp());
        boolean hasPayPwd = false;
        if (currUser.getPayPassword() != null)
            hasPayPwd = true;
        ajax.put("hasPayPwd", hasPayPwd);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
