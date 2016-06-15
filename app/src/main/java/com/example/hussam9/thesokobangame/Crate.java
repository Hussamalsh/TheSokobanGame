package com.example.hussam9.thesokobangame;

import android.content.Context;

/**
 * Created by Hussam on 6/10/2016.
 */
public class Crate extends SokoSquare
{
    private boolean marked;  // indicate if a crate is marked or not marked

    /**
     * The default constructor.
     * Initializes imgicon variable of the Crate. It set the row and the column of the Crate.
     * @param nr - the row of the Crate position
     * @param nc - the column of the Crate position
     * @param marked - a boolean variable to set the crate marked(true) or not(false)
     */
    public Crate(int nr, int nc, Context mC , boolean marked) {
        super(nr, nc, mC);
        this.marked = marked;
        //URL path;
        if(marked){
            this.setImg(getResources(), R.drawable.cratemarked);
        }else
        {
            this.setImg(getResources(), R.drawable.crate);
        }

    }

    /**
     * Accessor method.
     * @return - if the crate is marked or not
     */
    public boolean getMarked(){
        return marked;
    }

    /**
     * Mutator method.
     * @param n - set the crate marked or not marked
     */
    public void setMarked(Boolean n){
        this.marked = n;
    }

}
