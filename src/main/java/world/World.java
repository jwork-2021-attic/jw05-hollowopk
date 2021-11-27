package world;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

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

/**
 *
 * @author Aeranythe Echosong
 */
public class World implements Serializable {

    public ReentrantLock thingsLock = new ReentrantLock();
    public ReentrantLock portalsLock = new ReentrantLock();
    public ReentrantLock changeLock = new ReentrantLock();
    public ReentrantLock msgLock = new ReentrantLock();

    private final Tile[][] tiles;
    private final int width;
    private final int height;
    private final List<Thing> things;
    private final List<Item> portals;
    private List<String> portalPath;
    private int currentColor = 0;

    public final static int blockSize = 30;
    public final static int blockCount = 25;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.things = new ArrayList<>();
        this.portals = new ArrayList<>();
        this.portalPath = new ArrayList<>();
        portalPath.add("portal.png");
        portalPath.add("portal2.png");
        portalPath.add("portal3.png");
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTileToGround(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public Shape glyph(int x, int y) {
        return tile(x, y).getRectangle();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean dig(int x, int y) {
        boolean res = false;
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if (tiles[x][y] == Tile.WALL) {
                res = true;
            }
            tiles[x][y] = Tile.FLOOR;
        }
        return res;
    }

    public void addAtEmptyLocation(Thing thing) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.things(x, y) != null);

        thing.setX(x);
        thing.setY(y);

        addThing(thing);
    }

    public boolean addBulletAtGivenLocation(int x, int y, Thing thing) {
        if (tile(x, y).isGround()) {
            Thing other = this.things(x, y);
            if (other == null) {
                thing.setX(x);
                thing.setY(y);
                addThing(thing);
            } else {
                if (other instanceof Creature) {
                    ((Creature) other).modifyHP(-10);
                } else if (other instanceof Item) {
                    if (!(((Item) other).getAi() instanceof BulletAI)) {
                        removeThing(other);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void addPlayer(Creature creature) {
        if (creature.getAI() instanceof PlayerAI) {
            creature.setX(0);
            creature.setY(0);
            addThing(creature);
        }
    }

    public Thing things(int x, int y) {
        thingsLock.lock();
        try {
            for (Thing c : this.things) {
                if (c.x() == x && c.y() == y) {
                    return c;
                }
            }
        } finally {
            thingsLock.unlock();
        }
        return null;
    }

    public List<Thing> getThings() {
        return this.things;
    }

    public void removeThing(Thing target) {
        thingsLock.lock();
        try {
            this.things.remove(target);
            if (target instanceof Item) {
                if (((Item) target).getAi() instanceof PortalAI) {
                    removePortal((Item) target);
                }
            }
        } finally {
            thingsLock.unlock();
        }
    }

    public void addThing(Thing thing) {
        thingsLock.lock();
        try {
            this.things.add(thing);
        } finally {
            thingsLock.unlock();
        }
    }

    public void addPortals(Item item1, Item item2) {
        addAtEmptyLocation(item1);
        addAtEmptyLocation(item2);
        item1.imagePath = item2.imagePath =
                portalPath.get(currentColor % portalPath.size());
        currentColor++;
        portalsLock.lock();
        try {
            this.portals.add(item1);
            this.portals.add(item2);
        } finally {
            portalsLock.unlock();
        }
    }

    public void removePortal(Item target) {
        portalsLock.lock();
        try {
            this.portals.remove(target);
        } finally {
            portalsLock.unlock();
        }
    }

}
