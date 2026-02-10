package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class GameCanvas extends Canvas {

  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;
  private static final Point virtualOffset = new Point();
  private static final RenderSize renderSize = new RenderSize();
  private static float renderScale = 1f;

  public GameCanvas() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setFocusable(true);
    setBackground(Color.BLACK);
  }

  public final int getRawWidth() {
    return getWidth();
  }

  public final int getRawHeight() {
    return getHeight();
  }

  public static Point getVirtualOffset() {
    return virtualOffset;
  }

  public static RenderSize getRenderSize() {
    return renderSize;
  }

  public static float getRenderScale() {
    return renderScale;
  }

  public void setVirtualOffset(int x, int y) {
    virtualOffset.x = x;
    virtualOffset.y = y;
  }

  public void setRenderSize(int w, int h) {
    renderSize.w = w;
    renderSize.h = h;
  }

  public void setRenderScale(float scale) {
    renderScale = scale;
  }

  public static class RenderSize {
    public int w = 0;
    public int h = 0;
  }
}
