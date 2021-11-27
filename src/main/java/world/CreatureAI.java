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

import java.util.Random;

/**
 *
 * @author Aeranythe Echosong
 */
class CreatureAI implements Runnable {

    protected Creature creature;

    public CreatureAI(Creature creature) {
        this.creature = creature;
        this.creature.setAI(this);
    }

    public void onEnter(int x, int y, Tile tile) {
    }

    public void onUpdate() {
    }

    public void onNotify(String message) {
    }

    public void onDelete() {
        creature.world.removeThing(creature);
    }

    public void fire(int direction, Creature owner) {}

    public void attack(Thing other) {
        if (other instanceof Creature) {
            Creature otherCreature = (Creature) other;
            if (otherCreature.getAI() instanceof BombMonsterAI) {
                otherCreature.getKilled();
            } else {
                int damage = Math.max(0, creature.attackValue() - otherCreature.defenseValue());
                damage = (int) (Math.random() * damage) + 1;

                otherCreature.modifyHP(-damage);


                creature.notify("You attack the\n%s for %d damage.", other.name, damage);
                otherCreature.notify("The %s attacks you\nfor %d damage.", creature.name, damage);
            }
        }
    }

    public boolean canSee(int x, int y) {
        return true;
    }

    @Override
    public void run() {}

    public void moveRandomly() {
        Random random = new Random();
        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                this.creature.moveBy(0, -1);
                break;
            case 1:
                this.creature.moveBy(0, 1);
                break;
            case 2:
                this.creature.moveBy(-1, 0);
                break;
            case 3:
                this.creature.moveBy(1, 0);
                break;
        }
        this.creature.rotate(direction);
    }

    public void rotate(int direction) {}

    public void spread() {}

}
