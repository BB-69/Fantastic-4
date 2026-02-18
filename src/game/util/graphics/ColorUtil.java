package game.util.graphics;

import java.awt.Color;

import game.util.calc.MathUtil;

public class ColorUtil {

  public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

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
        (int) MathUtil.clamp(0, 255, value * tintR),
        (int) MathUtil.clamp(0, 255, value * tintG),
        (int) MathUtil.clamp(0, 255, value * tintB)
    };
  }

  public static Color lerp(Color a, Color b, float t) {
    t = MathUtil.clamp(0, 1, t);

    int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
    int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
    int bC = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
    int aC = (int) (a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t);

    return new Color(r, g, bC, aC);
  }
}
