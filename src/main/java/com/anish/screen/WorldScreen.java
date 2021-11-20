package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.anish.calabashbros.BubbleSorter;
import com.anish.calabashbros.Calabash;
import com.anish.calabashbros.Colors;
import com.anish.calabashbros.Floor;
import com.anish.calabashbros.Monster;
import com.anish.calabashbros.World;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private Calabash[] bros;
    private Monster[][] monsters;
    private int curColorIndex = 0;
    private Monster m;
    String[] sortSteps;

    public WorldScreen() {
        world = new World();
        setNewMonster();
        world.put(m, 0, 0);
    }

    /*
    public WorldScreen() {
        world = new World();

        bros = new Calabash[7];

        int row = 8;
        init(row);


        bros[3] = new Calabash(new Color(204, 0, 0), 1, world);
        bros[5] = new Calabash(new Color(255, 165, 0), 2, world);
        bros[1] = new Calabash(new Color(252, 233, 79), 3, world);
        bros[0] = new Calabash(new Color(78, 154, 6), 4, world);
        bros[4] = new Calabash(new Color(50, 175, 255), 5, world);
        bros[6] = new Calabash(new Color(114, 159, 207), 6, world);
        bros[2] = new Calabash(new Color(173, 127, 168), 7, world);

        world.put(bros[0], 10, 10);
        world.put(bros[1], 12, 10);
        world.put(bros[2], 14, 10);
        world.put(bros[3], 16, 10);
        world.put(bros[4], 18, 10);
        world.put(bros[5], 20, 10);
        world.put(bros[6], 22, 10);

        //BubbleSorter<Calabash> b1 = new BubbleSorter<>();
        //b1.load(bros);
        //b1.sort();

        BubbleSorter<Monster> b2 = new BubbleSorter<>();
        b2.load(monsters);
        b2.sort();

        //sortSteps = this.parsePlan(b1.getPlan());
        sortSteps = this.parsePlan(b2.getPlan());
    }
    */

    private void init(int row) {
        monsters = new Monster[row][row];

        int []randomArray = getRandomArray(row*row);
        for (int i = 0;i < row*row;i++) {
            int r = (Colors.colors[i] >> 16) & 0xff;
            int g = (Colors.colors[i] >> 8) & 0xff;
            int b = Colors.colors[i] & 0xff;
            Monster m = new Monster(new Color(r, g, b), i, world);
            int j = randomArray[i];
            monsters[j/row][j%row] = m;
            world.put(m, 2*(j/row), 2*(j%row));
        }
    }

    public static int[] getRandomArray(int size) {
        int[] origin;
        int[] res;
        origin = new int[size];
        res = new int[size];
        for (int i = 0;i < size;i++) {
            origin[i] = i;
        }
        Random r = new Random();
        for (int j = 0;j < size;j++) {
            int index = r.nextInt(size-j);
            res[j] = origin[index];
            origin[index] = origin[size-1-j];
        }
        return res;
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Calabash[] bros, String step) {
        String[] couple = step.split("<->");
        getBroByRank(bros, Integer.parseInt(couple[0])).swap(getBroByRank(bros, Integer.parseInt(couple[1])));
    }

    private void execute(Monster[][] monsters, String step) {
        String[] couple = step.split("<->");
        getMonsterByRank(monsters, Integer.parseInt(couple[0])).swap(getMonsterByRank(monsters, Integer.parseInt(couple[1])));
    }

    private Calabash getBroByRank(Calabash[] bros, int rank) {
        for (Calabash bro : bros) {
            if (bro.getRank() == rank) {
                return bro;
            }
        }
        return null;
    }

    private Monster getMonsterByRank(Monster[][] monsters, int rank) {
        int row = monsters.length;
        for (int i = 0;i < row;i++) {
            for (int j = 0;j < row;j++) {
                if (monsters[i][j].getRank() == rank) {
                    return monsters[i][j];
                }
            }
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int curX = m.getX(), curY = m.getY();
        int[][] maze = world.getMaze();
        int size = world.getSize();
        if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (curY > 0) {
                world.put(new Floor(Color.cyan, world), curX, curY);
                if (maze[curX][curY-1] == 1) {                    
                    m.moveTo(curX, curY-1);
                } else {
                    world.put(new Floor(Color.red, world), curX, curY-1);
                    setNewMonster();
                    m.moveTo(0, 0);
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (curY+1 < size) {
                world.put(new Floor(Color.cyan, world), curX, curY);
                if (maze[curX][curY+1] == 1) {                    
                    m.moveTo(curX, curY+1);
                } else {
                    world.put(new Floor(Color.red, world), curX, curY+1);
                    setNewMonster();
                    m.moveTo(0, 0);
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_LEFT) {
            if (curX > 0) {
                world.put(new Floor(Color.cyan, world), curX, curY);
                if (maze[curX-1][curY] == 1) {
                    m.moveTo(curX-1, curY);
                } else {
                    world.put(new Floor(Color.red, world), curX-1, curY);
                    setNewMonster();
                    m.moveTo(0, 0);
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (curX+1 < size) {
                world.put(new Floor(Color.cyan, world), curX, curY);
                if (maze[curX+1][curY] == 1) {                    
                    m.moveTo(curX+1, curY);
                } else {
                    world.put(new Floor(Color.red, world), curX+1, curY);
                    setNewMonster();
                    m.moveTo(0, 0);
                }
            }
        }
        /*
        if (i < this.sortSteps.length) {
            //this.execute(bros, sortSteps[i]);
            this.execute(monsters, sortSteps[i]);
            i++;
        }
        */
        return this;
    }

    private void setNewMonster() {
        int r = (Colors.colors[curColorIndex%Colors.colors.length] >> 16) & 0xff;
        int g = (Colors.colors[curColorIndex%Colors.colors.length] >> 8) & 0xff;
        int b = Colors.colors[curColorIndex%Colors.colors.length] & 0xff;
        curColorIndex++;
        m = new Monster(new Color(r, g, b), 0, world);
    }

}
