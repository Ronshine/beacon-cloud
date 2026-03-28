package com.chl.test.mapper;

import com.chl.test.pojo.MobileArea;
import com.chl.test.pojo.MobileBlack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MobileBlackMapper {

    @Select("select black_number,client_id from mobile_black where is_delete = 0")
    List<MobileBlack> findAll();
}
