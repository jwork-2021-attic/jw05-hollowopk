package screen;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import world.ImageEnum;
import world.ThingData;
import world.Tile;
import world.World;

public class Screen {

    public static void displayMessages(Text msgText, String[] messagesList) {
        msgText.setText("");
        for (final String message : messagesList) {
            msgText.setText(msgText.getText() + "\n" + message);
        }
    }

    public static void displayState(Text stateText, String stateMessage) {
        stateText.setText(stateMessage);
    }

    public static void displayOutput(GridPane gridPane, Tile[][] tiles, ThingData[] thingData,
                                     ImageView playerView1, ImageView playerView2) {
        for (int x = 0; x < World.blockCount; x++) {
            for (int y = 0; y < World.blockCount; y++) {
                if (tiles[x][y] == Tile.WALL) {
                    ImageView tmp = new ImageView(ImageEnum.WALL.getImage());
                    tmp.setFitHeight(World.blockSize);
                    tmp.setFitWidth(World.blockSize);
                    gridPane.add(tmp, x, y);
                } else {
                    gridPane.add(new Rectangle(World.blockSize, World.blockSize, Color.BLACK), x, y);
                }
            }
        }
        int playerX = 0;
        int playerY = 0;
        int player2X = 0;
        int player2Y = 0;
        for (ThingData data : thingData) {
            if (data.getX() >= 0 && data.getX() < World.blockCount && data.getY() >= 0
                    && data.getY() < World.blockCount) {
                ImageView tmp = new ImageView(ImageEnum.getImageByName(data.getImagePath()));
                if (data.getPlayerNum() == 0) {
                    playerX = data.getX();
                    playerY = data.getY();
                    playerView1.setImage(ImageEnum.getImageByName(data.getImagePath()));
                } else if (data.getPlayerNum() == 1) {
                    player2X = data.getX();
                    player2Y = data.getY();
                    playerView2.setImage(ImageEnum.getImageByName(data.getImagePath()));
                }
                else {
                    tmp.setFitHeight(World.blockSize);
                    tmp.setFitWidth(World.blockSize);
                    gridPane.add(tmp, data.getX(), data.getY());
                }
            }
        }
        playerView1.setLayoutX(playerX * World.blockSize + 8 * World.blockSize - 15);
        playerView1.setLayoutY(playerY * World.blockSize + 10);
        playerView2.setLayoutX(player2X * World.blockSize + 8 * World.blockSize - 10);
        playerView2.setLayoutY(player2Y * World.blockSize + 10);
    }

    public static void displayOutput(GridPane gridPane, String message) {
        Text text = new Text(message);
        text.setFont(Font.font(null, FontWeight.BOLD, 32));
        text.setFill(Color.RED);
        gridPane.add(text, 0, 0);
    }

}
