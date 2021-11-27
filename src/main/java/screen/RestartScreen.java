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
import world.World;

/**
 *
 * @author Aeranythe Echosong
 */
public abstract class RestartScreen implements Screen {

    private ImageView playerView;

    public RestartScreen(ImageView playerView) {
        this.playerView = playerView;
    }

    @Override
    public abstract Screen displayOutput(GridPane gridPane);

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getCode()) {
            case ENTER:
                playerView.setLayoutX(8 * World.blockSize - 15);
                playerView.setLayoutY(10);
                playerView.setImage(new Image("player_right.gif"));
                return new PlayScreen(playerView);
            default:
                return this;
        }
    }

}
