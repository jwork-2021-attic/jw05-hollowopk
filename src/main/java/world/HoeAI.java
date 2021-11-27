package world;

public class HoeAI extends ItemAI {

    private static int count = 0;
    public static int maxCount = 10;
    public static double spreadChance = 0.5;

    public HoeAI(Item item, Factory factory) {
        super(item);
        this.factory = factory;
        count++;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (item.isExist()) {
            try {
                Thread.sleep(1000);
                spread();
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void onDelete() {
        count = Math.max(0, count - 1);
        super.onDelete();
    }

    @Override
    public void spread() {
        synchronized (this.getClass()) {
            if (count < maxCount && Math.random() < spreadChance) {
                factory.newHoe();
            }
        }
    }

}
