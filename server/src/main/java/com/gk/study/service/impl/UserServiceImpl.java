package com.gk.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.study.service.UserService;
import com.gk.study.entity.User;
import com.gk.study.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sun.mail.util.MailSSLSocketFactory;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    private Map<String, String> verificationCodes = new HashMap<>();

    @Override
    public List<User> getUserList(String keyword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotBlank(keyword)){
            // like查询
            queryWrapper.like("username", keyword);
        }
        queryWrapper.orderBy(true, false, "create_time");
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User getAdminUser(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", user.getPassword());
        queryWrapper.gt("role", "1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getNormalUser(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", user.getPassword());
        queryWrapper.ge("role", "1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void createUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void deleteUser(String id) {
        userMapper.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User getUserByToken(String token) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("token", token);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserDetail(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userId);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void sendResetPasswordEmail(String email){
        String code = generateVerificationCode();
        // 实现发送邮件的逻辑
        sendEmail(email, "您的验证码是: " + code);
        // 将验证码存储在内存中，通常推荐使用Redis或数据库
        verificationCodes.put(email, code);
    }

    @Override
    public boolean validateVerificationCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }


    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(999999 - 100000) + 100000);
    }


    public void sendEmail(String toEmail, String body) {
        try {
            // 1. 配置邮件属性
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qq.com");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");

            // 2. 设置SSL加密
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            // 3. 获取 Session 对象
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("3272376658@qq.com", "kleeoluycxrpcjed");
                }
            });
            session.setDebug(true); // 开启调试模式

            // 4. 通过 session 获取 transport 对象
            Transport ts = session.getTransport();

            // 5. 连接邮件服务器
            ts.connect("smtp.qq.com", "3272376658@qq.com", "kleeoluycxrpcjed");

            // 6. 创建邮件对象
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("3272376658@qq.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("家教系统验证码");
            message.setContent(body, "text/html;charset=UTF-8");

            // 7. 发送邮件
            ts.sendMessage(message, message.getAllRecipients());

            System.out.println("邮件发送成功！");

            // 8. 关闭连接
            ts.close();
        } catch (GeneralSecurityException e) {
            System.err.println("SSL加密配置出现问题：" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            System.err.println("邮件服务提供者错误：" + e.getMessage());
            e.printStackTrace();
        }  catch (MessagingException e) {
            System.err.println("邮件发送错误：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("发生未知错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
