package game.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import game.core.graphics.Sprite;
import game.core.node.Entity;
import game.input.KeyInput;
import game.input.MouseInput;
import game.util.Time;

public class Player extends Entity {

  private Sprite sprite;
  private float speed = 360f;

  public Player(float x, float y) {
    this.x = x;
    this.y = y;

    sprite = new Sprite("sob.png");
    sprite.setSize(32, 32);
  }

  @Override
  public void update() {

    sprite.update(Time.deltaTime);

    float dx = (KeyInput.isDown(KeyEvent.VK_A) ? -1 : 0) + (KeyInput.isDown(KeyEvent.VK_D) ? 1 : 0);
    float dy = (KeyInput.isDown(KeyEvent.VK_W) ? -1 : 0) + (KeyInput.isDown(KeyEvent.VK_S) ? 1 : 0);

    if (dx != 0 && dy != 0) {
      float len = (float) Math.sqrt(dx * dx + dy * dy);
      dx /= len;
      dy /= len;
    }
    vx = speed * dx;
    vy = speed * dy;
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    x += vx * Time.FIXED_DELTA;
    y += vy * Time.FIXED_DELTA;

    if (MouseInput.isAnyDown()) {
      Point pos = MouseInput.getPosition();
      x = pos.x;
      y = pos.y;
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    if (MouseInput.isAnyDown()) {
      sprite.setPosition(x, y);
    } else {
      int renderX = (int) lerp(prevX, x, alpha);
      int renderY = (int) lerp(prevY, y, alpha);

      sprite.setPosition(renderX, renderY);
    }

    sprite.draw(g);
  }
}
