package Pack.mapper;

import Pack.vo.UserLogin;
import Pack.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
	 // 로그인
    UserVo getUserAccount(String id);

    // 회원가입
    int saveUser(UserVo userVo);
}
