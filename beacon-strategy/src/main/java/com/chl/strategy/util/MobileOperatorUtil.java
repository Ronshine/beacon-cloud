package com.chl.strategy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MobileOperatorUtil {
    /**
     * 注入请求工具
     */
    @Autowired
    private RestTemplate restTemplate;

    private final String url = "https://cx.shouji.360.cn/phonearea.php?number=";

    /**
     * 注入json解析
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 常量
     */
    private final String CODE = "code";
    private final String DATA = "data";
    private final String PROVINCE = "province";
    private final String CITY = "city";
    private final String SP = "sp";
    private final String SPACE = " ";
    private final String SEPARATOR = ",";

    /**
     * 根据三方网站获取信息
     * @param mobile  传入的号码前7位
     */
    public String getMobileInfoBy360(String mobile){
        //1、获取信息
        String MobileInfoJSON = restTemplate.getForObject(url + mobile, String.class);

        //2.解析json
        Map map = null;
        try {
            map = objectMapper.readValue(MobileInfoJSON, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //判断获取到的信息是否可用
        Integer code = (Integer) map.get(CODE);
        if (code != 0){
            return null;
        }

        //获取具体信息
        Map<String,String> areaAndOperatorName = (Map<String, String>) map.get(DATA);
        String province = areaAndOperatorName.get(PROVINCE);
        String city = areaAndOperatorName.get(CITY);
        String sp = areaAndOperatorName.get(SP);

        //3、返回 省 市,运营商 格式
        return province + SPACE + city + SEPARATOR + sp;
    }
}
