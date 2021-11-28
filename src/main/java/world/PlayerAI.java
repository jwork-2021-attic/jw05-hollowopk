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
        this.messages = messages;
        this.factory = factory;
        this.creature.name = "player";
        hoes = 0;
    }

    public PlayerAI(Creature creature, List<String> messages, Factory factory, String name) {
        super(creature);
        this.messages = messages;
        this.factory = factory;
        this.creature.name = name;
        hoes = 0;
    }

    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            setPlayerLocation(x, y);
        } else if (tile.isWall()) {
            if (hoes > 0) {
                dig(x, y);
            }
        }
    }

    private void setPlayerLocation(int x, int y) {
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
            new Thread(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                creature.world.msgLock.lock();
                try {
                    this.messages.remove(message);
                } finally {
                    creature.world.msgLock.unlock();
                }
            }).start();
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
                    setPlayerLocation(otherPortal.x(), otherPortal.y());
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
        String name = this.creature.name;
        switch (direction) {
            case 0:
                creature.setImagePath(name + "_up.gif");
                break;
            case 1:
                creature.setImagePath(name + "_down.gif");
                break;
            case 2:
                creature.setImagePath(name + "_left.gif");
                break;
            case 3:
                creature.setImagePath(name + "_right.gif");
                break;
        }
    }

}
