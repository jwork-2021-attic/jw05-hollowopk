package world;

import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Thing implements Serializable {

    protected String name;

    protected String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    protected World world;

    public World world() {
        return world;
    }

    protected int x;
    protected int y;

    public int x() {
        return x;
    }

    public void setX(int x) {
        if (x >= 0 && x < world.width()) {
            this.x = x;
        }
    }

    public int y() {
        return y;
    }

    public void setY(int y) {
        if (y >= 0 && y < world.height()) {
            this.y = y;
        }
    }

    public Thing(World world, String imagePath) {
        this.world = world;
        this.imagePath = imagePath;
    }

}
