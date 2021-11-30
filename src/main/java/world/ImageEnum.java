package world;

import javafx.scene.image.Image;

import java.util.Objects;

public enum ImageEnum {

    PLAYER_LEFT("player_left.gif"),
    PLAYER_RIGHT("player_right.gif"),
    PLAYER_UP("player_up.gif"),
    PLAYER_DOWN("player_down.gif"),
    PLAYER2_LEFT("player2_left.gif"),
    PLAYER2_RIGHT("player2_right.gif"),
    PLAYER2_UP("player2_up.gif"),
    PLAYER2_DOWN("player2_down.gif"),

    MONSTER_LEFT("monster_left.png"),
    MONSTER_RIGHT("monster_right.png"),
    MONSTER_UP("monster_up.png"),
    MONSTER_DOWN("monster_down.png"),

    BOMB1("bomb1.png"),
    BOMB2("bomb2.png"),

    BULLET("bullet.png"),
    HOE("hoe.png"),
    MEDICINE("medicine.png"),
    PORTAL("portal.png"),
    PORTAL2("portal2.png"),
    PORTAL3("portal3.png"),
    WALL("wall.png");

    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage() {
        this.image = new Image(this.name);
    }

    private final String name;

    ImageEnum(String name) {
        this.name = name;
    }

    public static Image getImageByName(String imgName) {
        for (ImageEnum ie : ImageEnum.values()) {
            if (Objects.equals(ie.name, imgName)) {
                return ie.image;
            }
        }
        return null;
    }

}
