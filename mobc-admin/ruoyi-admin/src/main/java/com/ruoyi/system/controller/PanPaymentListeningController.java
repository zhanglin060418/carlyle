package com.ruoyi.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TransStatus;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.system.domain.PanChannel;
import com.ruoyi.system.domain.PanRecharge;
import com.ruoyi.system.domain.PanWithdraw;
import com.ruoyi.system.service.*;
import com.ruoyi.system.service.impl.TransServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@Api
@RestController
@RequestMapping("/system/payment")
public class PanPaymentListeningController extends BaseController {
    @Autowired
    private IPanRechargeService panRechargeService;

    @Autowired
    private IPanWithdrawService panWithdrawService;

    @Autowired
    private IPanChannelService panChannelService;

    @Autowired
    private ITransService iTransService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private RedisCache redisCache;

    private static final Logger logger = LoggerFactory.getLogger(TransServiceImpl.class);

    @ApiOperation("notify indo payment result")
    @RequestMapping("/notifyUrl")
    public AjaxResult paymentListening(HttpServletRequest request) throws Exception {
        logger.info("********交易管理-回调 Start********");
        AjaxResult ajax = AjaxResult.success();

        try {
            Map<String, String> param = initNotifyParam(request, "io");
            logger.info("********交易管理-回调 param:" + JSONObject.toJSONString(param));
            String content = param.get("notifyRequest");
            Map<String, Object> responseMap = JSONObject.parseObject(content);

            String orderNo = responseMap.get("orderNo").toString();
            String respCode = responseMap.get("respCode").toString();
            PanRecharge panRecharge = panRechargeService.selectPanRechargeByOrderNo(orderNo);
            PanWithdraw panWithdraw = panWithdrawService.selectPanWithdrawByOrderNo(orderNo);
            Long channelId =  null;
            if(null != panRecharge){
                channelId =  panRecharge.getChannelId();
                if(panRecharge.getStatus().equalsIgnoreCase(TransStatus.COMPLETED)){
                    return  error("panRecharge is final status : " +panRecharge.getStatus());
                }
            }else  if(null != panWithdraw){
                channelId =  panWithdraw.getChannelId();
                if(panWithdraw.getStatus().equalsIgnoreCase(TransStatus.COMPLETED)
                        ||panWithdraw.getStatus().equalsIgnoreCase(TransStatus.DECLINED)){
                    return  error("panWithdraw is final status : " +panWithdraw.getStatus());
                }
            }
            if(channelId == null){
                channelId =  1L;//默认按照第一个渠道处理，只是为了兼容之前的渠道问题
            }

            PanChannel panChannel = panChannelService.selectPanChannelByChannelId(channelId);
            if(null == panChannel){
                return  error("can't mapping channel info");
            }

            JSONObject channelJson = JSONObject.parseObject(panChannel.getJsonParam());
            String pub_key =channelJson.getString("pub_key");

            boolean isVerify = verify(getSignatureStr(responseMap), UriEncoder.decode(responseMap.get("signature").toString()), getPublicKey(pub_key), "UTF-8");
            /**
             * 验签是否通过
             */
            if (isVerify) {
                if(null == redisCache.getCacheObject(orderNo)){
                    redisCache.setCacheObject(orderNo,orderNo);
                    redisCache.expire(orderNo,5L, TimeUnit.MINUTES);
                }else{
                    return  error(" notify nearly 5 mins exists: " +  orderNo );
                }

                String respDesc = "";
                if (StringUtils.isNotNull(responseMap.get("respDesc"))) {
                    respDesc = UriEncoder.decode(responseMap.get("respDesc").toString());
                }
                logger.info("********交易管理-回调 responseMap" + JSONObject.toJSONString(responseMap));


                if (panRecharge != null) {
                    logger.info("********交易管理-充值 Info:" + JSONObject.toJSONString(panRecharge));
                    if (respCode.equals("0000") && !panRecharge.getStatus().equals(TransStatus.COMPLETED)) {
                        panRecharge.setStatus(TransStatus.COMPLETED);//Completed
                        iTransService.recharge(panRecharge);
                    }
                } else if (panWithdraw != null) {
                    String remark = "";
                    if(StringUtils.isNotEmpty(respDesc)){
                        if(respDesc.equals("交易成功")){
                            remark = "success";
                        }else if(respDesc.equals("Incorrect bank information")){
                            remark = "Incorrect bank information";
                        }else{
                            remark = "Please check account information";
                        }
                    }
                    panWithdraw.setRemark(remark);
                    panWithdraw.setRespDesc(respDesc);
                    logger.info("********交易管理-提现 Info:" + JSONObject.toJSONString(panWithdraw));
                    if (respCode.equals("0000") && !panWithdraw.getStatus().equals(TransStatus.COMPLETED)) {
                        panWithdraw.setStatus(TransStatus.COMPLETED);
                        iTransService.withdraw(panWithdraw);
                    } else if (respCode.equals("0068") && !panWithdraw.getStatus().equals(TransStatus.FAILED)) {
                        panWithdraw.setStatus(TransStatus.FAILED);
                        iTransService.withdraw(panWithdraw);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajax;
    }


    public static Map<String, Object> respStr2Map(String respStr) {
        Map<String, Object> sendMap = new TreeMap<>();
        String[] paramPairs = respStr.split("&");
        for (String param : paramPairs) {
            if (StringUtils.isEmpty(param) || param.split("=").length <= 1) {
                continue;
            }
            if (StringUtils.isEmpty(param.split("=")[0]) || StringUtils.isEmpty(param.split("=")[1])) {
                continue;
            }
            //避免出现有的参数具体的值中带了“=”造成计算错误
            sendMap.put(param.split("=")[0], param.substring(param.indexOf("=") + 1, param.length()));
        }
        return sendMap;
    }


    public static PublicKey getPublicKey(String key) throws Exception {
        if (key == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        byte[] buffer = Base64.decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return keyFactory.generatePublic(keySpec);
    }


    public static boolean verify(String withoutSignStr, String sign, PublicKey publicKey, String inputCharset) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            signature.update(withoutSignStr.getBytes(inputCharset));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getSignatureStr(Map<String, Object> respMap) {
        Map<String, Object> map = new TreeMap<String, Object>();
        map.putAll(respMap);
        StringBuffer signature = new StringBuffer();
        for (String key : map.keySet()) {
            if (!StringUtils.isEmpty(map.get(key).toString()) && !"signature".equals(key)) {
                signature.append(key + "=" + UriEncoder.decode((String) map.get(key)) + "&");
            }
        }
        return signature.toString().substring(0, signature.length() - 1);
    }


    private Map<String, String> initNotifyParam(HttpServletRequest request, String protocol) {
        Map<String, String> notifyRequest = new TreeMap<String, String>();
        switch (protocol) {
            case "post":
            case "get":
                notifyRequest = initParams(request);
                break;
            case "io":
                notifyRequest = intiParamsByIo(request);
                break;
            default:
                initParams(request);
                break;
        }


        return notifyRequest;
    }


    /**
     * 以IO流的方式接收上游通知
     *
     * @param request
     * @return
     */
    @SuppressWarnings("finally")
    private Map<String, String> intiParamsByIo(HttpServletRequest request) {
        Map<String, String> notifyRequest = new TreeMap<String, String>();
        try {
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            if (sb.length() > 0) {
                notifyRequest.put("notifyRequest", sb.toString());
            }
        } catch (Exception e) {

        } finally {
            return notifyRequest;
        }
    }

    private Map<String, String> initParams(HttpServletRequest request) {
        Enumeration<String> param = request.getParameterNames();
        Map<String, String> pramsMap = new TreeMap<String, String>();
        while (param.hasMoreElements()) {
            String string = (String) param.nextElement();
            pramsMap.put(string, request.getParameter(string));
        }
        return pramsMap;
    }


}
