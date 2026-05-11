package gift.academic.promo_it.dtos.otp;

public record OtpGenerateResponseDto(
        String code,
        String media) {

    public OtpGenerateResponseDto(String code, String media) {
        this.code = code;
        this.media = media;
    }
}
