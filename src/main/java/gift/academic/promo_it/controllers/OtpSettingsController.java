package gift.academic.promo_it.controllers;
import gift.academic.promo_it.dtos.OtpConfigResponseDto;
import gift.academic.promo_it.dtos.OtpConfigUpdateRequestDto;
import gift.academic.promo_it.services.OtpConfigService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
public class OtpSettingsController {

    private final OtpConfigService configService;

    public OtpSettingsController(OtpConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OtpConfigResponseDto> getSettings() {
        return ResponseEntity.ok(OtpConfigResponseDto.fromModel(configService.getConfig()));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OtpConfigResponseDto> updateSettings(
            @Valid @RequestBody OtpConfigUpdateRequestDto request) {

        var updatedModel = configService.updateConfig(request);
        return ResponseEntity.ok(OtpConfigResponseDto.fromModel(updatedModel));
    }

}