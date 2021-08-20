package com.sh303.circle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.LoginTicketDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.convent.UserConvert;
import com.sh303.circle.entity.User;
import com.sh303.circle.mapper.UserMapper;
import com.sh303.common.cache.Cache;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CommunityUtil;
import com.sh303.common.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: cloud-community
 * @description: 用户服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Slf4j
@org.apache.dubbo.config.annotation.Service
public class UserServiceImpl implements UserService, CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Cache cache;

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户
     * create time: 2021/8/16 9:20
     * @param userId 用户ID
     */
    @Override
    public UserDTO findUserById(Integer userId) {
        if (userId == null || userId < 0) {

        }
        // 从缓存中获取用户
        User user = getCache(userId);
        // 用户为空
        if (user == null) {
            // 添加用户到缓存中
            user = initCache(userId);
        }
        // pojo 转 dto
        UserDTO userDTO = UserConvert.INSTANCE.entity2dto(user);
        return userDTO;
    }

    /**
     * create by: Chen Bei Jin
     * description: 用户注册
     * create time: 2021/8/16 9:19
     * @param userDTO 用户信息
     */
    @Override
    public List<Map<String, Object>> register(UserDTO userDTO) {
        // 传值
        List<Map<String, Object>> results = new ArrayList<>();
        // 返回信息Map
        Map<String, Object> messageMap = new HashMap<>();
        // 用户信息Map
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
        // 注册用户 设置参数
        userDTO.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        userDTO.setPassword(CommunityUtil.md5(userDTO.getPassword() + userDTO.getSalt()));
        userDTO.setType(0);
        userDTO.setStatus(0);
        userDTO.setActivationCode(CommunityUtil.generateUUID());
        userDTO.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDTO.setCreateTime(new Date());
        // dto 转 pojo
        User entity = UserConvert.INSTANCE.dto2entity(userDTO);
        // 新增用户
        int result = userMapper.insert(entity);
        // 判断是否新增成功
        if (result > 0) {
            // 查询用户
            entity = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDTO.getUsername()));
            // pojo 转 dto
            userDTO = UserConvert.INSTANCE.entity2dto(entity);
        }
        // 添加参数
        entityMap.put("userDTO", userDTO);
        results.add(messageMap);
        results.add(entityMap);
        return results;
    }

    /**
     * create by: Chen Bei Jin
     * description: 用户激活
     * create time: 2021/8/16 9:21
     * @param userId 用户ID
     * @param code   激活编码
     */
    @Override
    public int activation(int userId, String code) {
        // 查询用户
        User user = userMapper.selectById(userId);
        // 判断激活状态
        if (user.getStatus() == 1) {
            // 已经激活
            return ACTIVATION_REPEAT;
            // 判断激活码
        } else if (user.getActivationCode().equals(code)) {
            // 查询用户
            User entity = userMapper.selectById(userId);
            entity.setStatus(1);
            // 更新用户
            userMapper.updateById(entity);
            // 清除缓存
            clearCache(userId);
            // 成功
            return ACTIVATION_SUCCESS;
        } else {
            // 失败
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 用户登录
     * create time: 2021/8/16 16:20
     * @param username       用户账号
     * @param password       密码
     * @param expiredSeconds 过期时间
     */
    @Override
    public Map<String, Object> login(String username, String password, long expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        // 验证账号
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }
        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }
        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }
        // 生成登录凭证
        LoginTicketDTO loginTicket = new LoginTicketDTO();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        // 把数据 写入 redis
        cache.set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * create by: Chen Bei Jin
     * description: 退出登录
     * create time: 2021/8/16 17:05
     * @param ticket
     */
    @Override
    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicketDTO loginTicket = (LoginTicketDTO) cache.get(redisKey);
        loginTicket.setStatus(1);
        cache.set(redisKey, loginTicket);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询登录
     * create time: 2021/8/16 17:34
     * @param ticket 凭证
     */
    @Override
    public LoginTicketDTO findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicketDTO) cache.get(redisKey);
    }

    /**
     * create by: Chen Bei Jin
     * description: 更新头像
     * create time: 2021/8/19 10:34
     */
    @Override
    public int updateHeader(int userId, String headerUrl) {
        User entity = new User();
        entity.setHeaderUrl(headerUrl);
        // 根据用户ID修改头像
        int rows = userMapper.update(entity, new LambdaQueryWrapper<User>().eq(User::getId, userId));
        // 清除用户ID为userID的缓存
        clearCache(userId);
        return rows;
    }

    /**
     * create by: Chen Bei Jin
     * description: 1. 优先从缓存中取值
     * create time: 2021/8/16 9:21
     * @param userId 用户ID
     */
    private User getCache(int userId) {
        // 通过ID 获取缓存的 Key值
        String redisKey = RedisKeyUtil.getUserKey(userId);
        // 返回 用户对象
        return (User) cache.get(redisKey);
    }

    /**
     * create by: Chen Bei Jin
     * description: 2. 取不到值初始化缓存数据
     * create time: 2021/8/16 9:21
     * @param userId 用户ID
     */
    private User initCache(int userId) {
        // 查询用户
        User user = userMapper.selectById(userId);
        // 通过ID 获取缓存的 Key值
        String redisKey = RedisKeyUtil.getUserKey(userId);
        // 缓存设置参数
        cache.set(redisKey, user, 3600);
        return user;
    }

    /**
     * create by: Chen Bei Jin
     * description: 3. 数据变更时清除缓存数据
     * create time: 2021/8/16 9:20
     * @param userId 用户ID
     */
    private void clearCache(Integer userId) {
        // 通过ID 获取缓存的 Key值
        String redisKey = RedisKeyUtil.getUserKey(userId);
        // 删除缓存
        cache.del(redisKey);
    }



}
