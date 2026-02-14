package game.nodes.coin;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import game.core.graphics.Sprite;

public class CoinSprite extends Sprite {

  private final SpawnAnimation spawnAnim = new SpawnAnimation();
  private final ShimmerAnimation shimmerAnim = new ShimmerAnimation();

  public CoinSprite(String textureName) {
    super(textureName);
  }

  public void spawn() {
    spawnAnim.start();
    shimmerAnim.stop();
  }

  public void shimmer() {
    shimmer(false);
  }

  public void shimmer(boolean looped) {
    shimmerAnim.start(looped);
    spawnAnim.stop();
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);

    spawnAnim.update(deltaTime);
    shimmerAnim.update(deltaTime);
  }

  @Override
  public void draw(Graphics2D g) {
    if (!visible || alpha <= 0f)
      return;

    AffineTransform old = g.getTransform();
    Composite oldComp = g.getComposite();

    g.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

    g.translate(x, y);
    g.rotate(rotation);

    int drawX = (int) (-width / 2);
    int drawY = (int) (-height / 2);

    if (spawnAnim.isActive()) {
      spawnAnim.draw(g, image, (int) width, (int) height, drawX, drawY);
    } else {
      g.drawImage(image, drawX, drawY,
          (int) width, (int) height, null);

      if (shimmerAnim.isActive()) {
        shimmerAnim.draw(g, image,
            (int) width, (int) height, drawX, drawY);
      }
    }

    g.setTransform(old);
    g.setComposite(oldComp);
  }

  public boolean isSpawning() {
    return spawnAnim.isActive();
  }

  public boolean isShimmering() {
    return shimmerAnim.isActive();
  }
}

/* ===================================================== */
/* ================= Spawn Animation =================== */
/* ===================================================== */

class SpawnAnimation {

  private float progress = 0f;
  private float speed = 4f;
  private boolean active = false;

  public void start() {
    progress = 0f;
    active = true;
  }

  public void stop() {
    active = false;
  }

  public boolean isActive() {
    return active;
  }

  public void update(float dt) {
    if (!active)
      return;

    progress += speed * dt;

    if (progress >= 1f) {
      progress = 1f;
      active = false;
    }
  }

  public void draw(Graphics2D g,
      BufferedImage image,
      int w, int h,
      int drawX, int drawY) {

    int halfW = w / 2;

    BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    Graphics2D bg = buffer.createGraphics();

    if (progress < 0.5f) {
      float offset = halfW * progress * 2;
      int revealPos = (int) (halfW - offset);
      int revealWidth = (int) (offset * 2);
      bg.setClip(revealPos, 0, revealWidth, h);
    }

    bg.drawImage(image, 0, 0, w, h, null);
    bg.setComposite(AlphaComposite.SrcAtop);
    bg.setColor(Color.WHITE);

    if (progress >= 0.5f) {
      float offset = halfW * (progress - 0.5f) * 2;

      int leftPos = (int) (-offset);
      int rightPos = (int) (halfW + offset);

      bg.setClip(0, 0, halfW, h);
      bg.fillRect(leftPos, 0, halfW, h);

      bg.setClip(halfW, 0, halfW, h);
      bg.fillRect(rightPos, 0, halfW, h);
    } else {
      bg.fillRect(0, 0, w, h);
    }

    bg.dispose();
    g.drawImage(buffer, drawX, drawY, null);
  }
}

/* ===================================================== */
/* ================= Shimmer Animation ================= */
/* ===================================================== */

class ShimmerAnimation {

  private float offset = -200f;
  private float speed = 250f;
  private int stripeWidth = 30;
  private float timer = 0f;
  private float interval = 6f;
  private boolean active = false;
  private boolean looped = false;

  public void start(boolean looped) {
    timer = 0f;

    this.looped = looped;
    active = true;
  }

  public void stop() {
    active = false;
  }

  public boolean isActive() {
    return active;
  }

  public void update(float dt) {
    if (!active)
      return;

    timer += dt;
    offset = -200f + speed * timer;

    if (timer > interval) {
      timer = 0f;
      offset = -200f;
      if (!looped)
        active = false;
    }
  }

  public void draw(Graphics2D g,
      BufferedImage image,
      int w, int h,
      int drawX, int drawY) {

    BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    Graphics2D bg = buffer.createGraphics();

    bg.drawImage(image, 0, 0, w, h, null);
    bg.setComposite(AlphaComposite.SrcAtop);
    bg.setColor(Color.WHITE);

    bg.rotate(Math.toRadians(-20));
    bg.fillRect((int) offset, -1000,
        stripeWidth, 2000);

    bg.dispose();

    g.drawImage(buffer, drawX, drawY, null);
  }
}
