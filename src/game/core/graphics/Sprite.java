package game.core.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import game.core.AssetManager;

public class Sprite {

  // === DATA ===
  protected BufferedImage image;
  protected String name;

  public float x, y;
  public float width, height;

  public float rotation = 0f; // radians
  public float rotationVelocity = 0f; // radians/sec

  public float alpha = 1f; // 0..1
  public boolean visible = true;

  private boolean colorInverted = false;

  // === CONSTRUCTOR ===
  /* 'assets/textures/<name>' */
  public Sprite(String textureName) {
    if (textureName == null) {
      return;
    }

    image = AssetManager.getTexture(textureName);
    setSprite(textureName, image);
  }

  // === LOGIC UPDATE ===
  public void update(float deltaTime) {
    rotation += rotationVelocity * deltaTime;
  }

  // === RENDER ===
  public void draw(Graphics2D g) {
    if (!visible || this.alpha <= 0f)
      return;

    AffineTransform old = g.getTransform();
    Composite oldComp = g.getComposite();

    g.setComposite(AlphaComposite.getInstance(
        AlphaComposite.SRC_OVER, this.alpha));

    // Move to sprite center
    g.translate(x, y);
    g.rotate(rotation);

    // Draw centered
    g.drawImage(
        image,
        (int) (-width / 2),
        (int) (-height / 2),
        (int) width,
        (int) height,
        null);

    g.setTransform(old);
    g.setComposite(oldComp);
  }

  public void setSprite(String name, BufferedImage image) {
    this.name = name;
    this.image = image;
    width = image.getWidth();
    height = image.getHeight();
    colorInverted = false;
  }

  // === HELPERS ===
  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void setSize(float w, float h) {
    this.width = w;
    this.height = h;
  }

  public void setCenter(float cx, float cy) {
    this.x = cx - width / 2;
    this.y = cy - height / 2;
  }

  public boolean isColorInverted() {
    return colorInverted;
  }

  public void invertColor() {
    if (colorInverted) {
      image = AssetManager.getTexture(name);
      colorInverted = false;
      return;
    } else {
      BufferedImage safeImage = AssetManager.getTextureSafe(name + "_inverted");
      if (safeImage != null) {
        image = safeImage;
        colorInverted = true;
        return;
      }
    }

    BufferedImage copy = new BufferedImage(
        image.getWidth(),
        image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = copy.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();

    RescaleOp invertFilter = new RescaleOp(
        new float[] { -1f, -1f, -1f, 1f }, // scale
        new float[] { 255f, 255f, 255f, 0f }, // offset
        null);

    invertFilter.filter(copy, copy);

    image = copy;
    AssetManager.addTexture(name + "_inverted", copy);
    colorInverted = true;
  }
}
