package com.pixel.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Hand {

    private Texture openHand;
    private Texture closedHand;
    private Vector3 position;
    private Rectangle bounds;
    private int handWidth;
    private int handHeight;

    private boolean hasActiveCard;
    private Card activeCard;
    private int activeCardSavePositionX;
    private int activeCardSavePositionY;
    private int activeCardOffsetX;
    private int activeCardOffsetY;
    private String activeCardType;

    private ArrayList<Card> activeStack;
    private int activeStackTableauColumn;
    private int activeStackTableauCardNum;

    private int activeCardFoundationIndex;


    private boolean isClosed;

    public Hand(int x, int y, int width, int height){

        position = new Vector3(x, y, 0);

        openHand = new Texture("handUI.png");
        closedHand = new Texture("handUIClosed.png");

        isClosed = false;

        handWidth = width;
        handHeight = height;

        bounds = new Rectangle(x, y, width, height);



    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public Texture getTexture(){
        if (isClosed){ return closedHand;}
        else {return openHand;}
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;

        bounds = new Rectangle(x, y, handWidth, handHeight);
    }

    public int getWidth(){
//        if (isClosed){ return closedHand.getWidth();}
//        else {return openHand.getWidth();}

        return handWidth;
    }

    public int getHeight(){
//        if (isClosed){ return closedHand.getHeight();}
//        else {return openHand.getHeight();}

        return handHeight;
    }

    public void setHasActiveCard(boolean hasActiveCard){
//        this.hasActiveCard = hasActiveCard;
        if (!hasActiveCard){
            activeCard = null;
        }
    }

    public void setActiveCard(Card activeCard, int clickPosX, int clickPosY, int tableauColumn, int tableauCardIndex){
        System.out.println("Active tableau card set!");
        this.hasActiveCard = true;
        this.activeCard = activeCard;

        activeCardOffsetX = clickPosX - (int) activeCard.getPosition().x;
        activeCardOffsetY = clickPosY - (int) activeCard.getPosition().y;

//        System.out.println("X offset: " + activeCardOffsetX);
//        System.out.println("Y offset: " + activeCardOffsetY);

        activeCardSavePositionX = (int) activeCard.getPosition().x;
        activeCardSavePositionY = (int) activeCard.getPosition().y;

        activeStackTableauColumn = tableauColumn;
        activeStackTableauCardNum = tableauCardIndex;

        activeCardType = "tableau";

    }

    public void setActiveCard(Card activeCard,int clickPosX, int clickPosY){
        System.out.println("Active waste card set!");
        this.hasActiveCard = true;
        this.activeCard = activeCard;

        activeCardOffsetX = clickPosX - (int) activeCard.getPosition().x;
        activeCardOffsetY = clickPosY - (int) activeCard.getPosition().y;

        activeCardSavePositionX = (int) activeCard.getPosition().x;
        activeCardSavePositionY = (int) activeCard.getPosition().y;

        activeCardType = "waste";
    }

    public void setActiveCard(Card activeCard, int clickPosX, int clickPosY, int foundationIndex){

        System.out.println("Active foundation card set!");
        this.hasActiveCard = true;
        this.activeCard = activeCard;

        activeCardOffsetX = clickPosX - (int) activeCard.getPosition().x;
        activeCardOffsetY = clickPosY - (int) activeCard.getPosition().y;

        activeCardSavePositionX = (int) activeCard.getPosition().x;
        activeCardSavePositionY = (int) activeCard.getPosition().y;

        activeCardFoundationIndex = foundationIndex;

        activeCardType = "foundation";

    }

    public void setActiveStack(ArrayList<Card> newActiveStack){
        activeStack = newActiveStack;
    }

    public ArrayList<Card> getActiveStack(){ return activeStack;}

    public boolean checkHasActiveCard(){
        return hasActiveCard;
    }

    public Card getActiveCard(){
        return activeCard;
    }

    public int getActiveCardSavePositionX(){
        return activeCardSavePositionX;
    }

    public int getActiveCardSavePositionY(){
        return activeCardSavePositionY;
    }

    public int getActiveCardOffsetX(){return activeCardOffsetX;}
    public int getActiveCardOffsetY(){return activeCardOffsetY;}

    public int getActiveStackTableauColumn() {
        return activeStackTableauColumn;
    }

    public int getActiveStackTableauCardNum() {
        return activeStackTableauCardNum;
    }

    public int getActiveCardFoundationIndex() { return activeCardFoundationIndex;}

    public void clearActiveHand(){
        this.activeCard = null;
        this.hasActiveCard = false;
        this.activeStack = new ArrayList<>();
    }

    public String getActiveCardType(){return activeCardType;}

}
