package game.nodes.ui.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import game.GameCanvas;
import game.core.StateManager;
import game.core.graphics.Sprite;
import game.core.node.Node;
import game.core.node.event.Button;
import game.core.node.ui.Text;

public class MenuUIManager extends Node {

    private Sprite background;
    private Button startButton;
    private Text titleText;

    public MenuUIManager() {
        super();

        background = new Sprite("menu_background.png");
        background.setSize(GameCanvas.WIDTH, GameCanvas.HEIGHT);

        titleText = new Text("CONNECT 4");
        titleText.size = 60;
        titleText.color = new Color(255, 255, 255);
        titleText.setPosition(GameCanvas.WIDTH / 2f, 150);
        titleText.updateTextMetrics();

        startButton = new Button(this);
        startButton.setSize(200, 60);
        startButton.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 100);
        startButton.color = new Color(40, 40, 40, 200);

        Text startBtnLabel = new Text("START GAME");
        startBtnLabel.color = Color.WHITE;
        startBtnLabel.size = 20;
        startBtnLabel.setParent(startButton);
        startBtnLabel.setPosition(0, 0); 

        addChildren(titleText, startButton);

        startButton.getClickSignal().connect(args -> {
            StateManager.getGlobalSignal().emit("requestStartGame");
        });
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics2D g, float alpha) {
        background.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f);
        background.draw(g);
    }
}