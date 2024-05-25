package com.pixel.game;

import com.badlogic.gdx.Game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SolitaireGame extends Game {

    private List<Card> deck;
    private Stack<Card>[] tableau;
    private Stack<Card>[] foundation;
    private Stack<Card> drawPile;
    private Stack<Card> discardPile;

    @Override
    public void create() {
        initializeGame();
    }

    private void initializeGame() {
        // Create a deck of cards
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank, 60, 80));
            }
        }
        Collections.shuffle(deck);

        // Initialize tableau, foundation, draw pile, and discard pile
        tableau = new Stack[7];
        foundation = new Stack[4];
        for (int i = 0; i < 7; i++) {
            tableau[i] = new Stack<>();
            for (int j = 0; j < i + 1; j++) {
                Card card = deck.remove(0);
                if (j == i) {
                    card.setFaceUp(true);
                }
                tableau[i].push(card);
            }
        }
        for (int i = 0; i < 4; i++) {
            foundation[i] = new Stack<>();
        }
        drawPile = new Stack<>();
        discardPile = new Stack<>();
        for (int i = 0; i < deck.size(); i++) {
            drawPile.push(deck.get(i));
        }
    }

    public void moveCardToFoundation(Card card, int foundationIndex) {
        foundation[foundationIndex].push(card);
    }

    public void moveCardToTableau(Card card, int tableauIndex) {
        tableau[tableauIndex].push(card);
    }

    public void moveCardToDrawPile(Card card) {
        drawPile.push(card);
    }

    public void moveCardToDiscardPile(Card card) {
        discardPile.push(card);
    }

    public boolean checkWinCondition() {
        for (int i = 0; i < 4; i++) {
            if (foundation[i].size() != 13) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLoseCondition() {
        // Check if there are any valid moves left
        for (int i = 0; i < 7; i++) {
            Stack<Card> stack = tableau[i];
            if (!stack.isEmpty()) {
                Card topCard = stack.peek();
                if (isCardMovableToFoundation(topCard)) {
                    return false;
                }
                for (int j = 0; j < 4; j++) {
                    if (!foundation[j].isEmpty()) {
                        Card foundationTop = foundation[j].peek();
                        if (canCardBePlacedOnFoundation(topCard, foundationTop)) {
                            return false;
                        }
                    }
                }
            }
        }
        return drawPile.isEmpty();
    }

    private boolean isCardMovableToFoundation(Card card) {
        for (int i = 0; i < 4; i++) {
            if (canCardBePlacedOnFoundation(card, foundation[i].peek())) {
                return true;
            }
        }
        return false;
    }

    private boolean canCardBePlacedOnFoundation(Card card, Card foundationTop) {
        if (foundationTop == null) {
            return card.getRank() == Rank.ACE;
        } else {
            return card.getRank().ordinal() == foundationTop.getRank().ordinal() + 1 &&
                card.getSuit() == foundationTop.getSuit();
        }
    }

    // Additional game logic methods can be added here

    public void render(SpriteBatch sb) {

//        //setting the projection matrix I think because we resized the camera view
//        sb.setProjectionMatrix(cam.combined);
//
//        sb.begin();
//        //draw the background
//        sb.draw(background, 0, 0, 2000, 2000);
//
//
//        //draw the hand
//        sb.draw(hand.getTexture(), hand.getPosition().x, hand.getPosition().y, hand.getWidth(), hand.getHeight());
//
//        sb.end();

    }


}

