package com.sh303.circle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sh303.circle.api.CommentService;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.dto.CommentDTO;
import com.sh303.circle.convent.CommentConvert;
import com.sh303.circle.entity.Comment;
import com.sh303.circle.filter.SensitiveFilter;
import com.sh303.circle.mapper.CommentMapper;
import com.sh303.common.domain.PageVO;
import com.sh303.common.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 评论服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.apache.dubbo.config.annotation.Service
public class CommentServiceImpl implements CommentService, CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子的信息
     * create time: 2021/8/21 14:38
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     */
    @Override
    public PageVO<CommentDTO> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        // 当前页，总条数 构造 page 对象
        Page<CommentDTO> page = new Page<>(offset, (limit == null || limit < 1 ? 10 : limit));
        // 分页查询帖子评论
        List<Comment> comments = commentMapper.selectCommentsPageByEntity(page, entityType, entityId);
        List<CommentDTO> commentDTOS = CommentConvert.INSTANCE.entityList2dtoList(comments);
        // 打印日志
        logger.debug("find load comment list from DB.");
        return new PageVO<>(commentDTOS, page.getTotal(), offset, limit);
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子的回复数量
     * create time: 2021/8/21 14:36
     * @param entityType
     * @param entityId
     */
    @Override
    public int findCommentCount(int entityType, int entityId) {
        logger.debug("find comment count");
        return commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getEntityType, entityType).eq(Comment::getEntityId, entityId));
    }

    /**
     * create by: Chen Bei Jin
     * description: 添加帖子的回复信息
     * create time: 2021/8/21 14:39
     * @param commentDTO
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int addComment(CommentDTO commentDTO) {
        if (commentDTO == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        // 第一次 进行 评论 Html编码转译 设置 评论内容
        commentDTO.setContent(HtmlUtils.htmlEscape(commentDTO.getContent()));
        // 第二次 进行 评论敏感词过滤 设置 评论内容
        commentDTO.setContent(sensitiveFilter.filter(commentDTO.getContent()));

        // dto转pojo
        Comment comment = CommentConvert.INSTANCE.dto2entity(commentDTO);
        // 添加评论
        logger.debug("insert comment");
        int rows = commentMapper.insert(comment);

        // 更新帖子评论数量
        if (commentDTO.getEntityType() == ENTITY_TYPE_POST) {
            // 获取帖子评论数量
            logger.debug("find comment count");
            int count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getEntityType, commentDTO.getEntityType()).eq(Comment::getEntityId, commentDTO.getEntityId()));
            // 帖子评论数量更新
            logger.debug("update discussPost");
            discussPostService.updateCommentCount(commentDTO.getEntityId(), count);
        }
        return rows;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取触发的评论事件
     * create time: 2021/8/21 14:58
     * @param id
     */
    @Override
    public CommentDTO findCommentById(int id) {
        logger.debug("find comment where id");
        // 通过评论ID查询评论
        Comment comment = commentMapper.selectById(id);
        CommentDTO commentDTO = CommentConvert.INSTANCE.entity2dto(comment);
        return commentDTO;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子的回复数量
     * create time: 2021/8/21 15:02
     * @param id
     */
    @Override
    public int findCommentCountById(int id) {
        logger.debug("find comment count where id");
        return commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getId, id));
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取用户所有的回帖
     * create time: 2021/8/21 15:12
     * @param id
     * @param offset
     * @param limit
     */
    @Override
    public PageVO<CommentDTO> findCommentsByUserId(Integer id, Integer offset, Integer limit) {
        // 当前页，总条数 构造 page 对象
        Page<CommentDTO> page = new Page<>(offset, (limit == null || limit < 1 ? 10 : limit));
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, id));
        List<CommentDTO> commentDTOS = CommentConvert.INSTANCE.entityList2dtoList(comments);
        // 打印日志
        logger.debug("find load comment list from DB.");
        return new PageVO<>(commentDTOS, page.getTotal(), offset, limit);
    }
}
