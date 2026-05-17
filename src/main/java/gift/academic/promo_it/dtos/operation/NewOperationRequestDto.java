package gift.academic.promo_it.dtos.operation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record NewOperationRequestDto(
        @NotNull
        @NotBlank
        @Length(min=1, max=100)
        String operationName) {
}
