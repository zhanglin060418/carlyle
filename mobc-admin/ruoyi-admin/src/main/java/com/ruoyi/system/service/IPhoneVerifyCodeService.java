package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PhoneVerifyCode;
import com.ruoyi.system.domain.ScenesVerifyCode;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2023-04-30
 */
public interface IPhoneVerifyCodeService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param phoneNo 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public PhoneVerifyCode selectPhoneVerifyCodeByPhoneNo(String phoneNo);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param phoneVerifyCode 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<PhoneVerifyCode> selectPhoneVerifyCodeList(PhoneVerifyCode phoneVerifyCode);

    /**
     * 新增【请填写功能名称】
     *
     * @param phoneVerifyCode 【请填写功能名称】
     * @return 结果
     */
    public int insertPhoneVerifyCode(PhoneVerifyCode phoneVerifyCode);

    /**
     * 修改【请填写功能名称】
     *
     * @param phoneVerifyCode 【请填写功能名称】
     * @return 结果
     */
    public int updatePhoneVerifyCode(PhoneVerifyCode phoneVerifyCode);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param phoneNos 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deletePhoneVerifyCodeByPhoneNos(String[] phoneNos);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param phoneNo 【请填写功能名称】主键
     * @return 结果
     */
    public int deletePhoneVerifyCodeByPhoneNo(String phoneNo);

    public int insertScenesVerifyCode(ScenesVerifyCode scenesVerifyCode);

    public int updateScenesVerifyCode(ScenesVerifyCode scenesVerifyCode);

    public List<ScenesVerifyCode> selectScenesVerifyCodeList(ScenesVerifyCode scenesVerifyCode);
}
