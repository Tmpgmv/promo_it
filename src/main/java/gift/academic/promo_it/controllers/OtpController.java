package gift.academic.promo_it.controllers;

import gift.academic.promo_it.dtos.otp.OtpConfigResponseDto;
import gift.academic.promo_it.dtos.otp.OtpConfigUpdateRequestDto;
import gift.academic.promo_it.dtos.otp.OtpGenerateRequestDto;
import gift.academic.promo_it.dtos.otp.OtpGenerateResponseDto;
import gift.academic.promo_it.services.EmailService;
import gift.academic.promo_it.services.OtpConfigService;
import gift.academic.promo_it.validators.OtpRequestValidationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.academic.promo_it.constants.Media;

import java.util.Locale;

@RestController
@RequestMapping("/otp")
public class OtpController {

    private final OtpRequestValidationService validationService;
    private final OtpConfigService configService;
    private final EmailService emailService;

    public OtpController(OtpRequestValidationService validationService,
                         OtpConfigService configService,
                         EmailService emailService) {
        this.validationService = validationService;
        this.configService = configService;
        this.emailService = emailService;
    }

    @GetMapping("/settings")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OtpConfigResponseDto> getSettings() {
        return ResponseEntity.ok(OtpConfigResponseDto.fromModel(configService.getConfig()));
    }

    @PutMapping("/settings")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OtpConfigResponseDto> updateSettings(
            @Valid @RequestBody OtpConfigUpdateRequestDto request) {

        var updatedModel = configService.updateConfig(request);
        return ResponseEntity.ok(OtpConfigResponseDto.fromModel(updatedModel));
    }


    @PostMapping("/generate")
    public ResponseEntity<OtpGenerateResponseDto> generate(@Valid @RequestBody OtpGenerateRequestDto request) {
        validationService.validateOtpGenerateRequest(request);
        emailService.sendEmail("grabl@mail.ru", "3333");
        return ResponseEntity.ok(new OtpGenerateResponseDto("234", "email"));
    }

}