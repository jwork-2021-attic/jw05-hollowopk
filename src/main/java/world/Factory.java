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

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class Factory implements Serializable {

    private final World world;

    public Factory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(this.world,  "player_right.gif", 100, 20, 5, 40);
        new PlayerAI(player, messages, this, "player");
        world.addPlayer(player);
        return player;
    }

    public Creature newPlayer2(List<String> messages) {
        Creature player = new Creature(this.world,  "player2_right.gif", 100, 20, 5, 40);
        new PlayerAI(player, messages, this, "player2");
        world.addAtEmptyLocation(player);
        return player;
    }

    public void newPortals() {
        Item portal1 = new Item(this.world, "portal.png");
        Item portal2 = new Item(this.world, "portal.png");
        new PortalAI(portal1, this, portal2);
        new PortalAI(portal2, this, portal1);
        world.addPortals(portal1, portal2);
    }

    public void newMedicine() {
        Item medicine = new Item(this.world, "medicine.png");
        new MedicineAI(medicine, this);
        world.addAtEmptyLocation(medicine);
    }

    public void newHoe() {
        Item hoe = new Item(this.world, "hoe.png");
        new HoeAI(hoe, this);
        world.addAtEmptyLocation(hoe);
    }

    public void newBullet(int x, int y, int direction, Creature owner) {
        Item bullet = new Item(this.world, "bullet.png");
        if (world.addBulletAtGivenLocation(x, y, bullet)) {
            new BulletAI(bullet, this, direction, owner);
        }
    }

    public void newMonster() {
        Creature monster = new Creature(this.world, "monster_right.png", 1, 15, 5, 0);
        new MonsterAI(monster, this);
        world.addAtEmptyLocation(monster);
    }

    public void newBombMonster() {
        Creature bombMonster = new Creature(this.world, "bomb1.png", 1, 0, 0, 0);
        new BombMonsterAI(bombMonster, this);
        world.addAtEmptyLocation(bombMonster);
    }

}
