package com.ctsi.ssdc.captcha.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.ctsi.ssdc.cache.LoginCache;
import com.ctsi.ssdc.captcha.anno.BiyiCaptcha;
import com.ctsi.ssdc.captcha.component.CaptchaService;
import com.ctsi.ssdc.captcha.component.ValidateRule;
import com.ctsi.ssdc.captcha.constants.CaptchaEnhanceConstant;
import com.ctsi.ssdc.captcha.exception.CaptchaValidateException;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.exception.ErrorConstants;
import com.ctsi.ssdc.util.JacksonUtil;
import com.ctsi.ssdc.util.SpringUtil;
import com.ctsi.ssdc.wrapper.CustomHttpServletRequestWrapper;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 验证码增强处理类
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-04-07 8:51
 **/
@Configuration
public class CaptchaHandlerEnhanceInterceptor  implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(CaptchaHandlerEnhanceInterceptor.class);

    /**
     * 密码错误次数
     */
    @Value("${ctsi.login.captcha-attempts:3}")
    private int captchaAttempts;

    @Resource(name = "${ctsi.login.cache:guavaLoginCache}")
    LoginCache loginCache;

    @Autowired
    private com.anji.captcha.service.CaptchaService captchaService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(request);
        if (handler instanceof HandlerMethod) {

            BiyiCaptcha biyiCaptcha = ((HandlerMethod) handler).getMethodAnnotation(BiyiCaptcha.class);

            // controller没有添加BiyiCaptcha注解
            if (biyiCaptcha == null) {
                return true;
            }

            // 判断是否为增强验证码
            String captchaVerification = request.getHeader(CaptchaEnhanceConstant.CAPTCHAVERIFICATION);

            if(null != captchaVerification
                    && !captchaVerification.equals(CaptchaEnhanceConstant.UNDEFINED)
                    && StringUtil.isNotEmpty(captchaVerification)){
                //进行二次验证
                CaptchaVO captchaVO = new CaptchaVO();
                captchaVO.setCaptchaVerification(captchaVerification);
                ResponseModel responseModel = captchaService.verification(captchaVO);

                if(responseModel.isSuccess()){
                    return true;
                }else {
                    throw new CaptchaValidateException(request.getRequestURI());
                }
            }

            // 字符验证码
            //获取BiyiCaptcha注解中的属性
            String ruleName = biyiCaptcha.rule();
            log.debug(ruleName);
            String serviceName = biyiCaptcha.service();
            log.debug(serviceName);

            //根据注解中的bean名获取spring容器中的bean
            ValidateRule rule = (ValidateRule) SpringUtil.getBean(ruleName);
            CaptchaService service = (CaptchaService) SpringUtil.getBean(serviceName);

            log.debug(request.getHeader(CaptchaEnhanceConstant.CAPTCHA_KEY));
            log.debug(request.getHeader(CaptchaEnhanceConstant.CAPTCHA));

            JSONObject jsonObject = JSON.parseObject(requestWrapper.getBody());
            String userName = String.valueOf(jsonObject.get("username"));
            if(StringUtils.isEmpty(userName)){
                return true;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("loginFailed:");
            stringBuffer.append(userName);
            String countKey = stringBuffer.toString();
            int count = 0;
            if (loginCache != null) {
                count  = loginCache.get(countKey) != null ? loginCache.get(countKey) : 0;
            }
            //如果请求header中带有key和captcha
            if (count >= captchaAttempts){
                if (StringUtil.isNotEmpty(request.getHeader(CaptchaEnhanceConstant.CAPTCHA_KEY))
                        && StringUtil.isNotEmpty(request.getHeader(CaptchaEnhanceConstant.CAPTCHA))) {

                    String key = request.getHeader(CaptchaEnhanceConstant.CAPTCHA_KEY);
                    log.debug(key);
                    String captchaJson = request.getHeader(CaptchaEnhanceConstant.CAPTCHA);
                    log.debug(captchaJson);

                    @SuppressWarnings("unchecked")
                    Map<String,String> captcha = (Map<String,String>) JacksonUtil.json2Bean(captchaJson, Map.class);

                    log.debug(captcha.toString());

                    //比较验证码
                    if(rule.accordWihtRule(key)) {
                        if(service.validateCaptcha(key,captcha)) {
                            log.debug("Captcha validate success");
                            return true;
                        }else {//验证码认证失败
                            throw new CaptchaValidateException(request.getRequestURI());
                        }
                    }

                } else {//请求header中没有
                    throw new BadRequestAlertException(ErrorConstants.CAPTCHA_TYPE, "The information of captcha is missing", "captcha.header", "missing");
                }
            }

        }
        return true;
    }
}
