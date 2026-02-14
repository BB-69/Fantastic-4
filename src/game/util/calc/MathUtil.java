package game.util.calc;

public class MathUtil {
  public static float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }

  public static float clamp(float v) {
    return Math.max(0, Math.min(255, v));
  }

}
