package game.nodes.coin;

import java.awt.Graphics2D;

import game.core.AssetManager;
import game.core.node.Entity;
import game.core.signal.Signal;
import game.util.Time;
import game.util.calc.MathUtil;

public class Coin extends Entity {

  private CoinSprite sprite;

  public static final float COIN_SIZE = 52f;

  private int player;

  private boolean initPosition = false;

  private int targetX = 0;
  private boolean isMovingToTargetX = false;

  private boolean despawning = false;
  private Signal signalCoinRemoved;

  public Coin(int player) {
    super();

    this.player = player;

    layer = -2;
    initSprite();
  }

  private void initSprite() {
    sprite = new CoinSprite(String.format("coin%s.png", player == 0 ? "_red" : ""));
    sprite.setSize(COIN_SIZE, COIN_SIZE);
  }

  public void attachCoinRemovedSignal(Signal signalCoinRemoved) {
    this.signalCoinRemoved = signalCoinRemoved;
  }

  @Override
  public void update() {
    sprite.update(Time.deltaTime);

    if (despawning && !sprite.isSpawning()) {
      explode();
    }
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (!sprite.isSpawning() && isMovingToTargetX) {
      setWorldX(MathUtil.lerp(getWorldX(), targetX, 6 * Time.FIXED_DELTA));

      if (Math.abs(getWorldX() - targetX) < 1) {
        setWorldX(targetX);
        isMovingToTargetX = false;
      }
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    drawSprite(g, alpha);
  }

  private void drawSprite(Graphics2D g, float alpha) {
    int renderX = (int) MathUtil.lerp(getPrevWorldX(), getWorldX(), initPosition ? alpha : 1);
    int renderY = (int) MathUtil.lerp(getPrevWorldY(), getWorldY(), initPosition ? alpha : 1);

    if (!initPosition)
      initPosition = true;

    sprite.setPosition(renderX, renderY);

    sprite.draw(g);
  }

  public void initPosition() {
    initPosition = false;
  }

  public int getPlayer() {
    return player;
  }

  public void setPlayer(int player) {
    this.player = player;

    String textureName = String.format("coin%s.png", player == 0 ? "_red" : "");
    sprite.setSprite(textureName, AssetManager.getTexture(textureName));
    sprite.setSize(COIN_SIZE, COIN_SIZE);
  }

  public void spawn() {
    setActive(true);
    sprite.spawn();
  }

  public void deSpawn() {
    setActive(true);
    sprite.deSpawn();
    despawning = true;
  }

  private void explode() {
    despawning = false;
    getNodeManagerInstance().addNode(new CoinExplodeAni(getWorldX(), getWorldY()));
    if (signalCoinRemoved != null)
      signalCoinRemoved.emit();
    destroyRecursive();
  }

  public boolean isSpawning() {
    return sprite.isSpawning();
  }

  public void flash(float duration) {
    if (!isActive())
      return;
    sprite.flashAnim(duration);
  }

  public boolean isFlashing() {
    return sprite.isFlashing();
  }

  public void shimmer() {
    shimmer(false);
  }

  public void shimmer(boolean looped) {
    if (!isActive())
      return;
    sprite.shimmer(looped);
  }

  public boolean isShimmering() {
    return sprite.isShimmering();
  }

  public void moveToX(int x) {
    targetX = x;
    isMovingToTargetX = true;
  }
}
