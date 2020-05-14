package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.User;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;
import zju.group1.forum.service.EncryptService;
import zju.group1.forum.service.MailService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "注册")
@RestController
public class RegisterController {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private RedisProvider redisProvider;

    @ApiOperation("用户注册")
    @PostMapping(value = "/register")
    public Message register(@Valid User user,
                            BindingResult bindingResult,
                            HttpServletRequest request) throws IOException {
        Message message = new Message();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (message.getMessage() == null)
                    message.setMessage(fieldError.getDefaultMessage());
                else
                    message.setMessage(message.getMessage() + ";" + fieldError.getDefaultMessage());
            }
            message.setState(false);
            return message;
        }

        HttpSession session = request.getSession();

        if (session.getAttribute(user.getEmail()) == null || !session.getAttribute(user.getEmail()).toString().equals(user.getToken())) {
            message.setState(false);
            message.setMessage("您输入的验证码有误或为空");
            return message;
        }

        if (userMapper.isUserExist(user) > 0) {
            message.setState(false);
            message.setMessage("用户已存在");
            return message;
        }

        userMapper.createUser(user);
        message.setState(true);
        String authorizeToken = encryptService.getMD5Code(user.getEmail());
        redisProvider.setAuthorizeToken(authorizeToken, user.getEmail());
        message.setMessage("用户创建成功");
        message.setAuthorizeToken(authorizeToken);
        return message;
    }

    @ApiOperation("发送邮件认证")
    @PostMapping(value = "/applyEmail")
    public Message register(@RequestParam("email") String email,
                            HttpServletRequest request) throws IOException {
        Message message = new Message();
        HttpSession session = request.getSession();
        String token = UUID.randomUUID().toString().substring(0,6);

        session.setAttribute(email, token);
        mailService.sendToken(email, token);
        System.out.println(session.getAttribute(email));
        message.setState(true);
        message.setMessage("邮件发送成功!");
        return message;
    }
}
