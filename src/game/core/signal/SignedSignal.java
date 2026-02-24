package game.core.signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SignedSignal {
  private final List<BiConsumer<String, Object[]>> listeners = new ArrayList<>();
  private final Map<Object, BiConsumer<String, Object[]>> ownerListeners = new HashMap<>();

  public void connect(BiConsumer<String, Object[]> listener) {
    listeners.add(listener);
  }

  public void connect(Object owner, BiConsumer<String, Object[]> listener) {
    ownerListeners.put(owner, listener);
    listeners.add(listener);
  }

  public void disconnect(BiConsumer<String, Object[]> listener) {
    listeners.remove(listener);
  }

  public void disconnect(Object owner) {
    BiConsumer<String, Object[]> c = ownerListeners.remove(owner);
    if (c != null)
      listeners.remove(c);
  }

  public void emit(String signalName, Object... args) {
    for (BiConsumer<String, Object[]> c : new ArrayList<>(listeners)) {
      c.accept(signalName, args);
    }
  }
}
