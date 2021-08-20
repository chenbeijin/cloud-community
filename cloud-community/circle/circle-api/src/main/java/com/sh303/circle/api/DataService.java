package com.sh303.circle.api;

import java.util.Date;

/**
 * @program: cloud-community
 * @description: 网站数据统计服务
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface DataService {

    /**
     * create by: Chen Bei Jin
     * description: 将指定的IP计入UV
     * create time: 2021/8/17 8:18
     * @param ip
     */
    void recordUV(String ip);

    /**
     * create by: Chen Bei Jin
     * description: 统计指定日期范围内的UV
     * create time: 2021/8/17 8:18
     * @param start
     * @param end
     */
    long calculateUV(Date start, Date end);

    /**
     * create by: Chen Bei Jin
     * description: 将指定用户计入DAU
     * create time: 2021/8/17 8:18
     * @param userId
     */
    void recordDAU(int userId);

    /**
     * create by: Chen Bei Jin
     * description: 统计指定日期范围内的DAU
     * create time: 2021/8/17 8:17
     * @param start
     * @param end
     */
    long calculateDAU(Date start, Date end);
}
