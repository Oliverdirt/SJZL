package com.ctsi.ssdc.sensitiveword.config;

import java.util.List;

import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWordExample;
import com.ctsi.ssdc.sensitiveword.service.CscpSensitiveWordService;
import com.ctsi.ssdc.sensitiveword.util.SensitiveWordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 *  敏感词配置
 * @author ctsi
 *
 */
@Configuration
public class SensitiveWordConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SensitiveWordConfig.class);
	
	@Value("${ctsi.sensitiveword.global-filter.enable:false}")
	private boolean globalEnable;
	
	@Value("${ctsi.sensitiveword.method-filter.enable:true}")
	private boolean methodEnable;
	
	@Autowired
	private CscpSensitiveWordService cscpSensitiveWordService;

    public boolean isGlobalEnable() {
		return globalEnable;
	}

	public void setGlobalEnable(boolean globalEnable) {
		this.globalEnable = globalEnable;
	}

	public boolean isMethodEnable() {
		return methodEnable;
	}

	public void setMethodEnable(boolean methodEnable) {
		this.methodEnable = methodEnable;
	}

	private static class SensitiveWordInit{
	}

	@Bean
	public SensitiveWordInit getInit() {
		log.info("是否启用全局敏感词过滤:" + isGlobalEnable());
		log.info("是否启用方法级敏感词过滤:" + isMethodEnable());
    	if(isGlobalEnable() || isMethodEnable()) {
    		CscpSensitiveWordExample example = new CscpSensitiveWordExample();
    		example.setOrderByClause("update_time asc");
    		List <CscpSensitiveWord> list = cscpSensitiveWordService.findByExample(example).getData();
    		SensitiveWordUtil.init(list);
    	}
    	return new SensitiveWordInit();
    }
}