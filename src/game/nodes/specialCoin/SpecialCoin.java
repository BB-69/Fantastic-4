package game.nodes.specialCoin;

import game.nodes.coin.Coin;

public class SpecialCoin extends Coin {

  private int player;

  enum CoinAttribute {
    Duplicator, Bomb, Swapper;

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
