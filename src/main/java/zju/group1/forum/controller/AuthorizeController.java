package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zju.group1.forum.dto.AccessToken;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.User;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Value;
import zju.group1.forum.provider.RedisProvider;
import zju.group1.forum.service.EncryptService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "登录")
@RestController
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private RedisProvider redisProvider;

    @Autowired
    private EncryptService encryptService;

    @Value("${github.client.id}")
    private String ClientId;

    @Value("${github.client.secret}")
    private String ClientSecret;

    @Value("${github.redirect.uri}")
    private String RedirectURI;

    @Autowired
    private UserMapper userMapper;

    @ApiOperation("Github登录")
    @PostMapping(value = "/githubLogin")
    public Message callback(@RequestParam("code") String code,
                            @RequestParam("state") String state) throws IOException {
        Message message = new Message();

        AccessToken accessTokenDTO = new AccessToken();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(ClientId);
        accessTokenDTO.setClient_secret(ClientSecret);
        accessTokenDTO.setRedirect_uri(RedirectURI);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        if (accessToken == null) {
            message.setState(false);
            message.setMessage("登陆失败");
        } else {
            User user = githubProvider.getUser(accessToken);
            if (user.getName() == null) {
                message.setState(false);
                message.setMessage("github上的用户名为空");
                return message;
            }
            if (user.getEmail() == null) {
                message.setState(false);
                message.setMessage("github上的邮箱为空");
                return message;
            }
            if (userMapper.isUserExist(user) == 0) {
                userMapper.createUser(user);
            }

            String authorizeToken = encryptService.getMD5Code(user.getEmail());
            redisProvider.setAuthorizeToken(authorizeToken, user.getEmail());
            message.setState(true);
            message.setMessage(user.getName() + ";" + user.getAvatarUrl());
            message.setAuthorizeToken(authorizeToken);
        }
        return message;
    }

    @ApiOperation("普通登录")
    @PostMapping(value = "/login")
    public Message email_login(@RequestParam("email") String email,
                               @RequestParam("password") String password) throws IOException {

        Message message = new Message();
        String pwd = userMapper.verifyUser(email);

        if (pwd == null || !pwd.equals(password)) {
            message.setState(false);
            message.setMessage("邮箱或密码错误");
        } else {
            message.setState(true);
            message.setMessage("登陆成功！");
            String authorizeToken = encryptService.getMD5Code(email);
            redisProvider.setAuthorizeToken(authorizeToken, email);
            message.setAuthorizeToken(authorizeToken);
        }

        return message;
    }
}
