package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.core.node.Entity;
import game.nodes.coin.Coin;

public class BoardPiece extends Entity {

  private float pieceWidth = 0f;
  private float pieceHeight = 0f;

  private int val = 0;
  private Coin coin;

  public BoardPiece(int val, float pieceSize) {
    super();

    this.pieceWidth = pieceSize;
    this.pieceHeight = pieceSize;
    setValue(val);
  }

  public BoardPiece(int val, float pieceWidth, float pieceHeight) {
    super();

    this.pieceWidth = pieceWidth;
    this.pieceHeight = pieceHeight;
    setValue(val);
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
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    Area rect = new Area(new Rectangle2D.Float(
        -pieceWidth / 2f,
        -pieceHeight / 2f,
        pieceWidth,
        pieceHeight));

    float innerRectWidth = pieceWidth * 0.9f;
    float innerRectHeight = pieceHeight * 0.9f;
    Area innerRect = new Area(new Rectangle2D.Float(
        -innerRectWidth / 2f,
        -innerRectHeight / 2f,
        innerRectWidth,
        innerRectHeight));

    float holeSize = Math.min(pieceWidth, pieceHeight) * 0.8f;
    Area hole = new Area(new Ellipse2D.Float(
        (int) (-holeSize / 2f),
        (int) (-holeSize / 2f),
        (int) holeSize,
        (int) holeSize));

    rect.subtract(hole);
    innerRect.subtract(hole);

    g.setColor(Color.BLACK);
    g.fill(rect);
    g.setColor(Color.BLUE);
    g.fill(innerRect);

    g.setTransform(old);
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
