package com.chl.test.mapper;

import com.chl.test.pojo.MobileArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DirtyWordMapper {

    @Select("select dirtyword from mobile_dirtyword")
    List<String> findDirtyWord();
}
