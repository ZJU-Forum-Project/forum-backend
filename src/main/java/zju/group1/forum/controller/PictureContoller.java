package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.Picture;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PictureMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;
import zju.group1.forum.tool.ImageProcess;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Array;
import java.util.*;
import java.util.List;

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

    @Autowired
    private ImageProcess imageProcess;

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
    public List<Picture> seePicture(@RequestParam("postId") int postId) {
        int maxFloorNumberByPostID = pictureMapper.getMaxFloorNumberByPostID(postId);
        List<Picture> all_pictures = new ArrayList<>();
        List<Picture> pictures = pictureMapper.getPictureByPostID(postId);
        for (int i = 0; i <= maxFloorNumberByPostID; i++) {
            all_pictures.add(new Picture());
        }
        for (Picture picture : pictures) {
            all_pictures.get(picture.getFloorNumber()).setUrl(picture.getUrl());
            all_pictures.get(picture.getFloorNumber()).setFloorNumber(picture.getFloorNumber());
            all_pictures.get(picture.getFloorNumber()).setPostId(picture.getPostId());
            all_pictures.get(picture.getFloorNumber()).setPictureId(picture.getPictureId());
        }
        return all_pictures;
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


    @ApiOperation("获取图片")
    @GetMapping(value = "/getBase64PictureByUrl")
    public String getBase64PictureByUrl(@RequestParam(value = "url") String URL) throws Exception {
        File file = new File("/home/" + URL);
        BufferedImage image = ImageIO.read(file);
        image = imageProcess.ensureOpaque(image);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String format = URL.substring(URL.indexOf('.') + 1);
        ImageIO.write(image, format, stream);
        byte[] encode = Base64.getEncoder().encode(stream.toByteArray());

        if (format.equals("gif")) {
            return "data:image/gif;base64," + new String(encode);
        } else if (format.equals("png")) {
            return "data:image/png;base64," + new String(encode);
        } else {
            return "data:image/jpeg;base64," + new String(encode);
        }
    }


    @RequestMapping(value = "/getPictureByUrl", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPictureByUrl(@RequestParam(value = "url") String URL) throws IOException {
        File file = new File("/home/" + URL);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
}
