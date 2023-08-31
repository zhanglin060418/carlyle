package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TransType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MyQrCodeUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.system.domain.PanUserAsset;
import com.ruoyi.system.domain.PanUserBalance;
import com.ruoyi.system.domain.PanUserSocial;
import com.ruoyi.system.domain.TeamOverview;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.system.domain.ResetPwdPayload;
import com.ruoyi.web.controller.system.domain.ResetUserPwdPayload;
import com.ruoyi.web.controller.system.domain.SetPwdPayload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Api
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
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

    @Autowired
    private IPanUserBalanceService panUserBalanceService;

    @Autowired
    private IPanRechargeService panRechargeService;

    @Autowired
    private IPanWithdrawService panWithdrawService;

    @Autowired
    private IPurchaseService panPurchaseService;

    @Autowired
    private IPanUserAssetService panAssetService;


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
        ajax.put("total_income", transService.getTotalIncome(userId));
        return ajax;
    }

    /**
     * 获取用户列表
     */
    @ApiOperation("Get user list")
    @PreAuthorize("@ss.hasPermi('system:user:index')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @ApiOperation("Get user list detail")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/listDetail")
    public TableDataInfo listDetail(SysUser user) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("02")) {
            user.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            user.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<SysUser> list = userService.selectUserListBySys(user);

        return getDataTable(list);
    }

    @PostMapping("/setPayPwd")
    public AjaxResult setPaymentPassword(@RequestBody SetPwdPayload pwdPayload) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(pwdPayload.getUserId());
        user.setPayPassword(SecurityUtils.encryptPassword(pwdPayload.getPassword()));
        userService.updateUser(user);
        return ajax;
    }

    @PutMapping("/resetPayPwd")
    public AjaxResult resetPaymentPassword(@RequestBody ResetPwdPayload resetPwdPayload) {
        if (SecurityUtils.matchesPassword(resetPwdPayload.getOldPwd(), getLoginUser().getPassword())) {
            SysUser user = userService.selectUserById(getLoginUser().getUserId());
            user.setPayPassword(SecurityUtils.encryptPassword(resetPwdPayload.getNewPwd()));
            user.setUpdateBy(getUsername());
            return toAjax(userService.resetPwd(user));
        } else {
            throw new ServiceException("Old password is incorrect!");
        }
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
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @ApiOperation("Import")
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
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
    @PreAuthorize("@ss.hasPermi('system:user:query')")
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
     * 新增业务员 --经理操作
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "新增业务员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        log.info("***********************新增业务员" + user);
        LoginUser currentUser = getLoginUser();
        if(!currentUser.getUserType().equals("02")){
            return error("新增业务员'" + user.getUserName() + "'失败，你可以联系经理添加");
        }
        if (!userService.checkUserNameUnique(user)) {
            return error("新增业务员'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("新增业务员'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增业务员'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setInviteCode(RandomStringUtils.randomAlphabetic(12));
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));

        /**
         * 新增user_balance表记录
         *
         */

        user.setUserType("01");
        user.setDeptId((long) 105);
        Long roleId = 3L;
        user.setRoleId(roleId);
        user.setManagerId(currentUser.getUserId());
        SysUser sysUser =userService.selectUserById(currentUser.getUserId());
        user.setCreateBy(currentUser.getUserId().toString());
        user.setAgentId(sysUser.getAgentId());
        int i = userService.insertUserSys(user);
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
        }else{
            return error("新增业务员'" + user.getUserName() + "'异常，请联系管理员添加角色");
        }
        return toAjax(i);
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
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
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
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
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles
                : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }


    @RequestMapping("/captchaQrcode")
    public AjaxResult captchaQrcode(HttpServletResponse response, @RequestParam String domain) throws IOException
    {
        LoginUser loginUser = getLoginUser();
        SysUser user = userService.selectUserById(loginUser.getUserId());
        AjaxResult ajax = AjaxResult.success();
        String url = domain+"#/register?c="+user.getInviteCode();
        BufferedImage image = MyQrCodeUtil.createImage( url);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ajax.put("userQrcode", Base64.encode(os.toByteArray()));
        return ajax;
    }

    @RequestMapping("/getTodayData")
    public AjaxResult getTodayData(SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            sysUser.setTopId(currentUser.getUserId());
        } else if (currentUser.getUserType().equals("02")) {
            sysUser.setManagerId(currentUser.getUserId());
        } else if (currentUser.getUserType().equals("03")) {
            sysUser.setAgentId(currentUser.getUserId());
        }
        // 今日充值用户
        Long todayRechargeUsers = 0L;
        // 今日订单用户
        Long todayOrderUsers  = 0L;
        // 今日提现用户
        Long todayWithdrawUsers = 0L;
        List<TeamOverview> todayTranslistUsers =  transService.getTodayTransUsers(sysUser);
        if(todayTranslistUsers.size()>0){
            for(TeamOverview teamUser : todayTranslistUsers){
                if(teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name()) ){
                    todayRechargeUsers =teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name()) ){
                    todayWithdrawUsers=teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name()) ){
                    todayOrderUsers =teamUser.getTotalPeopleCount();
                }
            }
        }
        // 今日充值单数
        Long todayRechargeCount = 0L;
        // 今日订单单数
        Long todayOrderCount  = 0L;
        // 今日提现单数
        Long todayWithdrawCount = 0L;
        List<TeamOverview> todayTransCountlist=  transService.todayTransCountlist(sysUser);
        if(todayTransCountlist.size()>0){
            for(TeamOverview teamUser : todayTransCountlist){
                if(teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name()) ){
                    todayRechargeCount =teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name()) ){
                    todayWithdrawCount=teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name()) ){
                    todayOrderCount =teamUser.getTotalPeopleCount();
                }
            }
        }
        // 今日充值金额
        Long todayRechargeAmt =0L;
        // 今日提现金额
        Long todayWithdrawAmt = 0L;
        // 今日订单金额
        Long todayOrderAmt = 0L;
        // 今日返佣金额
        Long todayRewardAmt =0L;
        // 今日返利金额
        Long todayInterestAmt =0L;
        // 今日返本金额
        Long todayPrincipalAmt =0L;
        // 增值宝今日利息
        Long todayTreasureRewardAmt =0L;
        Long todaySignInRewardAmt =0L;
        List<TeamOverview> dayList = transService.getTotalTransInfoByDay(sysUser);
        if(dayList.size()>0){
            for(TeamOverview teamUser : dayList){
                if(teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name()) ){
                    todayRechargeAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name()) ){
                    todayWithdrawAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name()) ){
                    todayOrderAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Principal_Return.name()) ){
                    todayPrincipalAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Treasure_Reward.name()) ){
                    todayTreasureRewardAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.SignIn_Reward.name()) ){
                    todaySignInRewardAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Product_Daily_Interest.name()) ){
                    todayInterestAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Commission_A_Reward.name())||
                        teamUser.getTransType().equalsIgnoreCase(TransType.Commission_B_Reward.name())||
                        teamUser.getTransType().equalsIgnoreCase(TransType.Commission_C_Reward.name())){
                    todayRewardAmt = todayRewardAmt +teamUser.getTransAmt();
                }
            }
        }
        //今日注册用户
        Long todayRegisterUsers= userService.todayRegisterUsers(sysUser);
        //今日活跃用户
        Long todayActiveUsers=transService.todayActiveUsers(sysUser);
        //今日首次充值用户
        Long todayFristRechargeUsers=transService.todayFristRechargeUsers(sysUser);
        ajax.put("todayRegisterUsers",todayRegisterUsers);
        ajax.put("todayActiveUsers",todayActiveUsers);
        ajax.put("todayFristRechargeUsers",todayFristRechargeUsers);
        ajax.put("todayRechargeCount",todayRechargeCount);
        ajax.put("todayOrderCount",todayOrderCount);
        ajax.put("todayWithdrawCount",todayWithdrawCount);
        ajax.put("todayRechargeUsers",todayRechargeUsers);
        ajax.put("todayOrderUsers",todayOrderUsers);
        ajax.put("todayWithdrawUsers",todayWithdrawUsers);
        ajax.put("todayRechargeAmt",todayRechargeAmt);
        ajax.put("todayWithdrawAmt",todayWithdrawAmt);
        ajax.put("todayOrderAmt",todayOrderAmt);
        ajax.put("todayRewardAmt",todayRewardAmt);
        ajax.put("todayInterestAmt",todayInterestAmt);
        ajax.put("todayPrincipalAmt",todayPrincipalAmt);
        ajax.put("todayTreasureRewardAmt",todayTreasureRewardAmt);
        ajax.put("todaySignInRewardAmt",todaySignInRewardAmt);
        return ajax;
    }
    @RequestMapping("/getTotalData")
    public AjaxResult getTotalData(SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        PanUserBalance userBalance = new PanUserBalance();
        LoginUser currentUser = getLoginUser();
        if(currentUser.getUserType().equals("01")){
            sysUser.setTopId(currentUser.getUserId());
            userBalance.setTopId(currentUser.getUserId());
        }else if(currentUser.getUserType().equals("02")){
            sysUser.setManagerId(currentUser.getUserId());
            userBalance.setManagerId(currentUser.getUserId());
        }else if(currentUser.getUserType().equals("03")){
            sysUser.setAgentId(currentUser.getUserId());
            userBalance.setAgentId(currentUser.getUserId());
        }


        //总用户数
        int totalPeople  = userService.selectUserTotalCount(sysUser);
        PanUserBalance currPanUserBalance= iPanUserBalanceService.getUserBalanceCount(userBalance);
        //总余额
        BigDecimal totalBalance = currPanUserBalance.getBalance();
        // 总增值宝余额
        BigDecimal assetTotalBalance = currPanUserBalance.getAssetBalance();
        ajax.put("totalPeople",totalPeople);
        ajax.put("totalBalance",totalBalance);
        ajax.put("assetTotalBalance",assetTotalBalance);

        return ajax;
    }
    @RequestMapping("/getTotalDataByFrist")
    public AjaxResult getTotalDataByFrist(SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        LoginUser currentUser = getLoginUser();
        Long agentBalance = 0L;

        if(currentUser.getUserType().equals("01")){
            sysUser.setTopId(currentUser.getUserId());
            agentBalance = panWithdrawService.getAgentBalance(currentUser.getUser().getAgentId()).longValue();
        }else if(currentUser.getUserType().equals("02")){
            sysUser.setManagerId(currentUser.getUserId());
            agentBalance = panWithdrawService.getAgentBalance(currentUser.getUser().getAgentId()).longValue();
        }else if(currentUser.getUserType().equals("03")){
            sysUser.setAgentId(currentUser.getUserId());
            agentBalance = panWithdrawService.getAgentBalance(currentUser.getUserId()).longValue();
        }

        // 总充值金额
        Long totalRechargeAmt =0L;
        // 总提现金额
        Long totalWithdrawAmt = 0L;
        // 总订单金额
        Long totalOrderAmt = 0L;
        // 总返佣金额
        Long totalRewardAmt =0L;
        // 总返利金额
        Long totalInterestAmt =0L;
        // 总返本金额
        Long totalPrincipalAmt =0L;
        // 总赠送订单金额
        Long totalRewardProductAmt =0L;
        // 总注册奖励金额
        Long totalRegisterRewardAmt =0L;
        // 手工加款金额
        Long totalSalarySubsidyBonusAmt =0L;
        // 手工减款金额
        Long totalManualAdjustmentAmt =0L;
        // 增值宝总利息
        Long totalTreasureRewardAmt =0L;
        // 首单总返利利息
        Long totalFirstPurchaseRewardAmt =0L;
        Long totalSignInRewardAmt = 0L;

        List<TeamOverview> list = transService.getTotalTransInfo(sysUser);
        if(list.size()>0){
            for(TeamOverview teamUser : list){
                if(teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name()) ){
                    totalRechargeAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name()) ){
                    totalWithdrawAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name()) ){
                    totalOrderAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Product_Daily_Interest.name()) ){
                    totalInterestAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Principal_Return.name()) ){
                    totalPrincipalAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Reward_Product.name())|| teamUser.getTransType().equalsIgnoreCase(TransType.Manual_Reward_Product.name()) ){
                    totalRewardProductAmt = totalRewardProductAmt + teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Register_Reward.name()) ){
                    totalRegisterRewardAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Salary.name()) ||teamUser.getTransType().equalsIgnoreCase(TransType.Subsidy.name())||teamUser.getTransType().equalsIgnoreCase(TransType.Bonus.name())||teamUser.getTransType().equalsIgnoreCase(TransType.Offline_Recharge.name())){
                    totalSalarySubsidyBonusAmt =totalSalarySubsidyBonusAmt+teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Manual_Adjustment.name()) ){
                    totalManualAdjustmentAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Treasure_Reward.name()) ){
                    totalTreasureRewardAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Child_First_Purchase_Reward.name()) ){
                    totalFirstPurchaseRewardAmt =teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.SignIn_Reward.name()) ){
                    totalSignInRewardAmt = teamUser.getTransAmt();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Commission_A_Reward.name())||
                        teamUser.getTransType().equalsIgnoreCase(TransType.Commission_B_Reward.name())||
                        teamUser.getTransType().equalsIgnoreCase(TransType.Commission_C_Reward.name())){
                    totalRewardAmt = totalRewardAmt +teamUser.getTransAmt();
                }
            }

        }


        ajax.put("agentBalance",agentBalance);
        ajax.put("totalRechargeAmt",totalRechargeAmt);
        ajax.put("totalWithdrawAmt",totalWithdrawAmt);
        ajax.put("totalOrderAmt",totalOrderAmt);
        ajax.put("totalRewardAmt",totalRewardAmt);
        ajax.put("totalInterestAmt",totalInterestAmt);
        ajax.put("totalPrincipalAmt",totalPrincipalAmt);
        ajax.put("totalRewardProductAmt",totalRewardProductAmt);
        ajax.put("totalRegisterRewardAmt",totalRegisterRewardAmt);
        ajax.put("totalSalarySubsidyBonusAmt",totalSalarySubsidyBonusAmt);
        ajax.put("totalManualAdjustmentAmt",totalManualAdjustmentAmt);
        ajax.put("totalTreasureRewardAmt",totalTreasureRewardAmt);
        ajax.put("totalFirstPurchaseRewardAmt",totalFirstPurchaseRewardAmt);
        ajax.put("totalSignInRewardAmt",totalSignInRewardAmt);
        return ajax;

    }

    @RequestMapping("/getTotalDataSecond")
    public AjaxResult getTotalDataSecond(SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        LoginUser currentUser = getLoginUser();
        if(currentUser.getUserType().equals("01")){
            sysUser.setTopId(currentUser.getUserId());
        }else if(currentUser.getUserType().equals("02")){
            sysUser.setManagerId(currentUser.getUserId());
        }else if(currentUser.getUserType().equals("03")){
            sysUser.setAgentId(currentUser.getUserId());
        }

        // 总充值用户数
        Long totalRechargeUsers = 0L;
        // 总订单用户数
        Long totalOrderUsers  = 0L;
        // 总提现用户数
        Long totalWithdrawUsers = 0L;

        List<TeamOverview> translistUsers =  transService.getToatlTransUsers(sysUser);
        if(translistUsers.size()>0){
            for(TeamOverview teamUser : translistUsers){
                if(teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name()) ){
                    totalRechargeUsers =teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name()) ){
                    totalWithdrawUsers=teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name()) ){
                    totalOrderUsers =teamUser.getTotalPeopleCount();
                }
            }
        }

        // 总充值单数
        Long totalRechargeCount = 0L;
        // 总购买单数
        Long totalOrderCount  = 0L;
        // 总提现单数
        Long totalWithdrawCount = 0L;
        // 总赠送单数
        Long totalRewardProductCount = 0L;
        List<TeamOverview> totalTransCountlist=  transService.totalTransCountlist(sysUser);
        if(totalTransCountlist.size()>0){
            for(TeamOverview teamUser : totalTransCountlist){
                if(teamUser.getTransType().equalsIgnoreCase(TransType.Recharge.name()) ){
                    totalRechargeCount =teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Withdraw.name()) ){
                    totalWithdrawCount =teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Buy_Product_Balance.name()) ){
                    totalOrderCount =teamUser.getTotalPeopleCount();
                }else if(teamUser.getTransType().equalsIgnoreCase(TransType.Reward_Product.name())|| teamUser.getTransType().equalsIgnoreCase(TransType.Manual_Reward_Product.name()) ){
                    totalRewardProductCount = totalRewardProductCount+teamUser.getTotalPeopleCount();
                }
            }
        }
        ajax.put("totalRechargeUsers",totalRechargeUsers);
        ajax.put("totalOrderUsers",totalOrderUsers);
        ajax.put("totalWithdrawUsers",totalWithdrawUsers);
        ajax.put("totalRechargeCount",totalRechargeCount);
        ajax.put("totalOrderCount",totalOrderCount);
        ajax.put("totalWithdrawCount",totalWithdrawCount);
        ajax.put("totalRewardProductCount",totalRewardProductCount);
        return ajax;
    }


}
