package com.sh303.community.controller;

import com.sh303.circle.api.CommentService;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.CommentDTO;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.common.cache.Cache;
import com.sh303.common.domain.Page;
import com.sh303.common.domain.PageVO;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CommunityUtil;
import com.sh303.common.util.RedisKeyUtil;
import com.sh303.community.common.util.HostHolder;
import com.sh303.community.entity.Event;
import com.sh303.community.event.EventProducer;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.stream.events.Comment;
import java.util.*;

/**
 * @program: cloud-community
 * @description: 评论控制器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Reference
    private DiscussPostService discussPostService;

    @Reference
    private UserService userService;

    @Reference
    private CommentService commentService;

    /**
     * 线程缓存
     */
    @Autowired
    private HostHolder hostHolder;

    /*@Autowired
    private LikeService likeService;*/

    /**
     * kafka生产者
     */
    @Autowired
    private EventProducer eventProducer;

    /**
     * redis缓存
     */
    @Autowired
    private Cache cache;

    /**
     * create by: Chen Bei Jin
     * description: 添加帖子
     * create time: 2021/8/20 10:11
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        // 获取线程中的用户对象
        UserDTO userDTO = hostHolder.getUser();
        // 判断用户是否登录
        if (userDTO == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }
        // 设置评论内容
        DiscussPostDTO discussPostDTO = new DiscussPostDTO();
        discussPostDTO.setUserId(userDTO.getId());
        discussPostDTO.setTitle(title);
        discussPostDTO.setContent(content);
        discussPostDTO.setCreateTime(new Date());
        // 初始化参数
        discussPostDTO.setType(0);
        discussPostDTO.setStatus(0);
        discussPostDTO.setCommentCount(0);
        discussPostDTO.setScore(0.0);
        // 发布评论 返回 ID
        int discussPostId = discussPostService.addDiscussPost(discussPostDTO);
        // 设置ID
        discussPostDTO.setId(discussPostId);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(userDTO.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPostDTO.getId());
        // kafka 处理事件 生产者
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        cache.addSet(redisKey, discussPostDTO.getId());

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子
     * create time: 2021/8/22 10:22
     */
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") Integer discussPostId, Model model, Page page) {
        if (discussPostId == null || "".equals(discussPostId) || discussPostId == 0) {
            return CommunityUtil.getJSONString(403, "帖子ID格式错误!");
        }
        // 帖子
        DiscussPostDTO discussPostDTO = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPostDTO);
        // 作者
        UserDTO userDTO = userService.findUserById(discussPostDTO.getUserId());
        model.addAttribute("user", userDTO);
        // 点赞数量
        /*long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);*/
        // 点赞状态
        /*int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);*/

        // 评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPostDTO.getCommentCount());

        // 评论列表
        PageVO<CommentDTO> commentDTOS = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPostDTO.getId(), page.getCurrent(), page.getLimit());
        List<CommentDTO> commentList = commentDTOS.getItems();
        // 评论Vo显示列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            // 遍历评论列表
            for (CommentDTO commentDTO : commentList) {
                // 评论Vo
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", commentDTO);
                // 作者
                commentVo.put("user", userService.findUserById(commentDTO.getUserId()));
                // 点赞数量
//                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, commentDTO.getId());
//                commentVo.put("likeCount", likeCount);
//                // 点赞状态
//                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
//                commentVo.put("likeStatus", likeStatus);

                PageVO<CommentDTO> entityList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, commentDTO.getId(), 0, Integer.MAX_VALUE);
                // 回复列表
                List<CommentDTO> replyList = entityList.getItems();
                // 回复Vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (CommentDTO reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        UserDTO target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 点赞数量
//                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
//                        replyVo.put("likeCount", likeCount);
//                        // 点赞状态
//                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
//                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, commentDTO.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }

    /**
     * 置顶.
     * @param id
     * @return
     *//*
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id) {
        discussPostService.updateType(id, 1);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0);
    }

    *//**
     * 加精.
     * @param id
     * @return
     *//*
    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return CommunityUtil.getJSONString(0);
    }

    *//**
     * 删除.
     * @param id
     * @return
     *//*
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);

        // 触发删帖事件
        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }*/

}
