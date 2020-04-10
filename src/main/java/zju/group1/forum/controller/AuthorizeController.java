package zju.group1.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zju.group1.forum.dto.AccessToken;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.User;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String ClientId;

    @Value("${github.client.secret}")
    private String ClientSecret;

    @Value("${github.redirect.uri}")
    private String RedirectURI;

    @Autowired
    private UserMapper userMapper;

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
            if(user.getName() == null){
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
            message.setState(true);
            message.setMessage(user.getName() + ";" + user.getAvatarUrl());
        }
        return message;
    }

    @PostMapping(value = "/login")
    public Message email_login(@RequestParam("email") String email,
                               @RequestParam("password") String password) throws IOException {

        Message message = new Message();
        String pwd = userMapper.verifyUser(email);

        System.out.println(pwd);
        System.out.println(password);
        if (pwd == null || !pwd.equals(password)) {
            message.setState(false);
            message.setMessage("邮箱或密码错误");
        } else {
            message.setState(true);
            message.setMessage("登陆成功！");
        }

        return message;
    }
}
