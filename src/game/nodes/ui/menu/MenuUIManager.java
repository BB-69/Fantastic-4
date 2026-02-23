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
    private Button quitButton;
    private Text titleText;

    public MenuUIManager() {
        super();

        // Background
        background = new Sprite("menu_background.png");
        background.setSize(GameCanvas.WIDTH, GameCanvas.HEIGHT);

        // Title Game
        titleText = new Text("FANTASTIC 4");
        titleText.size = 60;
        titleText.color = new Color(255, 255, 255);
        titleText.setPosition(GameCanvas.WIDTH / 2f, 150);
        titleText.updateTextMetrics();

        // Start Game
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

        // Quit Game
        quitButton = new Button(this);
        quitButton.setSize(200, 60);
        quitButton.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 180);
        quitButton.color = new Color(40, 40, 40, 200);

        Text quitBtnLabel = new Text("QUIT GAME");
        quitBtnLabel.color = Color.WHITE;
        quitBtnLabel.size = 20;
        quitBtnLabel.setParent(quitButton);
        quitBtnLabel.setPosition(0, 0); 

        addChildren(titleText, startButton, quitButton);

        quitButton.getClickSignal().connect(args -> {
            StateManager.getGlobalSignal().emit("quit"); 
        });

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