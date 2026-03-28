package com.chl.test.mapper;

import com.chl.test.pojo.Channel;
import com.chl.test.pojo.ClientChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClientChannelMapper {

    /** 根据查询全部channel信息 */
    @Select("select * from client_channel where is_delete = 0")
    List<ClientChannel> findAll();

}
