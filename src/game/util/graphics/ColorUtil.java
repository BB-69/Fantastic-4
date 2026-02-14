package game.util.graphics;

import game.util.calc.MathUtil;

public class ColorUtil {
  public static int getAlpha(int argb) {
    return (argb >> 24) & 0xff;
  }

  public static int[] unpackRGB(int argb) {
    return new int[] {
        (argb >> 16) & 0xff,
        (argb >> 8) & 0xff,
        argb & 0xff
    };
  }

  public static int packARGB(int a, int r, int g, int b) {
    return (a << 24) | (r << 16) | (g << 8) | b;
  }

  public static float computeLuminance(int r, int g, int b) {
    return (0.299f * r + 0.587f * g + 0.114f * b) / 255f;
  }

  public static float applyToneCurve(
      float brightness,
      float gamma,
      float boost) {
    brightness = (float) Math.pow(brightness, gamma);
    brightness = Math.min(1f, brightness * boost);
    return brightness;
  }

  public static int[] applyTint(
      float brightness,
      float tintR,
      float tintG,
      float tintB) {
    int value = (int) (brightness * 255);

    return new int[] {
        (int) MathUtil.clamp(value * tintR),
        (int) MathUtil.clamp(value * tintG),
        (int) MathUtil.clamp(value * tintB)
    };
  }

}
