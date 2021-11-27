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

import javafx.scene.image.ImageView;

/**
 *
 * @author Aeranythe Echosong
 */
public class Creature extends Thing {

    private CreatureAI ai;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    public CreatureAI getAI() {
        return this.ai;
    }

    private int maxHP;

    public int maxHP() {
        return this.maxHP;
    }

    private boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }

    private int hp;

    public int hp() {
        return this.hp;
    }

    public void modifyHP(int amount) {
        int newHP = Math.min(this.hp + amount, this.maxHP);
        if (newHP > this.hp) {
            notify("you got " + (newHP - this.hp) + " treatment!");
        }
        this.hp = newHP;
        if (this.hp < 1) {
            isAlive = false;
            ai.onDelete();
        }
    }

    public void getKilled() {
        modifyHP(-this.hp);
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    private int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }

    private int visionRadius;

    public int visionRadius() {
        return this.visionRadius;
    }

    private int direction;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean canSee(int wx, int wy) {
        return ai.canSee(wx, wy);
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void setTileToGround(int wx, int wy) {
        world.setTileToGround(wx, wy);
    }

    public void fire(int direction) {
        this.ai.fire(direction, this);
    }

    public void moveBy(int mx, int my) {
        world.changeLock.lock();
        try {
            Thing other = world.things(x() + mx, y() + my);

            if (other == null) {
                ai.onEnter(x() + mx, y() + my, world.tile(x() + mx, y() + my));
            } else {
                boolean isPortal =  (other instanceof Item && ((Item) other).getAi() instanceof PortalAI);
                ai.attack(other);
                other = world.things(x() + mx, y() + my);
                if (other == null && !isPortal) {
                    ai.onEnter(x() + mx, y() + my, world.tile(x() + mx, y() + my));
                }
            }
        } finally {
            world.changeLock.unlock();
        }
    }

    public void rotate(int direction) {
        ai.rotate(direction);
    }

    public void update() {
        this.ai.onUpdate();
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround();
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public void notify(String message) {
        ai.onNotify(message);
    }

    public Creature(World world, String imagePath, int maxHP, int attack, int defense, int visionRadius) {
        super(world, imagePath);
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
    }

}
