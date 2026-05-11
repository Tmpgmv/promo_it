package gift.academic.promo_it.validators;



import gift.academic.promo_it.constants.Media;
import gift.academic.promo_it.dtos.otp.OtpGenerateRequestDto;
import gift.academic.promo_it.exceptions.InvalidRequestException;
import gift.academic.promo_it.repositories.OperationRepository;
import org.springframework.stereotype.Service;

@Service
public class OtpRequestValidationService {

    private final OperationRepository operationRepository;

    public OtpRequestValidationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public void validateOtpGenerateRequest(OtpGenerateRequestDto request) {
        validateOperationId(request.operationId());
        validateMedia(request.media());
    }

    private void validateOperationId(Long operationId) {
        if (!operationRepository.findById(operationId).isPresent()) {
            throw new InvalidRequestException("Wrong operation id: " + operationId);
        }
    }

    private void validateMedia(String media) {
        Media.fromSlug(media);
    }
}