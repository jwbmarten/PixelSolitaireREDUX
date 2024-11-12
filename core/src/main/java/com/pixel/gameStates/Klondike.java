package com.pixel.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pixel.game.Animation;
import com.pixel.game.Card;
import com.pixel.game.Hand;
import com.pixel.game.PixelSolitaire;
import com.pixel.game.Rank;
import com.pixel.game.SolitaireInputProcessor;
import com.pixel.game.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Klondike extends PlayState{

    private OrthographicCamera cam;
    private ExtendViewport viewport;
//    private FillViewport viewport;
//    private ScreenViewport viewport;
//    private StretchViewport viewport;
    private Texture optionsButton;
    private Rectangle optionsBounds;

    private Texture undoButton;
    private Rectangle undoBounds;

    private Texture background;
    private Texture menu1;

    private int menuDrawX = 600;
    private int menuDrawY = 150;
    private int menuDrawWidth = 400;
    private int menuDrawHeight = 640;

    private int menuSmallButtonWidth = (int) (menuDrawWidth * .17) ;
    private int menuSmallButtonHeight = (int) (menuDrawHeight * .1);

    private Texture sfxOnButton;
    private Texture sfxOffButton;
    private Texture sfxActiveButton;
    private Rectangle sfxBounds;

    private int musicDrawXOffset = (int) (menuDrawWidth * 0.20);
    private int musicDrawYOffset = (int) (menuDrawHeight * 0.77);
//    private int sfxDrawXOffset = (int) (menuDrawWidth * 0.20);
//    private int sfxDrawYOffset = (int) (menuDrawHeight * 0.77);

    private Music music;
    private boolean musicIsOn;
    private Texture musicOnButton;
    private Texture musicOffButton;
    private Texture musicActiveButton;
    private Rectangle musicBounds;


    private Sound grabCard;
    private Sound placeCard;
    private boolean sfxIsOn;

//    private int musicDrawXOffset = (int) (menuDrawWidth * 0.41);
//    private int musicDrawYOffset = (int) (menuDrawHeight * 0.77);
    private int sfxDrawXOffset = (int) (menuDrawWidth * 0.41);
    private int sfxDrawYOffset = (int) (menuDrawHeight * 0.77);

    private Texture closeMenuButton;
    private Rectangle closeMenuBounds;

    private int closeMenuXOffset = (int) (menuDrawWidth * 0.62);
    private int closeMenuYOffset = (int) (menuDrawHeight * 0.77);

    private Texture newGameButtonActive;
    private Texture newGameButton;
    private Texture newGameButtonHover;
    private Rectangle newGameBounds;

    private int newGameXOffset = (int) (menuDrawWidth * 0.15);
    private int newGameYOffset = (int) (menuDrawHeight * 0.54);
    private int newGameWidth = (int) (menuDrawWidth * 0.7);
    private int newGameHeight = (int) (menuDrawHeight * .18);

    private Texture mainMenuButtonActive;
    private Texture mainMenuButton;
    private Texture mainMenuButtonHover;
    private Rectangle mainMenuBounds;

    private int mainMenuXOffset = (int) (menuDrawWidth * 0.13);
    private int mainMenuYOffset = (int) (menuDrawHeight * 0.32);
    private int mainMenuWidth = (int) (menuDrawWidth * 0.74);
    private int mainMenuHeight = (int) (menuDrawHeight * .18);

    private Texture exitGameButtonActive;
    private Texture exitGameButton;
    private Texture exitGameButtonHover;
    private Rectangle exitGameBounds;

    private int exitGameXOffset = (int) (menuDrawWidth * 0.13);
    private int exitGameYOffset = (int) (menuDrawHeight * 0.08);
    private int exitGameWidth = (int) (menuDrawWidth * 0.74);
    private int exitGameHeight = (int) (menuDrawHeight * .18);

    private Animation winAnimation;
    private Texture cardBack;
    private Texture noCard;
    private Hand hand;
    private SolitaireInputProcessor solitaireInputProcessor;

    private boolean gameIsWon;
    private boolean optionsPressed;


    private List<Card> deck;
    private ArrayList<Card>[] tableau = new ArrayList[7];
    private Stack<Card>[] foundation;
    private Stack<Card> drawPile;
    private Rectangle drawPileBounds;
    private ArrayList<Card> wastePile;
    private ArrayList<Card> discardPile;

    private GameSnapshot currentGameSnapshot;
    private Stack<GameSnapshot> history;

    private int cardDisplayMargin;
    private int cardDisplayOffsets;
    private int topRowVerticalPosition;
    private int tableauVerticalPosition;
    private int wastePileHorizontalOffset;

    int cardWidth = PixelSolitaire.WIDTH/11;
    int cardHeight = (int) (cardWidth * 1.25);
    private int tableauOffset = (int) (cardHeight * 0.2);

    class GameSnapshot {
        Stack<Card> drawPile;
        ArrayList<Card> wastePile;
        ArrayList<Card> discardPile;
        ArrayList<Card>[] tableau;
        Stack<Card>[] foundation;
        ArrayList<Boolean>[] cardsFaceDirection;
        ArrayList<Vector3>[] cardPositions;

        String topDrawPileCard;

        GameSnapshot(Stack<Card> drawPile, ArrayList<Card> discardPile, ArrayList<Card> wastePile, ArrayList<Card>[] tableau, Stack<Card>[] foundation) {

//            this.topDrawPileCard = drawPile.peek().getSuit().toString() + drawPile.peek().getRank().toString();

            // Deep copy all lists and stacks to avoid reference issues
            this.drawPile = (Stack<Card>) drawPile.clone();
            this.discardPile = new ArrayList<>(discardPile);
            this.wastePile = new ArrayList<>(wastePile);

            this.tableau = new ArrayList[tableau.length];
            this.cardsFaceDirection = new ArrayList[tableau.length];
            this.cardPositions = new ArrayList[tableau.length];
            for (int i = 0; i < tableau.length; i++) {
                this.tableau[i] = new ArrayList<>(tableau[i]);
                this.cardsFaceDirection[i] = new ArrayList<>();
                this.cardPositions[i] = new ArrayList<>();
                for (Card card : this.tableau[i]) {
                    this.cardsFaceDirection[i].add(card.isFaceUp());
                    this.cardPositions[i].add(new Vector3(card.getPosition()));
                }
            }

            this.foundation = new Stack[foundation.length];
            for (int i = 0; i < foundation.length; i++) {
                this.foundation[i] = (Stack<Card>) foundation[i].clone();
            }
        }
    }


    private GameSnapshot captureGameState() {
        System.out.println("Gamesnap captured!");
        return new GameSnapshot(drawPile, discardPile, wastePile, tableau, foundation);
    }

    private void saveGameState() {

//        System.out.println("Pushing gamesnap with following top draw card: " + currentGameSnapshot.drawPile.peek().getSuit().toString() + currentGameSnapshot.drawPile.peek().getRank().toString());
        history.push(currentGameSnapshot);
        currentGameSnapshot = captureGameState();

        ;
    }

    public void undo() {
        if (history.size() > 1) {
            GameSnapshot previousState = history.pop();
            currentGameSnapshot = previousState;

            drawPile = (Stack<Card>) previousState.drawPile.clone();
            wastePile = new ArrayList<>(previousState.wastePile);
            discardPile = new ArrayList<>(previousState.discardPile);

            System.out.println("Draw Stack Size: " + drawPile.size() + "  discard Stack Size: " + discardPile.size());
            System.out.println("Below is the new draw pile order/stack");
            for (Card card: drawPile){
                System.out.println(card.getSuit().toString() + " " + card.getRank().toString());
            }
            System.out.println("Below is the new discard pile order/stack");
            for (Card card: discardPile){
                System.out.println(card.getSuit().toString() + " " + card.getRank().toString());
            }

            // Restore waste pile card positions
            if (!wastePile.isEmpty()) {
                int iDraw = 0;
                for (Card card : wastePile) {
                    card.setPosition(cardDisplayMargin + (int) (cardWidth * 1.5) + (wastePileHorizontalOffset * iDraw), topRowVerticalPosition);
                    iDraw++;
                    card.setFaceUp(true);
                }
            }

            // Restore tableau cards
            for (int i = 0; i < 7; i++) {
                tableau[i] = new ArrayList<>(previousState.tableau[i]);
                int currentCardVertOffset = 0;
                for (int j = 1; j < tableau[i].size(); j++) {
                    Card card = tableau[i].get(j);
                    card.setFaceUp(previousState.cardsFaceDirection[i].get(j));
                    card.setPosition((int) previousState.cardPositions[i].get(j).x, (int) previousState.cardPositions[i].get(j).y);
                    currentCardVertOffset += (int) tableauOffset * 0.75;
                }
            }

            // Restore foundation cards
            foundation = new Stack[4];
            for (int i = 0; i < 4; i++) {
                foundation[i] = (Stack<Card>) previousState.foundation[i].clone();
                if (!foundation[i].isEmpty()) {
                    Card foundationTop = foundation[i].peek();
                    foundationTop.setFaceUp(true);
                    foundationTop.setPosition(cardDisplayMargin + (cardDisplayOffsets * (i + 3)), topRowVerticalPosition);
                }
            }
        }

        System.out.println("finishing with history size: " + history.size());
    }





    public Klondike(GameStateManager gsm) {

        super(gsm);

//        gameIsWon = true;
        optionsPressed = false;

        solitaireInputProcessor = new SolitaireInputProcessor(this);
        Gdx.input.setInputProcessor(solitaireInputProcessor);
        cam = new OrthographicCamera();
        cam.setToOrtho(false, PixelSolitaire.WIDTH, PixelSolitaire.HEIGHT);
//        viewport = new FillViewport(PixelSolitaire.WIDTH, PixelSolitaire.HEIGHT, cam);
        viewport = new ExtendViewport(PixelSolitaire.WIDTH, PixelSolitaire.HEIGHT, cam);
//        viewport = new StretchViewport(PixelSolitaire.WIDTH, PixelSolitaire.HEIGHT, cam);
//        viewport = new ScreenViewport(cam);


        hand = new Hand(35, 35, PixelSolitaire.WIDTH/35,(int) (PixelSolitaire.WIDTH/25 * 1.2));
//        hand = new Hand(50, 50, (int) PixelSolitaire.WIDTH/28, (int) (PixelSolitaire.WIDTH/28));


        background = new Texture("gameGreenBackground.png");

//        menu1 = new Texture("menuWorking2.png");
        menu1 = new Texture("menuBlank.png");

        optionsButton = new Texture("optionsButton.png");
        optionsBounds = new Rectangle(5, 5, 186, 53);

        undoButton = new Texture("undoButton.png");
        undoBounds = new Rectangle(210, 5, 55, 55);

        sfxOnButton = new Texture("SFXOn.png");
        sfxOffButton = new Texture("SFXOff.png");
        sfxIsOn= true;
        sfxActiveButton = sfxOnButton;
        sfxBounds = new Rectangle(menuDrawX + sfxDrawXOffset, menuDrawY + sfxDrawYOffset, menuSmallButtonWidth, menuSmallButtonHeight);
        grabCard = Gdx.audio.newSound(Gdx.files.internal("grabCard.mp3"));
        placeCard = Gdx.audio.newSound(Gdx.files.internal("placeCard.mp3"));

        musicIsOn = true;
        music = Gdx.audio.newMusic(Gdx.files.internal("peacefully.mp3"));
        music.play();
        music.setVolume(0.5f);
        musicOnButton = new Texture("MusicOn.png");
        musicOffButton = new Texture("MusicOff.png");
        musicActiveButton = musicOnButton;
        musicBounds = new Rectangle(menuDrawX + musicDrawXOffset, menuDrawY + musicDrawYOffset, menuSmallButtonWidth, menuSmallButtonHeight);

        closeMenuButton = new Texture("closeMenu.png");
        closeMenuBounds = new Rectangle(menuDrawX + closeMenuXOffset, menuDrawY + closeMenuYOffset, menuSmallButtonWidth, menuSmallButtonHeight);

        newGameButton = new Texture("menuNewGame.png");
        newGameButtonHover = new Texture("menuNewGameHover.png");
        newGameBounds = new Rectangle(menuDrawX + newGameXOffset, menuDrawY + newGameYOffset, newGameWidth, newGameHeight);
        newGameButtonActive = newGameButton;

        mainMenuButton = new Texture("menuMainMenu.png");
        mainMenuButtonHover = new Texture("menuMainMenuHover.png");
        mainMenuBounds = new Rectangle(menuDrawX + mainMenuXOffset, menuDrawY + mainMenuYOffset, mainMenuWidth, mainMenuHeight);
        mainMenuButtonActive = mainMenuButton;

        exitGameButton = new Texture("menuExitGame.png");
        exitGameButtonHover = new Texture("menuExitGameHover.png");
        exitGameBounds = new Rectangle(menuDrawX + exitGameXOffset, menuDrawY + exitGameYOffset, exitGameWidth, exitGameHeight);
        exitGameButtonActive = exitGameButton;

//        cardBack = new Texture("card_back.png");
        cardBack = new Texture("card_back_traditional.png");
        noCard = new Texture("NOCARD.png");

        //horizontal left margin for displaying cards
        cardDisplayMargin = PixelSolitaire.WIDTH/21;

        // how much horizontal offset between drawing cards
        cardDisplayOffsets = (int) (PixelSolitaire.WIDTH/7.5);

        topRowVerticalPosition = PixelSolitaire.HEIGHT - (int) (cardHeight * 1.1);

        tableauVerticalPosition = (int) (cardHeight * 2.2);

        wastePileHorizontalOffset = (int) (cardWidth * 0.3);

        Texture winningAnimationTex = new Texture("winAnimation.png");
        winAnimation = new Animation(new TextureRegion(winningAnimationTex), 12, 6, true, "WinAnimation");


        initializeGame();
    }


    private void initializeGame() {

        gameIsWon = false;

        history = new Stack<GameSnapshot>();

        // Create a deck of cards
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {

            if (suit != Suit.NOCARD_FOUNDATION && suit != Suit.NOCARD_TABLEAU) {

                for (Rank rank : Rank.values()) {

                    if (rank != Rank.NOCARD) {

                        deck.add(new Card(suit, rank, cardWidth, cardHeight));
                    }
                }
            }
        }
        Collections.shuffle(deck);

        // Initialize tableau, foundation, draw pile, and discard pile
        for (int i = 0; i < 7; i++) {
            tableau[i] = new ArrayList<Card>();
            tableau[i].add(new Card(Suit.NOCARD_TABLEAU, Rank.NOCARD, cardWidth, cardHeight));
            tableau[i].get(0).setFaceUp(true);
            tableau[i].get(0).setPosition(cardDisplayMargin + (cardDisplayOffsets * i), PixelSolitaire.HEIGHT - tableauVerticalPosition);
        }
        foundation = new Stack[4];

        int tempCardDisplayMargin = cardDisplayMargin;

        ///
        for (int i = 0; i < 7; i++) {

            int currentCardVertOffset = 0;

            for (int j = 0; j < i + 1; j++) {
                Card card = deck.remove(0);
                if (j == i) {
                    card.setFaceUp(true);
                }
                tableau[i].add(card);

                card.setPosition(tempCardDisplayMargin, (PixelSolitaire.HEIGHT - tableauVerticalPosition - currentCardVertOffset));

                currentCardVertOffset += (int) tableauOffset * 0.75;
            }

            tempCardDisplayMargin += cardDisplayOffsets;
        }
        for (int i = 0; i < 4; i++) {
            foundation[i] = new Stack<>();
            foundation[i].add(new Card(Suit.NOCARD_FOUNDATION, Rank.NOCARD, cardWidth, cardHeight));
            foundation[i].get(0).setFaceUp(true);
            foundation[i].get(0).setPosition(cardDisplayMargin + (cardDisplayOffsets * (i + 3)), topRowVerticalPosition);
        }
        drawPile = new Stack<>();
        discardPile = new ArrayList<>();
        for (int i = 0; i < deck.size(); i++) {
            drawPile.push(deck.get(i));
        }

        wastePile = new ArrayList<Card>();
        drawPileBounds = new Rectangle(cardDisplayMargin, topRowVerticalPosition, cardWidth, cardHeight);
        ArrayList<Card> activeStack = new ArrayList<Card>();
        hand.setActiveStack(activeStack);

        GameSnapshot initialGameSnap = captureGameState();
        currentGameSnapshot = captureGameState();
        history.push(captureGameState());
    }


    public void drawCards(){
        //if the draw pile is empty, transfer cards from discard to draw
        if (drawPile.isEmpty()) {
            if (wastePile != null && !wastePile.isEmpty()) {
                for (int i = 0; i < wastePile.size(); i++) {
                    Card discardedCard = wastePile.get(i);
                    discardedCard.setFaceUp(false);
                    discardPile.add(discardedCard);
                }
            }
            wastePile = new ArrayList<Card>();
            moveDiscardToDrawPile();
            saveGameState(); // Ensure state is saved after transfer
            return;
        }

        // if the waste pile currently has cards in it, push them to the discard pile and then empty the waste pile
        if (wastePile != null && !wastePile.isEmpty()){
            for (int i = 0; i < wastePile.size(); i++) {
                Card discardedCard = wastePile.get(i);
                discardedCard.setFaceUp(false);
                discardPile.add(discardedCard);
            }
            wastePile = new ArrayList<Card>();
        }

        // number of cards to be drawn is 3
        int numCardsToDraw = 3;

        // if the draw pile has less than 3 cards, then set the number of cars to be drawn to that size
        if (drawPile.size() < 3 && !drawPile.isEmpty()){
            numCardsToDraw = drawPile.size();
        }

        // pop the cards from the draw pile, set it face up, and add them to the waste pile
        for (int i = 0; i < numCardsToDraw; i++){
            Card drawnCard = drawPile.pop();
            drawnCard.setFaceUp(true);
            wastePile.add(drawnCard);
            drawnCard.setPosition(cardDisplayMargin + (int) (cardWidth * 1.5) + (wastePileHorizontalOffset * i), topRowVerticalPosition);
        }

        saveGameState();
    }


//    public void moveCardToDiscardPile(Card card) {
//        discardPile.push(card);
//    }

    public boolean checkWinCondition() {
        for (int i = 0; i < 4; i++) {
            if (foundation[i].size() != 14) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLoseCondition() {
        // Check if there are any valid moves left
        for (int i = 0; i < 7; i++) {
            ArrayList<Card> stack = tableau[i];
            if (!stack.isEmpty()) {
                Card topCard = stack.get(stack.size() -1);
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
        if (foundationTop.getRank() == Rank.NOCARD) {
            return card.getRank() == Rank.ACE;
        } else {
            return card.getRank().ordinal() == foundationTop.getRank().ordinal() + 1 &&
                card.getSuit() == foundationTop.getSuit();
        }
    }

    @Override
    public void update(float dt) {

        if (checkWinCondition()){
            gameIsWon = true;
        }

        if (gameIsWon){
            winAnimation.update(dt);
        }

    }

    @Override
    public void checkIfCardClicked(float xPos, float yPos) {
        int intXPos = (int) xPos;
        int intYPos = (int) yPos;

        //check if the options button was clicked
        if (optionsBounds.contains(intXPos, intYPos)){
            optionsPressed = true;
//            initializeGame();
        }

        //check if undo button was clicked
        if (undoBounds.contains(intXPos, intYPos)){
            undo();
        }

        if (optionsPressed && sfxBounds.contains(intXPos, intYPos)){
            toggleSFX();
        }

        if (optionsPressed && musicBounds.contains(intXPos, intYPos)){
            toggleMusic();
        }

        if (optionsPressed && newGameBounds.contains(intXPos, intYPos)){
            initializeGame();
            optionsPressed = false;
        }

        if (optionsPressed && closeMenuBounds.contains(intXPos, intYPos)){
            optionsPressed = false;
        }

        //check if the exit game button was clicked
        if (optionsPressed && exitGameBounds.contains(intXPos, intYPos)){
            Gdx.app.exit();
        }

        // check if the draw pile was clicked
        if (drawPileBounds.contains(intXPos, intYPos)){
            drawCards();
        }

        // check if waste pile was clicked
        if (wastePile != null && !wastePile.isEmpty()){
            Card topWaste = wastePile.get(wastePile.size()-1);
            if (topWaste.getBounds().contains(intXPos, intYPos)){
                playGrabCard();
                hand.setActiveCard(topWaste, intXPos, intYPos);
                ArrayList<Card> activeStack = new ArrayList<Card>();
                activeStack.add(topWaste);
            }
        }

        // check if a foundation card was clicked
        for (int i = 0; i < 4; i++) {
            Stack<Card> foundationStack = foundation[i];
            Card topFoundation = foundationStack.peek();

            if (topFoundation.getRank() != Rank.NOCARD){
                Rectangle cardBounds = topFoundation.getBounds();
                if(cardBounds.contains(intXPos, intYPos)){
                    playGrabCard();
                    hand.setActiveCard(topFoundation, intXPos, intYPos, i);
                    ArrayList<Card> activeStack = new ArrayList<Card>();
                    activeStack.add(topFoundation);
                }
            }
        }

        // check if a tableau card was clicked
        for (int i = 0; i < 7; i++) {
            ArrayList<Card> tableauStack = tableau[i];
            if (!tableauStack.isEmpty()) {
                int stackSize = tableauStack.size();
                for (int j = stackSize - 1; j >= 0; j--) {
                    Card card = tableauStack.get(j);
                    if (card.isFaceUp() && card.getRank() != Rank.NOCARD) {
                        Rectangle cardBounds = card.getBounds();
                        if (cardBounds.contains(intXPos, intYPos)) {
                            // The click is within the bounds of this card
                            playGrabCard();
                            hand.setActiveCard(card, intXPos, intYPos, i, j);
                            ArrayList<Card> activeStack = new ArrayList<>(tableauStack.subList(j, tableauStack.size()));
                            hand.setActiveStack(activeStack);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean checkIfCardPlaced() {


        for (int i = 0; i <4; i++){
            Stack<Card> foundationStack = foundation[i];
            Card topFoundation = foundationStack.peek();

            if (hand.getActiveCard().getBounds().overlaps(topFoundation.getBounds())){
                if (canCardBePlacedOnFoundation(hand.getActiveCard(), topFoundation)){

                    playPlaceCard();
                    hand.getActiveCard().setPosition(cardDisplayMargin + (cardDisplayOffsets * (i + 3)), topRowVerticalPosition);
                    foundationStack.push(hand.getActiveCard());

                    //if the active card is from the waste pile, remove it from the waste pile before clearing hand so it doesn't get flipped over during the next draw
                    if(hand.getActiveCardType().equals("waste")){
                        wastePile.remove(wastePile.size()-1);
                    }

                    hand.clearActiveHand();

                    if (hand.getActiveCardType().equals("tableau")) {
                        tableau[hand.getActiveStackTableauColumn()] = new ArrayList<>(tableau[hand.getActiveStackTableauColumn()].subList(0, hand.getActiveStackTableauCardNum()));
                        if (hand.getActiveStackTableauCardNum() > 0) {
                            tableau[hand.getActiveStackTableauColumn()].get(hand.getActiveStackTableauCardNum() - 1).setFaceUp(true);
                        }
                    }
                    /// GET SNAPSHOT /////////
                    saveGameState();
                    return true;
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            ArrayList<Card> tableauStack = tableau[i];
            Card tableauBottom = tableauStack.get(tableauStack.size() - 1);
            if (hand.getActiveCard().getBounds().overlaps(tableauBottom.getBounds())) {
                if (checkCardCanBePlaced(tableauBottom, hand.getActiveCard())) {

                    playPlaceCard();
                    //if a foundation card is being placed back in the tableau
                    if (hand.getActiveCardType().equals("foundation")){
                        Card activeCard = hand.getActiveCard();
                        if (tableauBottom.getRank() == Rank.NOCARD){
                            activeCard.setPosition((int) tableauBottom.getPosition().x, (int) (tableauBottom.getPosition().y));
                        } else {
                            activeCard.setPosition((int) tableauBottom.getPosition().x, (int) (tableauBottom.getPosition().y - tableauOffset));}
                        tableau[i].add(activeCard);
                        foundation[hand.getActiveCardFoundationIndex()].pop();
                        hand.clearActiveHand();

                        /// GET SNAPSHOT /////////
                        saveGameState();
                        return true;
                    }

                    //if a waste card is being placed in the tableau
                    if (hand.getActiveCardType().equals("waste")){
                        Card activeCard = hand.getActiveCard();
                        if (tableauBottom.getRank() == Rank.NOCARD){
                            activeCard.setPosition((int) tableauBottom.getPosition().x, (int) (tableauBottom.getPosition().y));
                        } else {
                            activeCard.setPosition((int) tableauBottom.getPosition().x, (int) (tableauBottom.getPosition().y - tableauOffset));}                        tableau[i].add(activeCard);
                        if(hand.getActiveCardType().equals("waste")){
                            wastePile.remove(wastePile.size()-1);
                        }

                        /// GET SNAPSHOT /////////
                        saveGameState();

                        return true;
                    }

                    //else if a tableau card is being placed elsewhere in the tableau
                    else if (hand.getActiveCardType().equals("tableau")) {
                        int offsetCounter = 1;
                        if (tableauBottom.getRank() == Rank.NOCARD){
                            offsetCounter = 0;
                        }
                        for (Card card : hand.getActiveStack()) {
                            if (card.getRank() != Rank.NOCARD) {
                                card.setPosition((int) (tableauBottom.getPosition().x), (int) (tableauBottom.getPosition().y) - tableauOffset * offsetCounter);
                                tableau[i].add(card);
                                offsetCounter++;
                            }
                        }

                        tableau[hand.getActiveStackTableauColumn()] = new ArrayList<>(tableau[hand.getActiveStackTableauColumn()].subList(0, hand.getActiveStackTableauCardNum()));
                        if (hand.getActiveStackTableauCardNum() > 0) {
                            tableau[hand.getActiveStackTableauColumn()].get(hand.getActiveStackTableauCardNum() - 1).setFaceUp(true);
                        }

                        /// GET SNAPSHOT /////////
                        saveGameState();

                        return true;}
                }
            }
        }

        return false;
    }


    public boolean checkCardCanBePlaced(Card targetCard, Card activeCard) {
        Rank targetRank = targetCard.getRank();
        Suit targetSuit = targetCard.getSuit();
        Rank activeCardRank = activeCard.getRank();
        Suit activeCardSuit = activeCard.getSuit();

        //check if King placed on empty tableau stack
        if (activeCardRank == Rank.KING && targetRank == Rank.NOCARD){
            return true;
        }

        // Check if the target card rank is one more than the active card rank
        if (targetRank.ordinal() == activeCardRank.ordinal() + 1) {
            // Check for alternate colors (red and black)
            if ((activeCardSuit == Suit.HEARTS || activeCardSuit == Suit.DIAMONDS) &&
                (targetSuit == Suit.CLUBS || targetSuit == Suit.SPADES)) {
                return true;
            } else if ((activeCardSuit == Suit.CLUBS || activeCardSuit == Suit.SPADES) &&
                (targetSuit == Suit.HEARTS || targetSuit == Suit.DIAMONDS)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Hand getHand(){ return hand;}

    @Override
    public Camera getCam(){return cam;}

    @Override
    public int getTableauOffset() {
        return tableauOffset;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();
    }

    private void moveDiscardToDrawPile() {

        // Initialize an auxiliary stack
        Stack<Card> auxiliaryStack = new Stack<>();

        // Transfer all elements to the auxiliary stack (reverse order)
        while (!discardPile.isEmpty()) {
            drawPile.push(discardPile.remove(discardPile.size()-1));
        }

//        // Transfer all elements from the auxiliary stack to the final stack (preserve order)
//        while (!auxiliaryStack.isEmpty()) {
//            drawPile.push(auxiliaryStack.pop());
//        }


    }

    private void toggleMusic(){
        if (musicIsOn){
            musicIsOn = false;
            musicActiveButton = musicOffButton;
            music.pause();
        } else {
            musicIsOn = true;
            musicActiveButton = musicOnButton;
            music.play();
        }
    }

    private void toggleSFX(){
        if (sfxIsOn){
            sfxIsOn = false;
            sfxActiveButton = sfxOffButton;
        } else {
            sfxIsOn = true;
            sfxActiveButton = sfxOnButton;
        }
    }

    public void playGrabCard(){
        if (sfxIsOn){
            grabCard.play();
        }
    }

    public void playPlaceCard(){
        if (sfxIsOn){
            placeCard.play();
        }
    }

    @Override
    public boolean getOptionsPressed(){return optionsPressed;}

    @Override
    public void checkButtonHovered(float xPos, float yPos){
        int intXPos = (int) xPos;
        int intYPos = (int) yPos;

        if (newGameBounds.contains(intXPos, intYPos)){
            newGameButtonActive = newGameButtonHover;
        } else {
            newGameButtonActive = newGameButton;
        }

        if (mainMenuBounds.contains(intXPos, intYPos)){
            mainMenuButtonActive = mainMenuButtonHover;
        } else {
            mainMenuButtonActive = mainMenuButton;
        }

        if (exitGameBounds.contains(intXPos, intYPos)){
            exitGameButtonActive = exitGameButtonHover;
        } else {
            exitGameButtonActive = exitGameButton;
        }
    }


    @Override
    public void render(SpriteBatch sb) {

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //setting the projection matrix I think because we resized the camera view
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        //draw the background
        sb.draw(background, 0, 0, 4400, 2000);


        //draw the options button
        sb.draw(optionsButton, 5, 5, 186, 53);

        //draw the undo button
        sb.draw(undoButton, 210, 5, 55, 55);

//        sb.draw(exitGameButton, 1400, 5, 186, 53);

        //if the draw pile is not empty, draw a card back, otherwise draw the no card border
        if (!drawPile.isEmpty()){ sb.draw(cardBack, cardDisplayMargin, topRowVerticalPosition, cardWidth, cardHeight);}
        else {sb.draw(noCard, cardDisplayMargin, topRowVerticalPosition, cardWidth, cardHeight);}

        // draw the waste pile
        if (wastePile != null && !wastePile.isEmpty()){

//            int wastePileHorizontalOffset = (int) (cardWidth*.15);

            for (Card card: wastePile){
                sb.draw(card.getTexture(), card.getPosition().x, card.getPosition().y, cardWidth, cardHeight);
//                wastePileHorizontalOffset += wastePileHorizontalOffset;
            }
        }

        ////////////////////////////
        // draw the foundation

        for (int i =0; i <4; i++){
            Card topFoundation = foundation[i].get(foundation[i].size() -1);
            sb.draw(topFoundation.getTexture(), topFoundation.getPosition().x, topFoundation.getPosition().y, topFoundation.getCardWidth(), topFoundation.getCardHeight() );
        }

        ////////////////////////////
        //draw the tableau
        // for each column in the tableau
        for (int i =0; i < 7; i++){

            ArrayList<Card> tableauStack = tableau[i];

            int currentCardVertOffset = 0;

            if (!tableauStack.isEmpty()){

                for (Card card: tableauStack){
                    if (card.equals(hand.getActiveCard())){continue;}
                    sb.draw(card.getTexture(), card.getPosition().x , card.getPosition().y, card.getCardWidth(), card.getCardHeight());
                }
            }
        }



        if (hand.getActiveStack() != null && !hand.getActiveStack().isEmpty()){
            for (Card card: getHand().getActiveStack()){
                sb.draw(card.getTexture(), card.getPosition().x , card.getPosition().y, card.getCardWidth(), card.getCardHeight());
            }
        }



//        cardDisplayMargin = 40;


        if (hand.getActiveStack() != null && hand.getActiveStack().isEmpty()){
            if (hand.getActiveCard() != null && hand.checkHasActiveCard()){
                Card card = hand.getActiveCard();
                sb.draw(card.getTexture(), card.getPosition().x , card.getPosition().y, card.getCardWidth(), card.getCardHeight());
            }
        }

        if (optionsPressed) {
            sb.draw(menu1, menuDrawX, menuDrawY, menuDrawWidth, menuDrawHeight);
            sb. draw(sfxActiveButton, menuDrawX + sfxDrawXOffset, menuDrawY + sfxDrawYOffset, menuSmallButtonWidth, menuSmallButtonHeight);
            sb.draw(musicActiveButton, menuDrawX + musicDrawXOffset, menuDrawY + musicDrawYOffset, menuSmallButtonWidth, menuSmallButtonHeight);
            sb.draw(closeMenuButton, menuDrawX + closeMenuXOffset, menuDrawY + closeMenuYOffset, menuSmallButtonWidth, menuSmallButtonHeight);
            sb.draw(newGameButtonActive, menuDrawX + newGameXOffset, menuDrawY + newGameYOffset, newGameWidth, newGameHeight);
            sb.draw(mainMenuButtonActive, menuDrawX + mainMenuXOffset, menuDrawY + mainMenuYOffset, mainMenuWidth, mainMenuHeight);
            sb.draw(exitGameButtonActive, menuDrawX + exitGameXOffset, menuDrawY + exitGameYOffset, exitGameWidth, exitGameHeight);
        }

        if (gameIsWon){
            Gdx.graphics.requestRendering();
            sb.draw(winAnimation.getFrame(), 500, 150, 600, 600);
        }

        //draw the hand
        sb.draw(hand.getTexture(), hand.getPosition().x, hand.getPosition().y, hand.getWidth(), hand.getHeight());

        sb.end();

    }

}
