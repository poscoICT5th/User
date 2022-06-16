package Pack.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResult {
	String token;
	String sessionID;

	public LoginResult(String sessionID, String token) {
		this.token = token;
		this.sessionID = sessionID;
	}
}
