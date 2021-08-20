package com.sh303.circle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.convent.DiscussPostConvert;
import com.sh303.circle.entity.DiscussPost;
import com.sh303.circle.mapper.DiscussPostMapper;
import com.sh303.common.domain.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 个人帖子服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Slf4j
@org.apache.dubbo.config.annotation.Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     * 通过用户ID查询用户帖子
     * @param userId 用户ID
     * @param offset 页码
     * @param limit  每页显示
     * @return
     */
    @Override
    public PageVO<DiscussPostDTO> findDiscussPosts(String userId, Integer offset, Integer limit, Integer orderMode) {
        // 当前页，总条数 构造 page 对象
        Page<DiscussPost> page = new Page<>(offset, (limit == null || limit < 1 ? 10 : limit));
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(page, userId, orderMode);
        List<DiscussPostDTO> discussPostDTOS = DiscussPostConvert.INSTANCE.entityList2dtoList(discussPosts);
        return new PageVO<>(discussPostDTOS, page.getTotal(), offset, limit);
    }

    /**
     * 通过用户ID查询用户帖子行数
     * @param userId 用户ID
     * @return
     */
    @Override
    public int findDiscussPostsRows(String userId) {
        Integer count = 0;
        if ("0".equals(userId)) {
            count = discussPostMapper.selectCount(new LambdaQueryWrapper<DiscussPost>().notIn(DiscussPost::getStatus, "2"));
        } else {
            count = discussPostMapper.selectCount(new LambdaQueryWrapper<DiscussPost>().notIn(DiscussPost::getStatus, "2").eq(DiscussPost::getUserId, userId));
        }
        return count;
    }

}
