package gift.academic.promo_it.services;

import gift.academic.promo_it.constants.Role;
import gift.academic.promo_it.dtos.auth.RegisterRequestDto;
import gift.academic.promo_it.dtos.operation.NewOperationRequestDto;
import gift.academic.promo_it.exceptions.DuplicateException;
import gift.academic.promo_it.exceptions.LoginOccupiedException;
import gift.academic.promo_it.models.Operation;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.OperationRepository;
import gift.academic.promo_it.repositories.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation createOperation(NewOperationRequestDto operationRequestDto) {
        Operation operation;
        try {
            operation = operationRepository.create(operationRequestDto.operationName());
        } catch (DuplicateKeyException ex) {
            throw new DuplicateException("Duplicate operation name.");
        }
        return operation;
    }
}
