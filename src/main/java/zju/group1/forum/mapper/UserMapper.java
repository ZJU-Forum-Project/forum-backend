package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import zju.group1.forum.dto.User;

@Mapper
public interface UserMapper {

    @Insert("insert into user (email,name,password,avatarUrl) values (#{email},#{name},#{password},#{avatarUrl})")
    void createUser(User user);

    @Select("select password from user where email = #{email}")
    String verifyUser(String email);

    @Update("update Forum set password = #{password} where email = #{email} ")
    void updatePassword(String email, String password);

    @Select("select count(*) from user where name=#{name}")
    int isUserExist(User user);
}
