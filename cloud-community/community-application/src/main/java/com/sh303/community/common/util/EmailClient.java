package com.sh303.community.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @program: cloud-community
 * @description: 邮箱客户端工具类
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Component
public class EmailClient {

    private static final Logger logger = LoggerFactory.getLogger(EmailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 获取 nacos 配置中心
     * 配置中心 修改时 也会 动态获取 配置
     */
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    private String from;

    /**
     * create by: Chen Bei Jin
     * description: 发送邮件
     * create time: 2021/8/16 14:25
     * @param to
     * @param subject
     * @param content
     */
    public void sendMail(String to, String subject, String content) {
        from = configurableApplicationContext.getEnvironment().getProperty("spring.mail.username");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败:" + e.getMessage());
        }
    }

}
