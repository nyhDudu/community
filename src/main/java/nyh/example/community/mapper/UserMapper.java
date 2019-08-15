package nyh.example.community.mapper;

import nyh.example.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
//    把User里面的name匹配到#{name}
//    因为Interface方法默认public 所以public可以去掉
//   public void insert(User user);
    void insert(User user);
}

