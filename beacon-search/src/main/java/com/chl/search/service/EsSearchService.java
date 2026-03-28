package com.chl.search.service;

import java.io.IOException;
import java.util.Map;

public interface EsSearchService {
    /**
     * 向es中添加一条日志信息
     * @param index 索引名称
     * @param id 唯一标识
     * @param json 具体内容
     */
    void index(String index,String id,String json) throws IOException;

    /**
     *
     * @param index 索引
     * @param id 唯一标识
     * @return true：代表该条数据存在 false：该条消息不存在
     */
    boolean isExist(String index,String id)throws IOException;

    /**
     *
     * @param index 索引
     * @param id 唯一标识
     * @param map 需要修改的参数构建的map
     */
    void update(String index, String id, Map<String,Object> map)throws IOException;
}
