package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {
    boolean existsByToken(String token);
}