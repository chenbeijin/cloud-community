package com.sh303.community.controller;

import com.sh303.circle.api.CommentService;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.dto.CommentDTO;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.common.cache.Cache;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.RedisKeyUtil;
import com.sh303.common.util.StringUtil;
import com.sh303.community.common.util.HostHolder;
import com.sh303.community.entity.Event;
import com.sh303.community.event.EventProducer;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @program: cloud-community
 * @description: 评论控制器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Reference
    private CommentService commentService;

    @Reference
    private DiscussPostService discussPostService;

    /**
     * 线程缓存
     */
    @Autowired
    private HostHolder hostHolder;

    /**
     * kafka生产者
     */
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private Cache cache;

    /**
     * create by: Chen Bei Jin
     * description: 增加评论
     * create time: 2021/8/22 9:22
     * @param discussPostId
     * @param commentDTO
     */
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, CommentDTO commentDTO) {
        commentDTO.setUserId(hostHolder.getUser().getId());
        commentDTO.setStatus(0);
        commentDTO.setCreateTime(new Date());
        // 设置默认值
        if (commentDTO.getTargetId() == null){
            commentDTO.setTargetId(0);
        }
        // 添加帖子评论
        commentService.addComment(commentDTO);

        // 系统通知：触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(commentDTO.getEntityType())
                .setEntityId(commentDTO.getEntityId())
                .setData("postId", discussPostId);
        // 判断是否为帖子
        if (commentDTO.getEntityType() == ENTITY_TYPE_POST) {
            // 查询帖子
            DiscussPostDTO target = discussPostService.findDiscussPostById(commentDTO.getEntityId());
            // 设置帖子和评论的关系
            event.setEntityUserId(target.getUserId());
            // 判断是否为评论
        } else if (commentDTO.getEntityType() == ENTITY_TYPE_COMMENT) {
            // 查询评论
            CommentDTO target = commentService.findCommentById(commentDTO.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        // 生产者: 处理事件
        eventProducer.fireEvent(event);
        // 判断是否为帖子
        if (commentDTO.getEntityType() == ENTITY_TYPE_POST) {
            // 触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(commentDTO.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            // 生产者: 处理事件
            eventProducer.fireEvent(event);

            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            cache.addSet(redisKey, discussPostId);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }
}
