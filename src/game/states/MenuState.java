package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.core.signal.CanConnectSignal;
import game.nodes.ui.menu.MenuUIManager;
import game.nodes.ui.menu.AnimatedMenuBackground;

public class MenuState extends GameState implements CanConnectSignal {

    private final MenuState Instance = this;

    private MenuUIManager ui;
    private AnimatedMenuBackground bg;

    public MenuState() {
        super();
        stateName = "menu";

        bg = new AnimatedMenuBackground();
        nodeManager.addNode(bg);

        ui = new MenuUIManager();
        nodeManager.addNode(ui);

        StateManager.getGlobalSignal().connect(Instance, Instance::onGlobalSignal);

    }

    private void onGlobalSignal(String signalName, Object... args) {
        if (signalName.equals("requestStartGame")) {
            StateManager.getGlobalSignal().emit("transitionToState", "play");
        }
    }

    @Override
    public void disconnectSignals() {
        StateManager.getGlobalSignal().disconnect(Instance);
    }
}