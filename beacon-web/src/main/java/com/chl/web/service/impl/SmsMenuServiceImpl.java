package com.chl.web.service.impl;

import com.chl.web.mapper.SmsMenuMapper;
import com.chl.web.service.SmsMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SmsMenuServiceImpl implements SmsMenuService {
    @Resource
    private SmsMenuMapper smsMenuMapper;

    @Override
    public List<Map<String, Object>> findMenuByUserId(Integer userId) {
        //获取所有菜单对象
        List<Map<String, Object>> menuList = smsMenuMapper.findMenuByUserId(userId);

        //创建返回集合
        List<Map<String, Object>> list = new ArrayList<>();

        //对menuList进行迭代获取全部是父节点的队列
        ListIterator<Map<String, Object>> parentIterator = menuList.listIterator();
        while (parentIterator.hasNext()){
            Map<String, Object> menu = parentIterator.next();
            if (menu.get("type").equals(0)){
                list.add(menu);
                parentIterator.remove();
            }else {
                //不是父级菜单跳出循环
                break;
            }
        }

        //二级菜单
        for (Map<String, Object> parentMenu : list) {
            List<Map<String, Object>> sonList = new ArrayList<>();
            ListIterator<Map<String, Object>> sonIterator = menuList.listIterator();
            while (sonIterator.hasNext()) {
                Map<String, Object> sonMenu = sonIterator.next();
                if (Objects.equals((Long) sonMenu.get("parentId"), (Long) parentMenu.get("id"))){
                    sonList.add(sonMenu);
                    sonIterator.remove();
                }
            }
            parentMenu.put("list",sonList);
        }

        return list;
    }
}
