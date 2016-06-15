package com.example.hussam9.thesokobangame;

import android.content.Context;

/**
 * Created by Hussam on 6/10/2016.
 */
public class Wall extends SokoSquare{

    /**
     * The default constructor.
     * Initializes imgicon variable of the Wall object. It set the row and the column of
     * the Wall.
     * @param nr - the row of the player position
     * @param nc - the column of the player position
     */
    public Wall(int nr, int nc, Context mC) {
        super(nr, nc, mC);
        this.setImg(getResources(), R.drawable.wall);
    }

}
