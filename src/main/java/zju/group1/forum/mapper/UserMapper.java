package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import zju.group1.forum.dto.User;

@Mapper
public interface UserMapper {

    @Insert("insert into user (email,name,account,password) values (#{email},#{name},#{account},#{password}")
    void createUser(User user);

}
