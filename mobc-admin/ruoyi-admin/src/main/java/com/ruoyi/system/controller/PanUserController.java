package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanUserAsset;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.domain.PanUserSocial;
import com.ruoyi.system.domain.TeamOverview;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.system.SysUserController;
import com.ruoyi.web.controller.system.domain.ResetPwdPayload;
import com.ruoyi.web.controller.system.domain.ResetUserPwdPayload;
import com.ruoyi.web.controller.system.domain.SetPwdPayload;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 主页Controller
 *
 * @author ruoyi
 * @date 2023-04-21
 */
@RestController
@RequestMapping("/system/panUser")
public class PanUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private IPanTransactionHistoryService transService;

    @Autowired
    private IPanUserBalanceService iPanUserBalanceService;
    @Autowired
    private IPanUserAssetService iPanUserAssetService;


    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    /**
     * 根据用户编号获取详细信息
     */
    @ApiOperation("Verify username")
    @GetMapping("/verifyUsername")
    public AjaxResult getVerifyUsername(@RequestParam String username) {
        SysUser user = userService.selectUserByUserName(username);
        AjaxResult ajax = AjaxResult.success();
        if (user == null)
            ajax = AjaxResult.error();
        return ajax;
    }

    @ApiOperation("Get invite code")
    @GetMapping("/getInviteCode")
    public AjaxResult getInviteCode(@RequestParam Long userId) {
        SysUser user = userService.selectUserById(userId);
        AjaxResult ajax = AjaxResult.success();
        String invite_code = user.getInviteCode();
        ajax.put("invite_code", invite_code);
        return ajax;
    }

    @GetMapping("/verifyPayPassword")
    public AjaxResult verifyPayPassword(@RequestParam String password) {
        SysUser user = userService.selectUserById(getLoginUser().getUserId());
        if (SecurityUtils.matchesPassword(password, user.getPayPassword())) {
            AjaxResult ajax = AjaxResult.success();
            return ajax;
        } else {
            throw new ServiceException("Payment password is incorrect!");
        }
    }

    @ApiOperation("Get total team")
    @GetMapping("/getTotalTeam")
    public AjaxResult getTotalTeam(@RequestParam Long userId) {
        SysUser childUser = new SysUser();
        childUser.setParentId(userId);
        List<SysUser> childList = userService.selectUserList(childUser);
        int total_a = childList.size();

        SysUser grandUser = new SysUser();
        grandUser.setGrandId(userId);
        List<SysUser> grandList = userService.selectUserList(grandUser);
        int total_b = grandList.size();

        SysUser greatGrandUser = new SysUser();
        greatGrandUser.setGreatGrandId(userId);
        List<SysUser> greatGrandList = userService.selectUserList(greatGrandUser);
        int total_c = greatGrandList.size();

        List<SysUser> todayChildList = userService.selectAddedTodayChildUserList(childUser);
        List<SysUser> todayGrandList = userService.selectAddedTodayChildUserList(grandUser);
        List<SysUser> todayGreatGrandList = userService.selectAddedTodayChildUserList(greatGrandUser);
        int created_today = todayChildList.size() + todayGrandList.size() + todayGreatGrandList.size();

        int total_people = total_a + total_b + total_c;
        AjaxResult ajax = AjaxResult.success();
        ajax.put("total_a", total_a);
        ajax.put("total_b", total_b);
        ajax.put("total_c", total_c);
        ajax.put("total_people", total_people);
        ajax.put("created_today", created_today);

        return ajax;
    }

    @ApiOperation("Get daily income")
    @GetMapping("/getDailyIncome")
    public AjaxResult getUserDailyIncome(long userId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("daily_income", transService.getDailyIncome(userId));
        return ajax;
    }

    @ApiOperation("Get user list detail")
    @PreAuthorize("@ss.hasPermi('system:panUser:list')")
    @GetMapping("/listDetail")
    public TableDataInfo listDetail(SysUser user) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            user.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            user.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            user.setAgentId(currentUser.getUserId());
        }
        if (StringUtils.isBlank(user.getPropName())) {
            user.setPropName("createTime");
            //descending  ascending
            user.setSortType("descending");
        }
        startPage();
        List<SysUser> list = userService.selectUserListDetail(user);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:panUser:listUserReport')")
    @GetMapping("/listUserReport")
    public TableDataInfo listUserReport(SysUser user) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            user.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            user.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            user.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<SysUser> currUserList = userService.selectUserReportList(user);
        StringJoiner sj = new StringJoiner(",");
        if (currUserList.size() > 0) {
            currUserList.stream().forEach(users -> {
                sj.add(users.getUserId() + "");
            });

            List<TeamOverview> list = transService.getUserTransInfoByIds(sj.toString());
            Map<Long, JSONArray> amtMap = new HashMap<>();
            list.stream().forEach(teamOverview -> {
                Long uId = teamOverview.getUserId();
                if (null == amtMap.get(uId)) {
                    amtMap.put(uId, new JSONArray());
                }
                JSONArray array = amtMap.get(uId);
                JSONObject oneJ = new JSONObject();
                oneJ.put("amt", teamOverview.getTransAmt());
                oneJ.put("type", teamOverview.getTransType());
                array.add(oneJ);
                amtMap.put(uId, array);
            });

            //userId [{"type":"Child_First_Purchase_Reward","amt": 1000},
            //          {"type":"Commission_A_Reward","amt":1500},{}]

            currUserList.stream().forEach(userT -> {
                JSONArray array = amtMap.get(userT.getUserId());
                Long rechargeAmt = 0L;
                Long withdrawAmt = 0L;
                Long purchaseAmt = 0L;
                Long rewardAmt = 0L;
                Long tempReturnAmt = 0L;
                Long rewardProductAmt = 0L;
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String transType = jsonObject.getString("type");
                        Long transAmt = jsonObject.getLong("amt");
                        if (transType.equalsIgnoreCase(TransType.Recharge.name())) {
                            rechargeAmt = transAmt;
                        } else if (transType.equalsIgnoreCase(TransType.Withdraw.name())) {
                            withdrawAmt = transAmt;
                        } else if (transType.equalsIgnoreCase(TransType.Buy_Product_Balance.name())) {
                            purchaseAmt = transAmt;
                        } else if (transType.equalsIgnoreCase(TransType.Principal_Return.name())) {
                            tempReturnAmt = transAmt;
                        } else if (transType.equalsIgnoreCase(TransType.Reward_Product.name())) {
                            rewardProductAmt = transAmt;
                        } else if (transType.equalsIgnoreCase(TransType.Commission_A_Reward.name()) ||
                                transType.equalsIgnoreCase(TransType.Commission_B_Reward.name()) ||
                                transType.equalsIgnoreCase(TransType.Commission_C_Reward.name()) ||
                                transType.equalsIgnoreCase(TransType.Child_First_Purchase_Reward.name())) {
                            rewardAmt = rewardAmt + transAmt;
                        }
                    }
                }
                userT.setRechargeAmt(rechargeAmt);
                userT.setWithdrawAmt(withdrawAmt);
                userT.setRewardAmt(rewardAmt);
                userT.setRewardProductAmt(rewardProductAmt);
                userT.setPurchaseAmt(rewardProductAmt + purchaseAmt);
                userT.setNoBackProductAmt(rewardProductAmt + purchaseAmt - tempReturnAmt);
            });
        }
        return getDataTable(currUserList);
    }


    @ApiOperation("Get user Team detail")
    @PreAuthorize("@ss.hasPermi('system:panUser:userTeamList')")
    @RequestMapping("/getUserTeamInfo")
    public AjaxResult getUserTeamInfo(@RequestParam Long userId, @RequestParam int currentPage, @RequestParam int pageSize) {
        AjaxResult ajax = AjaxResult.success();
        LoginUser currentUser = getLoginUser();
        SysUser sysUser = userService.selectUserById(userId);
        if (currentUser.getUserType().equals("01")) {
            if (sysUser.getUserType().equals("02")) {
                ajax = AjaxResult.error();
                ajax.put("msg", "您没有权限查看经理团队");
                return ajax;
            }
            if (sysUser.getUserType().equals("01") && !currentUser.getUsername().equals(sysUser.getUserName())) {
                ajax = AjaxResult.error();
                ajax.put("msg", "您没有权限查看其他业务员团队");
                return ajax;
            }
        }

        TeamOverview userInfo = transService.selectUserTeam(sysUser);
        sysUser.setSize(pageSize);
        sysUser.setCurrentPage(currentPage);
        List<TeamOverview> list = transService.selectUserTeamList(sysUser);
        ajax.put("teamUserInfo", userInfo);
        ajax.put("userTeamList", list);
        return ajax;
    }

    @PostMapping("/setPayPwd")
    public AjaxResult setPaymentPassword(@RequestBody SetPwdPayload pwdPayload) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(pwdPayload.getUserId());
        user.setPayPassword(SecurityUtils.encryptPassword(pwdPayload.getPassword()));
        userService.updateUser(user);
        return ajax;
    }


    @PutMapping("/resetUserPwd")
    public AjaxResult resetUserPassword(@RequestBody ResetUserPwdPayload resetUserPwdPayload) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserByUserName(resetUserPwdPayload.getPhoneNo());
        if (user == null) {
            ajax = AjaxResult.error();
            ajax.put("msg", "User does not exist");
            return ajax;
        }
        user.setPassword(SecurityUtils.encryptPassword(resetUserPwdPayload.getNewPwd()));
        userService.updateUser(user);
        return ajax;
    }

    @ApiOperation("Export")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:panUser:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            user.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            user.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            user.setAgentId(currentUser.getUserId());
        }
        List<SysUser> list = userService.selectUserListDetail(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @ApiOperation("Import")
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:panUser:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @ApiOperation("Import template")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @ApiOperation("Set social")
    @PostMapping("/setSocial")
    public AjaxResult setUserSocialInformation(@RequestBody PanUserSocial userSocial) {
        SysUser currUser = userService.selectUserById(userSocial.getUserId());
        currUser.setEmail(userSocial.getEmail());
        currUser.setWhatsApp(userSocial.getWhatsApp());
        currUser.setTelegram(userSocial.getTelegram());
        userService.updateUser(currUser);
        AjaxResult ajax = AjaxResult.success();
        return ajax;
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        LoginUser currentUser = getLoginUser();
        log.info("***********************注册用户" + user);
        if (!userService.checkUserNameUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));

        /**
         * 新增user_balance表记录
         *
         */
        if (currentUser.getUserType().equals("00") || currentUser.getUserType().equals("02")) {
            return error("新增用户'" + user.getUserName() + "'失败，请到app注册用户");
        }
        user.setUserType("10");
        user.setTopId(currentUser.getUserId());
        user.setRoleIds(new Long[]{2L});
        user.setDeptId((long) 105);
        int i = userService.insertUser(user);
        if (i > 0) {
            List<SysUser> userList = userService.selectUserList(user);
            PanUserBalance userBalance = new PanUserBalance();
            userBalance.setBalance(new BigDecimal(0));
            userBalance.setLockBalance(new BigDecimal(0));
            userBalance.setAssetBalance(new BigDecimal(0));
            userBalance.setAvailableAmt(new BigDecimal(0));
            userBalance.setTransitAmt(new BigDecimal(0));
            userBalance.setUserId(userList.get(0).getUserId());
            iPanUserBalanceService.insertPanUserBalance(userBalance);
            /**
             * 新增user_asset表记录
             *
             */
            PanUserAsset userAsset = new PanUserAsset();
            userAsset.setUserId(userList.get(0).getUserId());
            userAsset.setBalance(new BigDecimal(0));
            iPanUserAssetService.insertPanUserAsset(userAsset);
        }
        return toAjax(i);
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 重置密码
     */

    @PreAuthorize("@ss.hasPermi('system:panUser:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        System.out.println(user.getPassword());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    @PutMapping("/resetMyPwd")
    public AjaxResult restMyPwd(@RequestBody ResetPwdPayload resetPwdPayload) {
        if (SecurityUtils.matchesPassword(resetPwdPayload.getOldPwd(), getLoginUser().getPassword())) {
            SysUser user = userService.selectUserById(getLoginUser().getUserId());
            user.setPassword(SecurityUtils.encryptPassword(resetPwdPayload.getNewPwd()));
            user.setUpdateBy(getUsername());
            return toAjax(userService.resetPwd(user));
        } else {
            throw new ServiceException("Old password is incorrect!");
        }
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:edit')")
    @Log(title = "状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 邀请码是否可用
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:edit')")
    @Log(title = "邀请码是否可用", businessType = BusinessType.UPDATE)
    @PutMapping("/changeIsInviteCode")
    public AjaxResult changeIsInviteCode(@RequestBody SysUser user) {
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 是否可提现
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:edit')")
    @Log(title = "是否可提现", businessType = BusinessType.UPDATE)
    @PutMapping("/changeIsWithdarw")
    public AjaxResult changeIsWithdarw(@RequestBody SysUser user) {
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 是否返息
     */
    @PreAuthorize("@ss.hasPermi('system:panUser:edit')")
    @Log(title = "是否返息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeIsRebate")
    public AjaxResult changeIsRebate(@RequestBody SysUser user) {
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

}
