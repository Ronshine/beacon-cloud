package com.chl.web.controller;

import com.chl.common.constant.WebConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.utils.R;
import com.chl.common.vo.ResultVO;
import com.chl.web.VO.SearchVO;
import com.chl.web.client.SearchClient;
import com.chl.web.entity.ClientBusiness;
import com.chl.web.entity.SmsUser;
import com.chl.web.service.ClientBusinessService;
import com.chl.web.service.SmsRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class SearchController {
    @Autowired
    private SmsRoleService smsRoleService;

    @Autowired
    private ClientBusinessService clientBusinessService;

    @Autowired
    private SearchClient searchClient;

    @GetMapping("/sys/search/list")
    public ResultVO list(@RequestParam Map<String,Object> map){
        //1、判断用户是否登入
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null){
            log.info("【搜索短信信息】 该用户未登录");
            return R.error(ExceptionEnum.NOT_LOGIN);
        }

        String clientIDStr = (String) map.get("clientID");
        Long clientID = null;
        if(!ObjectUtils.isEmpty(clientIDStr)){
            clientID = Long.parseLong(clientIDStr);
        }

        //1.1、通过判断给用户是否拥有管理员身份进行判断是能否查询对应公司的短信信息
        Set<String> roleNames = smsRoleService.findRoleByUserId(smsUser.getId());
        if (roleNames != null && !roleNames.contains(WebConstants.ROOT)){
            //非管理员身份
            //1.2、查询当前登录用户所包含的全部客户id（公司信息）
            List<ClientBusiness> allClientIDs = clientBusinessService.findByUserId(smsUser.getId());

            //1.3、判断用户传入的clientID是否为空
            if (clientID == null){
                List<Long> list = new ArrayList<>();
                for (ClientBusiness userClientID : allClientIDs) {
                    list.add(userClientID.getId());
                }
                map.put("clientID",list);
            }else {
                boolean flag = false;
                for (ClientBusiness userClientID : allClientIDs) {
                    if (clientID.equals(userClientID.getId())){
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    // 请求参数不满足当前登录用户的信息
                    log.info("【搜索短信信息】 用户权限不足！！");
                    return R.error(ExceptionEnum.SMS_NO_AUTHOR);
                }
            }
        }

        //2、调用搜索模块查询数据，返回total和rows
        Map<String, Object> result = searchClient.findSmsByParameters(map);

        //3、判断返回的total，如果total为0，正常返回
        Long total = Long.parseLong(result.get("total") + "");
        if (total == 0) {
            return R.ok(0L,null);
        }

        //4、如果数据正常，做返回数据的封装，声明SearchSmsVO的实体类，
        List<Map> list = (List<Map>) result.get("rows");
        List<SearchVO> rows = new ArrayList<>();
        // 遍历集合，封装数据
        for (Map row : list) {
            SearchVO vo = new SearchVO();
            try {
                BeanUtils.copyProperties(vo,row);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rows.add(vo);
        }

        //4、如果数据正常，做返回数据的封装
        return R.ok(total,rows);
    }
}
