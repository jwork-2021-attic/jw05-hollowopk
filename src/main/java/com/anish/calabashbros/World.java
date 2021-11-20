package com.anish.calabashbros;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;

import com.anish.screen.*;
import java.awt.Color;

public class World {

    public static final int WIDTH = 30;
    public static final int HEIGHT = WIDTH;

    private Tile<Thing>[][] tiles;
    private int [][]maze;

    public World() {

        MazeGenerator mazeGenerator = new MazeGenerator(WIDTH);
        mazeGenerator.generateMaze();
        maze = mazeGenerator.getMaze();

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                if (maze[i][j] == 1) {
                    tiles[i][j].setThing(new Floor(Color.black, this));
                } else {
                    tiles[i][j].setThing(new Wall(this, Color.white));
                }
            }
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getSize() {
        return WIDTH;
    }

}
