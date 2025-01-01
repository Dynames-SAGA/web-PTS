package com.gk.study.controller;

import com.gk.study.common.APIResponse;
import com.gk.study.common.ResponeCode;
import com.gk.study.entity.User;
import com.gk.study.permission.Access;
import com.gk.study.permission.AccessLevel;
import com.gk.study.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
/**
 * @author lengqin1024(微信)
 * @email net936@163.com
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    String salt = "abcd1234";

    @Autowired
    UserService userService;

    @Value("${File.uploadPath}")
    private String uploadPath;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse list(String keyword) {
        List<User> list = userService.getUserList(keyword);
        return new APIResponse(ResponeCode.SUCCESS, "Search Successfully", list);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public APIResponse detail(String userId) {
        User user = userService.getUserDetail(userId);
        return new APIResponse(ResponeCode.SUCCESS, "Search Successfully", user);
    }

    // 后台用户登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public APIResponse login(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword() + salt).getBytes()));
        User responseUser = userService.getNormalUser(user);
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(responseUser);
        if (responseUser != null) {
            return new APIResponse(ResponeCode.SUCCESS, "Search Successfully", responseUser);
        } else {
            return new APIResponse(ResponeCode.FAIL, "Incorrect username or password");
        }
    }

    // 普通用户登录
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public APIResponse userLogin(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword() + salt).getBytes()));
        User responseUser = userService.getNormalUser(user);
        if (responseUser != null) {
            return new APIResponse(ResponeCode.SUCCESS, "Search Successfully", responseUser);
        } else {
            return new APIResponse(ResponeCode.FAIL, "Incorrect username or password");
        }
    }

    // 普通用户注册
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    @Transactional
    public APIResponse userRegister(User user) throws IOException {

        if (!StringUtils.isEmpty(user.getUsername())
                && !StringUtils.isEmpty(user.getPassword())
                && !StringUtils.isEmpty(user.getRePassword())
                && !StringUtils.isEmpty(user.getEmail())
                && !StringUtils.isEmpty(user.getMobile())) {
            // 查重
            if (userService.getUserByUserName(user.getUsername()) != null) {
                return new APIResponse(ResponeCode.FAIL, "Duplicate user name");
            }
            // 验证密码
            if (!user.getPassword().equals(user.getRePassword())) {
                return new APIResponse(ResponeCode.FAIL, "Inconsistent passwords");
            }
            String md5Str = DigestUtils.md5DigestAsHex((user.getPassword() + salt).getBytes());
            // 设置密码
            user.setPassword(md5Str);
            md5Str = DigestUtils.md5DigestAsHex((user.getUsername() + salt).getBytes());
            // 设置token
            user.setToken(md5Str);

            String avatar = saveAvatar(user);
            if (!StringUtils.isEmpty(avatar)) {
                user.avatar = avatar;
            }
            // 设置角色
            user.setRole(String.valueOf(User.NormalUser));
            // 设置状态
            user.setStatus("0");
            user.setCreateTime(String.valueOf(System.currentTimeMillis()));

            userService.createUser(user);
            return new APIResponse(ResponeCode.SUCCESS, "Created Successfully");
        }
        return new APIResponse(ResponeCode.FAIL, "Creation Failure");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional
    public APIResponse create(User user) throws IOException {

        if (!StringUtils.isEmpty(user.getUsername()) || !StringUtils.isEmpty(user.getPassword())) {
            // 查重
            if (userService.getUserByUserName(user.getUsername()) != null) {
                return new APIResponse(ResponeCode.FAIL, "Duplicate user name");
            }
            String md5Str = DigestUtils.md5DigestAsHex((user.getPassword() + salt).getBytes());
            // 设置密码
            user.setPassword(md5Str);
            md5Str = DigestUtils.md5DigestAsHex((user.getUsername() + salt).getBytes());
            // 设置token
            user.setToken(md5Str);
            user.setCreateTime(String.valueOf(System.currentTimeMillis()));

            String avatar = saveAvatar(user);
            if (!StringUtils.isEmpty(avatar)) {
                user.avatar = avatar;
            }
            userService.createUser(user);
            return new APIResponse(ResponeCode.SUCCESS, "Created Successfully");
        }
        return new APIResponse(ResponeCode.FAIL, "Creation Failure");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public APIResponse delete(String ids) {
        System.out.println("ids===" + ids);
        // 批量删除
        String[] arr = ids.split(",");
        for (String id : arr) {
            userService.deleteUser(id);
        }
        return new APIResponse(ResponeCode.SUCCESS, "Deleted successfully");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public APIResponse update(User user) throws IOException {
        // update不能修改密码，故置空
        user.setPassword(null);
        String avatar = saveAvatar(user);
        if (!StringUtils.isEmpty(avatar)) {
            user.avatar = avatar;
        }
        userService.updateUser(user);
        System.out.println(user);
        return new APIResponse(ResponeCode.SUCCESS, "Update Successfully");
    }


    @Access(level = AccessLevel.LOGIN)
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @Transactional
    public APIResponse updateUserInfo(User user) throws IOException {
        User tmpUser = userService.getUserDetail(user.getId());
        if (tmpUser.getRole().equals(String.valueOf(User.NormalUser))) {
            // username和password不能改，故置空
            user.setUsername(null);
            user.setPassword(null);
            user.setRole(String.valueOf(User.NormalUser));
            String avatar = saveAvatar(user);
            if (!StringUtils.isEmpty(avatar)) {
                user.avatar = avatar;
            }
            userService.updateUser(user);
            return new APIResponse(ResponeCode.SUCCESS, "Update Successfully");
        } else {
            return new APIResponse(ResponeCode.FAIL, "Unauthorised operation");
        }
    }

    @Access(level = AccessLevel.LOGIN)
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @Transactional
    public APIResponse updatePwd(String userId, String password, String newPassword) throws IOException {
        User user = userService.getUserDetail(userId);
        if (user.getRole().equals(String.valueOf(User.NormalUser))) {
            String md5Pwd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if (user.getPassword().equals(md5Pwd)) {
                user.setPassword(DigestUtils.md5DigestAsHex((newPassword + salt).getBytes()));
                userService.updateUser(user);
            } else {
                return new APIResponse(ResponeCode.FAIL, "Incorrect original passwd");
            }
            return new APIResponse(ResponeCode.SUCCESS, "Update Successfully");
        } else {
            return new APIResponse(ResponeCode.FAIL, "Unauthorised operation");
        }

    }

    public String saveAvatar(User user) throws IOException {
        MultipartFile file = user.getAvatarFile();
        String newFileName = null;
        if (file != null && !file.isEmpty()) {

            // 存文件
            String oldFileName = file.getOriginalFilename();
            String randomStr = UUID.randomUUID().toString();
            newFileName = randomStr + oldFileName.substring(oldFileName.lastIndexOf("."));
            String filePath = uploadPath + File.separator + "avatar" + File.separator + newFileName;
            File destFile = new File(filePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            file.transferTo(destFile);
        }
        if (!StringUtils.isEmpty(newFileName)) {
            user.avatar = newFileName;
        }
        return newFileName;
    }

    @RequestMapping(value = "/sendResetPasswordEmail", method = RequestMethod.POST)
    @Transactional
    public APIResponse sendResetPasswordEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new APIResponse(ResponeCode.FAIL, "Email cannot be empty");
        }
        userService.sendResetPasswordEmail(email);
        return new APIResponse(ResponeCode.SUCCESS, "Verification code sent");
    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    @Transactional
    public APIResponse resetPwd(String username, String email, String code, String password, String rePassword) {
        System.out.print(username + " " +  email + " " +  code + " " +  password + " " +  rePassword);
        if (username == null || username.isEmpty()) {
            return new APIResponse(ResponeCode.FAIL, "Username cannot be empty");
        }

        if (email == null || email.isEmpty()) {
            return new APIResponse(ResponeCode.FAIL, "Mail cannot be empty");
        }

        if (code == null || code.isEmpty()) {
            return new APIResponse(ResponeCode.FAIL, "Verification code cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            return new APIResponse(ResponeCode.FAIL, "New passwd cannot be empty");
        }

        if (rePassword == null || rePassword.isEmpty()) {
            return new APIResponse(ResponeCode.FAIL, "Confirmed passwd cannot be empty");
        }

        if (!userService.validateVerificationCode(email, code)) {
            return new APIResponse(ResponeCode.FAIL, "Incorrect verification code");
        }

        User user = userService.getUserByEmail(email);
        if (user == null || !user.getUsername().equals(username)) {
            return new APIResponse(ResponeCode.FAIL, "User does not exist");
        }

        // 验证新密码和确认密码是否一致
        if (!password.equals(rePassword)) {
            return new APIResponse(ResponeCode.FAIL, "Confirmation of consistency");
        }

        // 这里可以加入新密码的复杂度验证
        String md5NewPwd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        user.setPassword(md5NewPwd);
        userService.updateUser(user);
        return new APIResponse(ResponeCode.SUCCESS, "Password reset successfully");
    }
}