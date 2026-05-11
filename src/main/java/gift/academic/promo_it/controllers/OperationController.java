package gift.academic.promo_it.controllers;

import gift.academic.promo_it.dtos.OtpConfigResponseDto;
import gift.academic.promo_it.models.Operation;
import gift.academic.promo_it.repositories.OperationRepository;
import gift.academic.promo_it.services.OtpConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operation")
public class OperationController {

    private final OperationRepository operationRepository;

    public OperationController(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ORDINARY')")
    public List<Operation> getAllOperations() {
        List<Operation> operations = operationRepository.findAll();
        return operations;
    }
}
