package nz.ac.canterbury.seng302.gardenersgrove.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, BadCredentialsException {
        // Set the response status to unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.sendRedirect(request.getContextPath() + "/login?error=" + exception.getMessage());
    }
}
