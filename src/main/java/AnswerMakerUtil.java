import java.util.Date;

public class AnswerMakerUtil {

    public static String answerTemplate(ResponseCode code, ContentType type, String content) {
        final StringBuilder builder = new StringBuilder(255);                   // Should be enough for headers.

        builder.append("HTTP/1.1 ").append(code.toString()).append("\n\r");
        builder.append("Date: ").append(new Date().toString()).append("\n\r");
        builder.append("Server: Some self-written thing").append("\n\r");
        builder.append("Content-Length: ").append(content.length() - 1).append("\n\r");
        builder.append("Content-Type: ").append(type.toString()).append("\n\r");
        builder.append("Connection: Closed").append("\n\r").append("\n\r");
        builder.append(content);

        return builder.toString();
    }

    public static String answerTemplate(ResponseCode code, ContentType type, int contentSize) {
        final StringBuilder builder = new StringBuilder(255);                   // Should be enough for headers.

        builder.append("HTTP/1.1 ").append(code.toString()).append("\n\r");
        builder.append("Date: ").append(new Date().toString()).append("\n\r");
        builder.append("Server: Some self-written thing").append("\n\r");
        builder.append("Content-Length: ").append(contentSize - 1).append("\n\r");
        builder.append("Content-Type: ").append(type.toString()).append("\n\r");
        builder.append("Connection: Closed").append("\n\r").append("\n\r");

        return builder.toString();
    }

    public static String answerAsDirectory() {
        return make200("<html>Directory index file</html>\\n");
    }

    public static String make200(String content) {
        return answerTemplate(ResponseCode.CODE_200, ContentType.TEXT, content);
    }
    public static String make400() {
        return answerTemplate(ResponseCode.CODE_400, ContentType.TEXT, "");
    }
    public static String make403() {
        return answerTemplate(ResponseCode.CODE_403, ContentType.TEXT, "");
    }
    public static String make404() {
        return answerTemplate(ResponseCode.CODE_404, ContentType.TEXT, "");
    }
    public static String make405() {
        return answerTemplate(ResponseCode.CODE_405, ContentType.TEXT, "");
    }
}
