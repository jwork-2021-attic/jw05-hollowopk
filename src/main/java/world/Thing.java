package world;

import javafx.scene.image.ImageView;

public class Thing {

    protected String name;

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

    protected ImageView imageView;

    public ImageView imageView() {
        return this.imageView;
    }

    public void setImageView(String path) {
        ImageView imageView = new ImageView(path);
        this.imageView = imageView;
        imageView.setFitWidth(World.blockSize);
        imageView.setFitHeight(World.blockSize);
    }

    public Thing(World world, ImageView imageView) {
        this.world = world;
        imageView.setFitWidth(World.blockSize);
        imageView.setFitHeight(World.blockSize);
        this.imageView = imageView;
    }

}
