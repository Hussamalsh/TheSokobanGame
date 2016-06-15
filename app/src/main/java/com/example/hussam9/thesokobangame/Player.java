package com.example.hussam9.thesokobangame;


import android.content.Context;

/**
 * Created by Hussam on 6/10/2016.
 */
public class Player extends SokoSquare
{

    /**
     * The default constructor.
     * Initializes imgicon variable of the player. It set the row and the column of the player.
     * @param r - the row of the player position
     * @param c - the column of the player position
     */
    public Player(int r, int c, Context mC)
    {
        super(r, c, mC);
        this.setImg(getResources(), R.drawable.player);
    }
}
