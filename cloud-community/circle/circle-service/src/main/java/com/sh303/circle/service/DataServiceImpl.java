package com.sh303.circle.service;

import com.sh303.circle.api.DataService;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.common.cache.Cache;
import com.sh303.common.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: cloud-community
 * @description: 网站数据统计服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.apache.dubbo.config.annotation.Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private Cache cache;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    /**
     * create by: Chen Bei Jin
     * description: 将指定的IP计入UV
     * create time: 2021/8/17 8:18
     * @param ip
     */
    @Override
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        logger.debug("redis key ip add UV");
        cache.addUV(redisKey, ip);
    }

    /**
     * create by: Chen Bei Jin
     * description: 统计指定日期范围内的UV
     * create time: 2021/8/17 8:18
     * @param start
     * @param end
     */
    @Override
    public long calculateUV(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的Key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }

        // 合并这些数据
        String redisKey = RedisKeyUtil.getUVKey(df.format(start), df.format(end));
        cache.unionUV(redisKey, keyList);
        logger.debug("redis key union UV");

        // 返回统计的结果
        logger.debug("redis key size UV");
        return cache.sizeUV(redisKey);
    }

    /**
     * create by: Chen Bei Jin
     * description: 将指定用户计入DAU
     * create time: 2021/8/17 8:18
     * @param userId
     */
    @Override
    public void recordDAU(int userId) {
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        logger.debug("redis set Bit DAU");
        cache.setBit(redisKey, userId, true);
    }

    /**
     * create by: Chen Bei Jin
     * description: 统计指定日期范围内的DAU
     * create time: 2021/8/17 8:17
     * @param start
     * @param end
     */
    @Override
    public long calculateDAU(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的Key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        // 进行OR运算
        logger.debug("redis operation");
        return (long) cache.getRedisTemplate().execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(), keyList.toArray(new byte[0][0]));
                return connection.bitCount(redisKey.getBytes());
            }
        });
    }
}
