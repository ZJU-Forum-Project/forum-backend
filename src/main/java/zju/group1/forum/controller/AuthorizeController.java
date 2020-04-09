package zju.group1.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zju.group1.forum.dto.AccessToken;
import zju.group1.forum.dto.User;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Value;

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

    @GetMapping(value = "/toLogin")
    public User callback(@RequestParam("urlParam") String urlParam) throws IOException {
        String code = urlParam.split("&")[0].split("=")[1];
        String state = urlParam.split("&")[1].split("=")[1];

        AccessToken accessTokenDTO = new AccessToken();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(ClientId);
        accessTokenDTO.setClient_secret(ClientSecret);
        accessTokenDTO.setRedirect_uri(RedirectURI);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        User user = githubProvider.getUser(accessToken);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
//        user.setGmtCreate(String.valueOf(System.currentTimeMillis()));
//        user.setGmtModified(user.getGmtCreate());
        System.out.println(user);
        userMapper.createUser(user);
        return user;
    }
}
