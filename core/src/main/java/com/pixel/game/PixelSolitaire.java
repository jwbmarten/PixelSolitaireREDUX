package com.pixel.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pixel.gameStates.GameStateManager;
import com.pixel.gameStates.MenuState;
import com.pixel.gameStates.PlayState;

public class PixelSolitaire extends ApplicationAdapter {

    Texture img;

    private SpriteBatch batch;

    private GameStateManager gsm;

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;


    @Override
    public void create () {

        batch = new SpriteBatch();
        gsm = new GameStateManager();
        gsm.push(new MenuState(gsm));
//		gsm.push(new PlayState(gsm));


    }

    @Override
    public void render () {

        gsm.update(Gdx.graphics.getDeltaTime());
        ScreenUtils.clear(0, 0, 0.2f, 1);
        gsm.render(batch);


    }


    @Override
    public void dispose() {

        batch.dispose();
    }

}
