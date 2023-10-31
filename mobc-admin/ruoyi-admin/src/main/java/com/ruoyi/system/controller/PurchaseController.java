package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品支付Controller
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/system/purchase")
public class PurchaseController extends BaseController {

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IPanUserBalanceService panUserBalanceService;

    @Autowired
    private IPanProductService panProductService;

    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IPanTransactionHistoryService transService;


    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @PreAuthorize("@ss.hasPermi('system:purchase:getTotalPurchase')")
    @GetMapping("/getTotalPurchase")
    public TableDataInfo getTotalPurchase(Purchase purchase) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            purchase.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            purchase.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            purchase.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<Purchase> list = purchaseService.getTotalPurchase(purchase);
        return getDataTable(list);
    }

    /**
     * 查询产品支付列表
     */

    @GetMapping("getPurchaseHistory")
    public AjaxResult getPurchaseHistory(@RequestParam Long userId) {
        Purchase purchase = new Purchase();
        purchase.setBuyer(userId);
        List<Purchase> list = purchaseService.selectPurchaseListByUser(purchase);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }

    @GetMapping("/getPurchaseCount")
    public AjaxResult getPurchaseCount(@RequestParam Long userId) {
        Purchase purchase = new Purchase();
        purchase.setBuyer(userId);
        Purchase currPurchase = purchaseService.getPurchaseCount(purchase);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, currPurchase);
        return ajax;
    }

    @RequestMapping("/getPurchaseNews")
    public AjaxResult getPurchaseNews(String userId) {
        AjaxResult ajax = AjaxResult.success();
        List<PurchaseNews> purchaseNewsList = new ArrayList<PurchaseNews>();
        if (StringUtils.isNotEmpty(userId)) {
            logger.info("********PurchaseController getPurchaseNews userId:" + userId);
            List<Purchase> purchaseList = purchaseService.selectLast3Purchase(Long.parseLong(userId));
            if (purchaseList.size() > 0) {
                for (int i = 0; i < purchaseList.size(); i++) {
                    PurchaseNews purchaseNews = new PurchaseNews();
                    purchaseNews.setUserName(purchaseList.get(i).getUserName());
                    purchaseNews.setAmount(purchaseList.get(i).getAmount());
                    purchaseNews.setRemark(purchaseList.get(i).getOrderNo());
                    purchaseNewsList.add(purchaseNews);
                }
            }
        }
        ajax.put(AjaxResult.DATA_TAG, purchaseNewsList);
        return ajax;
    }

    /**
     * 根据orderNo查询产品收益列表
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/getPurchaseInterestList")
    public AjaxResult getPurchaseInterestList(@RequestParam String orderNo, @RequestParam Long userId, @RequestParam int currentPage, @RequestParam int pageSize) {
        /**
         *查询交易记录表，返回此购买产品订单收益记录
         * amount：金额
         * transactionDate：时间
         */

        PanTransactionHistory trans = new PanTransactionHistory();
        trans.setCurrentPage(currentPage);
        trans.setSize(pageSize);
        trans.setOrOrderId(orderNo);
        trans.setUserId(userId);
        List<PanTransactionHistory> purchaseInterestList = transService.getPurchaseInterestList(trans);

        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, purchaseInterestList);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('system:purchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(Purchase purchase) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            purchase.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            purchase.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            purchase.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<Purchase> list = purchaseService.selectPurchaseList(purchase);
        Long amountCount = purchaseService.selectPurchaseListCount(purchase);
        return getDataTable(list, amountCount);
    }

    /**
     * 导出产品支付列表
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:export')")
    @Log(title = "产品支付", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Purchase purchase) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            purchase.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            purchase.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            purchase.setAgentId(currentUser.getUserId());
        }
        List<Purchase> list = purchaseService.selectPurchaseList(purchase);
        ExcelUtil<Purchase> util = new ExcelUtil<Purchase>(Purchase.class);
        util.exportExcel(response, list, "产品支付数据");
    }

    /**
     * 获取产品支付详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(purchaseService.selectPurchaseById(id));
    }


    /**
     * 验证已经购买此产品数量
     *
     * @param purchaseCreate
     * @return
     */
    @GetMapping("/verifyCount")
    public AjaxResult verifyCount(@RequestBody PurchaseCreate purchaseCreate) {
        AjaxResult ajaxResult = AjaxResult.success();

        //查询已购买份数
        Purchase currPurchase = new Purchase();
        currPurchase.setBuyer(purchaseCreate.getUserId());
        currPurchase.setProduct(purchaseCreate.getProductId());
        List<Purchase> buyCountByUserList = purchaseService.selectPurchaseCountByUser(currPurchase);
        // get product details
        PanProduct panProduct = panProductService.selectPanProductById(purchaseCreate.getProductId());
        // 判断份额
        if ((panProduct.getCopies() - buyCountByUserList.size()) < 1) {
            ajaxResult = AjaxResult.error();
            ajaxResult.put("msg", "Not enough Copies");
        }
        return ajaxResult;
    }

    /**
     * 新增产品支付
     */
