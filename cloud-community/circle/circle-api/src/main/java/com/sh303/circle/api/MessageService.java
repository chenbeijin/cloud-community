package com.sh303.circle.api;

import com.sh303.circle.api.dto.MessageDTO;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 私信会话管理服务
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface MessageService {

    /**
     * create by: Chen Bei Jin
     * description: 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * create time: 2021/8/17 10:28
     * @param userId
     * @param offset
     * @param limit
     */
    List<MessageDTO> findConversations(int userId, int offset, int limit);

    /**
     * create by: Chen Bei Jin
     * description: 查询当前用户的会话数量
     * create time: 2021/8/17 10:28
     * @param userId
     */
    int findConversationCount(int userId);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个会话所包含的私信列表
     * create time: 2021/8/17 10:28
     * @param conversationId
     * @param offset
     * @param limit
     */
    List<MessageDTO> findLetters(String conversationId, int offset, int limit);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个会话所包含的私信数量
     * create time: 2021/8/17 10:28
     * @param conversationId
     */
    int findLetterCount(String conversationId);

    /**
     * create by: Chen Bei Jin
     * description: 查询未读私信的数量
     * create time: 2021/8/17 10:28
     * @param userId
     * @param conversationId
     */
    int findLetterUnreadCount(int userId, String conversationId);

    /**
     * create by: Chen Bei Jin
     * description: 新增消息
     * create time: 2021/8/17 10:30
     * @param messageDTO
     */
    int addMessage(MessageDTO messageDTO);

    /**
     * create by: Chen Bei Jin
     * description: 修改消息的状态
     * create time: 2021/8/17 10:30
     * @param ids
     */
    int readMessage(List<Integer> ids);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题下最新的通知
     * create time: 2021/8/17 10:30
     * @param userId
     * @param topic
     */
    MessageDTO findLatestNotice(int userId, String topic);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题所包含的通知数量
     * create time: 2021/8/17 10:30
     * @param userId
     * @param topic
     */
    int findNoticeCount(int userId, String topic);

    /**
     * create by: Chen Bei Jin
     * description: 查询未读的通知的数量
     * create time: 2021/8/17 10:31
     * @param userId
     * @param topic
     */
    int findNoticeUnreadCount(int userId, String topic);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题的所包含的通知列表
     * create time: 2021/8/17 10:31
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     */
    List<MessageDTO> findNotices(int userId, String topic, int offset, int limit);

}
