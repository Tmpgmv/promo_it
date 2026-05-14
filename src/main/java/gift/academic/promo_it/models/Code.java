package gift.academic.promo_it.models;

import gift.academic.promo_it.constants.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import java.time.Duration;
import java.time.OffsetDateTime;

@Table("code")
public class Code {

    @Id
    private Long id;

    @Column("expires_at")
    private OffsetDateTime expiresAt;

    private String code;
    private Long operationId;
    private Long userId;
    private String status;

    public Code() {}

    public Code(String code,
                Long operationId,
                Long userId,
                Duration lifespan) {
        this.code = code;
        this.operationId = operationId;
        this.userId = userId;
        setExpiresAt(lifespan);
        this.status = Status.ACTIVE.getValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return Status.ACTIVE.getValue().equals(status);
    }

    public boolean isUsed() {
        return Status.USED.getValue().equals(status);
    }

    public boolean isExpired() {
        return Status.EXPIRED.getValue().equals(status);
    }

    public void markAsUsed() {
        this.status = Status.USED.getValue();
    }

    public void markAsExpired() {
        this.status = Status.EXPIRED.getValue();
    }

    public boolean isValid() {
        return isActive() && expiresAt != null && expiresAt.isAfter(OffsetDateTime.now());
    }

    public void setExpiresAt(Duration lifespan) {
        this.expiresAt = OffsetDateTime.now().plus(lifespan);
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getCode() {
        return code;
    }

    public Long getOperationId() {
        return operationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", expiresAt=" + expiresAt +
                ", code='" + code + '\'' +
                ", operationId=" + operationId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}