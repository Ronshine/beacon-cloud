package com.chl.test.mapper;

import com.chl.test.pojo.ClientBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientBusinessMapper {

    /** 根据id查询ClientBusiness信息 */
    @Select("select * from client_business where id = #{id}")
    ClientBusiness findById(@Param("id")Long id);

}
