package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zju.group1.forum.dto.*;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PictureMapper;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.ReplyMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
 * 帖子的数据库名称为post_picture
 create table post_picture
(
	postId mediumint not null,
	floorNumber mediumint not null,
	url varchar(100) not null
);
 * */

@Api(tags = "操作图片和头像")
@RestController
public class PictureContoller {
    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisProvider redisProvider;

    @Value("${spring.picDir}")
    private String picDir;

    @ApiOperation("上传楼层图片")
    @PostMapping(value = "/uploadPicture")
    @AuthToken
    public Message uploadPicture(@RequestParam("postId") int postId,
                                 @RequestParam("floorNumber") int floorNumber,
                                 @RequestParam(value = "file") MultipartFile file) throws IOException {
        Message message = new Message();
        if (file.isEmpty()) {
            message.setState(false);
            message.setMessage("文件为空");
            return message;
        }
        String filename = file.getOriginalFilename();
        if(filename==null){
            message.setState(false);
            message.setMessage("文件名为空");
            return message;
        }
        String suffixName = filename.substring(filename.lastIndexOf("."));

        String saveFilename = UUID.randomUUID().toString();
        String filepath = saveFilename + suffixName;
        File dest = new File(picDir + filepath);
        file.transferTo(dest);
        pictureMapper.insertPicture(postId, floorNumber, filepath);
        message.setState(true);
        return message;
    }

    @ApiOperation("查看帖子图片")
    @PostMapping(value = "/seePicture")
    @AuthToken
    public List<Picture> seePicture(@RequestParam("postId") int postId) throws IOException {
 //       List<List<String>> arrayList = new ArrayList<>();

//        int maxFloorN = pictureMapper.getMaxFloorNumberByPostID(postId);
//        for (int i = 0; i <= maxFloorN; i++) {
//            arrayList.add(new ArrayList<>());
//        }
//        for (Picture picture : pictureByPostID) {
//            arrayList.get(picture.getFloorId()).add(picture.getUrl());
//        }
        return pictureMapper.getPictureByPostID(postId);
    }

    @ApiOperation("删除楼层图片")
    @PostMapping(value = "/deletePicture")
    @AuthToken
    public Message deletePicture(@RequestParam(value = "pictureId") int pictureId) throws IOException {
        Message message = new Message();
        Picture picture = pictureMapper.getPictureByPictureID(pictureId);
        File dest = new File(picDir + picture.getUrl());
        if(dest.delete()){
            pictureMapper.deletePictureByPictureID(pictureId);
            message.setState(true);
        }else {
            message.setState(false);
        }
        return message;
    }

    @ApiOperation("上传头像")
    @PostMapping(value = "/uploadAvatar")
    @AuthToken
    public Message uploadAvatar(@RequestParam(value = "Authorization") String token,
                                @RequestParam(value = "file") MultipartFile file) throws IOException {
        Message message = new Message();
        if (file.isEmpty()) {
            message.setState(false);
            message.setMessage("文件为空");
            return message;
        }
        String filename = file.getOriginalFilename();
        if(filename==null){
            message.setState(false);
            message.setMessage("文件名为空");
            return message;
        }
        String suffixName = filename.substring(filename.lastIndexOf("."));

        String saveFilename = UUID.randomUUID().toString();
        String filepath = saveFilename + suffixName;
        File dest = new File(picDir + filepath);
        file.transferTo(dest);

        String email = redisProvider.getAuthorizedName(token);

        userMapper.updateAvatarUrl(email, filepath);
        message.setState(true);
        return message;
    }

}
