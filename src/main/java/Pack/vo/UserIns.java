package Pack.vo;

public class UserIns {
	String id;
    String pw;
    String name;
    String phone;
    String email;
    String team;
    String auth;
    String menu_option;
    
	public UserIns(String id, String pw, String name, String phone, String email, String team, String auth,
			String menu_option) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.team = team;
		this.auth = auth;
		this.menu_option = menu_option;
	}
}
