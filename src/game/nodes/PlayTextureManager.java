package game.nodes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.core.AssetManager;
import game.core.node.Node;
import game.core.signal.SignedSignal;
import game.util.graphics.ColorUtil;

public class PlayTextureManager extends Node {

  private final PlayTextureManager Instance = this;

  private SignedSignal globalSignal;

  public PlayTextureManager(SignedSignal globalSignal) {
    super();

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance::onGlobalSignal);

    initTextures();
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void initTextures() {

    AssetManager.getTexture("coin.png", "wooden-box.png", "rotate-left.png");

    { // coin.png -> tint red
      BufferedImage src = AssetManager.getTexture("coin.png");
      int w = src.getWidth();
      int h = src.getHeight();

      BufferedImage coin_red = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

      for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {

          int argb = src.getRGB(x, y);

          int a = ColorUtil.getAlpha(argb);
          int[] rgb = ColorUtil.unpackRGB(argb);

          float brightness = ColorUtil.computeLuminance(rgb[0], rgb[1], rgb[2]);
          brightness = ColorUtil.applyToneCurve(brightness, 4.5f, 3.4f);

          int[] tinted = ColorUtil.applyTint(brightness, 1.0f, 0.15f, 0.17f);

          int newArgb = ColorUtil.packARGB(a, tinted[0], tinted[1], tinted[2]);

          coin_red.setRGB(x, y, newArgb);
        }
      }

      AssetManager.addTexture("coin_red.png", coin_red);
    }

    { // wooden-box.png -> darker
      BufferedImage src = AssetManager.getTexture("wooden-box.png");
      int w = src.getWidth();
      int h = src.getHeight();

      BufferedImage wooden_box_dark = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

      for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {

          int argb = src.getRGB(x, y);

          int a = ColorUtil.getAlpha(argb);
          int[] rgb = ColorUtil.unpackRGB(argb);

          rgb[0] *= 0.35f;
          rgb[1] *= 0.35f;
          rgb[2] *= 0.35f;

          int newArgb = ColorUtil.packARGB(a, rgb[0], rgb[1], rgb[2]);

          wooden_box_dark.setRGB(x, y, newArgb);
        }
      }

      AssetManager.addTexture("wooden-box_dark.png", wooden_box_dark);
    }
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      default:
    }
  }
}
