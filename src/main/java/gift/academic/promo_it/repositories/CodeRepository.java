package gift.academic.promo_it.repositories;

import gift.academic.promo_it.models.Code;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository {

    // Validate OTP code for a specific user and operation
    @Query("SELECT * FROM code WHERE user_id = :userId AND operation_id = :operationId AND code = :code AND status = 'active' AND expires_at > :now")
    Optional<Code> validateCode(
            @Param("userId") Long userId,
            @Param("operationId") Long operationId,
            @Param("code") String code,
            @Param("now") OffsetDateTime now
    );

    // Check if a valid code exists
    @Query("SELECT EXISTS(SELECT 1 FROM code WHERE user_id = :userId AND operation_id = :operationId AND code = :code AND status = 'active' AND expires_at > :now)")
    boolean existsValidCode(
            @Param("userId") Long userId,
            @Param("operationId") Long operationId,
            @Param("code") String code,
            @Param("now") OffsetDateTime now
    );

    // Mark code as used
    @Modifying
    @Query("UPDATE code SET status = 'used' WHERE code = :code AND status = 'active'")
    int markCodeAsUsed(@Param("code") String code);


    // Mark expired codes
    @Modifying
    @Query("UPDATE code SET status = 'expired' WHERE expires_at < :now AND status = 'active'")
    int markExpiredCodes(@Param("now") OffsetDateTime now);


    // Delete all codes for a user
    @Modifying
    @Query("DELETE FROM code WHERE user_id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}