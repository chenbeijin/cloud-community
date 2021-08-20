package com.sh303.common.domain;

import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: 封装分页相关的信息
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public class Page implements Serializable {

    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 显示上限
     */
    private int limit = 10;

    /**
     * 数据总数(用于计算总页数)
     */
    private int rows;

    /**
     * 查询路径(用于复用分页链接)
     */
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 10) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取当前页的起始行
     * create time: 2021/8/16 14:04
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取总页数
     * create time: 2021/8/16 14:04
     */
    public int getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取起始页码
     * create time: 2021/8/16 14:04
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取结束页码
     * create time: 2021/8/16 14:05
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

}
