package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import zju.group1.forum.dto.Picture;

import java.util.List;

@Mapper
public interface PictureMapper {
    @Select("select max(floorId) from post_picture where postId = #{postId}")
    int getMaxFloorNumberByPostID(int postId);

    @Select("select * from post_picture where postId = #{postId} ")
    List<Picture> getPictureByPostID(int postId);

    @Insert("insert into post_picture (postId,floorNumber,url) values (#{postId},#{floorNumber},#{url})")
    void insertPicture(int postId, int floorNumber, String url);

    @Select("select * from post_picture where pictureId = #{pictureId}")
    Picture getPictureByPictureID(int pictureId);

    @Delete("delete from post_picture where pictureId = #{pictureId}")
    void deletePictureByPictureID(int pictureId);
}
