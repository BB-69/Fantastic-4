package game.util;

public class Log {
  private static void logWithPrefix(String prefix, String content) {
    System.out.println(String.format("%s:%s: %s", Time.getTextCurrent(), prefix, content));
  }

  public static void logInfo(String content) {
    logWithPrefix("[INFO]", content);
  }

  public static void logWarn(String content) {
    logWithPrefix("[WARN]", content);
  }

  public static void logErr(String content) {
    logWithPrefix("[ERR]", content);
  }
}
