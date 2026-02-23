package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.nodes.ui.menu.MenuUIManager;
import game.nodes.ui.menu.AnimatedMenuBackground;

public class MenuState extends GameState {

    private MenuUIManager ui;
    private AnimatedMenuBackground bg;

    public MenuState() {
        super();
        stateName = "menu";

        bg = new AnimatedMenuBackground();
        nodeManager.addNode(bg);
        
        ui = new MenuUIManager();
        nodeManager.addNode(ui);

        StateManager.getGlobalSignal().connect(this::onGlobalSignal);

    }

    private void onGlobalSignal(String signalName, Object... args) {
        if (signalName.equals("requestStartGame")) {
            StateManager.getGlobalSignal().emit("transitionToState", "play"); 
        }
    }
}