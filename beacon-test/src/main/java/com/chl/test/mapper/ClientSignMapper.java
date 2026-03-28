package com.chl.test.mapper;

import com.chl.test.pojo.ClientSign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClientSignMapper {

    @Select("select * from client_sign where client_id = #{client_id}")
    List<ClientSign> findByClientId(@Param(value = "client_id")Long clientId);

}
