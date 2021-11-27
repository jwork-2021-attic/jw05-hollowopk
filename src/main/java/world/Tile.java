package world;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

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
public enum Tile implements Serializable {

    FLOOR(Color.WHITE),

    WALL(Color.BLACK),

    BOUNDS(Color.CYAN);

    private static final int blockSize = 30;

    private Rectangle rectangle;

    public Rectangle getRectangle() {
        return rectangle;
    }

    private Color color;

    public Color color() {
        return color;
    }

    public boolean isWall() {
        return this == Tile.WALL;
    }

    public boolean isGround() {
        return this != Tile.WALL && this != Tile.BOUNDS;
    }

    Tile(Color color) {
        this.rectangle = new Rectangle(blockSize, blockSize, color);
        this.color = color;
    }

}
