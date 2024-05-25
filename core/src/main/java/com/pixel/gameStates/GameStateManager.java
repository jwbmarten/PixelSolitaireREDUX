package com.pixel.gameStates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;

    /**
     * CONSTRUCTOR
     */
    public GameStateManager(){
        //a stack is a data structure type that has unique methods like push and pop and peek, etc
        states = new Stack<State>();
    }


    public void push(State state){

        states.push(state);
    }

    public void pop(){

        //removes the state from the stack, dispose is good memory practice
        states.pop().dispose();
    }

    public void set(State state){

        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){

        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){

        states.peek().render(sb);
    }

}

