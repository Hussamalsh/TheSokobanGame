package com.example.hussam9.thesokobangame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Hussam on 6/10/2016.
 */
public class SokoSquare extends View {
    public static final int ImgSize = 32;  //The game imageicon size is 32x*32px
    protected int c;       // the column of the square (its can be player,crate or a blank)
    protected int r;       // the row of the square (its can be player,crate or a blank)
    //private ImageIcon img; // the imageicon of a square (its can be player,crate or a blank)
    protected Bitmap img;
    protected Context mContext;

    SurfaceHolder surfaceHolder;

    public SokoSquare(int row, int Col, Context mC)   //x y needed
    {
        super(mC);
        this.r = row;
        this.c = Col;
        this.mContext = mC;

    }


    public void translate(int nr, int nc) {
        setC(nc);
        setR(nr);
    }

    /**
     * Draws the map.
     * This void method is called everytime the player make an events by the Board
     * to draw and update the map that can be seen in the GUI
     *
     * @param canvas - the graphics context
     */
    private float x;
    private float y;
    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        Log.d("Canvas :: ", "draw: "  + (canvas.getWidth()/12.4f) + " H " +(canvas.getWidth()) );
        x = getC() * (canvas.getWidth()/12.4f);
        y = getR() * (canvas.getWidth()/12.4f);
        //canvas.drawBitmap(getImg(), getC() * (ImgSize * 4), getR() * (ImgSize * 4), null);  //84
        canvas.drawBitmap(getImg(),x ,y , null);
        //getImg().paintIcon(null, g2d, getC()*ImgSize, getR()*ImgSize);
    }

    /**
     * Mutator method.
     *
     * @param col - set a new column to the square
     */
    public void setC(int col) {
        this.c = col;
    }

    /**
     * Mutator method.
     *
     * @param row - set a new row to the square
     */
    public void setR(int row) {

        this.r = row;
    }

    /**
     * Accessor method.
     *
     * @return - get column of the square
     */
    public int getC() {
        return this.c;
    }

    /**
     * Accessor method.
     *
     * @return - get row of the square
     */
    public int getR() {
        return this.r;
    }


    /**
     * Mutator method.
     *
     * @param rs - set a new row to the square
     */
    public void setImg(Resources rs, int dr) {
        //this.img = decodeSampledBitmapFromResource(getResources(), dr, 32, 32);

        this.img = BitmapFactory.decodeResource(getResources(), dr);
    }

    /**
     * Accessor method.
     * Gets the image of square
     *
     * @return - image tha squre have
     */
    public Bitmap getImg() {

        return this.img;
    }

    /*public int getCalcX()
    {
        return x;
    }

    public int getCalcY(){
        return  y;
    }*/



}