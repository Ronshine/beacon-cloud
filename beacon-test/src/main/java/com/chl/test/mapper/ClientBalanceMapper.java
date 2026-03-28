package com.chl.test.mapper;

import com.chl.test.pojo.ClientBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientBalanceMapper {

    @Select("select * from client_balance where client_id = #{client_id}")
    ClientBalance findByClientId(@Param(value = "client_id")Long clientId);

}
