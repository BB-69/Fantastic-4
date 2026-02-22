package game.nodes.coin;

import java.awt.Graphics2D;

import game.core.AssetManager;
import game.core.audio.Sound;
import game.core.node.Entity;
import game.core.signal.Signal;
import game.util.Time;
import game.util.calc.MathUtil;

public class Coin extends Entity {

  protected CoinSprite sprite;

  protected Sound fadeInSound = new Sound("ringing-riser.wav");
  protected Sound fuseNBoomSound = new Sound("fuse-n-small-explosion.wav");

  public static final float COIN_SIZE = 44f;

  private int player;

  private boolean initPosition = false;

  private int targetX = 0;
  private boolean isMovingToTargetX = false;

  private boolean despawning = false;
  private Signal signalCoinRemoved;

  public Coin(int player) {
    super();

    fadeInSound.setVolume(-2);
    fuseNBoomSound.setVolume(4);

    this.player = player;

    layer = -2;
    initSprite();
  }

  private void initSprite() {
    sprite = new CoinSprite(String.format("coin%s.png",
        player == -1
            ? "_gray"
            : player == 0
                ? "_red"
                : ""));
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
    // int renderX = (int) MathUtil.lerp(getPrevWorldX(), getWorldX(), initPosition
    // ? alpha : 1);
    // int renderY = (int) MathUtil.lerp(getPrevWorldY(), getWorldY(), initPosition
    // ? alpha : 1);

    if (!initPosition)
      initPosition = true;

    sprite.setPosition(getWorldX(), getWorldY());

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

    fadeInSound.playAt(3.6f);
  }

  public void deSpawn() {
    setActive(true);
    sprite.deSpawn();
    despawning = true;

    fuseNBoomSound.playAt(2.48f);
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

  public void setGlow(boolean active) {
    sprite.setGlow(active);
  }

  public boolean isGlowing() {
    return sprite.isGlowing();
  }

  public void moveToX(int x) {
    targetX = x;
    isMovingToTargetX = true;
  }

  @Override
  public void destroy() {
    super.destroy();
  }
}
