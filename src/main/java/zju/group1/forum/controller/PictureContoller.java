package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zju.group1.forum.dto.*;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.ReplyMapper;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 帖子的数据库名称为post_picture
 create table post_picture
(
	postId mediumint not null,
	floorId mediumint not null,
	url varchar(100) not null
);
 * */

@Api(tags = "操作图片和头像")
@RestController
public class PictureContoller {
    @ApiOperation("上传楼层图片")
    @PostMapping(value = "/uploadPicture")
    @AuthToken
    public Message uploadPictures(@RequestParam("postId") int postId, @RequestParam("floorNumber") int floorNumber, @RequestParam(value = "file") MultipartFile file) throws IOException {
        Message message = new Message();

        message.setState(true);
        return message;
    }

    @ApiOperation("查看楼层图片")
    @PostMapping(value = "/seePicture")
    @AuthToken
    public PictureMessage seePictures(@RequestParam("postId") int postId) throws IOException {
        PictureMessage message = new PictureMessage();

        return message;
    }

    @ApiOperation("删除楼层图片")
    @PostMapping(value = "/deletePicture")
    @AuthToken
    public Message deletePictures(@RequestParam(value = "pictureId") int pictureId) throws IOException {
        Message message = new Message();

        message.setState(true);
        return message;
    }

    @ApiOperation("上传头像")
    @PostMapping(value = "/uploadAvatar")
    @AuthToken
    public Message uploadAvatars(@RequestParam("Authorization") String token, @RequestParam(value = "file") MultipartFile file) throws IOException {
        Message message = new Message();

        message.setState(true);
        return message;
    }

    @ApiOperation("URL获取图片")
    @PostMapping(value = "/getPicture")
    public BufferedImage getImage(@RequestParam("url") String url) throws IOException {

        // URL应该进行更改
        return ImageIO.read(new FileInputStream(new File(url)));
    }
}
