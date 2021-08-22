package com.sh303.circle.service;

import com.sh303.circle.api.MessageService;
import com.sh303.circle.api.dto.MessageDTO;
import com.sh303.circle.convent.MessageConvert;
import com.sh303.circle.entity.Message;
import com.sh303.circle.filter.SensitiveFilter;
import com.sh303.circle.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 私信会话管理服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.apache.dubbo.config.annotation.Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * create by: Chen Bei Jin
     * description: 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * create time: 2021/8/17 10:28
     * @param userId
     * @param offset
     * @param limit
     */
    @Override
    public List<MessageDTO> findConversations(int userId, int offset, int limit) {
        // 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
        logger.debug("desc find message newest one message");
        List<Message> messageList = messageMapper.selectConversations(userId, offset, limit);
        List<MessageDTO> messageDTOList = MessageConvert.INSTANCE.entityList2dtoList(messageList);
        return messageDTOList;
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询当前用户的会话数量
     * create time: 2021/8/17 10:28
     * @param userId
     */
    @Override
    public int findConversationCount(int userId) {
        // 查询当前用户的会话数量
        logger.debug("find message count");
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某个会话所包含的私信列表
     * create time: 2021/8/17 10:28
     * @param conversationId
     * @param offset
     * @param limit
     */
    @Override
    public List<MessageDTO> findLetters(String conversationId, int offset, int limit) {
        // 查询某个会话所包含的私信列表
        logger.debug("find message Letters");
        List<Message> messageList = messageMapper.selectLetters(conversationId, offset, limit);
        List<MessageDTO> messageDTOList = MessageConvert.INSTANCE.entityList2dtoList(messageList);
        return messageDTOList;
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某个会话所包含的私信数量
     * create time: 2021/8/17 10:28
     * @param conversationId
     */
    @Override
    public int findLetterCount(String conversationId) {
        // 查询某个会话所包含的私信数量
        logger.debug("find message Letters count");
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询未读私信的数量
     * create time: 2021/8/17 10:28
     * @param userId
     * @param conversationId
     */
    @Override
    public int findLetterUnreadCount(int userId, String conversationId) {
        // 查询未读私信的数量
        logger.debug("find message Letters Unread Count");
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    /**
     * create by: Chen Bei Jin
     * description: 新增消息
     * create time: 2021/8/17 10:30
     * @param messageDTO
     */
    @Override
    public int addMessage(MessageDTO messageDTO) {
        messageDTO.setContent(HtmlUtils.htmlEscape(messageDTO.getContent()));
        messageDTO.setContent(sensitiveFilter.filter(messageDTO.getContent()));
        Message message = MessageConvert.INSTANCE.dto2entity(messageDTO);
        // 新增消息
        logger.debug("insert message");
        return messageMapper.insertMessage(message);
    }

    /**
     * create by: Chen Bei Jin
     * description: 修改消息的状态
     * create time: 2021/8/17 10:30
     * @param ids
     */
    @Override
    public int readMessage(List<Integer> ids) {
        // 修改消息的状态
        logger.debug("update message");
        return messageMapper.updateStatus(ids, 1);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题下最新的通知
     * create time: 2021/8/17 10:30
     * @param userId
     * @param topic
     */
    @Override
    public MessageDTO findLatestNotice(int userId, String topic) {
        // 查询某个主题下最新的通知
        logger.debug("desc find message newest one Latest Notice message");
        Message message = messageMapper.selectLatestNotice(userId, topic);
        MessageDTO messageDTO = MessageConvert.INSTANCE.entity2dto(message);
        return messageDTO;
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题所包含的通知数量
     * create time: 2021/8/17 10:30
     * @param userId
     * @param topic
     */
    @Override
    public int findNoticeCount(int userId, String topic) {
        // 查询某个主题所包含的通知数量
        logger.debug("find message Notice Count");
        return messageMapper.selectNoticeCount(userId, topic);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询未读的通知的数量
     * create time: 2021/8/17 10:31
     * @param userId
     * @param topic
     */
    @Override
    public int findNoticeUnreadCount(int userId, String topic) {
        // 查询未读的通知的数量
        logger.debug("find message Notice Unread Count");
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某个主题的所包含的通知列表
     * create time: 2021/8/17 10:31
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     */
    @Override
    public List<MessageDTO> findNotices(int userId, String topic, int offset, int limit) {
        // 查询某个主题的所包含的通知列表
        logger.debug("find message Notices");
        List<Message> messageList = messageMapper.selectNotices(userId, topic, offset, limit);
        List<MessageDTO> messageDTOList = MessageConvert.INSTANCE.entityList2dtoList(messageList);
        return messageDTOList;
    }
}
