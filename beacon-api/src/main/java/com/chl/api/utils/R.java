package com.chl.api.utils;

import com.chl.api.vo.ResultVO;
import com.chl.common.exception.ApiException;

/**
 * 工具类判断是否发送成功
 */
public class R {
    public static ResultVO ok(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("操作成功");
        return resultVO;
    }

    public static ResultVO error(ApiException ex){
        ResultVO r = new ResultVO();
        r.setCode(ex.getCode());
        r.setMsg(ex.getMessage());
        return r;
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO r = new ResultVO();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
