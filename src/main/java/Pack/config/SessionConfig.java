package Pack.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Configuration;

import Pack.vo.UserLogin;

@WebListener
public class SessionConfig implements HttpSessionListener{
	
    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    //중복로그인 지우기
    public synchronized static String getSessionidCheck(String type, String compareId){
	String result = "";
	System.out.println("이거 실행함");
	for( String key : sessions.keySet() ){
		HttpSession hs = sessions.get(key);
		UserLogin auth = new UserLogin();
		if(hs != null) {
			auth = (UserLogin) hs.getAttribute(type);
			if(auth != null && auth.getId().toString().equals(compareId)) {
				result = key.toString();
			}
		}
	}
	removeSessionForDoubleLogin(result);
	return result;
    }
    
    private static void removeSessionForDoubleLogin(String userId){    	
        System.out.println("remove userId : " + userId);
        if(userId != null && userId.length() > 0){
            sessions.get(userId).invalidate();
            sessions.remove(userId);    		
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
    	System.out.println(123);
        System.out.println(hse);
        sessions.put(hse.getSession().getId(), hse.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
    	System.out.println("세션 파괴 ㅇㅇ");
        if(sessions.get(hse.getSession().getId()) != null){
            sessions.get(hse.getSession().getId()).invalidate();
            sessions.remove(hse.getSession().getId());	
        }
    }
}    