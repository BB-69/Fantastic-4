package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.nodes.ui.menu.MenuUIManager;

public class MenuState extends GameState {

    private MenuUIManager ui;

    public MenuState() {
        super();
        stateName = "menu";
        
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