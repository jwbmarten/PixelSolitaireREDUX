package com.pixel.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pixel.game.Card;
import com.pixel.game.Hand;
import com.pixel.game.PixelSolitaire;
import com.pixel.game.SolitaireInputProcessor;


public class PlayState extends State {

    private OrthographicCamera cam;
    private ExtendViewport viewport;
    private Texture background;
    private Hand hand;
    private SolitaireInputProcessor solitaireInputProcessor;




    public PlayState(GameStateManager gsm) {


        //the super constructor creates a new stack object?
        super(gsm);

    }


    @Override
    protected void handleInput() {
//        if (Gdx.input.isTouched()) {
//            // Convert screen coordinates to world coordinates
//            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
//            cam.unproject(touchPos); // Adjusts touchPos to world coordinates
//            hand.setPosition((touchPos.x - hand.getWidth()/2), (touchPos.y - hand.getHeight()/2));


//            System.out.println("X position clicked: " + Gdx.input.getX());
//            System.out.println("Y position clicked: " + Gdx.input.getY());

//        }


    }

    @Override
    public void update(float dt) {
//        handleInput();

//        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }


    @Override
    public void render(SpriteBatch sb) {

        //setting the projection matrix I think because we resized the camera view
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        //draw the background
        sb.draw(background, 0, 0, 2000, 2000);


        //draw the hand
        sb.draw(hand.getTexture(), hand.getPosition().x, hand.getPosition().y, hand.getWidth(), hand.getHeight());

        sb.end();

    }

    @Override
    public void dispose() {

    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();
    }


    @Override
    public Camera getCam(){return cam;}

    @Override
    public Hand getHand() {
        return null;
    }

    @Override
    public String getStateType() {
        return "play";
    }

    @Override
    public void checkIfCardClicked(float xPos, float yPos) {
        return;
    }

    @Override
    public int getTableauOffset() {
        return 0;
    }

    @Override
    public boolean checkIfCardPlaced() {
        return false;
    }

    @Override
    public  boolean getOptionsPressed(){return false;};

    @Override
    public  void checkButtonHovered(float xPos, float yPos){return;};

}


