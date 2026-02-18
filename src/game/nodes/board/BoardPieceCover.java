package game.nodes.board;

import java.awt.Graphics2D;

import game.core.graphics.Sprite;
import game.core.node.Entity;
import game.util.Time;
import game.util.calc.MathUtil;

public class BoardPieceCover extends Entity {

  private Sprite frontSprite;

  private boolean initPosition = false;

  private BoardPiece.SpritePhase spritePhase;
  private float spriteTransitionSpd = 3f;

  public BoardPieceCover() {
    initSprite();
  }

  private void initSprite() {
    frontSprite = new Sprite("wooden-box.png");
    frontSprite.setSize(Board.PIECE_WIDTH, Board.PIECE_HEIGHT);

    layer = -1;
  }

  @Override
  public void update() {
    spriteTransitionUpdate();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    int renderX = (int) MathUtil.lerp(getPrevWorldX(), getWorldX(), initPosition ? alpha : 1);
    int renderY = (int) MathUtil.lerp(getPrevWorldY(), getWorldY(), initPosition ? alpha : 1);

    if (!initPosition)
      initPosition = true;

    if (spritePhase != BoardPiece.SpritePhase.Reveal) {
      frontSprite.setPosition(renderX, renderY);
      frontSprite.draw(g);
    }
  }

  private void spriteTransitionUpdate() {
    if (spritePhase == BoardPiece.SpritePhase.ToReveal) {
      frontSprite.alpha -= spriteTransitionSpd * Time.deltaTime;
      if (frontSprite.alpha <= 0) {
        frontSprite.alpha = 0;
        spritePhase = BoardPiece.SpritePhase.Reveal;
      }
    } else if (spritePhase == BoardPiece.SpritePhase.ToHide) {
      frontSprite.alpha += spriteTransitionSpd * Time.deltaTime;
      if (frontSprite.alpha >= 1) {
        frontSprite.alpha = 1;
        spritePhase = BoardPiece.SpritePhase.Hide;
      }
    }
  }

  private void setSpritePhase(BoardPiece.SpritePhase phase) {
    this.spritePhase = phase;
  }

  public void onUpdateSpritePhase(Object... args) {
    setSpritePhase((BoardPiece.SpritePhase) args[0]);
  }
}
