# javascript-sdk

You can secure login of your JavaScript Web application with SAASPASS in few steps. Here you will see details about the required steps. 

Note: Your application login may require user to authenticate themselves with username/password + SAASPASS or just username + SAASPASS. So if your login flow has username-password authentication, you should do this authentication first and then start SAASPASS authentication that is explained below.

Note about JavaScript SDK sample project:

  - You will find usage of SAASPASS JavaScript SDK that is available with running implementation of each step as example.

  - It is maven project that can be executed standalone by running **SdkRunTest.java**. This is using Jetty server that is integrated with eclipse IDE. It uses port 4000 and backend side that represents your app server (**YourAppRest.java**) is using Java REST implementation.

  - The project has frontend pages under /resources/static folder. **index.html** represents your app login page that will ask user to authenticate with SAASPASS and **'success.html'** represents app landing page after user authenticated successfully.

  - Project has backend classes. **myorganization.appserver** package contains YourAppRest.java that will handle the submit from login page for SAASPASS authentication and 'saaspass' package contains SAASPASS SDK classes that are for handling SAASPASS authentication.

## Integration Steps

  - If you haven't created yet, go to your SAASPASS admin portal and create a custom application for this integration. Create user accounts under Groups & Users section and assign these accounts to your application. From the application management section, get your App Key and App Password which you will need during integration.
  - On your login page that will require users to authenticate with SAASPASS, inject SAASPASS login widget that is asking SAASPASS authentication. Use <iframe> to load widget which is here: 'https://www.saaspass.com/sd/widget/' This requires App Key of your custom application as parameter. This iframe will load the SAASPASS login widget and wait for user to authenticate himself with username and SAASPASS account.
     - On JavaScript SDK Project: In **index.html** , there is:
     ```
     <iframe src="https://www.saaspass.com/sd/widget/?appKey=LAJC2CYB5SZNCDPW"
     onload="saaspassWidgetLoaded(this)" id="widgetId"
     ></iframe>
     ```
   - If before asking SAASPASS authentication, you already know which user you want to authenticate (prior to this step, you might already validate username and password of user; so would know the username), you can post this username to SAASPASS widget, so that it will fill the username automatically in the input and user won't need to enter username again.
     - On JavaScript SDK Project: In **index.html** , there is:
     ```
     function saaspassWidgetLoaded(iframe) {
     var username = getUrlParam('username');
       if (username) {
         iframe.contentWindow.postMessage({username: username}, iframe.src);
       }
     }
     ```
   - Now user needs to authenticate with SAASPASS. After successful authentication, SAASPASS login widget will post back the response to the caller page of your application. This response is JWT (JSON Web Token) that contains info (claims) about authenticated user and it is signed by SAASPASS using your App Password (secret). So you need to handle this response on the caller page and post it to your application server to continue. (If you don't have a backend in your application, then you should do JWT validation and use the user claims in your web page to continue)
     - On JavaScript SDK Project: In **index.html** , there is:
     ```
     function doPostBack(response) {}
     ```
     - This function creates a hidden input, sets response from SAASPASS as value of the input and posts it to the backend server.
     - Server class that handles this will be **YourAppRest.java** and the request path for this is defined as **../api/apprest/consume**.
   - Now you have the JWT that returned by SAASPASS on your application server. Validate this JWT using your App Password and if it is valid, get user claims inside it.
   - Claim attributes are: **username** that keeps the username of authenticated user and **timestamp** that keeps the time that SAASPASS authenticated the user.
     - On JavaScript SDK Project: In **YourAppRest.java > consume()** , there is:
     ```
     SaaspassJwtClaims loggedInUserClaims = SaaspassJwtValidator.validateJwtAndReturnClaims(jwt, yourAppSecret);
     ```
     - SaaspassJwtValidator.java is a util class provided in sample project that takes JWT and App Password to validate JWT and returns user claims with SaaspassJwtClaims object.
   
   - Note: If in the beginning of the flow you passed username parameter to SAASPASS widget, then it is recommended you to validate if username you received from SAASPASS is the same with username you provided.
   - Use the claims as you need and complete the authentication.
     - On JavaScript SDK Project: In **YourAppRest.java > consume()** , there is:
     ```
     String successPage = "/success.html";
     String contextPath = request.getContextPath();
     String usernameParam = "?username="+loggedInUserClaims.getUsername();
     response.sendRedirect(contextPath + successPage+usernameParam);
     ```
     - This uses username of authenticated user and redirects user to 'success.html' in the project which welcomes user with his username.
