package com.ruoyi.web.controller.appclient;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.impl.SendSmsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.jar.JarEntry;

/**
 * Created by chaopan on 2023/5/23
 */
@Controller
@RequestMapping("/4app")
public class AppClientCtrl {
    @Autowired
    private ISysConfigService iSysConfigService;

    private static final Logger log = LoggerFactory.getLogger(AppClientCtrl.class);
    /**
     * 获取可用的链接 因为需要多个url同时进行公布
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/avaliableH5url")
    public AjaxResult avaliableUrl( HttpServletRequest request) throws Exception{
        //理论上是配置了，然后以逗号分割
        String avaliableH5url = iSysConfigService.selectConfigByKey("avaliableH5url");
        if(StringUtils.isEmpty(avaliableH5url)){
            log.info("sorry can't get avaliableH5url please check it again!");
            return AjaxResult.error(HttpStatus.MOVED_PERM," avaliableH5url is empty!");
        }else{
            return AjaxResult.error(HttpStatus.SUCCESS,avaliableH5url);
        }



    }

}
