package gift.academic.promo_it.constants;

public enum Role {
    ADMIN ("admin", "Администратор"),
    ORDINARY("ordinary", "Пользователь");

    Role(String slug, String name){
        this.slug = slug;
        this.name = name;
    }

    private final String slug;
    private final String name;

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public static Role fromSlug(String slug) {
        for (Role role : values()) {
            if (role.slug.equals(slug)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role slug: " + slug);
    }
}
