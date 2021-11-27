package world;

import java.io.Serializable;

public class ThingData implements Serializable {

    int playerNum;

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    int x;

    public int getX() {
        return x;
    }

    int y;

    public int getY() {
        return y;
    }

    public ThingData(String imagePath, int x, int y) {
        this.imagePath = imagePath;
        this.x = x;
        this.y = y;
    }

}
