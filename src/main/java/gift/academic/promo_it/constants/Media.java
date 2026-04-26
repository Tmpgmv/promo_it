package gift.academic.promo_it.constants;

public enum Media {
    SMS("sms", "SMS"),
    EMAIL("email", "Email"),
    MESSENGER("max", "Max"),
    FILE("file", "Файл");

    Media(String slug, String media) {
        this.slug = slug;
        this.media = media;
    }

    private final String slug;
    private final String media;
}
