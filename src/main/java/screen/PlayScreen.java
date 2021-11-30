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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import world.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private ImageView playerView;
    private final List<String> messages;
    private final Image wallImage = new Image("wall.png");
    private final Image grassImage = new Image("grass.png");

    public PlayScreen(ImageView playerView) {
        this.playerView = playerView;
        createWorld();
        this.messages = new ArrayList<>();

        Factory factory = new Factory(this.world);
        createThings(factory);
    }

    public void setPlayerView(ImageView playerView) {
        this.playerView = playerView;
    }

    private void createThings(Factory factory) {
        this.player = factory.newPlayer(this.messages, playerView);
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

    private void displayTiles(GridPane gridPane, int left, int top) {
        // Show terrain
        for (int x = 0; x < World.blockCount; x++) {
            for (int y = 0; y < World.blockCount; y++) {
                int wx = x + left;
                int wy = y + top;
                if (world.glyph(wx, wy) == Tile.WALL.getRectangle()) {
                    ImageView tmp = new ImageView(wallImage);
                    tmp.setFitHeight(World.blockSize);
                    tmp.setFitWidth(World.blockSize);
                    gridPane.add(tmp, x, y);
                    //gridPane.add(new Rectangle(World.blockSize, World.blockSize, Color.WHITE), x, y);
                } else {
                    ImageView tmp = new ImageView(grassImage);
                    tmp.setFitHeight(World.blockSize);
                    tmp.setFitWidth(World.blockSize);
                    //gridPane.add(tmp, x, y);
                    gridPane.add(new Rectangle(World.blockSize, World.blockSize, Color.BLACK), x, y);
                }
            }
        }
        // Show creatures
        world.thingsLock.lock();
        try {
            for (Thing thing : world.getThings()) {
                if (thing.x() >= left && thing.x() < left + World.blockCount && thing.y() >= top
                        && thing.y() < top + World.blockCount) {
                    if (thing.imageView() != playerView) {
                        gridPane.add(thing.imageView(), thing.x() - left, thing.y() - top);
                    }
                }
            }
        } finally {
            world.thingsLock.unlock();
        }
    }

    public void displayMessages(Text msgText) {
        world.msgLock.lock();
        msgText.setText("");
        try {
            for (final String message : messages) {
                msgText.setText(msgText.getText() + "\n" + message);
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        removeMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } finally {
            world.msgLock.unlock();
        }
    }

    public void displayState(Text stateText) {
        String stats = String.format("%d/%d hp", player.hp(), player.maxHP());
        String hoeStats = String.format("You have \n%d hoes", ((PlayerAI) player.getAI()).getHoes());
        stateText.setText(stats + "\n\n" + hoeStats);
    }

    @Override
    public Screen displayOutput(GridPane gridPane) {
        displayTiles(gridPane, getScrollX(), getScrollY());
        if (player.hp() <= 0) {
            return new LoseScreen(playerView);
        }
        if (player.x() == World.blockCount - 1 && player.y() == World.blockCount - 1) {
            return new WinScreen(playerView);
        }
        return this;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getCode()) {
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
