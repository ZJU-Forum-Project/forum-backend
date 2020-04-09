package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import zju.group1.forum.dto.User;

@Mapper
public interface UserMapper {

    @Insert("insert into user (email,name,password) values (#{email},#{name},#{password})")
    void createUser(User user);

    @Select("select count(*) from user where name=#{name}")
    int isUserExist(User user);
}
