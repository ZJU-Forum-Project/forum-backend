package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.InfoMessage;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.OtherUserInfoMessage;
import zju.group1.forum.dto.UserInfo;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.UserInfoMapper;
import zju.group1.forum.mapper.UserMapper;
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

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisProvider redisProvider;

    @ApiOperation("判断是否管理员")
    @PostMapping(value = "/isAdmin")
    @AuthToken
    public Message isAdmin(@RequestParam("Authorization") String authorizaToken) {
        Message message = new Message();
        /*message的message属性值为1表示是管理员，0表示不是管理员*/

        String email = redisProvider.getAuthorizedName(authorizaToken);
        String admin = userMapper.isAdmin(email);

        message.setState(true);
        message.setMessage(admin);
        message.setAuthorizeToken(authorizaToken);

        return message;
    }

    @ApiOperation("查看他人个人主页")
    @PostMapping(value = "/info")
    @AuthToken
    public OtherUserInfoMessage userInfo(@RequestParam("username") String username){
        OtherUserInfoMessage otherUserInfoMessage = new OtherUserInfoMessage();
        UserInfo userInfo = userInfoMapper.getUserInfo(userMapper.getEmailByName(username));
        //逻辑判断是否隐藏
        if(userInfo.getBirth_hidden()==0){ otherUserInfoMessage.setBirth(userInfo.getBirth()); }
        else{ otherUserInfoMessage.setBirth(new Date(0,1,1)); }
        if(userInfo.getGender_hidden()==0){otherUserInfoMessage.setGender(userInfo.getGender());}
        else {otherUserInfoMessage.setGender("");}
        if(userInfo.getHometown_hidden()==0){otherUserInfoMessage.setHometown(userInfo.getHometown());}
        else {otherUserInfoMessage.setHometown("");}
        if(userInfo.getOrganization_hidden()==0){otherUserInfoMessage.setOrganization(userInfo.getOrganization());}
        else {otherUserInfoMessage.setOrganization("");}
        otherUserInfoMessage.setReputation(userInfo.getReputation());
        otherUserInfoMessage.setSignature(userInfo.getSignature());
        otherUserInfoMessage.setAvatarUrl(userMapper.getAvatarUrlByName(username));
        //返回
        return otherUserInfoMessage;
    }

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
        //设置头像
        infoMessage.setAvatarUrl(userMapper.getAvatarUrlByName(userMapper.searchName(email)));
        if (userInfoMapper.isUserInfoExist(email) == 0) {
            userInfoMapper.createUserInfo(email);
        }
        UserInfo userInfo = userInfoMapper.getUserInfo(email);

        infoMessage.setState(true);
        infoMessage.setMessage("查询成功！");
        infoMessage.setAuthorizeToken(authorizaToken);
        infoMessage.setInfo(userInfo);

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
