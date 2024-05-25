package com.pixel.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.pixel.gameStates.MenuState;
import com.pixel.gameStates.PlayState;
import com.pixel.gameStates.State;

public class SolitaireInputProcessor implements InputProcessor {
    private State state;

    public SolitaireInputProcessor(State State) {
        this.state = State;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        state.getCam().unproject(worldCoordinates); // Use viewport's camera for conversion
//        System.out.println("Mouse moved to X position: " + worldCoordinates.x);
//        System.out.println("Mouse moved to Y position: " + worldCoordinates.y);

        switch (state.getStateType()){
            case "play":
                state.getHand().setClosed(true);
                state.checkIfCardClicked(worldCoordinates.x, worldCoordinates.y);
                break;
        }

        // Convert screen coordinates to world coordinates
//        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
//        game.getCamera().unproject(worldCoordinates);

        // Handle input based on the coordinates
//        game.handleInput(worldCoordinates.x, worldCoordinates.y);

        return true;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Hand hand = state.getHand();

        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        state.getCam().unproject(worldCoordinates); // Use viewport's camera for conversion

        switch (state.getStateType()){
            case "play":
                hand.setClosed(false);
                if (hand.checkHasActiveCard()) {

                    if (state.checkIfCardPlaced()) {
                        hand.clearActiveHand();
                        break;
                    }

                    Card activeCard = hand.getActiveCard();
//                    System.out.println("Attempting to reset card to old position! " + hand.getActiveCardSavePositionX());
                    int offsetCounter = 0;

                    if (hand.getActiveCardType().equals("foundation") || hand.getActiveCardType().equals("waste")) {
                        activeCard.setPosition((hand.getActiveCardSavePositionX()), hand.getActiveCardSavePositionY());

                    } else if (hand.getActiveCardType().equals("tableau")) {
                        for (Card card : hand.getActiveStack()) {
                            card.setPosition((hand.getActiveCardSavePositionX()), (hand.getActiveCardSavePositionY()) - state.getTableauOffset() * offsetCounter);
                            offsetCounter++;
                        }
                        activeCard.setPosition((hand.getActiveCardSavePositionX()), hand.getActiveCardSavePositionY());
                    }
                    hand.clearActiveHand();
                    break;

                }
        }

        return false;
    }

    /**
     * Called when the touch gesture is cancelled. Reason may be from OS interruption to touch becoming a large surface such as
     * the user cheek). Relevant on Android and iOS only. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        Hand hand = state.getHand();

        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        state.getCam().unproject(worldCoordinates); // Use viewport's camera for conversion
//        System.out.println("Mouse moved to X position: " + worldCoordinates.x);
//        System.out.println("Mouse moved to Y position: " + worldCoordinates.y);
        hand.setPosition((int) (worldCoordinates.x - state.getHand().getWidth() / 2),
            (int) (worldCoordinates.y - state.getHand().getHeight() / 2));

        if (hand.checkHasActiveCard()){
            Card activeCard = hand.getActiveCard();

            int offsetCounter = 0;

            if (hand.getActiveCardType().equals("foundation") ||hand.getActiveCardType().equals("waste")){
                activeCard.setPosition((int) worldCoordinates.x - hand.getActiveCardOffsetX() , (int) worldCoordinates.y - hand.getActiveCardOffsetY());
            }

            else if (hand.getActiveCardType().equals("tableau")) {
                for (Card card : hand.getActiveStack()) {
                    card.setPosition((int) worldCoordinates.x - hand.getActiveCardOffsetX(), (int) worldCoordinates.y - hand.getActiveCardOffsetY() - state.getTableauOffset() * offsetCounter);
                    offsetCounter++;
                }
            }
//            activeCard.setPosition((int) worldCoordinates.x - hand.getActiveCardOffsetX() , (int) worldCoordinates.y - hand.getActiveCardOffsetY() );
//            System.out.println("Active card X position: " + activeCard.getPosition().x);
//            System.out.println("Active card Y position: " + activeCard.getPosition().y);

        }

        return false;
    }

    // Other input methods such as touchUp, touchDragged, etc. can be implemented as needed

    @Override
    public boolean keyDown(int keycode) {
        // Handle key down events
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Handle key up events
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // Implement other methods from the InputProcessor interface as needed

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        state.getCam().unproject(worldCoordinates); // Use viewport's camera for conversion
//        System.out.println("Mouse moved to X position: " + worldCoordinates.x);
//        System.out.println("Mouse moved to Y position: " + worldCoordinates.y);
        state.getHand().setPosition((int) (worldCoordinates.x - state.getHand().getWidth() / 2),
            (int) (worldCoordinates.y - state.getHand().getHeight() / 2));
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Handle mouse wheel scrolled events
        return false;
    }
}
