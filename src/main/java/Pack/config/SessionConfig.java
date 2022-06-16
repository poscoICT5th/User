package Pack.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Configuration;

import Pack.vo.SessionDTO;
import Pack.vo.UserLogin;

@Configuration
@WebListener
public class SessionConfig implements HttpSessionListener{
	
    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    //중복로그인 지우기
    public synchronized static String getSessionidCheck(String sessionId, String compareId){
	String deleteSessionID = "";
	System.out.println("이거 실행함");
	for (String key : sessions.keySet()) {
		System.out.println(key);
	}
	System.out.println("keyset 출력끝");
	for( String key : sessions.keySet() ){
		if (key.equals(sessionId)) {
			continue;
		}
		System.out.println(key);
		HttpSession hs = sessions.get(key);
		if(hs != null) {
			SessionDTO sessionDTO = (SessionDTO) hs.getAttribute(compareId);
			if (sessionDTO == null) {
				continue;
			}
			String beforeSessionID = sessionDTO.getSessionID();
			System.out.println(beforeSessionID);
			if(beforeSessionID != null && !beforeSessionID.equals(sessionId)) {
				deleteSessionID = key.toString();
			}
		}
	}
	removeSessionForDoubleLogin(deleteSessionID);
	return deleteSessionID;
    }
    
    private static void removeSessionForDoubleLogin(String deleteSessionID){    	
        System.out.println("remove sessionId : " + deleteSessionID);
        if(deleteSessionID != null && deleteSessionID.length() > 0){
            sessions.get(deleteSessionID).invalidate();
            sessions.remove(deleteSessionID);
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
    	System.out.println("세션만드 ㅇㅇ");
        System.out.println(hse.getSession().getId());
        sessions.put(hse.getSession().getId(), hse.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        if(sessions.get(hse.getSession().getId()) != null){
        	System.out.println("세션 파괴 ㅇㅇ" + hse.getSession().getId());
            sessions.get(hse.getSession().getId()).invalidate();
            sessions.remove(hse.getSession().getId());	
        }
    }

	public boolean isValidateSession(String sessionID, String id) {
		System.out.println("아이디는 : " + id + "sessionID : " + sessionID);
		for( String key : sessions.keySet() ){
			if (!sessionID.equals(key)) {
				continue;
			}
			HttpSession hs = sessions.get(key);
			if(hs != null) {
				SessionDTO sessionDTO = (SessionDTO) hs.getAttribute(id);
				if (sessionDTO == null) {
					continue;
				}
				System.out.println(sessionDTO);
				if (id.equals(sessionDTO.getUserVo().getId()) && sessionID.equals(sessionDTO.getSessionID())) {
					return true;
				}
			}
		}
		return false;
	}
}    