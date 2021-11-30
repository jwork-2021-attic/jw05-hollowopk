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

import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class Factory {

    private final World world;

    public Factory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages, ImageView playerView) {
        Creature player = new Creature(this.world, playerView, 100, 20, 5, 40);
        new PlayerAI(player, messages, this);
        world.addPlayer(player);
        return player;
    }

    public void newPortals() {
        Item portal1 = new Item(this.world, new ImageView("portal.png"));
        Item portal2 = new Item(this.world, new ImageView("portal.png"));
        world.addPortals(portal1, portal2);
        new PortalAI(portal1, this, portal2);
        new PortalAI(portal2, this, portal1);
    }

    public void newMedicine() {
        Item medicine = new Item(this.world, new ImageView("medicine.png"));
        world.addAtEmptyLocation(medicine);
        new MedicineAI(medicine, this);
    }

    public void newHoe() {
        Item hoe = new Item(this.world, new ImageView("hoe.png"));
        world.addAtEmptyLocation(hoe);
        new HoeAI(hoe, this);
    }

    public void newBullet(int x, int y, int direction, Creature owner) {
        Item bullet = new Item(this.world, new ImageView("bullet.png"));
        if (world.addBulletAtGivenLocation(x, y, bullet)) {
            new BulletAI(bullet, this, direction, owner);
        }
    }

    public void newMonster() {
        Creature monster = new Creature(this.world, new ImageView("monster_right.png"), 1, 15, 5, 0);
        world.addAtEmptyLocation(monster);
        new MonsterAI(monster, this);
    }

    public void newBombMonster() {
        Creature bombMonster = new Creature(this.world, new ImageView("bomb1.png"), 1, 0, 0, 0);
        world.addAtEmptyLocation(bombMonster);
        new BombMonsterAI(bombMonster, this);
    }

}
