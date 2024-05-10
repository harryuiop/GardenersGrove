package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ResetPasswordToken;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    public boolean checkTokenExists(String token) {
        return resetPasswordTokenRepository.existsByToken(token);
    }

    public void addToken(ResetPasswordToken resetPasswordToken) {
        resetPasswordTokenRepository.save(resetPasswordToken);
    }


}