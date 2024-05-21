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
     * @return ResetPasswordToken entity or null if entity is not found by user id.
     */
    public ResetPasswordToken getTokenByUserId(long userId) {

        return resetPasswordTokenRepository.getResetPasswordTokenByUserId(userId);
    }

    /**
     * Hash and save token.
     *
     * @param token The plain text token as UUID.
     * @param userId User id associated with token.
     */
    public void addToken(String token, long userId) {

        // remove previous token and add new one
        ResetPasswordToken tokenEntity = resetPasswordTokenRepository.getResetPasswordTokenByUserId(userId);
        if (tokenEntity != null) {
            deleteToken(tokenEntity);
        }
        String hashedToken = hashToken(token);
        resetPasswordTokenRepository.save(new ResetPasswordToken(hashedToken, userId));
    }

    /**
     * Delete reset password token entity
     *
     * @param token entity to delete
     */
    public void deleteToken(ResetPasswordToken token) {
        resetPasswordTokenRepository.delete(token);
    }

    /**
     * hash token before save to db
     *
     * @param resetPasswordTokenUUID token to use for link of reset password
     * @return encoded token
     */
    private String hashToken(String resetPasswordTokenUUID) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
        return encoder.encode(resetPasswordTokenUUID);
    }


}