package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.*;
import zju.group1.forum.dto.Picture;
import java.util.List;

@Mapper
public interface PictureMapper {
    @Select("select max(floorId) from post_picture where postId = #{postId}")
    int getMaxFloorNumberByPostID(int postId);

    @Select("select floorId,url from post_picture where postId = #{postId}")
    List<Picture> getPictureByPostID(int postId);

    @Insert("insert into post_picture (postId,floorId,url) values (#{postId},#{floorId},#{url})")
    void insertPicture(int postId, int floorId, String url);

}
