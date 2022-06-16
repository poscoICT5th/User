package Pack.controller;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Pack.config.JwtTokenProvider;
import Pack.config.SessionConfig;
import Pack.service.UserService;
import Pack.vo.LoginResult;
import Pack.vo.SessionDTO;
import Pack.vo.UserLogin;
import Pack.vo.UserVo;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
	private final UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;    
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	SessionConfig sessionConfig;
	
	@PostMapping("/login")
	public LoginResult login(HttpSession session, @RequestBody UserLogin userLogin){// @RequestParam String id, @RequestParam String pw) {
		System.out.println(userLogin);
		UserVo result = userService.loadUserByUsername(userLogin.getId());
//		System.out.println(userLogin.getPw() + "::" + result.getPw());
//		System.out.println(passwordEncoder.matches(userLogin.getPw(), result.getPw()));
    	if(!passwordEncoder.matches(userLogin.getPw(), result.getPw())) {
    		System.out.println("비밀번호가 일치하지 않습니다.");
    		throw new UsernameNotFoundException("User not authorized.");
    	}
    	String jwtTokenValue = jwtTokenProvider.createToken(result.getId(), result);
    	System.out.println(jwtTokenValue);

    	String userId = SessionConfig.getSessionidCheck(session.getId(), result.getId());
        session.setMaxInactiveInterval(60*60 * 24);
        SessionDTO sessionDTO = new SessionDTO(result, session.getId(), jwtTokenValue);
        session.setAttribute(result.getId(), sessionDTO);
        System.out.println(session.getAttributeNames().toString());
        
    	return new LoginResult(sessionDTO.getSessionID(), jwtTokenValue);
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
	
	@RequestMapping("/successLogout")
	public String logout() {
		System.out.println("로그아웃함");
		return "logout";
	}
	
	@GetMapping("/sessionCheck")
	public Boolean sessionCheck(@RequestParam String sessionID, @RequestParam String token) {
		System.out.println(token);
		if (!jwtTokenProvider.validateToken(token)) {
			System.out.println("여기");
			return false; 
		};
		String id = jwtTokenProvider.getUserPk(token);
		if (!sessionConfig.isValidateSession(sessionID, id)) {
			System.out.println("여기 22");
			return false;
		};
		return true;
	}
}
