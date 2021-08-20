package com.sh303.circle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sh303.circle.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 信息 Mapper
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Repository
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * create by: Chen Bei Jin
     * description: 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * create time: 2021/8/17 10:28
     * @param userId
     * @param offset
     * @param limit
     */
    List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * create by: Chen Bei Jin
     * description: 查询当前用户的会话数量
     * create time: 2021/8/17 10:28
     * @param userId
     */
    int selectConversationCount(int userId);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个会话所包含的私信列表
     * create time: 2021/8/17 10:28
     * @param conversationId
     * @param offset
     * @param limit
     */
    List<Message> selectLetters(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个会话所包含的私信数量
     * create time: 2021/8/17 10:28
     * @param conversationId
     */
    int selectLetterCount(String conversationId);

    /**
     * create by: Chen Bei Jin
     * description: 查询未读私信的数量
     * create time: 2021/8/17 10:28
     * @param userId
     * @param conversationId
     */
    int selectLetterUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     * create by: Chen Bei Jin
     * description: 新增消息
     * create time: 2021/8/17 10:30
     * @param message
     */
    int insertMessage(Message message);

    /**
     * create by: Chen Bei Jin
     * description: 修改消息的状态
     * create time: 2021/8/17 10:30
     * @param ids
     * @param status
     */
    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题下最新的通知
     * create time: 2021/8/17 10:30
     * @param userId
     * @param topic
     */
    Message selectLatestNotice(@Param("userId") int userId, @Param("topic") String topic);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题所包含的通知数量
     * create time: 2021/8/17 10:30
     * @param userId
     * @param topic
     */
    int selectNoticeCount(@Param("userId") int userId, @Param("topic") String topic);

    /**
     * create by: Chen Bei Jin
     * description: 查询未读的通知的数量
     * create time: 2021/8/17 10:31
     * @param userId
     * @param topic
     */
    int selectNoticeUnreadCount(@Param("userId") int userId, @Param("topic") String topic);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题的所包含的通知列表
     * create time: 2021/8/17 10:31
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     */
    List<Message> selectNotices(@Param("userId") int userId, @Param("topic") String topic, @Param("offset") int offset, @Param("limit") int limit);

}


