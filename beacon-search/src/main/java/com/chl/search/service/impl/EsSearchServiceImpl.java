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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

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

    @Override
    public Map<String, Object> findSmsByParameters(Map<String, Object> parameters) throws IOException {
        //1、创建查询请求
        SearchRequest request = new SearchRequest(SearchUtil.getCurrentYearIndex());

        //2、封装查询条件
        //2.1 参数全部取出来
        Object fromObj = parameters.get("from");
        Object sizeObj = parameters.get("size");
        Object contentObj = parameters.get("content");
        Object mobileObj = parameters.get("mobile");
        Object startTimeObj = parameters.get("starttime");
        Object stopTimeObj = parameters.get("stoptime");
        Object clientIDObj = parameters.get("clientID");

        //2.2、对clientID进行操作
        List<Integer> clientIDList = null;
        if (clientIDObj instanceof List) {
            clientIDList = (List<Integer>) clientIDObj;  // 直接使用 Integer
        } else if (!ObjectUtils.isEmpty(clientIDObj)) {
            clientIDList = Collections.singletonList(Integer.parseInt(clientIDObj.toString()));
        }

        //2.3 条件封装
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // ========================封装查询条件到boolQuery========================================
        //2.3.1、关键字
        if (!ObjectUtils.isEmpty(contentObj)) {
            boolQuery.must(QueryBuilders.matchQuery("text", contentObj));
            // 高亮。设置给sourceBuilder
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("text");
            highlightBuilder.preTags("<span style='color: red'>");
            highlightBuilder.postTags("</span>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        //2.3.2、手机号
        if (!ObjectUtils.isEmpty(mobileObj)) {
            boolQuery.must(QueryBuilders.prefixQuery("mobile", (String) mobileObj));
        }

        //2.3.6 分页查询
        sourceBuilder.from(Integer.parseInt(fromObj + ""));
        sourceBuilder.size(Integer.parseInt(sizeObj + ""));

        //2.3.3、开始时间
        if(!ObjectUtils.isEmpty(startTimeObj)){
            boolQuery.must(QueryBuilders.rangeQuery("sendTime").gte(startTimeObj));
        }

        //2.3.4、结束时间
        if(!ObjectUtils.isEmpty(stopTimeObj)){
            boolQuery.must(QueryBuilders.rangeQuery("sendTime").lte(stopTimeObj));
        }

        //2.3.5、客户id
        if(clientIDList != null){
            boolQuery.must(QueryBuilders.termsQuery("clientId",clientIDList));
        }


        // ========================封装查询条件到boolQuery========================================
        sourceBuilder.query(boolQuery);
        request.source(sourceBuilder);

        //3、执行查询
        SearchResponse resp = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        //4、封装数据
        long total = resp.getHits().getTotalHits().value;
        List<Map> rows = new ArrayList<>();
        for (SearchHit hit : resp.getHits().getHits()) {
            Map<String, Object> row = hit.getSourceAsMap();
            List sendTime = (List) row.get("sendTime");
            String sendTimeStr = listToDateString(sendTime);
            row.put("sendTimeStr",sendTimeStr);
            row.put("corpname",row.get("sign"));
            //高亮结果处理
            HighlightField highlightField = hit.getHighlightFields().get("text");
            if (highlightField != null){
                String textHighLight = highlightField.getFragments()[0].toString();
                row.put("text",textHighLight);
            }
            rows.add(row);
        }
        //5、返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    /**
     * 拼接时间表示格式
     */
    private String listToDateString(List sendTime) {
        String year = sendTime.get(0) + "";
        Integer monthInt = (Integer) sendTime.get(1);
        Integer dayInt = (Integer) sendTime.get(2);
        Integer hourInt = (Integer) sendTime.get(3);
        Integer minuteInt = (Integer) sendTime.get(4);
        Integer secondInt = (Integer) sendTime.get(5);

        String month = monthInt / 10 == 0 ? "0" + monthInt : monthInt + "";
        String day = dayInt / 10 == 0 ? "0" + dayInt : dayInt + "";
        String hour = hourInt / 10 == 0 ? "0" + hourInt : hourInt + "";
        String minute = minuteInt / 10 == 0 ? "0" + minuteInt : minuteInt + "";
        String second = secondInt / 10 == 0 ? "0" + secondInt : secondInt + "";
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
}
