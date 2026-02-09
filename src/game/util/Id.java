package game.util;

import java.util.HashMap;

public class Id {
  private static HashMap<String, Integer> ids = new HashMap<>();

  public static String getUniqueId(String prefix) {
    int n = ids.computeIfAbsent(prefix, p -> 0) + 1;
    ids.put(prefix, n);
    return String.format("%s:%d", prefix, n);
  }
}
