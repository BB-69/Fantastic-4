package game.core.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import game.core.AssetManager;

public class Sprite {

  // === DATA ===
  private BufferedImage image;

  public float x, y;
  public float width, height;

  public float rotation = 0f; // radians
  public float rotationVelocity = 0f; // radians/sec

  public float alpha = 1f; // 0..1
  public boolean visible = true;

  // === CONSTRUCTOR ===
  public Sprite(String textureName) {
    image = AssetManager.getTexture(textureName);
    width = image.getWidth();
    height = image.getHeight();
  }

  // === LOGIC UPDATE ===
  public void update(float deltaTime) {
    rotation += rotationVelocity * deltaTime;
  }

  // === RENDER ===
  public void draw(Graphics2D g) {
    if (!visible || alpha <= 0f)
      return;

    AffineTransform old = g.getTransform();
    Composite oldComp = g.getComposite();

    g.setComposite(AlphaComposite.getInstance(
        AlphaComposite.SRC_OVER, alpha));

    // Move to sprite center
    g.translate(x + width / 2, y + height / 2);
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
}
