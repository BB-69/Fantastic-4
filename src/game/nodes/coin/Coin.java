package game.nodes.coin;

import java.awt.Graphics2D;

import game.core.AssetManager;
import game.core.graphics.AnimatedSprite;
import game.core.graphics.Animation;
import game.core.graphics.Animator;
import game.core.node.Entity;
import game.util.Time;

public class Coin extends Entity {

  private Animator animator;
  private AnimatedSprite sprite;
  public boolean spinning;

  public static final float COIN_SIZE = 52f;

  private int player;

  private boolean initPosition = false;

  public Coin(int player) {
    super();

    this.player = player;

    layer = -2;
    initSprite();
    setSpinning(false);
  }

  private void initSprite() {
    String[] frameGold = new String[8];
    for (int i = 0; i < frameGold.length; i++)
      frameGold[i] = "coin/gold00" + i + ".png";
    String[] frameSilver = new String[8];
    for (int i = 0; i < frameSilver.length; i++)
      frameSilver[i] = "coin/silver00" + i + ".png";

    Animation idleGold = new Animation(AssetManager.getTexture(new String[] { "coin/gold000.png" }), 1f, false);
    Animation spinGold = new Animation(AssetManager.getTexture(frameGold), 0.1f, true);
    Animation idleSilver = new Animation(AssetManager.getTexture(new String[] { "coin/silver000.png" }), 1f, false);
    Animation spinSilver = new Animation(AssetManager.getTexture(frameSilver), 0.1f, true);
    animator = new Animator();
    animator.add("idleGold", idleGold);
    animator.add("spinGold", spinGold);
    animator.add("idleSilver", idleSilver);
    animator.add("spinSilver", spinSilver);

    sprite = new AnimatedSprite(animator);
    sprite.setSize(COIN_SIZE, COIN_SIZE);
  }

  @Override
  public void update() {
    sprite.update(Time.deltaTime);
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    // AffineTransform old = g.getTransform();
    // g.translate(getWorldX(), getWorldY());

    // g.setColor(player == 0 ? Color.RED : Color.YELLOW);
    // g.fillOval((int) (-COIN_SIZE / 2f),
    // (int) (-COIN_SIZE / 2f),
    // (int) COIN_SIZE,
    // (int) COIN_SIZE);

    // g.setTransform(old);

    drawSprite(g, alpha);
  }

  private void drawSprite(Graphics2D g, float alpha) {
    int renderX = (int) lerp(getPrevWorldX(), getWorldX(), initPosition ? alpha : 1);
    int renderY = (int) lerp(getPrevWorldY(), getWorldY(), initPosition ? alpha : 1);

    if (!initPosition)
      initPosition = true;

    sprite.setPosition(renderX + 2, renderY);

    sprite.draw(g);
  }

  public void setPlayer(int player) {
    this.player = player;
  }

  public void setSpinning(boolean spinning) {
    this.spinning = spinning;

    if (spinning) {
      animator.play("spin" + (player == 0 ? "Gold" : "Silver"));
    } else {
      animator.play("idle" + (player == 0 ? "Gold" : "Silver"));
    }
  }
}
