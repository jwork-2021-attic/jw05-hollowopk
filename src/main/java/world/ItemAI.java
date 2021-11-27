package world;

public class ItemAI implements Runnable {

    protected Item item;
    protected Factory factory;

    public ItemAI(Item item) {
        this.item = item;
        this.item.setAi(this);
    }

    public void onEnter(int x, int y, Tile tile) {}

    public void onDelete() {
        item.world.removeThing(item);
    }

    public void attack(Thing other) {}

    public void spread() {}

    @Override
    public void run() {}
}
