package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BoardMapper {
    @Update("update board set introduction = #{intro} where boardid = #{boardId} ")
    void updateIntro(int boardId, String intro);

    @Select("select introduction from board where boardid = #{boardId}")
    String getIntro(int boardId);
}
