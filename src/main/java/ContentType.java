import java.util.ArrayList;
import java.util.List;

public final class ContentType {
    private String type;
    private List<String> aliases = new ArrayList<String>(2);

    private ContentType(String type, String... aliases) {
        this.type = type;
        for (String alias : aliases)
            this.aliases.add(alias);
    }

    @Override
    public String toString() {
        return type;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public static ContentType TEXT = new ContentType("text/plain");
    public static ContentType HTML = new ContentType("text/html", "html", "htm");
    public static ContentType CSS = new ContentType("text/css", "css");
    public static ContentType PNG = new ContentType("image/png", "png");
    public static ContentType JPEG = new ContentType("image/jpeg", "jpeg", "jpg");
    public static ContentType GIF = new ContentType("image/gif", "gif", "apng");
    public static ContentType JS = new ContentType("application/javascript", "js");

    public static ContentType[] types = {TEXT, HTML, CSS, JS, PNG, JPEG, GIF};
}
