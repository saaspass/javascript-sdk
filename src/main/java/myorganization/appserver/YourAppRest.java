package myorganization.appserver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import saaspass.SaaspassJwtClaims;
import saaspass.SaaspassJwtValidator;

/**
 * This represents your Application server (here Java REST implementation is used) that handles the result of user login that performed by SAASPASS.
 * It is using {@link SaaspassJwtValidator} to validate the JWT that SAASPASS produces and get the {@link SaaspassJwtClaims} about user.
 * @author SAASPASS dev
 *
 */
@Path("/apprest")
public class YourAppRest {

	/**
	 * This is the App Password (secret) that is given by SAASPASS for the custom application that you created.
	 * Replace the value with your App Password.
	 */
	final static String yourAppSecret = "7CFKBBO9TAY3BU5WWBOF1RHI5PXXW33P";
	
	/**
	 * Handles the request that is submitted within SAASPASS response form parameter.
	 * @param jwt JWT that SAASPASS returned.
	 * @param request request
	 * @param response response
	 * @return Response
	 * @throws IOException
	 */
	@POST
	@Path("consume")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response consume(
	        @FormParam("saaspass_response") String jwt, @Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
		
		/*
		 * Use SaaspassJwtValidator via passing JWT and your app secret to validate the JWT and get the user claims.
		 */
		SaaspassJwtClaims loggedInUserClaims = SaaspassJwtValidator.validateJwtAndReturnClaims(jwt, yourAppSecret);
		
		/*
		 * Now you have info about logged in user by SAASPASS.
		 */
		StringBuilder result = new StringBuilder();
	    result.append(String.format("Username: %s \n", loggedInUserClaims.getUsername()));
	    result.append(String.format("Timestamp: %s \n", loggedInUserClaims.getTimestamp()));
	    
	    System.out.println("logged in user info that received from SAASPASS::");
	    System.out.println(result.toString());

	    /*
	     * In this sample app, we just redirect user to success.html and display the username.
	     */
	    String successPage = "/success.html";
	    String contextPath = request.getContextPath();
	    String usernameParam = "?username="+loggedInUserClaims.getUsername();
	    response.sendRedirect(contextPath + successPage+usernameParam);
	    
	    return Response.status(200).entity(result.toString()).build();
	}
}