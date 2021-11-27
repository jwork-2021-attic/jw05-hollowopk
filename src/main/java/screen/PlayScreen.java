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
package screen;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import world.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen, Serializable {

    private World world;
    private Creature player;
    private final List<String> messages;

    public PlayScreen() {
        createWorld();
        this.messages = new ArrayList<>();

        Factory factory = new Factory(this.world);
        createThings(factory);
    }

    public Tile[][] getTiles() {
        Tile[][] tiles = world.getTiles();
        Tile[][] res = new Tile[tiles.length][tiles[0].length];
        for (int i = 0;i < tiles.length;i++) {
            for (int j = 0;j < tiles[0].length;j++) {
                res[i][j] = tiles[i][j];
            }
        }
        return res;
    }

    public ThingData[] getData() {
        List<Thing> thingList = world.getThings();
        List<ThingData> dataList = new ArrayList<>();
        world.thingsLock.lock();
        try {
            for (Thing thing : thingList) {
                ThingData data = new ThingData(thing.getImagePath(), thing.x(), thing.y());
                if (thing instanceof Creature && ((Creature) thing).getAI() instanceof PlayerAI) {
                    data.setPlayerNum(0);
                } else {
                    data.setPlayerNum(-1);
                }
                dataList.add(data);
            }
        } finally {
            world.thingsLock.unlock();
        }
        ThingData[] dataArray = new ThingData[dataList.size()];
        dataList.toArray(dataArray);
        return dataArray;
    }

    public String[] getMessages() {
        String []msgArray = new String[messages.size()];
        messages.toArray(msgArray);
        return msgArray;
    }

    public String getState() {
        String stats = String.format("%d/%d hp", player.hp(), player.maxHP());
        String hoeStats = String.format("You have \n%d hoes", ((PlayerAI) player.getAI()).getHoes());
        return (stats + "\n\n" + hoeStats);
    }

    public Screen check() {
        if (player.hp() <= 0) {
            return new LoseScreen();
        }
        if (player.x() == World.blockCount - 1 && player.y() == World.blockCount - 1) {
            return new WinScreen();
        }
        return this;
    }

    private void createThings(Factory factory) {
        this.player = factory.newPlayer(this.messages);
        for (int i = 0; i < 10; i++) {
            factory.newMedicine();
            factory.newMonster();
            factory.newHoe();
            factory.newBombMonster();
        }
        for (int i = 0;i < 2;i++) {
            factory.newPortals();
        }
    }

    private void createWorld() {
        world = new WorldBuilder(World.blockCount, World.blockCount).makeCaves().build();
    }

    @Override
    public Screen displayOutput(GridPane gridPane) {
        if (player.hp() <= 0) {
            return new LoseScreen();
        }
        if (player.x() == World.blockCount - 1 && player.y() == World.blockCount - 1) {
            return new WinScreen();
        }
        return this;
    }

    @Override
    public Screen respondToUserInput(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT:
                player.moveBy(-1, 0);
                player.rotate(2);
                break;
            case RIGHT:
                player.moveBy(1, 0);
                player.rotate(3);
                break;
            case UP:
                player.moveBy(0, -1);
                player.rotate(0);
                break;
            case DOWN:
                player.moveBy(0, 1);
                player.rotate(1);
                break;
            case W:
                player.fire(BulletAI.UP);
                player.rotate(0);
                break;
            case S:
                player.fire(BulletAI.DOWN);
                player.rotate(1);
                break;
            case A:
                player.fire(BulletAI.LEFT);
                player.rotate(2);
                break;
            case D:
                player.fire(BulletAI.RIGHT);
                player.rotate(3);
                break;
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - World.blockCount / 2, world.width() - World.blockCount));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - World.blockCount / 2, world.height() - World.blockCount));
    }

    public void removeMessage(String msg) {
        world.msgLock.lock();
        try {
            messages.remove(msg);
        } finally {
            world.msgLock.unlock();
        }
    }

}
