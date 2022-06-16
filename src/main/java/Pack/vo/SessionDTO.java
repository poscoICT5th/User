package Pack.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SessionDTO {
	UserVo userVo;
	String token;
	String sessionID;

	public SessionDTO(UserVo userVo, String sessionID, String token) {
		this.userVo = userVo;
		this.token = token;
		this.sessionID = sessionID;
	}
}
