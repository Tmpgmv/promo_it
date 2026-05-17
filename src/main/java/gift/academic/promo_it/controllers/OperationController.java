package gift.academic.promo_it.controllers;

import gift.academic.promo_it.dtos.operation.NewOperationRequestDto;
import gift.academic.promo_it.dtos.otp.OtpConfigResponseDto;
import gift.academic.promo_it.models.Operation;
import gift.academic.promo_it.repositories.OperationRepository;
import gift.academic.promo_it.services.OperationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operation")
public class OperationController {

    private final OperationRepository operationRepository;
    private final OperationService operationService;

    public OperationController(OperationRepository operationRepository, OperationService operationService) {
        this.operationRepository = operationRepository;
        this.operationService = operationService;
    }

    @GetMapping("/all")
    public List<Operation> getAllOperations() {
        List<Operation> operations = operationRepository.findAll();
        return operations;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Operation> createOperation(@Valid @RequestBody NewOperationRequestDto request) {
        Operation operation = operationService.createOperation(request);
        return ResponseEntity.ok(Operation.fromModel(operation));
    }



}
