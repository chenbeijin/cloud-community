package com.sh303.circle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.convent.UserConvert;
import com.sh303.circle.entity.User;
import com.sh303.circle.mapper.UserMapper;
import com.sh303.common.cache.Cache;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CommunityUtil;
import com.sh303.common.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Cache cache;

    /**
     * 通过用户ID查询用户
     * @param userId 用户ID
     * @return
     */
    @Override
    public UserDTO findUserById(Integer userId) {
        if (userId == null || userId < 0) {

        }
        User user = getCache(userId);
        if (user == null) {
            user = initCache(userId);
        }
        UserDTO userDTO = UserConvert.INSTANCE.entity2dto(user);
        return userDTO;
    }

    /**
     * 用户注册
     * @param userDTO 用户信息
     * @return
     */
    @Override
    public List<Map<String, Object>> register(UserDTO userDTO) {
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> messageMap = new HashMap<>();
        Map<String, Object> entityMap = new HashMap<>();
        // 空值处理
        if (userDTO == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(userDTO.getUsername())) {
            messageMap.put("usernameMsg", "账号不能为空!");
            results.add(messageMap);
            results.add(entityMap);
            return results;
        }
        if (StringUtils.isBlank(userDTO.getPassword())) {
            messageMap.put("passwordMsg", "密码不能为空!");
            results.add(messageMap);
            results.add(entityMap);
            return results;
        }
        if (StringUtils.isBlank(userDTO.getEmail())) {
            messageMap.put("emailMsg", "邮箱不能为空!");
            results.add(messageMap);
            results.add(entityMap);
            return results;
        }
        // 验证账号
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDTO.getUsername()));
        if (user != null) {
            messageMap.put("usernameMsg", "该账号已存在!");
            results.add(messageMap);
            results.add(entityMap);
            return results;
        }
        // 验证邮箱
        user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, userDTO.getEmail()));
        if (user != null) {
            messageMap.put("emailMsg", "该邮箱已被注册!");
            results.add(messageMap);
            results.add(entityMap);
            return results;
        }
        // 注册用户
        userDTO.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        userDTO.setPassword(CommunityUtil.md5(userDTO.getPassword() + userDTO.getSalt()));
        userDTO.setType(0);
        userDTO.setStatus(0);
        userDTO.setActivationCode(CommunityUtil.generateUUID());
        userDTO.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDTO.setCreateTime(new Date());
        User entity = UserConvert.INSTANCE.dto2entity(userDTO);
        int result = userMapper.insert(entity);
        if (result > 0) {
            entity = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDTO.getUsername()));
            userDTO = UserConvert.INSTANCE.entity2dto(entity);
        }
        entityMap.put("userDTO", userDTO);
        results.add(messageMap);
        results.add(entityMap);
        return results;
    }

    /**
     * 用户激活
     * @param userId 用户ID
     * @param code   激活编码
     * @return
     */
    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            User entity = userMapper.selectById(userId);
            entity.setStatus(1);
            userMapper.updateById(entity);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 1. 优先从缓存中取值
     * @param userId 用户ID
     * @return
     */
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) cache.get(redisKey);
    }

    /**
     * 2. 取不到值初始化缓存数据
     * @param userId 用户ID
     * @return
     */
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        cache.set(redisKey, user, 3600);
        return user;
    }

    /**
     * 3. 数据变更时清除缓存数据
     * @param userId 用户ID
     */
    private void clearCache(Integer userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        cache.del(redisKey);
    }
}
