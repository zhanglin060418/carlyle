package com.ruoyi.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.SendSmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Service
public class SendSmsServiceImpl implements SendSmsService {
    @Autowired
    private ISysConfigService iSysConfigService;

    static final String CIPHER = "00000000";

    private static final Logger log = LoggerFactory.getLogger(SendSmsServiceImpl.class);

    @Override
    public  String sendSms(String phoneNo, String digitCode) {

        log.info("*******SendSmsServiceImpl sendSms start..");

        String smsPlatformInfo = iSysConfigService.selectConfigByKey("sms_platform_info");
        JSONObject smsObject = JSON.parseObject(smsPlatformInfo);
        String url = smsObject.get("url").toString();
        String spid = smsObject.get("spid").toString();
        String pwd = smsObject.get("pwd").toString();
        String senderid = smsObject.get("senderid").toString();
        /**
         * 此模版不可修改
         */
        String msg = "Carlyle OTP is "+digitCode+", valid for 5 min, please do not share with others." ;

        log.info("********phoneNo:"+phoneNo+",msg:"+msg);

        String formats = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currenttime = sdf.format(new Date());
        String result = "";
        try {
            String unixtime = Date2TimeStamp(currenttime, formats);
            String md5pwd = encryptPwd(spid, pwd, unixtime);
            String newmsg = encodeHex(msg.getBytes("UTF-8"));
            HttpRequester request = HttpRequester.INS;

            Map<String, String> map = new HashMap<String, String>();
            map.put("spid", spid);
            map.put("pwd", md5pwd);
            map.put("phones", phoneNo);
            map.put("message", newmsg);
            map.put("senderid", senderid);
            map.put("timestamp", unixtime);
            String str = JSON.toJSONString(map);

            log.info("*************sendSms request info:"+str);

            Map<String, String> propertys = new HashMap<String, String>();
            propertys.put("content-type", "application/json");

            HttpRespons hr1 = request.send(url, "POST", map, propertys, str, "UTF-8");
            JSONObject jsonObject = JSON.parseObject(hr1.getContent());
            log.info("*************sendSms result info:"+hr1.getContent());
            result = jsonObject.get("status").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验证是否手机号码
     * @param phoneNo
     * @return
     */
    @Override
    public String verifyIsPhone(String phoneNo) {

        log.info("*******SendSmsServiceImpl verifyIsPhone start..");
        String smsPlatformInfo = iSysConfigService.selectConfigByKey("sms_platform_info");
        JSONObject smsObject = JSON.parseObject(smsPlatformInfo);
        String url = smsObject.get("verifyIsPhoneUrl").toString();
        String spid = smsObject.get("spid").toString();
        String pwd = smsObject.get("pwd").toString();
        log.info("********verifyIsPhone phoneNo:"+phoneNo);
        String formats = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currenttime = sdf.format(new Date());
        String res = "";
        try {
            String unixtime = Date2TimeStamp(currenttime, formats);
            String md5pwd = encryptPwd(spid, pwd, unixtime);
            HttpRequester request = HttpRequester.INS;

            Map<String, String> map = new HashMap<String, String>();
            map.put("spid", spid);
            map.put("pwd", md5pwd);
            map.put("phone", phoneNo);
            map.put("timestamp", unixtime);
            String str = JSON.toJSONString(map);

            log.info("*******verifyIsPhone request info:"+str);

            Map<String, String> propertys = new HashMap<String, String>();
            propertys.put("content-type", "application/json");

            HttpRespons hr1 = request.send(url, "POST", map, propertys, str, "UTF-8");
            JSONObject jsonObject = JSON.parseObject(hr1.getContent());
            log.info("*************verifyIsPhone result info:"+hr1.getContent());
            /**
             * status为0时验证通过
             */
            res = jsonObject.get("status").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static final String encodeHex(byte[] bytes) {
        char[] Digit = {'0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'};

        char[] ob = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            ob[i * 2] = Digit[(bytes[i] >>> 4) & 0X0F];
            ob[i * 2 + 1] = Digit[bytes[i] & 0X0F];
        }
        return new String(ob);
    }

    public static String Date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptPwd(String user, String password, String timestamp) {
        try {
            String passwordStr = user.toUpperCase() + CIPHER + password + timestamp;
            // 对密码进行加密
            return getMD5Str(passwordStr);
        } catch (Exception e) {
            return password;
        }
    }

    private static String getMD5Str(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte b : byteArray) {
            if (Integer.toHexString(0xFF & b).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & b));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & b));
            }
        }
        return md5StrBuff.toString();
    }

    public enum HttpRequester {
        INS;

        public HttpRespons send(String urlString,
                                String method,
                                Map<String, String> parameters,
                                Map<String, String> propertys,
                                String json,
                                String encode) throws IOException {
            HttpURLConnection urlConnection = null;
            boolean isjson = false;
            if (method.equalsIgnoreCase("GET") && parameters != null) {
                StringBuilder param = new StringBuilder(64);
                int i = 0;
                for (String key : parameters.keySet()) {
                    if (i == 0) {
                        param.append("?");
                    } else {
                        param.append("&");
                    }
                    param.append(key).append("=").append(parameters.get(key));
                    i++;
                }
                urlString += param;
            }
            URL url = new URL(urlString);
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                try {
                    trustAllHttpsCertificates();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                HttpsURLConnection.setDefaultHostnameVerifier(hv);
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            if (propertys != null) {
                for (String key : propertys.keySet()) {
                    if ("application/json".equals(propertys.get(key))
                            || "application/json;charset=utf-8".equals(propertys.get(key))) {
                        isjson = true;
                    }
                    urlConnection.addRequestProperty(key, propertys.get(key));
                }
            }
            urlConnection.addRequestProperty("Connection", "close");
            if (method.equalsIgnoreCase("POST") && parameters != null) {
                StringBuilder param = new StringBuilder(64);
                for (String key : parameters.keySet()) {
                    param.append("&");
                    param.append(key).append("=").append(parameters.get(key));
                }
                if (isjson) {
                    urlConnection.getOutputStream().write(json.getBytes());
                } else {
                    urlConnection.getOutputStream().write(param.toString().getBytes());
                }
                urlConnection.getOutputStream().flush();
                urlConnection.getOutputStream().close();
            }
            return this.makeContent(urlString, urlConnection, encode);
        }

        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: "
                        + urlHostName
                        + " vs. "
                        + session.getPeerHost());
                return true;
            }
        };

        private static void trustAllHttpsCertificates() throws Exception {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
            javax.net.ssl.TrustManager tm = new HttpRequester.miTM();
            trustAllCerts[0] = tm;
            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }

        static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
                return true;
            }

            public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
                return true;
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                                           String authType)
                    throws java.security.cert.CertificateException {
                return;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                                           String authType)
                    throws java.security.cert.CertificateException {
                return;
            }
        }

        private HttpRespons makeContent(String urlString,
                                        HttpURLConnection urlConnection,
                                        String encode) throws IOException {
            HttpRespons httpResponser = new HttpRespons();
            BufferedReader bufferedReader = null;
            try {
                InputStream in = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(in));
                httpResponser.contentCollection = new Vector<String>();
                StringBuilder temp = new StringBuilder(64);
                String line = bufferedReader.readLine();
                while (line != null) {
                    httpResponser.contentCollection.add(line);
                    temp.append(line).append("\r\n");
                    line = bufferedReader.readLine();
                }
                String ecod = urlConnection.getContentEncoding();
                if (ecod == null) {
                    ecod = encode;
                }
                httpResponser.urlString = urlString;
                httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
                httpResponser.file = urlConnection.getURL().getFile();
                httpResponser.host = urlConnection.getURL().getHost();
                httpResponser.path = urlConnection.getURL().getPath();
                httpResponser.port = urlConnection.getURL().getPort();
                httpResponser.protocol = urlConnection.getURL().getProtocol();
                httpResponser.query = urlConnection.getURL().getQuery();
                httpResponser.ref = urlConnection.getURL().getRef();
                httpResponser.userInfo = urlConnection.getURL().getUserInfo();
                httpResponser.content = new String(temp.toString().getBytes(), ecod);
                httpResponser.contentEncoding = ecod;
                httpResponser.code = urlConnection.getResponseCode();
                httpResponser.message = urlConnection.getResponseMessage();
                httpResponser.contentType = urlConnection.getContentType();
                httpResponser.method = urlConnection.getRequestMethod();
                httpResponser.connectTimeout = urlConnection.getConnectTimeout();
                httpResponser.readTimeout = urlConnection.getReadTimeout();
                return httpResponser;
            } catch (IOException e) {
                throw e;
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

    static class HttpRespons {
        String urlString;
        int defaultPort;
        String file;
        String host;
        String path;
        int port;
        String protocol;
        String query;
        String ref;
        String userInfo;
        String contentEncoding;
        String content;
        String contentType;
        int code;
        String message;
        String method;
        int connectTimeout;
        int readTimeout;
        Vector<String> contentCollection;

        public String getContent() {
            return content;
        }

        public String getContentType() {
            return contentType;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Vector<String> getContentCollection() {
            return contentCollection;
        }

        public String getContentEncoding() {
            return contentEncoding;
        }

        public String getMethod() {
            return method;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public String getUrlString() {
            return urlString;
        }

        public int getDefaultPort() {
            return defaultPort;
        }

        public String getFile() {
            return file;
        }

        public String getHost() {
            return host;
        }

        public String getPath() {
            return path;
        }

        public int getPort() {
            return port;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getQuery() {
            return query;
        }

        public String getRef() {
            return ref;
        }

        public String getUserInfo() {
            return userInfo;
        }
    }

    /***
     *  buka短信平台发送短信
     * @param digitCode
     * @param numbers
     * @return
     */
    public  String sendSmsBuka( String numbers,String digitCode) {
        //Buka_SMS_platform
        String smsPlatformInfo = iSysConfigService.selectConfigByKey("Buka_SMS_platform");
        JSONObject smsObject = JSON.parseObject(smsPlatformInfo);
        String url = smsObject.get("url").toString();
        String apiKey = smsObject.get("apiKey").toString();
        String apiPwd = smsObject.get("apiPwd").toString();
        String appId = smsObject.get("appId").toString();
        String senderId = smsObject.get("senderId").toString();

        String result ="";
        String content = "Carlyle OTP is "+digitCode+", valid for 1 hour, please do not share with others." ;
        HttpRequest request = HttpRequest.post(url);
        // 生成时间戳
        final String datetime = String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        //生成签名
        final String sign = SecureUtil.md5(apiKey.concat(apiPwd).concat(datetime));
        // 请求头
        request.header(Header.CONNECTION, "Keep-Alive")
                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                .header("Sign", sign)
                .header("Timestamp", datetime)
                .header("Api-Key", apiKey);

        final String params = JSONUtil.createObj()
                .set("appId", appId)
                .set("numbers", numbers)
                .set("content", content)
                .set("senderId", senderId)
                .toString();
          log.info("*************sendSmsBuka reqest info:"+params);
        HttpResponse response = request.body(params).execute();
        if (response.isOk()) {
            JSONObject jsonObject = JSON.parseObject(response.body());
            log.info("*************sendSmsBuka response info:"+response.body());
            result = jsonObject.get("status").toString() ;
        }
        return result;
    }
}
