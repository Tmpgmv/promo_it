package gift.academic.promo_it.controllers;
import gift.academic.promo_it.dtos.OtpConfigResponse;
import gift.academic.promo_it.models.OtpConfig;
import gift.academic.promo_it.services.OtpConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class OtpSettingsController {

    private final OtpConfigService configService;

    public OtpSettingsController(OtpConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OtpConfigResponse> getSettings() {
        return ResponseEntity.ok(OtpConfigResponse.fromModel(configService.getConfig()));
    }
}