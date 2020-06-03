package zju.group1.forum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import zju.group1.forum.dto.UserInfo;

@Mapper
public interface UserInfoMapper {

    @Insert("insert into user_info (email) values (#{email})")
    void createUserInfo(String email);

    @Select("select count(*) from user_info where email=#{email}")
    int isUserInfoExist(String email);

    @Select("select * from user_info where email=#{email}")
    UserInfo getUserInfo(String email);

    @Update("update user_info set real_name = #{real_name}, " +
            "real_name_hidden = #{real_name_hidden}, birth = #{birth}, birth_hidden = #{birth_hidden}, " +
            "phone = #{phone}, phone_hidden = #{phone_hidden}, gender = #{gender}, " +
            "gender_hidden = #{gender_hidden}, hometown = #{hometown}, hometown_hidden = #{hometown_hidden}, " +
            "organization = #{organization}, organization_hidden = #{organization_hidden}, " +
            "signature = #{signature} where email = #{email}")
    void updateUserInfo(UserInfo info);

    @Select("select reputation from user_info where email=#{email}")
    Integer getReputationByEmail(String email);
}
