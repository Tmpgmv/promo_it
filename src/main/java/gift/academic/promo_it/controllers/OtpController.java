package gift.academic.promo_it.controllers;

import gift.academic.promo_it.dtos.otp.OtpConfigResponseDto;
import gift.academic.promo_it.dtos.otp.OtpConfigUpdateRequestDto;
import gift.academic.promo_it.dtos.otp.OtpGenerateRequestDto;
import gift.academic.promo_it.dtos.otp.OtpGenerateResponseDto;
import gift.academic.promo_it.models.User;
import gift.academic.promo_it.repositories.UserRepository;
import gift.academic.promo_it.services.EmailService;
import gift.academic.promo_it.services.OtpConfigService;
import gift.academic.promo_it.services.OtpService;
import gift.academic.promo_it.services.UserService;
import gift.academic.promo_it.validators.OtpRequestValidationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import gift.academic.promo_it.constants.Media;

import java.util.Locale;

@RestController
@RequestMapping("/otp")
public class OtpController {

    private final OtpRequestValidationService validationService;
    private final OtpConfigService configService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public OtpController(OtpRequestValidationService validationService,
                         OtpConfigService configService, OtpService otpService,
                         EmailService emailService, UserRepository userRepository) {
        this.validationService = validationService;
        this.configService = configService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.userRepository = userRepository;
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
    @PreAuthorize("hasAuthority('ORDINARY')")
    public ResponseEntity<OtpGenerateResponseDto> generate(@Valid @RequestBody OtpGenerateRequestDto request) {
        validationService.validateOtpGenerateRequest(request);

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(userLogin).get();


        String code = otpService.generateCode();

        switch(Media.fromSlug(request.media())) {
            case EMAIL -> emailService.sendEmail(user.getEmail(), code);
        }


        return ResponseEntity.ok(new OtpGenerateResponseDto(code, request.media()));
    }

}