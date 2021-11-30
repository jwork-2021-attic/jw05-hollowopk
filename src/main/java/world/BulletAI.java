package world;

public class BulletAI extends ItemAI {

    public static int UP = 0;
    public static int DOWN = 1;
    public static int LEFT = 2;
    public static int RIGHT = 3;

    private final int direction;

    private final Creature owner;

    public BulletAI(Item item, Factory factory, int direction, Creature owner) {
        super(item);
        this.factory = factory;
        this.direction = direction;
        this.owner = owner;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (item.isExist()) {
            try {
                Thread.sleep(300);
                move();
            } catch (InterruptedException ignored) {}
        }
    }

    private void move() {
        switch (direction) {
            case 0:
                item.moveBy(0, -1);
                break;
            case 1:
                item.moveBy(0, 1);
                break;
            case 2:
                item.moveBy(-1, 0);
                break;
            case 3:
                item.moveBy(1, 0);
                break;
        }
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        if (!tile.isGround()) {
            item.world.removeThing(item);
            item.getRemoved();
        } else {
            item.setX(x);
            item.setY(y);
        }
    }

    @Override
    public void attack(Thing other) {
        if (!(other == owner)) {    //子弹不会攻击发射子弹的人
            if (other instanceof Item) {
                item.world.removeThing(other);
            } else if (other instanceof Creature) {
                Creature otherCreature = (Creature) other;
                otherCreature.modifyHP(-10);
            }
        }
        item.world.removeThing(item);
        item.getRemoved();
    }
}
