package com.chl.web.controller;

import com.chl.common.constant.WebConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.utils.R;
import com.chl.common.vo.ResultVO;
import com.chl.web.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {
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
}
