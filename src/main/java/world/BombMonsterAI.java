package world;

public class BombMonsterAI extends CreatureAI {

    private boolean countdown = false;
    private final Thread thread;

    private Factory factory;
    private static int count = 0;
    private static int maxCount = 10;
    private static double spreadChance = 0.3;

    public int state = 0;

    public BombMonsterAI(Creature creature, Factory factory) {
        super(creature);
        this.factory = factory;
        this.creature.name = "bomb monster";
        count++;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onDelete() {
        count = Math.max(0, count - 1);
        beginCountdown();
    }

    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }

    @Override
    public void attack(Thing other) {
        if (other instanceof Creature && ((Creature) other).getAI() instanceof PlayerAI){
            countdown = true;
            ((Creature) other).notify("You touched a bomb, run!");
        }
    }

    @Override
    public void run() {
        while (!countdown && creature.isAlive()) {
            try {
                Thread.sleep(5000);
                spread();
                moveRandomly();
            } catch (InterruptedException ignored) {}
        }
        if (countdown) {
            blink();
        }
        explode();
        super.onDelete();
    }

    private void blink() {
        String []paths = {"bomb2.png", "bomb1.png"};
        for (int i = 0;i < 10;i++) {
            try {
                creature.imagePath = paths[i % 2];
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void beginCountdown() {
        if (!countdown) {
            countdown = true;
            thread.interrupt();
        }
    }

    private void explode() {
        int myX = creature.x();
        int myY = creature.y();
        for (int x = myX - 1;x <= myX + 1;x++) {
            for (int y = myY - 1;y <= myY + 1;y++) {
                Thing other = creature.world.things(x, y);
                Tile tile = creature.tile(x, y);
                if (other != null && other != creature) {
                    if (other instanceof Creature) {
                        ((Creature) other).modifyHP(-50);
                    } else if (other instanceof Item) {
                        ((Item) other).getRemoved();
                    }
                }
                if (tile.isWall()) {
                    creature.setTileToGround(x, y);
                }
            }
        }
    }

    @Override
    public void spread() {
        synchronized (this.getClass()) {
            if (count < maxCount && Math.random() < spreadChance) {;
                factory.newBombMonster();
            }
        }
    }

}