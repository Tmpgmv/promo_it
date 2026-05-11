package gift.academic.promo_it.constants;

import gift.academic.promo_it.exceptions.InvalidRequestException;

import java.util.Locale;

public enum Media {
    SMS("sms"),
    EMAIL("email"),
    MESSENGER("messenger"),
    FILE("file");

    private final String slug;

    Media(String slug) {
        this.slug = slug;
    }

    public static Media fromSlug(String slug) {
        for (Media s : Media.values()) {
            if (s.slug.equalsIgnoreCase(slug.trim())) {
                return s;
            }
        }
        throw new InvalidRequestException("Wrong media.");
    }

    public String toString() {
        return slug.toLowerCase(Locale.ROOT);
    }
}
