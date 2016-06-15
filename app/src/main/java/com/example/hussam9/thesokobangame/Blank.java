package com.example.hussam9.thesokobangame;

import android.content.Context;

/**
 * Created by Hussam on 6/11/2016.
 */
public class Blank extends SokoSquare
{
    private boolean marked;     // indicate if a Blank is marked or not marked

    /**
     * The default constructor.
     * Initializes imgicon variable of the Blank. It set the row and the column of the Blank.
     * @param nr - the row of the Blank position
     * @param nc - the column of the Blank position
     * @param marked - a boolean variable to set the Blank marked(true) or not(false)
     */
    public Blank(int nr, int nc, Context mC ,boolean marked)
    {
        super(nr, nc,mC);
        this.marked = marked;

       // URL path;
        if(marked){
            this.setImg(getResources(), R.drawable.blankmarked);
        }else
        {
            this.setImg(getResources(), R.drawable.blank);
        }

    }

    /**
     * Accessor method.
     * @return - if the blank is marked or not
     */
    public boolean getMarked(){
        return marked;
    }

    /**
     * Mutator method.
     * @param n - set the blank marked or not marked
     */
    public void setMarked(Boolean n){
        this.marked = n;
    }

}

