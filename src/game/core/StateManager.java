package game.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.core.signal.SignedSignal;

public class StateManager {

  private static List<GameState> globalStates = new ArrayList<>();
  private static GameState current;

  private static SignedSignal globalSignal = new SignedSignal();

  public static void setState(GameState state) {
    current = state;
  }

  public static String currentState() {
    return current.getStateName();
  }

  public static void addGlobalState(GameState state) {
    globalStates.add(state);
  }

  public static void removeGlobalState(String stateName) {
    for (GameState state : globalStates)
      if (state.stateName.equals(stateName)) {
        globalStates.remove(state);
        break;
      }
  }

  public static String[] currentGlobalState() {
    return globalStates.stream().map(s -> s.stateName).toArray(String[]::new);
  }

  public static SignedSignal getGlobalSignal() {
    return globalSignal;
  }

  public static void fixedUpdate() {
    globalStates.stream().forEach(GameState::fixedUpdate);
    if (current != null)
      current.fixedUpdate();
  }

  public static void update() {
    globalStates.stream().forEach(GameState::update);
    if (current != null)
      current.update();
  }

  public static void render(Graphics2D g, float alpha) {
    globalStates.stream().forEach(s -> s.render(g, alpha));
    if (current != null)
      current.render(g, alpha);
  }
}
