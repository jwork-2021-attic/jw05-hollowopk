package world;

public class MonsterAI extends CreatureAI {

    private final Factory factory;
    private static int count = 0;
    private final static int maxCount = 15;
    private final static double spreadChance = 0.5;

    public MonsterAI(Creature creature, Factory factory) {
        super(creature);
        this.factory = factory;
        this.creature.name = "monster";
        count++;
        new Thread(this).start();
    }

    @Override
    public void onDelete() {
        count = Math.max(0, count - 1);
        super.onDelete();
    }

    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }

    @Override
    public void attack(Thing other) {
        if (other instanceof Item) {
            if (((Item) other).getAi() instanceof BulletAI) {
                creature.modifyHP(-10);     //被子弹打中
            }
            ((Item) other).getRemoved();
            creature.world.removeThing(other);   //妖怪可以摧毁道具
        } else if (other instanceof Creature && ((Creature) other).getAI() instanceof PlayerAI){
            super.attack(other);    //妖怪只对玩家造成伤害
        }
    }

    @Override
    public void run() {
        while (this.creature.hp() >= 1) {
            try {
                Thread.sleep(1000);
                moveRandomly();
                spread();
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void spread() {
        synchronized (this.getClass()) {
            if (count < maxCount && Math.random() < spreadChance) {
                factory.newMonster();
            }
        }
    }

    @Override
    public void rotate(int direction) {
        switch (direction) {
            case 0:
                creature.setImagePath("monster_up.png");
                break;
            case 1:
                creature.setImagePath("monster_down.png");
                break;
            case 2:
                creature.setImagePath("monster_left.png");
                break;
            case 3:
                creature.setImagePath("monster_right.png");
                break;
        }
    }
}
