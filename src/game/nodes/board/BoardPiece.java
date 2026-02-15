package game.nodes.board;

import java.awt.Graphics2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.core.graphics.Sprite;
import game.core.node.Entity;
import game.core.signal.Signal;
import game.nodes.coin.Coin;
import game.util.calc.MathUtil;

public class BoardPiece extends Entity {

  private Sprite backSprite;

  private BoardPieceCover cover = new BoardPieceCover();;

  public static enum SpritePhase {
    Reveal, Hide, ToReveal, ToHide
  };

  private SpritePhase spritePhase = SpritePhase.Hide;
  private Signal updateSpritePhase = new Signal();

  private boolean initPosition = false;

  private int val = 0;
  private Coin coin;

  public BoardPiece(int val) {
    super();

    setValue(val);

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

  public void revealBack() {
    spritePhase = SpritePhase.ToReveal;
  }

  public void hideBack() {
    spritePhase = SpritePhase.ToHide;
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

  public void receiveCoin(Coin coin) {
    this.coin = coin;
    coin.setParent(this);
    coin.initPosition();
    coin.setPosition(0, 0);
    coin.flash(0.15f);
    setValue(coin.getPlayer() + 1);

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.schedule(() -> {
      coin.shimmer();
    }, 200, TimeUnit.MILLISECONDS);
  }

  public Coin extractCoin() {
    Coin coin = this.coin;
    this.coin = null;
    return coin;
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
      } else
        coin.setPlayer(val - 1);
    }
  }
}
