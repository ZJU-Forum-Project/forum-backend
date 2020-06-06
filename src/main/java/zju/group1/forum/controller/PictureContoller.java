package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.Picture;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PictureMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if (filename == null) {
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
    public Map<Integer, Picture> seePicture(@RequestParam("postId") int postId) {
        Map<Integer, Picture> pictureMap = new HashMap<>();
        List<Picture> pictures = pictureMapper.getPictureByPostID(postId);
        for (Picture picture : pictures) {
            pictureMap.put(picture.getFloorNumber(), picture);
        }
        return pictureMap;
    }

    @ApiOperation("删除楼层图片")
    @PostMapping(value = "/deletePicture")
    @AuthToken
    public Message deletePicture(@RequestParam(value = "pictureId") int pictureId) {
        Message message = new Message();
        Picture picture = pictureMapper.getPictureByPictureID(pictureId);
        File dest = new File(picDir + picture.getUrl());
        if (dest.delete()) {
            pictureMapper.deletePictureByPictureID(pictureId);
            message.setState(true);
        } else {
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
        if (filename == null) {
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
