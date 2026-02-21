package game.nodes.specialCoin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import game.nodes.coin.Coin;
import game.nodes.specialCoin.SpecialCoin.CoinAttribute;
import game.util.Log;

public class SpecialCoinLogic {

  private final Queue<CoinTask> coinQueue = new ArrayDeque<>();
  private final List<CoinTask> scratch = new ArrayList<>(3);

  private int failedAttempts = 0;
  private double[] playerWeights = { 1.0, 1.0 }; // player 0, 1

  private static final double BASE_CHANCE = 20.0;
  private static final double INCREMENT = 2.0;
  private static final double MIN_WEIGHT = 0.25;
  private static final double WEIGHT_STEP = 0.15;

  public boolean tryCoin(int player) {
    if (coinQueue.size() >= 3)
      return false;

    double chance = BASE_CHANCE + failedAttempts * INCREMENT;

    // ramping base chance
    if (Math.random() * 100 >= chance) {
      failedAttempts++;
      return false;
    }

    // --- rejection check based on player weight ---
    double maxWeight = Math.max(playerWeights[0], playerWeights[1]);
    double acceptance = playerWeights[player] / maxWeight;

    if (Math.random() >= acceptance) {
      failedAttempts++;
      return false;
    }

    // success
    failedAttempts -= Math.min(failedAttempts, 5);

    SpecialCoin newCoin = new SpecialCoin(player, CoinAttribute.random());

    coinQueue.offer(new CoinTask(newCoin, 5));

    adjustWeights(player);

    return true;
  }

  private void adjustWeights(int selected) {
    int other = 1 - selected;

    playerWeights[selected] = Math.max(MIN_WEIGHT, playerWeights[selected] - WEIGHT_STEP);

    playerWeights[other] += WEIGHT_STEP;
  }

  SpecialCoin advanceTurn() {
    Iterator<CoinTask> it = coinQueue.iterator();

    SpecialCoin pending = null;

    while (it.hasNext()) {
      CoinTask task = it.next();
      task.remaining -= 1;

      if (task.remaining <= 0) {
        Log.logInfo("Sent " + switch (task.coin.getAttribute()) {
          case CoinAttribute.Splitter -> "Splitter";
          case CoinAttribute.Bomb -> "Boom";
          case CoinAttribute.Swapper -> "Swapper";
          case null -> "";
          default -> "";
        } + "Coin!");
        pending = task.coin;
        it.remove();
      }
    }

    return pending;
  }

  List<CoinTask> getCoinState() {
    scratch.clear();
    scratch.addAll(coinQueue);
    scratch.sort((a, b) -> Integer.compare(a.remaining, b.remaining));

    while (scratch.size() < 3) {
      CoinTask ct = new CoinTask(new SpecialCoin(-1, null), 0);
      ct.coin.setWorldPosition(-Coin.COIN_SIZE, -Coin.COIN_SIZE);
      scratch.add(ct);
    }

    return scratch;
  }
}

class CoinTask {
  final SpecialCoin coin;
  int remaining;

  CoinTask(SpecialCoin coin, int remaining) {
    this.coin = coin;
    this.remaining = remaining;
  }

  SpecialCoin getCoin() {
    return coin;
  }

  int getRemaining() {
    return remaining;
  }
}