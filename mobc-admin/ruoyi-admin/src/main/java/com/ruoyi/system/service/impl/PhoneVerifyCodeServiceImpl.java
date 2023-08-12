package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.ScenesVerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PhoneVerifyCodeMapper;
import com.ruoyi.system.domain.PhoneVerifyCode;
import com.ruoyi.system.service.IPhoneVerifyCodeService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-30
 */
@Service
public class PhoneVerifyCodeServiceImpl implements IPhoneVerifyCodeService
{
    @Autowired
    private PhoneVerifyCodeMapper phoneVerifyCodeMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param phoneNo 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public PhoneVerifyCode selectPhoneVerifyCodeByPhoneNo(String phoneNo)
    {
        return phoneVerifyCodeMapper.selectPhoneVerifyCodeByPhoneNo(phoneNo);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param phoneVerifyCode 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<PhoneVerifyCode> selectPhoneVerifyCodeList(PhoneVerifyCode phoneVerifyCode)
    {
        return phoneVerifyCodeMapper.selectPhoneVerifyCodeList(phoneVerifyCode);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param phoneVerifyCode 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertPhoneVerifyCode(PhoneVerifyCode phoneVerifyCode)
    {
        return phoneVerifyCodeMapper.insertPhoneVerifyCode(phoneVerifyCode);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param phoneVerifyCode 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updatePhoneVerifyCode(PhoneVerifyCode phoneVerifyCode)
    {
        return phoneVerifyCodeMapper.updatePhoneVerifyCode(phoneVerifyCode);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param phoneNos 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePhoneVerifyCodeByPhoneNos(String[] phoneNos)
    {
        return phoneVerifyCodeMapper.deletePhoneVerifyCodeByPhoneNos(phoneNos);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param phoneNo 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePhoneVerifyCodeByPhoneNo(String phoneNo)
    {
        return phoneVerifyCodeMapper.deletePhoneVerifyCodeByPhoneNo(phoneNo);
    }



    @Override
    public int insertScenesVerifyCode(ScenesVerifyCode scenesVerifyCode) {
        return phoneVerifyCodeMapper.insertScenesVerifyCode(scenesVerifyCode);
    }

    @Override
    public int updateScenesVerifyCode(ScenesVerifyCode scenesVerifyCode) {
        return phoneVerifyCodeMapper.updateScenesVerifyCode(scenesVerifyCode);
    }

    @Override
    public List<ScenesVerifyCode> selectScenesVerifyCodeList(ScenesVerifyCode scenesVerifyCode) {
        return phoneVerifyCodeMapper.selectScenesVerifyCodeList(scenesVerifyCode);
    }
}
