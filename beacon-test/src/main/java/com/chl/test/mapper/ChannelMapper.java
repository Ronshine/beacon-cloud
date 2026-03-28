package com.chl.test.mapper;

import com.chl.test.pojo.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChannelMapper {

    /** 根据查询全部channel信息 */
    @Select("select * from channel where is_delete = 0")
    List<Channel> findAll();

}
