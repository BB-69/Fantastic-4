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

    { // coin.png -> tint red
      BufferedImage src = AssetManager.getTexture("coin.png");
      int w = src.getWidth();
      int h = src.getHeight();

      BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

      for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {

          int argb = src.getRGB(x, y);

          int a = ColorUtil.getAlpha(argb);
          int[] rgb = ColorUtil.unpackRGB(argb);

          float brightness = ColorUtil.computeLuminance(rgb[0], rgb[1], rgb[2]);
          brightness = ColorUtil.applyToneCurve(brightness, 4.5f, 3.4f);

          int[] tinted = ColorUtil.applyTint(brightness, 1.0f, 0.15f, 0.17f);

          int newArgb = ColorUtil.packARGB(a, tinted[0], tinted[1], tinted[2]);

          result.setRGB(x, y, newArgb);
        }
      }

      AssetManager.addTexture("coin_red.png", result);
    }
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      default:
    }
  }
}
