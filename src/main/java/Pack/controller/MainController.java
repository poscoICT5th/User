package Pack.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pack.config.JwtTokenProvider;
import Pack.config.SessionConfig;
import Pack.service.UserService;
import Pack.vo.UserLogin;
import Pack.vo.UserVo;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class MainController {
	private final UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;    
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
	public String login(@RequestBody UserLogin userLogin) {
		System.out.println(userLogin);
		UserVo result = userService.loadUserByUsername(userLogin.getId());
		System.out.println(userLogin.getPw() + "::" + result.getPw());
		System.out.println(passwordEncoder.matches(userLogin.getPw(), result.getPw()));
    	if(!passwordEncoder.matches(userLogin.getPw(), result.getPw())) {
    		System.out.println("비밀번호가 일치하지 않습니다.");
    		throw new UsernameNotFoundException("User not authorized.");
    	}
    	
    	String userId = SessionConfig.getSessionidCheck(SESSION_KEY, auth.getUserId());
		
    	HttpSession session = request.getSession();
    	session.setAttribute(SESSION_KEY, auth);
    	session.setMaxInactiveInterval(SESSION_TIME);
    	
    	JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(null);
    	String jwtTokenValue = jwtTokenProvider.createToken(result.getUsername(), result);
    	System.out.println(jwtTokenValue);
		return jwtTokenValue;
	}
	
	@PostMapping("/signUp")
    public Boolean signUp(@RequestBody UserVo userVo) {
		System.out.println(userVo);
		int result = userService.joinUser(userVo);
        return result == 1 ? true : false;
    }
	
	@GetMapping("/failed")
	public String failed() {
		System.out.println("로그인 중복 들어옴??");
		return "failed";
	}
}
