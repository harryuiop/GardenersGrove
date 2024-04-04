package nz.ac.canterbury.seng302.gardenersgrove.authentication;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Custom Authentication Provider class, to allow for handling authentication in any way we see fit.
 * In this case using our existing {@link User}
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * Autowired's UserService for custom authentication using our own user objects
     */
    private final UserService userService;

    public CustomAuthenticationProvider(UserService userService) {
        super();
        this.userService = userService;
    }

    /**
     * Custom authentication implementation
     *
     * @param authentication An implementation object that must have non-empty email (name) and password (credentials)
     * @return A new {@link UsernamePasswordAuthenticationToken} if email and password are valid with users authorities
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        // Extract email and password from the authentication object
        String email = String.valueOf(authentication.getName());
        String password = String.valueOf(authentication.getCredentials());

        // Check if either email or password is null or empty
        if (
                email == null || email.isEmpty() || password == null || password.isEmpty()
        ) {
            throw new BadCredentialsException("Bad_Credentials");
        }

        // Attempt to retrieve user from the database using email and password
        User user = userService.getUserByEmailAndPassword(email, password);

        // If user is not found, throw BadCredentialsException
        if (user == null) {
            throw new BadCredentialsException("Invalid");
        }

        // If user is found, create and return a UsernamePasswordAuthenticationToken
        return new UsernamePasswordAuthenticationToken(
                user.getUserId(), // User ID - NOTE: This is how you change the principle name for spring secuirty of user entitys
                null, // Null credentials as they're already authenticated
                user.getAuthorities() // User's authorities (roles)
        );
    }

    /**
     * Indicates whether this AuthenticationProvider supports the provided authentication token class.
     *
     * @param authentication The class of the authentication token to be checked for support.
     * @return true if this AuthenticationProvider can process the provided authentication token class, false otherwise.
     * <p>
     * This method checks if the provided authentication token class is equal to UsernamePasswordAuthenticationToken.class,
     * indicating whether this AuthenticationProvider supports username/password authentication.
     * If the provided authentication class matches, the method returns true, indicating support for processing such tokens.
     * If the provided authentication class does not match, the method returns false, indicating lack of support.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // Check if the provided authentication token class is equal to UsernamePasswordAuthenticationToken.class
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
