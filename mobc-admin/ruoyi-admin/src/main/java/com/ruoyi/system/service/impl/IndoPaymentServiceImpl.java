package com.ruoyi.system.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.indo.payment.IndoPaymentService;
import com.indo.payment.IndoRechargeResult;
import com.indo.payment.IndoWithdrawResult;
import com.ruoyi.system.domain.PanChannel;
import com.ruoyi.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 银行名单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-09
 */
@Service
public class IndoPaymentServiceImpl implements IndoPaymentService {

    @Value("${spring.profiles.env}")
    private String activeProfile;

    @Autowired
    private PanChannelServiceImpl panChannelService;

    public static final String SIGN_SHA1_WITH_RSA = "SHA1WithRSA";
    private static final Logger log = LoggerFactory.getLogger(IndoPaymentServiceImpl.class);

    /**
     * 充值
     * @param  domain  创建订单的域名信息因为前端可能来自不同的域名来的
     * @return
     */
    @Override
    public IndoRechargeResult recharge(BigDecimal amount, String requestNo, String name, String mobile,
                                       String email, String domain) {
        log.info("********用户充值 requestNo:" + requestNo + ",domain ：" + domain);
        IndoRechargeResult indoRechargeResult = new IndoRechargeResult();
        try {
            PanChannel panChannel = panChannelService.selectPanChannelByStatus();
            String jsonString = panChannel.getJsonParam();
            JSONObject channelJsonParam = JSON.parseObject(jsonString);
            getUrlsByDomain(domain, channelJsonParam);

            String merNo = panChannel.getChannelMerno();
            String url = channelJsonParam.getString("endPoint_indo");
            String transAmt = String.valueOf(amount);
            String surl = channelJsonParam.getString("surl") + requestNo;
            String furl = channelJsonParam.getString("furl") + requestNo;

            Map<String, Object> sendMap = new TreeMap<>();
            sendMap.put("requestNo", requestNo);
            sendMap.put("version", channelJsonParam.get("version"));
            sendMap.put("productId", channelJsonParam.get("productId"));
            sendMap.put("transId", channelJsonParam.get("transRechargeId"));
            sendMap.put("merNo", merNo);
            sendMap.put("transAmt", transAmt);
            sendMap.put("name", "onemoretime");
            sendMap.put("mobile", mobile);
            sendMap.put("email", email);
            sendMap.put("surl", surl);
            sendMap.put("furl", furl);
            sendMap.put("currencyCode", "NGN");
            sendMap.put("notify_url", channelJsonParam.get("notify_url"));
            sendMap.put("signature", getSign(sendMap,channelJsonParam));
            log.info("********用户充值 sendMap:" + JSONObject.toJSONString(sendMap));

            HttpRequest request = new HttpRequest(url).method(Method.GET);
            request.setConnectionTimeout(20000);
            request.form(sendMap);
            log.info("********用户充值 requestInfo:" + JSONObject.toJSONString(request));

            HttpResponse response = request.execute();
            log.info("********用户充值 responseInfo:" + JSONObject.toJSONString(response));
            Map<String, Object> responseMap = respStr2Map(response.body());

            indoRechargeResult.setRequestNo(responseMap.get("requestNo").toString());
            indoRechargeResult.setOrderNo(responseMap.get("orderNo").toString());
            indoRechargeResult.setOrderDate(System.currentTimeMillis());
            indoRechargeResult.setPayInfoUrl(URLDecoder.decode(responseMap.get("payInfo").toString(),"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indoRechargeResult;
    }

    @Override
    public IndoRechargeResult recharge(BigDecimal amount, String requestNo, String name, String mobile,
                                       String email, String domain,String channel) {
        log.info("********用户充值 requestNo:" + requestNo + ",domain ：" + domain+ ",channel ：" + channel);
        IndoRechargeResult indoRechargeResult = new IndoRechargeResult();
        try {
            PanChannel panChannel = panChannelService.selectPanChannelByChannelId(Long.parseLong(channel));
            String jsonString = panChannel.getJsonParam();
            JSONObject channelJsonParam = JSON.parseObject(jsonString);
            getUrlsByDomain(domain, channelJsonParam);

            String merNo = panChannel.getChannelMerno();
            String url = channelJsonParam.getString("endPoint_indo");
            String transAmt = String.valueOf(amount);
            String surl = channelJsonParam.getString("surl") + requestNo;
            String furl = channelJsonParam.getString("furl") + requestNo;

            Map<String, Object> sendMap = new TreeMap<>();
            sendMap.put("requestNo", requestNo);
            sendMap.put("version", channelJsonParam.get("version"));
            sendMap.put("productId", channelJsonParam.get("productId"));
            sendMap.put("transId", channelJsonParam.get("transRechargeId"));
            sendMap.put("merNo", merNo);
            sendMap.put("transAmt", transAmt);
            sendMap.put("name", "onemoretime");
            sendMap.put("mobile", mobile);
            sendMap.put("email", email);
            sendMap.put("surl", surl);
            sendMap.put("furl", furl);
            sendMap.put("currencyCode", "NGN");
            sendMap.put("notify_url", channelJsonParam.get("notify_url"));
            sendMap.put("signature", getSign(sendMap,channelJsonParam));
            log.info("********用户充值 sendMap:" + JSONObject.toJSONString(sendMap));

            HttpRequest request = new HttpRequest(url).method(Method.GET);
            request.setConnectionTimeout(20000);
            request.form(sendMap);
            log.info("********用户充值 requestInfo:" + JSONObject.toJSONString(request));

            HttpResponse response = request.execute();
            log.info("********用户充值 responseInfo:" + JSONObject.toJSONString(response));
            Map<String, Object> responseMap = respStr2Map(response.body());

            indoRechargeResult.setRequestNo(responseMap.get("requestNo").toString());
            indoRechargeResult.setOrderNo(responseMap.get("orderNo").toString());
            indoRechargeResult.setOrderDate(System.currentTimeMillis());
            indoRechargeResult.setPayInfoUrl(URLDecoder.decode(responseMap.get("payInfo").toString(),"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indoRechargeResult;
    }

    /**
     * 提现
     * @param domain
     * @return
     */
    @Override
    public IndoWithdrawResult withdraw(String requestNo, String currencyCode, BigDecimal amount, String bankCode,
                                       String bankName, String cardNo, String beneficiaryNo, String beneficiaryName, String beneficiaryMobile,
                                       String beneficiaryEmail, String domain) {
        log.info("**********用户提现 requestNo：" + requestNo + " withdraw domain ：" + domain);
        IndoWithdrawResult indoWithdrawResult = new IndoWithdrawResult();
        try {
            //这里因为只有一个代付渠道，所以就特定通道即可
            PanChannel panChannel = panChannelService.selectPanProxyChannelByStatus();
            String jsonString = panChannel.getJsonParam();
            JSONObject channelJsonParam = JSON.parseObject(jsonString);
            getUrlsByDomain(domain, channelJsonParam);
            String merNo = panChannel.getChannelMerno();
            String url = channelJsonParam.getString("endPoint_indo");
            String transAmt = String.valueOf(amount);


            Map<String, Object> sendMap = new TreeMap<>();
            sendMap.put("requestNo", requestNo);
            sendMap.put("version", channelJsonParam.get("version"));
            sendMap.put("productId", channelJsonParam.get("productId"));
            sendMap.put("transId", channelJsonParam.get("transWithdrawId"));
            sendMap.put("merNo", merNo);
            sendMap.put("transAmt", transAmt);
            sendMap.put("bankCode", bankCode);
            sendMap.put("bankName", bankName);
            sendMap.put("cardNo", cardNo);
            sendMap.put("beneficiaryWay", "0");
            sendMap.put("remark", "00000");
            sendMap.put("currencyCode", currencyCode);
            sendMap.put("notify_url", channelJsonParam.get("notify_url"));
            sendMap.put("signature", getSign(sendMap,channelJsonParam));
            HttpRequest request = new HttpRequest(url);
            request.form(sendMap);
            log.info("**********用户提现 resuestMap：" + JSONObject.toJSONString(sendMap));
            HttpResponse response = request.execute();
            log.info("**********用户提现 responseBody：" + JSONObject.toJSONString(response.body()));
            Map<String, Object> responseMap = respStr2Map(response.body());
            log.info("**********用户提现 responseMap：" + JSONObject.toJSONString(responseMap));

            indoWithdrawResult.setRequestNo(requestNo);
            if (responseMap.get("respCode").toString().equals("0000") || responseMap.get("respCode").toString().equals("P000")) {
                indoWithdrawResult.setOrderNo(responseMap.get("orderNo").toString());
                indoWithdrawResult.setChannelId(panChannel.getChannelId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return indoWithdrawResult;
    }

    /**
     * 查询商户余额信息
     *
     * @throws Exception
     */
    @Override
    public JSONObject  queryBalance(String channelId) throws Exception {

        PanChannel panChannel = panChannelService.selectPanChannelByChannelId(Long.parseLong(channelId));
        String jsonString = panChannel.getJsonParam();
        JSONObject channelJsonParam = JSON.parseObject(jsonString);
        String merNo =panChannel.getChannelMerno()  ;//跟客服沟通获取到商户号信息，一般以8开头
        String pub_key = channelJsonParam.getString("pub_key");//跟客服沟通会获取到密钥包解压后“商户号_prv.pem”文件中复制，去掉换行即可，也可以使用文件加载方式，建议密文数据库保存
        String url = channelJsonParam.getString("endPoint");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String requestNo = formatter.format(new Date());
        String version = "V1.0";
        String transId = "09";
        HttpRequest request = new HttpRequest(url);
        Map<String, Object> sendMap = new TreeMap<>();
        sendMap.put("requestNo", requestNo);
        sendMap.put("version", version);
        sendMap.put("transId", transId);
        sendMap.put("merNo", merNo);
        sendMap.put("signature", getSign(sendMap,channelJsonParam));
        System.err.println("验签结果" + JSONObject.toJSONString(sendMap));
        request.form(sendMap);
        HttpResponse response = request.execute();
        System.out.println(JSONObject.toJSONString(response));
        System.out.println(response.body());
        Map<String, Object> responseMap = respStr2Map(response.body());
        boolean isVerify = verify(getSignatureStr(responseMap), responseMap.get("signature").toString(), getPublicKey(pub_key), "UTF-8");
        JSONObject jsonObject = new JSONObject();
        if(isVerify){
            jsonObject .put("balance",responseMap.get("avaBal"));
            jsonObject .put("freezebalance",responseMap.get("avaFreezeBal"));
        }else{
            return null;
        }
        return jsonObject;
    }

    private String getToSignStrByJson(JSONObject jsonObject) {
        TreeMap<String, Object> sortMap = MapUtil.sort(jsonObject);
        Set<String> keySet = sortMap.keySet();
        boolean isFirst = true;
        StringBuffer result = new StringBuffer("");
        for (String key : keySet) {
            if (null != jsonObject.get(key) && !StringUtils.isEmpty(jsonObject.getString(key))) {
                if (isFirst) {
                    result.append(key).append("=").append(jsonObject.getString(key));
                    isFirst = false;
                } else {
                    result.append("&").append(key).append("=").append(jsonObject.getString(key));
                }
            }
        }
        return result.toString();
    }


    private static String cutRemark(String remark) {
        int remarkMaxLength = 17;//相当于一个数字字符串长度
        if (remark.length() <= remarkMaxLength) {
            return remark;
        } else {
            StringBuffer result = new StringBuffer("");
            for (int i = remarkMaxLength; i > 0; i--) {
                result.append(remark.charAt(remark.length() - i));
            }
            return result.toString();
        }

    }
    private void getUrlsByDomain(String domain, JSONObject channelJsonParam) {
        //因为只有生产才涉及多域名
        if ("pro".equalsIgnoreCase(activeProfile) && !StringUtil.isEmpty(domain)) {
            domain = domain.split("\\.")[1]; // www.ngn.com - >  ngn
            //"notify_url":"https://service.domain.com/system/payment/notifyUrl"
            String notify_url = channelJsonParam.getString("notify_url").replaceAll("domain", domain);
            channelJsonParam.put("notify_url", notify_url);
            //"surl":"http://www.domain.com/#/result-vue?type=1&requestNo="
            String surl = channelJsonParam.getString("surl").replaceAll("domain", domain);
            channelJsonParam.put("surl", surl);
            // "furl":"http://www.domain.com/#/result-vue?type=0&requestNo="
            String furl = channelJsonParam.getString("furl").replaceAll("domain", domain);
            channelJsonParam.put("furl", furl);
            log.info("domain replace done");
            log.info("notify_url" + notify_url);
            log.info("surl" + surl);
            log.info("furl:" + furl);
        }
    }


    private String getSign(Map<String, Object> sendMap ,JSONObject channelJsonParam) throws Exception {
        String pri_key =channelJsonParam.getString("pri_key"); //跟客服沟通会获取到密钥包解压后“商户号_pub.pem”文件中复制，去掉换行即可，也可以使用文件加载方式，建议密文数据库保存
        PrivateKey privateKey = getPrivateKey(pri_key);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> item : sendMap.entrySet()) {
            if (isEmpty((String) item.getValue().toString())) {
                continue;
            }
            stringBuilder.append(item.getKey()).append('=').append(item.getValue()).append('&');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return signByPrivate(stringBuilder.toString(), privateKey, "UTF8");
    }


    public static PrivateKey getPrivateKey(String key) throws Exception {
//        byte[] keyBytes = buildPKCS8Key(key);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
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


    public static String signByPrivate(String content, PrivateKey privateKey, String input_charset) throws Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
        signature.initSign(privateKey);
        signature.update(content.getBytes(input_charset));
        return Base64.encode(signature.sign());
    }

    public Map<String, Object> respStr2Map(String respStr) {
        Map<String, Object> sendMap = new TreeMap<>();

        String[] paramPairs = respStr.split("&");
        for (String param : paramPairs) {
            if (isEmpty(param) || param.split("=").length <= 1) {
                continue;
            }
            if (isEmpty(param.split("=")[0]) || isEmpty(param.split("=")[1])) {
                continue;
            }
            //避免出现有的参数具体的值中带了“=”造成计算错误
            sendMap.put(param.split("=")[0], param.substring(param.indexOf("=") + 1, param.length()));
        }

        return sendMap;
    }


    public static boolean verify(String withoutSignStr, String sign, PublicKey publicKey, String inputCharset) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA1_WITH_RSA);
            signature.initVerify(publicKey);
            signature.update(withoutSignStr.getBytes(inputCharset));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEmpty(String data) {
        return data == null || data.trim().length() == 0;
    }

    public static String getSignatureStr(Map<String, Object> respMap) {
        Map<String, Object> map = new TreeMap<String, Object>();
        map.putAll(respMap);
        StringBuffer signature = new StringBuffer();
        for (String key : map.keySet()) {
            if (!isEmpty(map.get(key).toString()) && !"signature".equals(key)) {
                signature.append(key + "=" + map.get(key) + "&");
            }
        }
        return signature.toString().substring(0, signature.length() - 1);
    }


    static class Base64 {

        private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', '+', '/'};

        private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
                -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
                -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1,
                -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
                37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
                -1, -1, -1, -1, -1};

        private Base64() {
        }

        public static synchronized String encode(byte[] data) {
            int len = data.length;
            int i = 0;
            int b1, b2, b3;
            StringBuilder sb = new StringBuilder(len);

            while (i < len) {
                b1 = data[i++] & 0xff;
                if (i == len) {
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                    sb.append("==");
                    break;
                }
                b2 = data[i++] & 0xff;
                if (i == len) {
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                    sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                    sb.append("=");
                    break;
                }
                b3 = data[i++] & 0xff;
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
                sb.append(base64EncodeChars[b3 & 0x3f]);
            }
            return sb.toString();
        }

        public static synchronized byte[] decode(String str) {
            byte[] data = str.getBytes();
            int len = data.length;
            ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
            int i = 0;
            int b1, b2, b3, b4;

            while (i < len) {

                /* b1 */
                do {
                    b1 = base64DecodeChars[data[i++]];
                } while (i < len && b1 == -1);
                if (b1 == -1) {
                    break;
                }

                /* b2 */
                do {
                    b2 = base64DecodeChars[data[i++]];
                } while (i < len && b2 == -1);
                if (b2 == -1) {
                    break;
                }
                buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

                /* b3 */
                do {
                    b3 = data[i++];
                    if (b3 == 61) {
                        return buf.toByteArray();
                    }
                    b3 = base64DecodeChars[b3];
                } while (i < len && b3 == -1);
                if (b3 == -1) {
                    break;
                }
                buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

                /* b4 */
                do {
                    b4 = data[i++];
                    if (b4 == 61) {
                        return buf.toByteArray();
                    }
                    b4 = base64DecodeChars[b4];
                } while (i < len && b4 == -1);
                if (b4 == -1) {
                    break;
                }
                buf.write((int) (((b3 & 0x03) << 6) | b4));
            }
            return buf.toByteArray();
        }
    }

}
