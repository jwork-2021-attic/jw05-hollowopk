package world;

import javafx.scene.image.ImageView;

public class Item extends Thing {

    private boolean isExist = true;

    public boolean isExist() {
        return isExist;
    }

    public void getRemoved() {
        isExist = false;
        ai.onDelete();
    }

    private ItemAI ai;

    public ItemAI getAi() {
        return ai;
    }

    public void setAi(ItemAI ai) {
        this.ai = ai;
    }

    public Item(World world, String imagePath) {
        super(world, imagePath);
    }

    public void moveBy(int mx, int my) {
        world.changeLock.lock();
        try {
            Thing other = world.things(x() + mx, y() + my);

            if (other == null) {
                ai.onEnter(x() + mx, y() + my, world.tile(x() + mx, y() + my));
            } else {
                ai.attack(other);
                other = world.things(x() + mx, y() + my);
                if (other == null) {
                    ai.onEnter(x() + mx, y() + my, world.tile(x() + mx, y() + my));
                }
            }
        } finally {
            world.changeLock.unlock();
        }
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround();
    }

}