//    @PreAuthorize("@ss.hasPermi('system:purchase:add')")
    @Log(title = "产品购买", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurchaseCreate purchaseCreate) {
        logger.info("****产品购买-start****" + JSONObject.toJSONString(purchaseCreate));
        LoginUser loginUser = getLoginUser();
        AjaxResult finalAjax = AjaxResult.error();
        if (loginUser != null && loginUser.getUserId() != null) {
            SysUser sysUser = sysUserService.selectUserById(loginUser.getUserId());
            if (sysUser.getStatus().equals("1")) {
                finalAjax.put("msg", "Account has been disabled");
                return finalAjax;
            }
            PanUserBalance panUserBalance = panUserBalanceService.getPanUserBalanceByUserId(purchaseCreate.getUserId());
            PanProduct panProduct = panProductService.selectPanProductById(purchaseCreate.getProductId());
            if (!panProduct.getOnSale().equals("0")) {
                logger.info("****该基金已经停售！");
                finalAjax.put("msg", "The fund has been discontinued!");
                return finalAjax;
            }
            if (!DateUtils.isPastDate(panProduct.getSellingTimestamp())) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sellingTimestamp = sdf3.format(panProduct.getSellingTimestamp());
                logger.info("****该基金发售时间为:" + sellingTimestamp);
                finalAjax.put("msg", "The fund will be launched on:" + sellingTimestamp);
                return finalAjax;
            }
            //查询已购买份数
            Purchase currPurchase = new Purchase();
            currPurchase.setBuyer(purchaseCreate.getUserId());
            currPurchase.setProduct(purchaseCreate.getProductId());
            List<Purchase> buyCountByUserList = purchaseService.selectPurchaseCountByUser(currPurchase);
            // 判断份额
            if ((panProduct.getCopies() - buyCountByUserList.size()) < 1) {
                finalAjax.put("msg", "Not enough Copies");
                return finalAjax;
            }
            //判断余额
            if (purchaseCreate.getAmount().compareTo(panUserBalance.getAvailableAmt()) > 0) {
                finalAjax.put("msg", "Not enough balance");
                return finalAjax;
            }

            try {
                String orderNo = DateUtils.createOrderId("P");
                Purchase purchase = new Purchase();
                purchase.setOrderNo(orderNo);
                purchase.setBuyer(purchaseCreate.getUserId());
                purchase.setProduct(purchaseCreate.getProductId());
                purchase.setCycle(panProduct.getCycle());
                purchase.setDailyInterest(panProduct.getDailyInterest());
                purchase.setTotalInterest(new BigDecimal(0));
                purchase.setAmount(purchaseCreate.getAmount());
                purchase.setPayBack("0");
                if (panProduct.getHasGroupBuyOption().equals("0")) {
                    int luckyAmount = DateUtils.getluckyAmtRandom(panProduct.getLuckyNumberRangeStart(), panProduct.getLuckyNumberRangeEnd());

                    purchase.setIsLucky("0");
                    purchase.setLuckyAmt(new BigDecimal(luckyAmount).multiply(new BigDecimal(100)));
                } else {
                    purchase.setIsLucky("1");
                }
                purchase.setStatus(TransStatus.SUCCESS);

                logger.info("****产品购买-info:{}", JSONObject.toJSONString(purchase));
                String result = iTransService.purchaseBalance(purchase);
                if (result.equals(MessageStatus.SUCCESS)) {
                    finalAjax = AjaxResult.success();
                    finalAjax.put("data",purchase);
                } else {
                    finalAjax.put("msg", "Network exception, please try again later");
                }
            } catch (Exception e) {
                finalAjax.put("msg", "Network is abnormal, please try again!");
            }
        }
        return finalAjax;
    }

    /**
     * 修改产品支付
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:edit')")
    @Log(title = "产品支付", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Purchase purchase) {
        return toAjax(purchaseService.updatePurchase(purchase));
    }

    /**
     * 删除产品支付
     */
    @PreAuthorize("@ss.hasPermi('system:purchase:remove')")
    @Log(title = "产品支付", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(purchaseService.deletePurchaseByIds(ids));
    }

    @GetMapping("/getPurchaseListForBuy")
    public AjaxResult selectPurchaseListForBuy() {
        Purchase userPurchase = new Purchase();
        LoginUser currentUser = getLoginUser();
        logger.info("********PurchaseController selectPurchaseListForBuy userId:" + currentUser.getUserId());
        userPurchase.setBuyer(currentUser.getUserId());
        userPurchase.setStatus(TransStatus.SUCCESS);
        List<Purchase> userPurchaseList = purchaseService.selectPurchaseListForBuy(userPurchase);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, userPurchaseList);
        return ajax;
    }
}
