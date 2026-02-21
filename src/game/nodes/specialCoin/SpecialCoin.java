package game.nodes.specialCoin;

import game.nodes.coin.Coin;
import game.nodes.coin.CoinSprite;

public class SpecialCoin extends Coin {

  private int player;

  public enum CoinAttribute {
    Splitter, Bomb, Swapper;

    private static final CoinAttribute[] VALUES = values();
    private static final java.util.Random RANDOM = new java.util.Random();

    public static CoinAttribute random() {
      return VALUES[RANDOM.nextInt(VALUES.length)];
    }
  }

  private CoinAttribute attribute = null;

  public SpecialCoin(int player, CoinAttribute att) {
    super(player);

    this.player = player;
    this.attribute = att;

    initSprite();
  }

  private void initSprite() {
    sprite = new CoinSprite(String.format("coin%s%s.png",
        switch (attribute) {
          case CoinAttribute.Splitter -> "_split";
          case CoinAttribute.Bomb -> "_explosion";
          case CoinAttribute.Swapper -> "_interaction";
          case null -> "";
          default -> "";
        },
        player == -1
            ? "_gray"
            : player == 0
                ? "_red"
                : ""));
    sprite.setSize(COIN_SIZE, COIN_SIZE);
  }

  public int getPlayer() {
    return player;
  }

  public CoinAttribute getAttribute() {
    return attribute;
  }

  public void randomAttribute() {
    attribute = CoinAttribute.random();
  }
}
