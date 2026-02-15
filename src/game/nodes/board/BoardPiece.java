package game.nodes.board;

import java.awt.Graphics2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.core.graphics.Sprite;
import game.core.node.Entity;
import game.nodes.coin.Coin;
import game.util.calc.MathUtil;

public class BoardPiece extends Entity {

  private Sprite sprite;

  private float pieceWidth = 0f;
  private float pieceHeight = 0f;

  private boolean initPosition = false;

  private int val = 0;
  private Coin coin;

  public BoardPiece(int val, float pieceSize) {
    super();

    this.pieceWidth = pieceSize;
    this.pieceHeight = pieceSize;
    setValue(val);

    initSprite();
  }

  public BoardPiece(int val, float pieceWidth, float pieceHeight) {
    super();

    this.pieceWidth = pieceWidth;
    this.pieceHeight = pieceHeight;
    setValue(val);

    initSprite();
  }

  private void initSprite() {
    sprite = new Sprite("wooden-box.png");
    sprite.setSize(Board.PIECE_WIDTH, Board.PIECE_HEIGHT);

    layer = -3;
  }

  @Override
  public void update() {
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
    // AffineTransform old = g.getTransform();
    // g.translate(getWorldX(), getWorldY());

    // Area rect = new Area(new Rectangle2D.Float(
    // -pieceWidth / 2f,
    // -pieceHeight / 2f,
    // pieceWidth,
    // pieceHeight));

    // float innerRectWidth = pieceWidth * 0.9f;
    // float innerRectHeight = pieceHeight * 0.9f;
    // Area innerRect = new Area(new Rectangle2D.Float(
    // -innerRectWidth / 2f,
    // -innerRectHeight / 2f,
    // innerRectWidth,
    // innerRectHeight));

    // float holeSize = Math.min(pieceWidth, pieceHeight) * 0.8f;
    // Area hole = new Area(new Ellipse2D.Float(
    // (int) (-holeSize / 2f),
    // (int) (-holeSize / 2f),
    // (int) holeSize,
    // (int) holeSize));

    // rect.subtract(hole);
    // innerRect.subtract(hole);

    // g.setColor(Color.BLACK);
    // g.fill(rect);
    // g.setColor(Color.BLUE);
    // g.fill(innerRect);

    // g.setTransform(old);
  }

  private void drawSprite(Graphics2D g, float alpha) {
    int renderX = (int) MathUtil.lerp(getPrevWorldX(), getWorldX(), initPosition ? alpha : 1);
    int renderY = (int) MathUtil.lerp(getPrevWorldY(), getWorldY(), initPosition ? alpha : 1);

    if (!initPosition)
      initPosition = true;

    sprite.setPosition(renderX, renderY);

    sprite.draw(g);
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
