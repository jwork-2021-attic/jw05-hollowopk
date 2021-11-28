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
import java.util.Objects;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen, Serializable {

    private World world;
    private Creature player1;
    private Creature player2;
    private final List<String> messages1;
    private final List<String> messages2;

    public PlayScreen() {
        createWorld();
        this.messages1 = new ArrayList<>();
        this.messages2 = new ArrayList<>();

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
                if (thing == player1) {
                    data.setPlayerNum(0);
                }  else if (thing == player2) {
                    data.setPlayerNum(1);
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

    public String[] getMessages(int playerNum) {
        if (playerNum == 0) {
            String[] msgArray = new String[messages1.size()];
            messages1.toArray(msgArray);
            return msgArray;
        } else if (playerNum == 1) {
            String[] msgArray = new String[messages2.size()];
            messages2.toArray(msgArray);
            return msgArray;
        }
        return null;
    }

    public String getState(int playerNum) {
        Creature player = player1;
        if (playerNum == 1) {
            player = player2;
        }
        String stats = String.format("%d/%d hp", player.hp(), player.maxHP());
        String hoeStats = String.format("You have \n%d hoes", ((PlayerAI) player.getAI()).getHoes());
        return (stats + "\n\n" + hoeStats);
    }

    public String check(int playerNum) {
        Creature player = player1;
        Creature otherPlayer = player2;
        if (playerNum == 1) {
            player = player2;
            otherPlayer = player1;
        }
        if (player.hp() <= 0 ||
                (otherPlayer.x() == World.blockCount - 1 && otherPlayer.y() == World.blockCount - 1)) {
            return "lose";
        }
        if (otherPlayer.hp() <= 0 ||
                (player.x() == World.blockCount - 1 && player.y() == World.blockCount - 1)) {
            return "win";
        }
        return "playing";
    }

    private void createThings(Factory factory) {
        this.player1 = factory.newPlayer(this.messages1);
        this.player2 = factory.newPlayer2(this.messages2);
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
        if (player1.hp() <= 0) {
            return new LoseScreen();
        }
        if (player1.x() == World.blockCount - 1 && player1.y() == World.blockCount - 1) {
            return new WinScreen();
        }
        return this;
    }

    @Override
    public Screen respondToUserInput(KeyCode keyCode, int playerNum) {
        Creature player;
        if (playerNum == 0) {
            player = player1;
        } else {
            player = player2;
        }
        if (!Objects.equals(check(playerNum), "playing")) {
            switch (keyCode) {
                case ENTER:
                    return new PlayScreen();
                default:
                    return this;
            }
        } else {
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
        }
        return this;
    }

}
