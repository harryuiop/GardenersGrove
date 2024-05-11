package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ResetPasswordToken;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Methods to alter the ResetPasswordToken entity.
 * Tokens are hashed to prevent users data being changed in a database breach.
 */
@Service
public class ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    /**
     * @param userId Users id associated with token.
     * @return String value of token (hashed), or null if token is not found by user id.
     */
    public String getTokenByUserId(long userId) {
        ResetPasswordToken tokenEntity = resetPasswordTokenRepository.getResetPasswordTokenByUserId(userId);
        if (tokenEntity == null) return null;
        return tokenEntity.getToken();
    }

    /**
     * Hash and save token.
     *
     * @param token The plain text token as UUID.
     * @param userId User id associated with token.
     */
    public void addToken(String token, long userId) {
        String hashedToken = hashToken(token);
        resetPasswordTokenRepository.save(new ResetPasswordToken(hashedToken, userId));
    }

    private String hashToken(String resetPasswordTokenUUID) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
        return encoder.encode(resetPasswordTokenUUID);
    }


}