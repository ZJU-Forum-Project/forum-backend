package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.InfoMessage;
import zju.group1.forum.dto.UserInfo;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.UserInfoMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Api(tags = "个人信息")
@RestController
public class UserInfoController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisProvider redisProvider;

    @ApiOperation("查看个人信息")
    @PostMapping(value = "/queryinfo")
    @AuthToken
    public InfoMessage queryinfo(@RequestParam("Authorization") String authorizaToken) throws IOException {
        InfoMessage infoMessage = new InfoMessage();

        if (authorizaToken == null) {
            infoMessage.setState(false);
            infoMessage.setMessage("请重新登录");
            infoMessage.setAuthorizeToken(authorizaToken);
            return infoMessage;
        }

        String email = redisProvider.getAuthorizedName(authorizaToken);
        if (email == null) {
            infoMessage.setState(false);
            infoMessage.setMessage("无当前用户，请重新注册/登录");
            infoMessage.setAuthorizeToken(authorizaToken);
            return infoMessage;
        }

        if (userInfoMapper.isUserInfoExist(email) == 0) {
            userInfoMapper.createUserInfo(email);
        }
        UserInfo userInfo = userInfoMapper.getUserInfo(email);

        infoMessage.setState(true);
        infoMessage.setMessage("查询成功！");
        infoMessage.setAuthorizeToken(authorizaToken);
        infoMessage.setInfo(userInfo);
        /*
        infoMessage.setReal_name(userInfo.getReal_name());
        infoMessage.setReal_name_hidden(userInfo.getReal_name_hidden());
        infoMessage.setEmail(userInfo.getEmail());
        infoMessage.setEamil_hidden(userInfo.getEamil_hidden());
        infoMessage.setBirth(userInfo.getBirth());
        infoMessage.setBirth_hidden(userInfo.getBirth_hidden());
        infoMessage.setGender(userInfo.getGender());
        infoMessage.setGender_hidden(userInfo.getGender_hidden());
        infoMessage.setHometown(userInfo.getHometown());
        infoMessage.setHometown_hidden(userInfo.getHometown_hidden());
        infoMessage.setOrganization(userInfo.getOrganization());
        infoMessage.setOrganization_hidden(userInfo.getOrganization_hidden());
        infoMessage.setPhone(userInfo.getPhone());
        infoMessage.setPhone_hidden(userInfo.getPhone_hidden());
        infoMessage.setSignature(userInfo.getSignature());
         */
        return infoMessage;
    }



    @ApiOperation("修改个人信息")
    @PostMapping(value = "/editinfo")
    @AuthToken
    public InfoMessage editinfo(@RequestParam("Authorization") String authorizaToken,
                                @RequestParam("birth") String birth,
                                @RequestParam("birth_hidden") String birth_hidden,
                                @RequestParam("gender") String gender,
                                @RequestParam("gender_hidden") String gender_hidden,
                                @RequestParam("phone") String phone,
                                @RequestParam("phone_hidden") String phone_hidden,
                                @RequestParam("real_name") String real_name,
                                @RequestParam("real_name_hidden") String real_name_hidden,
                                @RequestParam("hometown") String hometown,
                                @RequestParam("hometown_hidden") String hometown_hidden,
                                @RequestParam("organization") String organization,
                                @RequestParam("organization_hidden") String organization_hidden,
                                @RequestParam("signature") String signature) throws IOException, ParseException  {
        InfoMessage infoMessage = new InfoMessage();

        if (authorizaToken == null) {
            infoMessage.setState(false);
            infoMessage.setMessage("请重新登录");
            infoMessage.setAuthorizeToken(authorizaToken);
            return infoMessage;
        }

        String email = redisProvider.getAuthorizedName(authorizaToken);
        if (email == null) {
            infoMessage.setState(false);
            infoMessage.setMessage("无当前用户，请重新注册/登录");
            infoMessage.setAuthorizeToken(authorizaToken);
            return infoMessage;
        }

        Date birth1;
        if (birth.equals("")) {
            birth1 = new java.sql.Date(0);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birth1 = new java.sql.Date(sdf.parse(birth).getTime());
        }

        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setEmail(email);
        newUserInfo.setEamil_hidden(1);
        newUserInfo.setBirth(birth1);
        newUserInfo.setBirth_hidden(Integer.parseInt(birth_hidden));
        newUserInfo.setGender(gender);
        newUserInfo.setGender_hidden(Integer.parseInt(gender_hidden));
        newUserInfo.setHometown(hometown);
        newUserInfo.setHometown_hidden(Integer.parseInt(hometown_hidden));
        newUserInfo.setOrganization(organization);
        newUserInfo.setOrganization_hidden(Integer.parseInt(organization_hidden));
        newUserInfo.setPhone(phone);
        newUserInfo.setPhone_hidden(Integer.parseInt(phone_hidden));
        newUserInfo.setReal_name(real_name);
        newUserInfo.setReal_name_hidden(Integer.parseInt(real_name_hidden));
        newUserInfo.setSignature(signature);

        userInfoMapper.updateUserInfo(newUserInfo);
        infoMessage.setState(true);
        infoMessage.setMessage("修改成功！");
        infoMessage.setAuthorizeToken(authorizaToken);
        infoMessage.setInfo(newUserInfo);

        return infoMessage;
    }
}
