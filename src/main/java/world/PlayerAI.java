/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package world;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayerAI extends CreatureAI {

    private Factory factory;

    private final List<String> messages;

    private int hoes;

    public int getHoes() {
        return hoes;
    }

    public PlayerAI(Creature creature, List<String> messages, Factory factory) {
        super(creature);
        setImageSize(2 * World.blockSize);
        this.messages = messages;
        this.factory = factory;
        this.creature.name = "player";
        hoes = 0;
    }

    private void setImageSize(int size) {
        creature.imageView.setFitHeight(size);
        creature.imageView.setFitWidth(size);
    }

    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            setImageLocation(x, y);
        } else if (tile.isWall()) {
            if (hoes > 0) {
                dig(x, y);
            }
        }
    }

    private void setImageLocation(int x, int y) {
        int transX = (x - creature.x) * World.blockSize;
        int transY = (y - creature.y) * World.blockSize;
        creature.imageView.setLayoutX(creature.imageView.getLayoutX() + transX);
        creature.imageView.setLayoutY(creature.imageView.getLayoutY() + transY);
        creature.setX(x);
        creature.setY(y);
    }

    public void dig(int wx, int wy) {
        if (creature.world().dig(wx, wy)) {
            hoes--;
        }
    }

    public void onNotify(String message) {
        creature.world.msgLock.lock();
        try {
            this.messages.add(message);
        } finally {
            creature.world.msgLock.unlock();
        }
    }

    @Override
    public void fire(int direction, Creature owner) {
        switch (direction) {
            case 0:
                factory.newBullet(creature.x(), creature.y() - 1, direction, owner);
                break;
            case 1:
                factory.newBullet(creature.x(), creature.y() + 1, direction, owner);
                break;
            case 2:
                factory.newBullet(creature.x() - 1, creature.y(), direction, owner);
                break;
            case 3:
                factory.newBullet(creature.x() + 1, creature.y(), direction, owner);
                break;
        }
    }

    @Override
    public void attack(Thing other) {
        if (other instanceof Item) {
            Item otherItem = (Item) other;
            if (otherItem.getAi() instanceof MedicineAI) {
                creature.modifyHP(10);
            } else if (otherItem.getAi() instanceof HoeAI) {
                hoes += 5;
                creature.notify("You got 5 hoes!");
            } else if (otherItem.getAi() instanceof PortalAI) {
                Item otherPortal = ((PortalAI) otherItem.getAi()).getOtherPortal();
                if (otherPortal != null) {
                    otherPortal.getRemoved();
                    setImageLocation(otherPortal.x(), otherPortal.y());
                }
            }
            ((Item) other).getRemoved();
            creature.world.removeThing(other);
        } else if (other instanceof Creature) {
            Creature otherCreature = (Creature) other;
            if (otherCreature.getAI() instanceof BombMonsterAI) {
                creature.notify("You touched a bomb, run!");
            }
            super.attack(otherCreature);
        }
    }

    @Override
    public void rotate(int direction) {
        switch (direction) {
            case 0:
                creature.imageView.setImage(new Image("player_up.gif"));
                break;
            case 1:
                creature.imageView.setImage(new Image("player_down.gif"));
                break;
            case 2:
                creature.imageView.setImage(new Image("player_left.gif"));
                break;
            case 3:
                creature.imageView.setImage(new Image("player_right.gif"));
                break;
        }
    }

}
