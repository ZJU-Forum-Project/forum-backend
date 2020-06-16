package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import zju.group1.forum.dto.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user (email,name,password,avatarUrl) values (#{email},#{name},#{password},#{avatarUrl})")
    void createUser(User user);

    @Select("select password from user where email = #{email}")
    String verifyUser(String email);

    @Update("update user set password = #{password} where email = #{email} ")
    void updatePassword(String email, String password);

    @Select("select count(*) from user where email=#{email}")
    int isUserExist(User user);

    @Select("select count(*) from user where name=#{name}")
    int isNameExist(User user);

    @Select("select name from user where email = #{email}")
    String searchName(String email);

    @Select("select isadmin from user where email = #{email}")
    String isAdmin(String email);

    @Update("update user set isadmin = #{isAdmin} where email = #{email} ")
    void setAdmin(String email, String isAdmin);

    @Update("update user set avatarUrl = #{filepath} where email = #{email}")
    void updateAvatarUrl(String email, String filepath);

    @Select("select avatarUrl from user where name=#{name}")
    String getAvatarUrlByName(String name);

    @Select("select email from user where name=#{name}")
    String getEmailByName(String name);
}
