import java.util.ArrayList;
import java.util.List;

public final class ContentType {
    private String type;
    private List<String> aliases = new ArrayList<String>(2);
    private boolean isBinary;

    private ContentType(String type, boolean isBinary, String... aliases) {
        this.type = type;
        this.isBinary = isBinary;
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

    public boolean isBinary() {
        return isBinary;
    }

    public static ContentType TEXT = new ContentType("text/plain", true);
    public static ContentType PNG = new ContentType("image/png", true, "png");
    public static ContentType JPEG = new ContentType("image/jpeg", true, "jpeg", "jpg");
    public static ContentType GIF = new ContentType("image/gif", true, "gif", "apng");
    public static ContentType SWF = new ContentType("application/x-shockwave-flash", true, "swf");
    public static ContentType JS = new ContentType("application/javascript", true, "js");
    public static ContentType HTML = new ContentType("text/html", true, "html", "htm", "txt");
    public static ContentType CSS = new ContentType("text/css", true, "css");

    public static ContentType[] types = {TEXT, HTML, CSS, JS, PNG, JPEG, GIF, SWF};
}
