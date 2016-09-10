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
    public static ContentType HTML = new ContentType("text/html", false, "html", "htm", "txt");
    public static ContentType CSS = new ContentType("text/css", false, "css");
    public static ContentType PNG = new ContentType("image/png", true, "png");
    public static ContentType JPEG = new ContentType("image/jpeg", true, "jpeg", "jpg");
    public static ContentType GIF = new ContentType("image/gif", true, "gif", "apng");
    public static ContentType JS = new ContentType("application/javascript", false, "js");
    public static ContentType SWF = new ContentType("application/x-shockwave-flash", false, "swf");

    public static ContentType[] types = {TEXT, HTML, CSS, JS, PNG, JPEG, GIF, SWF};
}
