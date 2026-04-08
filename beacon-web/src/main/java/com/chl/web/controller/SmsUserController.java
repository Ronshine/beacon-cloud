package com.chl.web.controller;

import com.chl.common.constant.WebConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.utils.R;
import com.chl.common.vo.ResultVO;
import com.chl.web.dto.UserDTO;
import com.chl.web.entity.SmsUser;
import com.chl.web.service.SmsMenuService;
import com.chl.web.service.SmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {
    @Autowired
    private SmsMenuService smsMenuService;

    /**
     * 登入接口
     * @param userDTO 用户数据传输对象
     * @param bindingResult  参数校验
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult){
        //1、非空校验
        if (bindingResult.hasErrors()){
            log.info("【认证操作】 参数不合法 userDTO : {}",userDTO);
            return R.error(ExceptionEnum.PARAMETER_ERROR);
        }

        //2、进行验证码比对校验(获取系统生成过在session域中的随机验证码)
        String sysKaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(WebConstants.KAPTCHA);
        if (!userDTO.getCaptcha().equals(sysKaptcha)){
            log.info("【认证操作】 验证码错误 ");
            return R.error(ExceptionEnum.CAPTCHA_ERROR);
        }

        //3、进行认证
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(),userDTO.getPassword());
        token.setRememberMe(userDTO.isRememberMe());
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            //4、根据Shiro的认证，返回响应信息
            log.info("【认证操作】 用户名或密码错误");
            return R.error(ExceptionEnum.AUTHENTICATION_ERROR);
        }

        return R.ok();
    }

    /**
     * 获取用户信息接口
     */
    @GetMapping("/user/info")
    public ResultVO info(){
        //1、获取登入的用户信息
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null){
            log.info("【获取用户信息】 该用户未登录");
            return R.error(ExceptionEnum.NOT_LOGIN);
        }

        //2、封装返回数据
        Map<String,Object> map = new HashMap<>();
        map.put("nickname",smsUser.getNickname());
        map.put("username",smsUser.getUsername());
        return R.ok(map);
    }

    @GetMapping("/menu/user")
    public ResultVO menuList(){
        //获取登入用户对象
        SmsUser smsUser = (SmsUser) SecurityUtils.getSubject().getPrincipal();
        if (smsUser == null){
            log.info("【获取用户信息】 该用户未登录");
            return R.error(ExceptionEnum.NOT_LOGIN);
        }

        //获取查询到的对应用户id的菜单信息
        List<Map<String, Object>> list = smsMenuService.findMenuByUserId(smsUser.getId());
        if (list == null){
            log.info("【获取用户对应菜单】 获取菜单失败！用户id : {}",smsUser.getId());
            return R.error(ExceptionEnum.MAKE_MENU_FAIL);
        }

        return R.ok(list);
    }
}
