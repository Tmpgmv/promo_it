package gift.academic.promo_it.constants;

public enum Status {
    ACTIVE("active", "Активен"),
    EXPIRED("expired", "Просрочен"),
    USED("used", "Был использован");

    private final String aStatus;
    private final String slug;

    Status(String slug, String aStatus) {
        this.slug = slug;
        this.aStatus = aStatus;
    }

    public String getSlug() {
        return slug;
    }

    public String getStatus() {
        return aStatus;
    }
}