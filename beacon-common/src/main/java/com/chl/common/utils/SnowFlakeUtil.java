package com.chl.common.utils;

import com.chl.common.enums.ExceptionEnum;
import com.chl.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花算法生成id
 * 总共64为是一个正整数的有序唯一标识
 * 1-12位：为我们直接生成的序列号
 * 13-17位：服务id
 * 18-22位：机器id
 * 23-63位：41bits位的时间戳
 * 64位：0代表正整数
 */
@Component
@Slf4j
public class SnowFlakeUtil {
    /** 从2025/11/30开始时时间戳能一个雪花id可以用后面生成的时间戳去和这个相减即可 */
    private long timeStart = 1764432000000L;

    /** 机器id需要从nacos中读取到 */
//    @Value("${snowflake.machineId}")
    private long machineId = 0L;

    /** 服务id需要从nacos中读取到 */
//    @Value("${snowflake.serverId}")
    private long serverId = 0L;

    /** 序列号名称 */
    private long sequence;

    /** 序列化的位数 */
    private long sequenceBits = 12L;

    /** 机器码位数 */
    private long machineBits = 5L;

    /** 服务id位数 */
    private long serverBits = 5L;

    /** 最大的机器id位数 */
    private long maxMachineBits = -1 ^ (-1 << machineBits);

    /** 最大的服务id位数 */
    private long maxServerBits = -1 ^ (-1 << serverBits);

    /** 最大的序列id位数 */
    private long MaxSequenceBits = -1 ^ (-1 << sequenceBits);

    /**
     * 使用@PostConstruct在初始化完成调用init方法
     */
    @PostConstruct
    public void init(){
        if (machineId > maxMachineBits || serverId > maxServerBits){
            log.info("【雪花算法-初始化】机器ID或服务ID超过最大范围值！！ ");
            throw new ApiException(ExceptionEnum.SNOW_OUT_OF_RANGE);
        }
    }

    /**
     * 计算服务id的偏移位 12位
     */
    private long serverBitsShift = sequenceBits;

    /**
     * 机器id偏移位 12 + 5 位
     */
    private long machineBitsShift = sequenceBits + serverBits;

    /**
     * 时间戳偏移位 12 + 5 + 5 位
     */
    private long timeStampBitsShift = sequenceBits + serverBits + machineBits;

    /**
     * 记录最近一次获取id的时间
     */
    private long lastTimestamp = -1;

    /** 获取当前系统时间的时间戳 */
    private long TimeGet(){
        return System.currentTimeMillis();
    }

    /**
     * 实现雪花算法的实际方法
     * @return 返回64位的雪花算法id
     */
    public synchronized long nextId(){
        //1. 获取当前系统时间的时间戳
        long timeStamp = TimeGet();

        //2、判断是否出现时间回拨抛出异常
        if(timeStamp < lastTimestamp){
            // 说明出现了时间回拨
            log.info("【雪花算法-获取id】 当前服务出现时间回拨！！！");
            throw new ApiException(ExceptionEnum.SNOWFLAKE_TIME_BACK);
        }

        //3. 判断当前时间是否为上一时间的
        if (timeStamp == lastTimestamp){
            sequence = (sequence + 1) & MaxSequenceBits;
            //判断sequence是否为0 如果为0证明达到了最大序列号在这一毫秒内
            if (sequence == 0){
                timeStamp = TimeGet();
                while (timeStamp <= lastTimestamp){
                    //再次获得时间进行判断是否还是原来时间
                    timeStamp = TimeGet();
                }
            }
        }else {
            sequence = 0;
        }

        //4. 将这一时间戳赋值给最后时间戳为下次生成做判断
        lastTimestamp = timeStamp;

        //5. 计算id 并返回
        return ((timeStamp - timeStart) << timeStampBitsShift) |
                (machineId << machineBitsShift) |
                (serverId << serverBitsShift) |
                sequence & Long.MAX_VALUE;
    }
}
