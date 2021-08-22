package com.sh303.circle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.convent.DiscussPostConvert;
import com.sh303.circle.entity.DiscussPost;
import com.sh303.circle.filter.SensitiveFilter;
import com.sh303.circle.mapper.DiscussPostMapper;
import com.sh303.common.domain.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: cloud-community
 * @description: 个人帖子服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.apache.dubbo.config.annotation.Service
public class DiscussPostServiceImpl implements DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostServiceImpl.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     * 最大大小
     */
    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    /**
     * 到期时间秒
     */
    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 帖子列表缓存
     */
    private LoadingCache<String, List<DiscussPost>> postListCache;

    /**
     * 帖子总数缓存
     */
    private LoadingCache<Integer, Integer> postRowsCache;

    /**
     * create by: Chen Bei Jin
     * description: 初始化方法  Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     * create time: 2021/8/20 9:25
     */
    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        // caffeine核心接口: Cache, LoadingCache, AsyncLoadingCache
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public List<DiscussPost> load(String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误!");
                        }
                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误!");
                        }
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);
                        // 二级缓存: redis -> mysql,暂不做处理
                        logger.debug("init load DiscussPost list from DB.");

                        return discussPostMapper.selectDiscussPostPages(0, offset, limit, 1);
                    }
                });

        // 初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public Integer load(Integer key) throws Exception {

                        logger.debug("init load DiscussPost rows from DB.");
                        return findDiscussPostsRows(key + "");
                    }
                });
    }

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户帖子
     * create time: 2021/8/20 9:26
     * @param userId 用户ID
     * @param offset 页码
     * @param limit  每页显示
     */
    @Override
    public PageVO<DiscussPostDTO> findDiscussPosts(String userId, Integer offset, Integer limit, Integer orderMode) {
        // 当前页，总条数 构造 page 对象
        Page<DiscussPost> page = new Page<>(offset, (limit == null || limit < 1 ? 10 : limit));
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(page, userId, orderMode);
        List<DiscussPostDTO> discussPostDTOS = DiscussPostConvert.INSTANCE.entityList2dtoList(discussPosts);
        // 打印日志
        logger.debug("find load DiscussPost list from DB.");
        return new PageVO<>(discussPostDTOS, page.getTotal(), offset, limit);
    }

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户帖子行数
     * create time: 2021/8/20 9:27
     * @param userId 用户ID
     */
    @Override
    public int findDiscussPostsRows(String userId) {
        Integer count = 0;
        String flag = "0";
        if (flag.equals(userId)) {
            count = discussPostMapper.selectCount(new LambdaQueryWrapper<DiscussPost>().notIn(DiscussPost::getStatus, "2"));
        } else {
            count = discussPostMapper.selectCount(new LambdaQueryWrapper<DiscussPost>().notIn(DiscussPost::getStatus, "2").eq(DiscussPost::getUserId, userId));
        }
        logger.debug("find load DiscussPost rows from DB.");

        return count;
    }

    /**
     * create by: Chen Bei Jin
     * description: 发布评论
     * create time: 2021/8/20 9:38
     * @param discussPostDTO
     */
    @Override
    public int addDiscussPost(DiscussPostDTO discussPostDTO) {
        if (discussPostDTO == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        discussPostDTO.setTitle(HtmlUtils.htmlEscape(discussPostDTO.getTitle()));
        discussPostDTO.setContent(HtmlUtils.htmlEscape(discussPostDTO.getContent()));
        // 过滤敏感词
        discussPostDTO.setTitle(sensitiveFilter.filter(discussPostDTO.getTitle()));
        discussPostDTO.setContent(sensitiveFilter.filter(discussPostDTO.getContent()));

        DiscussPost discussPost = DiscussPostConvert.INSTANCE.dto2entity(discussPostDTO);
        // 添加 评论
        logger.debug("insert DiscussPost from DB.");
        return discussPostMapper.insertDiscussPost(discussPost);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询帖子
     * create time: 2021/8/21 8:53
     * @param id
     */
    @Override
    public DiscussPostDTO findDiscussPostById(int id) {
        // 查询 评论
        logger.debug("from DiscussPost from DB where id");
        DiscussPost discussPost = discussPostMapper.selectById(id);
        DiscussPostDTO discussPostDTO = DiscussPostConvert.INSTANCE.entity2dto(discussPost);
        return discussPostDTO;
    }

    /**
     * create by: Chen Bei Jin
     * description: 修改评论数量
     * create time: 2021/8/21 14:51
     * @param id           帖子id
     * @param commentCount 评论数量
     */
    @Override
    public int updateCommentCount(int id, int commentCount) {
        DiscussPost entity = new DiscussPost();
        entity.setId(id);
        entity.setCommentCount(commentCount);
        return discussPostMapper.updateById(entity);
    }
}
