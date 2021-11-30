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
import screen.Screen;
import world.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainClient extends Application {

    private Pane pane;
    private GridPane gridPane;

    private Text stateText;
    private Text msgText;
    private ImageView playerView;
    private ImageView player2View;
    private List<Rectangle> borders;

    private String fileName = "record.txt";

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = initScene();

        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setScene(scene);
        stage.setTitle("My Roguelike Game");
        stage.show();
        for (ImageEnum imageEnum : ImageEnum.values()) {
            imageEnum.setImage();
        }
        startClient();
    }

    private void startClient() throws IOException, ClassNotFoundException {
        String host = "localhost";
        int port = 3457;
        Socket socket = new Socket(host, port);
        buildConnection(socket);
    }

    private void buildConnection(Socket socket) throws IOException {
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(fileName));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        gridPane.setOnKeyPressed(keyEvent -> {
            String name = keyEvent.getCode().getName();
            pw.println(name);
            pw.flush();
        });
        new Thread(() -> {
            while (!socket.isClosed()) {
                Object obj = null;
                try {
                    obj = is.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (obj != null) {
                    MapData mapData = (MapData) obj;
                    try {
                        fos.writeObject(mapData);
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        gridPane.getChildren().clear();
                        repaint(mapData);
                    });
                }
            }
        }).start();
    }

    public void repaint(MapData mapData) {

        Tile[][] tiles = mapData.getTiles();
        ThingData[] thingData = mapData.getData();
        String[] messages = mapData.getMessages();
        String stateMessage = mapData.getStateMessage();
        String gameState = mapData.getGameState();

        if (Objects.equals(gameState, "win")) {
            Screen.displayOutput(gridPane, "You won! Press enter to go again.");
            setBackground(gridPane, Color.WHITE);
            setVisible(false);
        } else if (Objects.equals(gameState, "lose")) {
            Screen.displayOutput(gridPane, "You lost! Press enter to try again.");
            setBackground(gridPane, Color.WHITE);
            setVisible(false);
        } else if (Objects.equals(gameState, "playing")) {
            setBackground(gridPane, Color.BLACK);
            setVisible(true);
            Screen.displayOutput(gridPane, tiles, thingData, playerView, player2View);
            Screen.displayMessages(msgText, messages);
            Screen.displayState(stateText, stateMessage);
        }
    }

    private void setBackground(Pane pane, Color color) {
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    private void setVisible(boolean visible) {
        playerView.setVisible(visible);
        player2View.setVisible(visible);
        msgText.setVisible(visible);
        stateText.setVisible(visible);
        for (Rectangle r : borders) {
            r.setVisible(visible);
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
        borders.add(r1);
        borders.add(r2);
        borders.add(r3);
        borders.add(r4);
        pane.getChildren().addAll(r1, r2, r3, r4);
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
        pane.getChildren().add(gridPane);
    }

    private void initPlayerView() {
        playerView = new ImageView("player_right.gif");
        playerView.setFitHeight(60);
        playerView.setFitWidth(60);
        player2View = new ImageView("player2_right.gif");
        player2View.setFitHeight(50);
        player2View.setFitWidth(50);
        pane.getChildren().add(playerView);
        pane.getChildren().add(player2View);
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
