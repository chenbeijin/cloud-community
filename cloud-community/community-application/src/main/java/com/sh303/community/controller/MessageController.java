package com.sh303.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.sh303.circle.api.MessageService;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.MessageDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.common.domain.Page;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CommunityUtil;
import com.sh303.community.common.util.HostHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @program: cloud-community
 * @description: 私信控制器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Controller
public class MessageController implements CommunityConstant {

    @Reference
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Reference
    private UserService userService;

    /**
     * create by: Chen Bei Jin
     * description: 私信列表
     * create time: 2021/8/22 10:39
     */
    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
        UserDTO userDTO = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(userDTO.getId()));
        // 会话列表 进行分页查询 获取 私信列表
        List<MessageDTO> conversationList = messageService.findConversations(userDTO.getId(), page.getOffset(), page.getLimit());
        // 设置返回列表
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (MessageDTO messageDTO : conversationList) {
                Map<String, Object> map = new HashMap<>();
                // 私信内容
                map.put("conversation", messageDTO);
                // 查询未读和已读的私信数
                map.put("letterCount", messageService.findLetterCount(messageDTO.getConversationId()));
                // 查询未读的私信数
                map.put("unreadCount", messageService.findLetterUnreadCount(userDTO.getId(), messageDTO.getConversationId()));
                // 目标ID 判断当前用户是否是私信的发起者  目标就是 接收 私信的用户
                int targetId = userDTO.getId().equals(messageDTO.getFromId()) ? messageDTO.getToId() : messageDTO.getFromId();
                // 查询收的用户ID 目标ID
                map.put("target", userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        // 查询未读私信的数量
        int letterUnreadCount = messageService.findLetterUnreadCount(userDTO.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        // 查询未读的通知的数量
        int noticeUnreadCount = messageService.findNoticeUnreadCount(userDTO.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);
        return "/site/letter";
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取私信详细内容
     * create time: 2021/8/22 10:40
     */
    @RequestMapping(path = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        // 私信列表 查询某个会话所包含的私信列表
        List<MessageDTO> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (MessageDTO messageDTO : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", messageDTO);
                map.put("fromUser", userService.findUserById(messageDTO.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        // 私信目标 私信间的关系
        model.addAttribute("target", getLetterTarget(conversationId));
        // 设置已读 获取私信所有ID
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            // 修改消息的状态
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    /**
     * create by: Chen Bei Jin
     * description: TODO
     * create time: 2021/8/22 10:54
     */
    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content) {
        // 根据发送私信给用户的名称查询 改用户
        UserDTO target = userService.findUserByName(toName);
        // 判断是否为空
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }
        // 创建私信对象
        MessageDTO messageDTO = new MessageDTO();
        // 设置私信参数
        messageDTO.setFromId(hostHolder.getUser().getId());
        messageDTO.setToId(target.getId());
        if (messageDTO.getFromId() < messageDTO.getToId()) {
            messageDTO.setConversationId(messageDTO.getFromId() + "_" + messageDTO.getToId());
        } else {
            messageDTO.setConversationId(messageDTO.getToId() + "_" + messageDTO.getFromId());
        }
        messageDTO.setContent(content);
        messageDTO.setCreateTime(new Date());
        messageDTO.setStatus(0);
        // 添加私信
        messageService.addMessage(messageDTO);

        return CommunityUtil.getJSONString(0);
    }

    /**
     * create by: Chen Bei Jin
     * description: TODO
     * create time: 2021/8/22 10:55
     */
    /*@RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        UserDTO user = hostHolder.getUser();

        // 查询评论类通知
        MessageDTO messageDTO = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        if (messageDTO != null) {
            Map<String, Object> messageVO = new HashMap<>();
            messageVO.put("message", messageDTO);

            String content = HtmlUtils.htmlUnescape(messageDTO.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("unread", unread);
            model.addAttribute("commentNotice", messageVO);
        }

        // 查询点赞类通知
        messageDTO = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if (messageDTO != null) {
            Map<String, Object> messageVO = new HashMap<>();
            messageVO.put("message", messageDTO);

            String content = HtmlUtils.htmlUnescape(messageDTO.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            messageVO.put("unread", unread);
            model.addAttribute("likeNotice", messageVO);
        }

        // 查询关注类通知
        messageDTO = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        if (messageDTO != null) {
            Map<String, Object> messageVO = new HashMap<>();
            messageVO.put("message", messageDTO);

            String content = HtmlUtils.htmlUnescape(messageDTO.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("unread", unread);
            model.addAttribute("followNotice", messageVO);
        }

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }*/

    /**
     * create by: Chen Bei Jin
     * description: TODO
     * create time: 2021/8/22 10:57
     */
    /*@RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        UserDTO userDTO = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(userDTO.getId(), topic));

        List<MessageDTO> noticeList = messageService.findNotices(userDTO.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (MessageDTO notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 通知作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getLetterIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }
*/

    /**
     * create by: Chen Bei Jin
     * description: 私信间的关系
     * create time: 2021/8/22 10:44
     */
    private UserDTO getLetterTarget(String conversationId) {
        // 截取 用户给谁的私信
        String[] ids = conversationId.split("_");
        // 发用户的ID
        int id0 = Integer.parseInt(ids[0]);
        // 收用户的ID
        int id1 = Integer.parseInt(ids[1]);
        // 判断是否是当前用户发的私信
        if (hostHolder.getUser().getId() == id0) {
            // 查询收的用户ID
            return userService.findUserById(id1);
            // 否则
        } else {
            // 查询发的用户ID
            return userService.findUserById(id0);
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取私信所有ID
     * create time: 2021/8/22 10:42
     */
    private List<Integer> getLetterIds(List<MessageDTO> letterList) {
        // 创建所有id列表
        List<Integer> ids = new ArrayList<>();
        // 判断私信列表是否不为空
        if (letterList != null) {
            for (MessageDTO messageDTO : letterList) {
                // 判断私信ID和私信状态
                if (hostHolder.getUser().getId().equals(messageDTO.getToId()) && messageDTO.getStatus() == 0) {
                    // 添加id
                    ids.add(messageDTO.getId());
                }
            }
        }
        return ids;
    }

}
