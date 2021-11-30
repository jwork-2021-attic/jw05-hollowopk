package javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import screen.PlayScreen;
import screen.RestartScreen;
import screen.Screen;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private Screen screen;
    private Pane pane;
    private GridPane gridPane;

    private Text stateText;
    private Text msgText;
    private ImageView playerView;
    private List<Rectangle> borders;

    @Override
    public void start(Stage stage) {

        Scene scene = initScene();
        screen = new PlayScreen(playerView);
        ((PlayScreen) screen).setPlayerView(playerView);

        stage.setScene(scene);
        stage.setTitle("My Roguelike Game");
        stage.show();

        setRepaintThread();

        repaint();
    }

    public void repaint() {
        screen = screen.displayOutput(gridPane);
        if (screen instanceof PlayScreen) {
            setBackground(pane, Color.WHITE);
            setBackground(gridPane, Color.BLACK);
            setVisible(true);
            ((PlayScreen) screen).displayMessages(msgText);
            ((PlayScreen) screen).displayState(stateText);
        } else if (screen instanceof RestartScreen) {
            setBackground(pane, Color.WHITE);
            setBackground(gridPane, Color.WHITE);
            setVisible(false);
        }
    }

    public void initBorder() {
        Rectangle r1 = new Rectangle(World.blockSize * 26, 10, Color.CYAN);
        r1.setLayoutX(8 * World.blockSize - 10);
        r1.setLayoutY(20);
        Rectangle r2 = new Rectangle(10, World.blockSize * 26, Color.CYAN);
        r2.setLayoutX(8 * World.blockSize - 10);
        r2.setLayoutY(20);
        Rectangle r3 = new Rectangle(World.blockSize * 26, 10, Color.CYAN);
        r3.setLayoutX(8 * World.blockSize - 10);
        r3.setLayoutY(790);
        Rectangle r4 = new Rectangle(10, World.blockSize * 26, Color.CYAN);
        r4.setLayoutX(1000);
        r4.setLayoutY(20);
        borders = new ArrayList<>();
        borders.addAll(List.of(r1, r2, r3, r4));
        pane.getChildren().addAll(r1, r2, r3, r4);
    }

    private void setBackground(Pane pane, Color color) {
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    private void setVisible(boolean visible) {
        playerView.setVisible(visible);
        msgText.setVisible(visible);
        stateText.setVisible(visible);
        for (Rectangle r : borders) {
            r.setVisible(visible);
        }
    }

    private void setRepaintThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30);
                    Platform.runLater(() -> {
                        gridPane.getChildren().clear();
                        repaint();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Scene initScene() {
        pane = new Pane();
        initGridPane();
        initText();
        initBorder();
        initPlayerView();
        int worldSize = World.blockSize * World.blockCount;
        Scene scene = new Scene(pane,
                worldSize + 10 * World.blockSize, worldSize + 2 * World.blockSize);
        gridPane.requestFocus();
        return scene;
    }

    private void initGridPane() {
        gridPane = new GridPane();
        gridPane.setLayoutX(8 * World.blockSize);
        gridPane.setLayoutY(World.blockSize);
        gridPane.setOnKeyPressed(keyEvent -> screen = screen.respondToUserInput(keyEvent));
        pane.getChildren().add(gridPane);
    }

    private void initPlayerView() {
        playerView = new ImageView("player_right.gif");
        playerView.setFitHeight(50);
        playerView.setFitWidth(50);
        playerView.setLayoutX(8 * World.blockSize - 15);
        playerView.setLayoutY(10);
        pane.getChildren().add(playerView);
    }

    private void initText() {
        msgText = new Text();
        stateText = new Text();
        msgText.setLayoutX(10);
        stateText.setLayoutX(10);
        msgText.setLayoutY(10 * World.blockSize);
        stateText.setLayoutY(4 * World.blockSize);
        msgText.setFont(Font.font(null, FontWeight.BOLD, 15));
        stateText.setFont(Font.font(null, FontWeight.BOLD, 25));
        pane.getChildren().addAll(msgText, stateText);
    }

    public static void main(String[] args) {
        launch();
    }

}