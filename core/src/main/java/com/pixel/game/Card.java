package com.pixel.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Card {

    private Suit suit;
    private Rank rank;
    private boolean isFaceUp;
    private Texture cardBack;
    private Texture cardFront;

    private int cardWidth;
    private int cardHeight;

    private Vector3 position;
    private Rectangle bounds;

    public Card(Suit suit, Rank rank, int CardWidth, int CardHeight) {
        this.suit = suit;
        this.rank = rank;
        this.isFaceUp = false;

        cardBack = new Texture("card_back_traditional.png");
        cardFront = new Texture(suit.toString() + "_" + rank.toString() + ".png");
        cardWidth = CardWidth;
        cardHeight = CardHeight;

        this.position = new Vector3(0,0,0);
    }

    // Getters and setters
    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    public Texture getTexture(){
        if (isFaceUp){
            return cardFront;
        }
        return cardBack;}

    public int getCardWidth(){return cardWidth;}

    public int getCardHeight(){return cardHeight;}

    public void setPosition(int xPosition, int yPosition){
        position.x = xPosition;
        position.y = yPosition;
        position.z = 0;
    }

    public Vector3 getPosition(){
        return this.position;
    }

    public Rectangle getBounds(){
//        System.out.println("Card: " + this.getRank().toString() + " pos X: " + position.x + " pos Y: " + position.y + " card width: " + cardWidth + " card height: " + cardHeight);
        return new Rectangle(position.x, position.y, cardWidth, cardHeight);
    }

}
