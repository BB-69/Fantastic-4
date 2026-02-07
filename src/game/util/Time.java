package game.util;

public final class Time {

  private static final long START_TIME = System.nanoTime();
  public static float deltaTime;
  public static final float FIXED_DELTA = 1f / 60f;

  private static long lastTime;

  private Time() {
  }

  public static void init() {
    lastTime = System.nanoTime();
  }

  public static float update() {
    long now = System.nanoTime();
    deltaTime = (now - lastTime) / 1_000_000_000f;
    lastTime = now;
    return deltaTime;
  }

  public static String getTextCurrent() {
    long elapsedNanos = System.nanoTime() - START_TIME;
    long totalMillis = elapsedNanos / 1_000_000;

    long minutes = totalMillis / 60_000;
    long seconds = (totalMillis % 60_000) / 1_000;
    long millis = totalMillis % 1_000;

    return String.format(
        "%02d:%02d:%03d",
        minutes, seconds, millis);
  }
}
