package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.*;
import zju.group1.forum.dto.Reply;

import java.util.List;

@Mapper
public interface ReplyMapper {
    @Select("select * from post_reply where postId = #{postId}")
    List<Reply> getReplyByPostID(int postId);

    @Select("select * from post_reply where id = #{floorId}")
    Reply getReplyByID(int floorId);

    @Select("select floorNumber from post_reply where id = #{floorId}")
    int getFloorNumberByID(int floorId);

    @Select("select postId from post_reply where id = #{floorId}")
    int getPostIdByID(int floorId);

    @Select("select author from post_reply where id = #{floorId}")
    String getAuthorIdByID(int floorId);

    @Insert("insert into post_reply (postId,author,content,replyId,floorNumber,replyTime,replyNumber) values (#{postId},#{author},#{content},#{replyId},#{floorNumber},now(),#{replyNumber})")
    void reply(Reply newReply);

    @Update("update post_reply set replyId=#{replyId},content=#{content} where id = #{id}")
    void modifyReply(Reply newReply);

    @Delete("delete from post_reply where id = #{floorId}")
    void deleteReply(int floorId);

    @Select("select * from post_reply " +
            "where (replyid in (select id from post_reply where author = #{name}) " +
            "or (postId in (select id from posting where author = #{name}) and replyId=0)) " +
            "and replyState=true")
    List<Reply> CheckReply(String name);

    @Select("select count(*) from post_reply " +
            "where (replyid in (select id from post_reply where author = #{name}) " +
            "or (postId in (select id from posting where author = #{name}) and replyId=0)) " +
            "and replyState=true")
    int getUnreadReplyNumber(String name);

    @Update("update post_reply set replyState=false where id = #{id}")
    void seenReply(int id);

    @Select("select IFNULL(max(floorNumber),0) from post_reply where postId = #{postId}")
    int getMaxFloorNumberByPostID(int postId);
}
