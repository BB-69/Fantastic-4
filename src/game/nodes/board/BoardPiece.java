package game.nodes.board;

import java.awt.Graphics2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.core.AssetManager;
import game.core.graphics.Sprite;
import game.core.node.Entity;
import game.core.node.Node;
import game.core.signal.CanConnectSignal;
import game.core.signal.Signal;
import game.nodes.coin.Coin;
import game.util.calc.MathUtil;

public class BoardPiece extends Entity implements CanConnectSignal {

  private final BoardPiece Instance = this;

  private Sprite backSprite;

  private BoardPieceCover cover = new BoardPieceCover();

  public static enum SpritePhase {
    Reveal, Hide, ToReveal, ToHide
  };

  private SpritePhase spritePhase = SpritePhase.Hide;
  private Signal updateSpritePhase = new Signal();

  private Signal signalCoinRemoved = new Signal();

  private boolean initPosition = false;

  private int val = 0;
  private int row;
  private int col;
  private Coin coin;

  public BoardPiece(int val, int row, int col) {
    super();

    setValue(val);
    this.row = row;
    this.col = col;

    signalCoinRemoved.connect(Instance::onCoinRemoved);

    initSprite();

    addChild(cover);
    updateSpritePhase.connect(cover::onUpdateSpritePhase);
  }

  private void initSprite() {
    backSprite = new Sprite("wooden-box_dark.png");
    backSprite.setSize(Board.PIECE_WIDTH, Board.PIECE_HEIGHT);
    backSprite.alpha = 0.92f;

    layer = -3;
  }

  public boolean isRevealed() {
    return spritePhase == SpritePhase.Reveal;
  }

  public void revealBack() {
    spritePhase = SpritePhase.ToReveal;
  }

  public void hideBack() {
    spritePhase = SpritePhase.ToHide;
  }

  public void flash() {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    float oldSpeed = cover.getTransitionSpd();
    float speed = 5f;

    spritePhase = SpritePhase.Hide;
    cover.setTransitionSpd(speed);
    cover.setTexture("wooden-box_red.png", AssetManager.getTexture("wooden-box_red.png"));

    scheduler.schedule(() -> {
      spritePhase = SpritePhase.ToReveal;
    }, 1, TimeUnit.MILLISECONDS);

    scheduler.schedule(() -> {
      cover.setTransitionSpd(oldSpeed);
      cover.setTexture("wooden-box.png", AssetManager.getTexture("wooden-box.png"));
      scheduler.shutdown();
    }, (int) (1f / oldSpeed * 1000) - 1, TimeUnit.MILLISECONDS);
  }

  public SpritePhase getSpritePhase() {
    return spritePhase;
  }

  @Override
  public void update() {
    updateSpritePhase.emit(spritePhase);
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (val < 3 && val > 0) {
      if (coin == null) {
        coin = new Coin(val - 1);
        coin.spawn();
        coin.setParent(this);
        coin.setPosition(0, 0);
        coin.attachCoinRemovedSignal(signalCoinRemoved);
      } else
        coin.setPlayer(val - 1);
    } else {
      if (coin != null) {
        coin.destroy();
        coin = null;
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

    if (spritePhase != SpritePhase.Hide) {
      backSprite.setPosition(renderX, renderY);
      backSprite.draw(g);
    }
  }

  public Coin getCoin() {
    return coin;
  }

  public void receiveCoin(Coin coin) {
    this.coin = coin;
    coin.setParent(this);
    coin.initPosition();
    coin.setPosition(0, 0);
    coin.attachCoinRemovedSignal(signalCoinRemoved);
    coin.flash(0.15f);
    setValue(coin.getPlayer() + 1);

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.schedule(() -> {
      coin.shimmer();

      scheduler.close();
    }, 200, TimeUnit.MILLISECONDS);
  }

  public Coin extractCoin() {
    Coin coin = this.coin;
    this.coin = null;
    this.val = 0;
    return coin;
  }

  public void destroyCoin() {
    if (coin != null)
      coin.destroy();
  }

  public void despawnCoin() {
    if (coin != null)
      coin.deSpawn();
  }

  public void setValue(int val) {
    this.val = val;
    if (val == 0) {
      coin = null;
    } else if (val < 3) {
      if (coin == null) {
        coin = new Coin(val - 1);
        coin.spawn();
        coin.setParent(this);
        coin.setPosition(0, 0);
        coin.attachCoinRemovedSignal(signalCoinRemoved);
      } else
        coin.setPlayer(val - 1);
    }
  }

  private void onCoinRemoved(Object... args) {
    Node n = getParent();
    if (n instanceof Board) {
      Board b = (Board) n;
      b.notifyCoinDespawnFinished(row, col);
    }
  }

  @Override
  public void disconnectSignals() {
    signalCoinRemoved.disconnect(Instance::onCoinRemoved);
    updateSpritePhase.disconnect(cover::onUpdateSpritePhase);
  }

  @Override
  public void destroy() {
    super.destroy();
    disconnectSignals();
  }
}
