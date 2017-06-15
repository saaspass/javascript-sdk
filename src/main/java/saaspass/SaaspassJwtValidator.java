package saaspass;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * This is JWT validator util class that validates the JWT that returned by SAASPASS and returns user claims in {@link SaaspassJwtClaims} object.
 * @author SAASPASS dev
 *
 */
public class SaaspassJwtValidator {
	
	/**
	 * Validates given JWT by using your app secret and returns the user claims.
	 * @param jwt JWT that returned by SAASPASS
	 * @param yourAppSecret your app secret
	 * @return {@link SaaspassJwtClaims}
	 */
	public static SaaspassJwtClaims validateJwtAndReturnClaims(String jwt, final String yourAppSecret) {
		
		try {
			jwt = URLDecoder.decode(jwt, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		
		SaaspassJwtClaims saaspassJwtClaims = new SaaspassJwtClaims();
		try {
			//This line will throw an exception if JWT token is not a signed with your app secret 
			Claims claims = Jwts.parser()
					.setSigningKey(yourAppSecret)
					.parseClaimsJws(jwt).getBody();
			
			//OK, we can trust this JWT
			saaspassJwtClaims.setUsername(claims.get("username").toString());
			saaspassJwtClaims.setTimestamp(Long.parseLong(claims.get("timestamp").toString()));
		} catch (SignatureException e) {
		    //don't trust the JWT!
			System.err.println(e);
		}
		
		return saaspassJwtClaims;
	}
	
}