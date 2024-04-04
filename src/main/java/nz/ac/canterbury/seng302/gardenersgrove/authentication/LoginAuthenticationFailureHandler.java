package nz.ac.canterbury.seng302.gardenersgrove.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * This class is used to do custom error handling for the authentication of the login page.
 */
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * This function is called when the login authentication fails. It redirects the user back to
     * the login page with the appropriate error message.
     * @param request the request during which the authentication attempt occurred.
     * @param response the response.
     * @param exception the exception which was thrown to reject the authentication
     * request.
     * @throws IOException An IO Exception
     * @throws BadCredentialsException The error as thrown by the customAuthenticationProvider
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, BadCredentialsException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.sendRedirect(request.getContextPath() + "/login?error=" + exception.getMessage());
    }
}
