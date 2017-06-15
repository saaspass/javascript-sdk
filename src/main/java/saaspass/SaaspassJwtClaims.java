package saaspass;

/**
 * This keeps claims that returned for logged in user by SAASPASS.
 * @author SAASPASS dev
 *
 */
public class SaaspassJwtClaims {

	/**
	 * Username of logged in user.
	 */
	private String username;
	
	/**
	 * Time info that SAASPASS authenticated the user.
	 */
	private Long timestamp;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
