package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PanBindCard;
import com.ruoyi.system.service.IPanBindCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户银行卡Controller
 *
 * @author ruoyi
 * @date 2023-04-09
 */
@RestController
@RequestMapping("/system/card")
public class PanBindCardController extends BaseController {
    @Autowired
    private IPanBindCardService panBindCardService;

    /**
     * 查询用户银行卡列表
     */
    @PreAuthorize("@ss.hasPermi('system:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(PanBindCard panBindCard) {
        LoginUser currentUser = getLoginUser();
        if (currentUser.getUserType().equals("01")) {
            panBindCard.setTopId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("02")) {
            panBindCard.setManagerId(currentUser.getUserId());
        }
        if (currentUser.getUserType().equals("03")) {
            panBindCard.setAgentId(currentUser.getUserId());
        }
        startPage();
        List<PanBindCard> list = panBindCardService.selectPanBindCardList(panBindCard);
        return getDataTable(list);
    }


    /**
     * 查询用户银行卡修改记录
     */
    @PreAuthorize("@ss.hasPermi('system:card:cardRecordList')")
    @GetMapping("/cardRecordList")
    public TableDataInfo cardRecordList(PanBindCard panBindCard) {
        startPage();
        List<PanBindCard> list = panBindCardService.selectPanBindCardRecordList(panBindCard);
        return getDataTable(list);
    }

    @GetMapping("/getUserCard")
    public AjaxResult getUserCard(@RequestParam Long userId) {
        PanBindCard panUserCard = new PanBindCard();
        panUserCard.setUserId(userId);
        List<PanBindCard> list = panBindCardService.selectPanBindCardList(panUserCard);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }

    @DeleteMapping("/deleteBankCard")
    public AjaxResult deleteBankCard(@RequestParam Long cardId) {
        return toAjax(panBindCardService.deletePanBindCardByCardId(cardId));
    }

    /**
     * 导出用户银行卡列表
     */
    @PreAuthorize("@ss.hasPermi('system:card:export')")
    @Log(title = "用户银行卡", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PanBindCard panBindCard) {
        List<PanBindCard> list = panBindCardService.selectPanBindCardList(panBindCard);
        ExcelUtil<PanBindCard> util = new ExcelUtil<PanBindCard>(PanBindCard.class);
        util.exportExcel(response, list, "用户银行卡数据");
    }

    /**
     * 获取用户银行卡详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:card:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
      PanBindCard card = panBindCardService.selectPanBindCardByCardId(cardId);
      return success(card);
    }

    /**
     * 新增银行卡验证
     */
    @RequestMapping(value = "/{validateCardNoByAdd}")
    public AjaxResult validateCardNoByAdd(@PathVariable("cardNo") String cardNo, @PathVariable("userId") Long userId) {
        AjaxResult addAjax = AjaxResult.success();
        List<PanBindCard> cardList = panBindCardService.selectCardInfoByCardNo(cardNo);
        if (cardList.size() > 0) {
            addAjax = AjaxResult.error();
            addAjax.put("msg", "number already exists");
        }
        return addAjax;
    }

    /**
     * 修改银行卡验证
     */
    @RequestMapping(value = "/{validateCardNoByRevise}")
    public AjaxResult validateCardNoByRevise(@PathVariable("cardNo") String cardNo, @PathVariable("userId") Long userId) {
        AjaxResult reviseAjax = AjaxResult.success();
        List<PanBindCard> cardList = panBindCardService.selectCardInfoByCardNo(cardNo);
        if (cardList.size() > 0) {
            if (cardList.get(0).getUserId() != userId) {
                reviseAjax = AjaxResult.error();
                reviseAjax.put("msg", "number already exists");
            }
        }
        return reviseAjax;
    }


    /**
     * 新增用户银行卡
     */
//  @PreAuthorize("@ss.hasPermi('system:card:add')")
    @Log(title = "用户银行卡", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PanBindCard panBindCard) {

        AjaxResult addAjax = AjaxResult.success();
        List<PanBindCard> cardList = panBindCardService.selectCardInfoByCardNo(panBindCard.getCardNo());
        if (cardList.size() > 0) {
            addAjax = AjaxResult.error();
            addAjax.put("msg", "number already exists");
        } else {
            panBindCardService.insertPanBindCard(panBindCard);
        }
        return addAjax;
    }


    /**
     * 修改用户银行卡
     */
//    @PreAuthorize("@ss.hasPermi('system:card:edit')")
    @Log(title = "用户银行卡", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PanBindCard panBindCard) {
        AjaxResult reviseAjax = AjaxResult.success();
        List<PanBindCard> cardList = panBindCardService.selectCardInfoByCardNo(panBindCard.getCardNo());
        if (cardList.size() > 0) {
            if (!cardList.get(0).getUserId().equals(panBindCard.getUserId()) ) {
                reviseAjax = AjaxResult.error();
                reviseAjax.put("msg", "number already exists");
            }
        }

        PanBindCard oldCard = panBindCardService.selectPanBindCardByCardId(panBindCard.getId());
        PanBindCard panBindCardRecord = new  PanBindCard();
        panBindCardRecord.setUserId(panBindCard.getUserId());
        panBindCardRecord.setName(oldCard.getName());
        panBindCardRecord.setBankCode(oldCard.getBankCode());
        panBindCardRecord.setOldCardNo(oldCard.getCardNo());
        panBindCardRecord.setMobile(oldCard.getMobile());
        panBindCardRecord.setNewCardNo(panBindCard.getCardNo());
        panBindCardService.insertPanBindCardRecord(panBindCardRecord);
        panBindCardService.updatePanBindCard(panBindCard);
        return reviseAjax;
    }

    /**
     * 删除用户银行卡
     */
    @PreAuthorize("@ss.hasPermi('system:card:remove')")
    @Log(title = "用户银行卡", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(panBindCardService.deletePanBindCardByCardIds(cardIds));
    }

}
