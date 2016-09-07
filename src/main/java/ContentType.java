
public final class ContentType {
    private String type;

    private ContentType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ContentType TEXT = new ContentType("text/html");
}
