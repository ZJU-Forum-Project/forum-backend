package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import zju.group1.forum.dto.GithubUser;

@Mapper
public interface UserMapper {

    @Insert("insert into user (login,id,bio,avatarUrl,token,gmtCreate,gmtModified) values (#{login},#{id},#{bio},#{avatar_url},#{token},#{gmtCreate},#{gmtModified}) " +
            "on DUPLICATE KEY UPDATE gmtModified=#{gmtModified},token=#{token}")
    void createUser(GithubUser user);

}
