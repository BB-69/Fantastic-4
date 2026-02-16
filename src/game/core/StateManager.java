package game.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

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
    Arrays.stream(
        Stream.concat(Stream.of(current), globalStates.stream()).sorted(Comparator.comparing(GameState::getStateOrder))
            .toArray(GameState[]::new))
        .forEach(GameState::fixedUpdate);
  }

  public static void update() {
    Arrays.stream(
        Stream.concat(Stream.of(current), globalStates.stream()).sorted(Comparator.comparing(GameState::getStateOrder))
            .toArray(GameState[]::new))
        .forEach(GameState::update);
  }

  public static void render(Graphics2D g, float alpha) {
    Arrays.stream(
        Stream.concat(Stream.of(current), globalStates.stream()).sorted(Comparator.comparing(GameState::getStateOrder))
            .toArray(GameState[]::new))
        .forEach(s -> s.render(g, alpha));
  }
}
