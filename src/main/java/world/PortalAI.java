package world;

public class PortalAI extends ItemAI {

    private Item otherPortal;

    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PortalAI(Item item, Factory factory, Item otherPortal) {
        super(item);
        this.factory = factory;
        this.otherPortal = otherPortal;
    }

    public Item getOtherPortal() {
        return otherPortal;
    }

    @Override
    public void onDelete() {
        super.onDelete();
        if (otherPortal.isExist()) {
            otherPortal.getRemoved();
        }
    }

}
