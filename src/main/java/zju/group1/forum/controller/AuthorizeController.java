package zju.group1.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zju.group1.forum.dto.AccessToken;
import zju.group1.forum.dto.GithubUser;
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
    public GithubUser callback(@RequestParam("urlParam") String urlParam) throws IOException {
        String code = urlParam.split("&")[0].split("=")[1];
        String state = urlParam.split("&")[1].split("=")[1];

        AccessToken accessTokenDTO = new AccessToken();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(ClientId);
        accessTokenDTO.setClient_secret(ClientSecret);
        accessTokenDTO.setRedirect_uri(RedirectURI);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        String token = UUID.randomUUID().toString();
        githubUser.setToken(token);
        githubUser.setGmtCreate(String.valueOf(System.currentTimeMillis()));
        githubUser.setGmtModified(githubUser.getGmtCreate());
        System.out.println(githubUser);
        userMapper.createUser(githubUser);
        return githubUser;
    }
}
