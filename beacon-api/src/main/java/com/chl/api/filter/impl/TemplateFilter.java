package com.chl.api.filter.impl;

import com.chl.api.clients.BeanCacheClient;
import com.chl.api.filter.CheckFilter;
import com.chl.common.constant.ApiConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.ApiException;
import com.chl.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;


@Service(value = "template")
@Slf4j
public class TemplateFilter implements CheckFilter {
    @Autowired
    private BeanCacheClient beanCacheClient;

    /**
     * 缓存中模板信息存放字段名
     */
    private static final String TEMPLATE_TEXT = "templateText";

    /**
     *  模板中变量的占位符
     */
    private static final String TEMPLATE_PLACEHOLDER = "#";

    @Override
    public void check(StandardSubmit submit) {
        log.info("【接口模块-校验信息模板】模板校验中！！！！");
        //1.获取短信内容还有签名已经对应签名id
        String text = submit.getText();
        String sign = submit.getSign();//纯文本没有【】
        Long signId = submit.getSignId();

        //2.将原本短信内容中的签名去掉
        text = text.replace(ApiConstants.SIGN_PREFIX + sign + ApiConstants.SIGN_SUFFIX,"");

        //3.从缓存中获取签名短信模板
        Set<Map> templates = beanCacheClient.sMembers(ApiConstants.CLIENT_TEMPLATE + signId);

        //4.遍历templates集合校验模板
        if (templates != null && templates.size() > 0){
            for (Map template : templates) {
                String templateText = (String) template.get(TEMPLATE_TEXT);
                //4.1 如果短信内容直接等于模板内容放行
                if (text.equals(templateText)){
                    log.info("【接口模块】校验模板通过 templateText = {}",templateText);
                    return;
                }
                //4.2 如果设置了短信中一个变量
                if (templateText != null && templateText.contains(TEMPLATE_PLACEHOLDER) &&
                templateText.length() - templateText.replaceAll(TEMPLATE_PLACEHOLDER,"").length() == 2){
                    //获取templateText的前缀消息
                    String templateTextPrefix = templateText.substring(0,templateText.indexOf(TEMPLATE_PLACEHOLDER));
                    //获取templateText的后缀消息
                    String templateTextSuffix = templateText.substring(templateText.lastIndexOf(TEMPLATE_PLACEHOLDER)+1);

                    //校验短信内容是否和templateText模板符合
                    if (text.startsWith(templateTextPrefix) && text.endsWith(templateTextSuffix)){
                        log.info("【接口模块】 校验模板通过 templateText = {}",templateText);
                        return;
                    }
                }
                //5 校验失败抛出错误
                log.info("【接口模块】 无匹配模板 ！");
                throw new ApiException(ExceptionEnum.ERROR_TEMPLATE);
            }
        }
    }
}
