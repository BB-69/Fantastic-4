package game.core.signal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SignedSignal {
  private final List<BiConsumer<String, Object[]>> listeners = new ArrayList<>();

  public void connect(BiConsumer<String, Object[]> listener) {
    listeners.add(listener);
  }

  public void emit(String signalName, Object... args) {
    for (BiConsumer<String, Object[]> c : listeners) {
      c.accept(signalName, args);
    }
  }
}
