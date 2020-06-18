package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.*;
import zju.group1.forum.dto.Posting;

import java.util.List;

@Mapper
public interface PostingsMapper {

    @Select("select * from posting where type = 1 order by time desc")
    List<Posting> listEmotion();

    @Select("select * from posting where type = 2 order by time desc")
    List<Posting> listInformation();

    @Select("select * from posting where type = 3 order by time desc")
    List<Posting> listIntern();

    @Select("select * from posting where type = 4 order by time desc")
    List<Posting> listStudy();

    @Select("select * from posting where author = #{name} order by time desc")
    List<Posting> listPersonalPostings(String name);

    @Select("select * from posting where id = #{postingID}")
    Posting getPostingByID(int postingID);

    @Select("select replyN from posting where id = #{postingID}")
    int searchReplyNById(int postingID);

    @Select("select title from posting where id = #{postingID}")
    String getPostingNameByID(int postingID);

    @Select("select LAST_INSERT_ID();")
    int getLastPostId();

    @Update("update posting set visitN = visitN + 1 where id = #{postingID}")
    void updateVisitNumber(int postingID);

    @Update("update posting set replyN = replyN + 1 where id = #{postingID}")
    void updateReplyNumber(int postingID);

    @Insert("insert into posting (author,title,type,content,time) values (#{author},#{title},#{type},#{content},now())")
    void Post(Posting newPosting);

    @Update("update posting set title=#{title},content=#{content} where id = #{id}")
    void modifyPosting(Posting newPosting);

    @Delete("delete from posting where id = #{postingID}")
    void deletePosting(int postingID);

    @Select("select author from posting where id = #{postingID}")
    String getAuthorById(int postingID);

    @Select("select * from posting order by time desc limit 10")
    List<Posting> listLastest();

    @Select("select * from posting where title like CONCAT('%',#{0},'%')")
    List<Posting> selectByTitle(String content);
}
