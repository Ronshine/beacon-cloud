package com.chl.web.controller;

import com.chl.common.constant.WebConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.utils.R;
import com.chl.common.vo.ResultVO;
import com.chl.web.VO.ClientBusinessVO;
import com.chl.web.entity.ClientBusiness;
import com.chl.web.entity.SmsUser;
import com.chl.web.service.ClientBusinessService;
import com.chl.web.service.SmsRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 客户controller
 */
@RestController
@Slf4j
public class ClientBusinessController {
    @Autowired
    private SmsRoleService smsRoleService;

    @Autowired
    private ClientBusinessService clientBusinessService;

    //地址
    @GetMapping("/sys/clientbusiness/all")
    public ResultVO all(){
        //1、获得当前登入的用户信息
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        //判断是否为空
        if (smsUser == null){
            log.info("【查询客户信息】 该用户未登录");
            return R.error(ExceptionEnum.NOT_LOGIN);
        }
        Integer userId = smsUser.getId();

        //2、通过用户id查询该用户所拥有的角色
        Set<String> roleNameSet = smsRoleService.findRoleByUserId(userId);

        //外部声明
        List<ClientBusiness> list = null;
        //3、通过用户是否为管理员区分可查询内容 管理员可以查询所有客户  非管理员只能查询所拥有的客户
        if (!roleNameSet.isEmpty() && roleNameSet.contains(WebConstants.root)){
            //管理员可以查询所有客户
            list = clientBusinessService.findAll();
        }else {
            //非管理员只能查询所拥有的客户
            list =  clientBusinessService.findByUserId(userId);
        }

        //4、将查询到的list的集合转换为需要响应回去的VO对象
        List<ClientBusinessVO> data = new ArrayList<>();
        for (ClientBusiness clientBusiness : list) {
            ClientBusinessVO vo = new ClientBusinessVO();
            BeanUtils.copyProperties(clientBusiness,vo);
            data.add(vo);
        }

        //5、返回
        return R.ok(data);
    }
}
