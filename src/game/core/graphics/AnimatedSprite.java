package game.core.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite {

  private Animator animator;

  public AnimatedSprite(Animator animator) {
    super(null);
    this.animator = animator;
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    if (animator != null)
      animator.update(deltaTime);
  }

  @Override
  public void draw(Graphics2D g) {
    if (animator == null || animator.getCurrentFrame() == null)
      return;

    setSprite("frame", animator.getCurrentFrame());
    super.draw(g);
  }

  @Override
  public void setSprite(String name, BufferedImage image) {
    this.name = name;
    this.image = image;
  }
}
