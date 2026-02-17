package game.util.calc;

public class MathUtil {
  public static float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }

  public static float clamp(float min, float max, float v) {
    return Math.max(min, Math.min(max, v));
  }
}
