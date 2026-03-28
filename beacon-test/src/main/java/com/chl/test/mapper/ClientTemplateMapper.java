package com.chl.test.mapper;

import com.chl.test.pojo.ClientTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClientTemplateMapper {

    @Select("select * from client_template where sign_id = #{sign_id}")
    List<ClientTemplate> findBySignId(@Param(value = "sign_id")Long signId);

}
