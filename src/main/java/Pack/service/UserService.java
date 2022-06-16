package Pack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Pack.config.JwtTokenProvider;
import Pack.mapper.UserMapper;
import Pack.vo.UserLogin;
import Pack.vo.UserVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	UserLogin userlogin;
	
	@Transactional
	public int joinUser(UserVo userVo) {
        userVo.setPw(passwordEncoder.encode(userVo.getPw()));
        System.out.println(userVo);
        return userMapper.saveUser(userVo);
	}
	
	@Override
    public UserVo loadUserByUsername(String id)throws UsernameNotFoundException {
		System.out.println(id);
    	System.out.println("로그인 서비스 들어옴");
        //여기서 받은 유저 패스워드와 비교하여 로그인 인증
        UserVo userVo = userMapper.getUserAccount(id);
        if (userVo == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        System.out.println(userVo);
        return userVo;
    }
}
