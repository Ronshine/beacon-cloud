package com.chl.search.service.impl;

import com.chl.common.constant.RabbitMQConstants;
import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.SearchException;
import com.chl.common.model.StandardReport;
import com.chl.search.service.EsSearchService;
import com.chl.search.util.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class EsSearchServiceImpl implements EsSearchService {
    /**
     * 判断是否添加成功的标识
     */
    private static final String CREATED = "created";

    private static final String UPDATE = "update";

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void index(String index, String id, String json) throws IOException {
        //1、构建的request
        IndexRequest request = new IndexRequest();

        //2、封装信息进入request
        request.index(index);
        request.id(id);
        request.source(json, XContentType.JSON);

        //3、将request信息发送给ES服务
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        //4、根据返回信息判断是否添加成功如果不成功抛出异常
        String result = response.getResult().getLowercase();
        if (!result.equals(CREATED)){
            log.info("【搜索模块-写入数据失败】 向es中添加数据失败 index = {},id = {},json = {}，result = {}",index,id,json,result);
            throw new SearchException(ExceptionEnum.SEARCH_NOT_SEND);
        }

        log.info("【搜索模块-写入数据成功】 向es中添加数据成功 index = {},id = {},json = {}，result = {}",index,id,json,result);
    }

    @Override
    public boolean isExist(String index, String id) throws IOException {
        //构建getRequest和ES交互判断该数据是否存在
        GetRequest getRequest = new GetRequest();

        //封装查找条件
        getRequest.index(index);
        getRequest.id(id);

        // 基于restHighLevelClient将查询指定id的文档是否存在的请求投递过去
        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void update(String index, String id, Map<String, Object> map) throws IOException {
        //1、查看数据是否存在
        boolean isExist = isExist(index, id);

        //如果当前数据不存在
        if (!isExist){
            //将report对象存入当前线程
            StandardReport report = SearchUtil.get();
            if (report.isReSearch()){
                //证明不是第一次写日志操作 直接报错写入日志
                log.error("【搜索模块-修改日志】 修改日志失败，report = {}",report);
            }else {
                //证明是第一次进行写日志操作 将判断是否是第一次写日志改为true 并重新发送消息到normal队列
                report.setReSearch(true);
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_NORMAL_QUEUE,"",report);

            }
            //将存入的对象销毁防止内存泄漏
            SearchUtil.remove();
            return;
        }

        //到这说明上面的校验一键通过该数据是存在的对数据进行更新
        UpdateRequest updateRequest = new UpdateRequest();

        //封装信息
        updateRequest.index(index);
        updateRequest.id(id);
        updateRequest.doc(map);

        //获取响应
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

        //根据响应结果判断该更新是否成功
        String result = update.getResult().getLowercase();

        if (!UPDATE.equals(result)){
            //证明更新失败抛出异常
            log.error("【搜索模块-修改日志失败】 index = {},id = {},doc = {}",index,id,map);
            throw new SearchException(ExceptionEnum.SEARCH_UPDATE_ERROR);
        }
        log.info("【搜索模块-修改日志成功】 文档修改成功index = {},id = {},doc = {}",index,id,map);
    }
}
