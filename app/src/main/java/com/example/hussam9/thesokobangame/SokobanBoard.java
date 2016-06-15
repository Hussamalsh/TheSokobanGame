package com.example.hussam9.thesokobangame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hussam on 6/10/2016.
 */
public class SokobanBoard extends View
{
    String  DEBUG_TAG  = "hussi ";

    private Context mContext;
    private Handler hr;
    private final int FRAME_RATE = 30;

    private Player soko;                       // the sokoban player object
    private Blank bm;    // represent a marked blank
    private Blank bnm;  // represent unmarked blanked
    private Crate cm;    // represent a marked crate
    private Crate cnm;  // represent a unmarked crate

    //An array that have the postion of the squares of the board
    private SokoSquare[][] map = new SokoSquare[10][10];


    boolean blankmark = false;  //this variable indicate if the player moved on a markedblank




    private boolean win = false;  		 //the variable indicate if there is a winner
    private int wincounter;	       	   	// Count how many marked blank must be filled with crates
    private int movecount;        		 // count how many times the player moved in the game
    private int currentlvl=0;            // count the level of the sokoban game

    TextView moves;


    private Point drag_start;
    private Point drag_stop;

    private ArrayList<String> mapList;   // MapList store all the symbols that represent a level

    private String level =
                      "    ######\n"
                    + "    ##   #\n"
                    + "    ##$  #\n"
                    + "  ####  $##\n"
                    + "  ##  $ $ #\n"
                    + "#### # ## #   ######\n"
                    + "##   # ## #####  ..#\n"
                    + "## $  $          ..#\n"
                    + "###### ### #@##  ..#\n"
                    + "    ##     #########\n"
                    + "    ########\n";

    private String level2 = "   #####  "
                          + " ###   #  "
                          + " #-$x  #  "
                          + " ### x-#  "
                          + " #-##x #  "
                          + " # # - ## "
                          + " #x vxx-# "
                          + " #   -  # "
                          + " ######## ";






    ArrayList world;

    SurfaceHolder surfaceHolder;


    public SokobanBoard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        hr = new Handler();

        drag_start = new Point();
        drag_stop = new Point();


        bm = new Blank(0,0,mContext,true);    // represent a marked blank
        bnm = new Blank(0,0,mContext,false);  // represent unmarked blanked
        cm = new Crate(0,0,mContext,true);    // represent a marked crate
        cnm = new Crate(0,0,mContext,false);  // represent a unmarked crate

        movecount=0;

        moves = (TextView) findViewById(R.id.moves_counter_txt);

        initWorld();

    }

    public SokobanBoard(Context context)
    {
        super(context);
        mContext = context;
        hr = new Handler();

        drag_start = new Point();
        drag_stop = new Point();


        bm = new Blank(0,0,mContext,true);    // represent a marked blank
        bnm = new Blank(0,0,mContext,false);  // represent unmarked blanked
        cm = new Crate(0,0,mContext,true);    // represent a marked crate
        cnm = new Crate(0,0,mContext,false);  // represent a unmarked crate

        movecount=0;

        moves = (TextView) findViewById(R.id.moves_counter_txt);

        initWorld();
    }




    protected void initWorld() {

        mapList = new ArrayList<>();
        String temp;
        temp = "";

        Log.d(DEBUG_TAG,"temp= " + temp);

        mapList.add(level2);


        int i = -1;
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 10; col++) {
                i++;
                Log.d(DEBUG_TAG,"mapList = " + mapList.get(0).substring(i, i + 1)  + " R = " + row + " C = " + col);
                if (mapList.get(0).substring(i, i + 1).equals(" ")) { //Blank
                    map[row][col] = new Blank(row, col,mContext, false);
                } else if (mapList.get(0).substring(i, i + 1).equals("-")) { //Blankmarked
                    map[row][col] = new Blank(row, col,mContext, true);
                   // addMarkedBlank(map[row][col]);//add wincounter
                } else if (mapList.get(0).substring(i, i + 1).equals("x")) { //crate
                    map[row][col] = new Crate(row, col,mContext, false);
                } else if (mapList.get(0).substring(i, i + 1).equals("v")) { //cratemarked
                    map[row][col] = new Crate(row, col,mContext, true);
                    //addMarkedBlank(map[row][col]);//add wincounter
                } else if (mapList.get(0).substring(i, i + 1).equals("#")) { //Wall
                    map[row][col] = new Wall(row, col,mContext);
                } else if (mapList.get(0).substring(i, i + 1).equals("$")) { //Player
                    map[row][col] = (SokoSquare) (soko = new Player(row, col,mContext));
                }
            }

    }

    public void buildWorld(Canvas canvas) {
        for(int r=0;r<10;r++)
        {
            for(int c=0;c<10;c++)
            {
                SokoSquare tile = map[r][c];
                if(tile != null)
                    tile.draw(canvas);
            }
        }
    }

    Canvas can;
    @Override
    protected void onDraw(Canvas canvas)
    {
        can= canvas;


        //canvas.rotate(30, getWidth()/2, getHeight()/2);
        //canvas.scale(0.7f, 0.7f, getWidth()/2, getHeight()/2);

        buildWorld(canvas);



        //hr.postDelayed(r, FRAME_RATE);

    }

   /* private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };*/



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drag_start.set((int) event.getX(), (int) event.getY());
                //Log.d(DEBUG_TAG,"Action was DOWN");
                break;
            case MotionEvent.ACTION_UP:
                drag_stop.set((int) event.getX(), (int) event.getY());
               // Log.d(DEBUG_TAG,"Action was UP");
                touchMove();
                break;
        }
        return true;
    }





    protected void touchMove()
    {
        /*int rm= (int)drag_start.y/(SokoSquare.ImgSize*4);
        int cm = (int) drag_start.x/(SokoSquare.ImgSize*4);*/

        int rm= (int)drag_start.y/(int)(can.getWidth()/12.4f);
        int cm = (int) drag_start.x/(int)(can.getWidth()/12.4f);



        Log.d(DEBUG_TAG,"Finger rm " + rm          +  "  cm=        " + cm );
        Log.d(DEBUG_TAG,"Soko row  " + soko.getR() +  "  soko Colm= " + soko.getC());

        if ((rm > soko.getR()) && cm == soko.getC())
        {
            downKeyButton();
        }

        if ((rm < soko.getR()) && cm == soko.getC())
        {
            upKeyButton();
        }

        if ((cm > soko.getC()) && rm == soko.getR())
        {
            rightKeyButton();
        }

        if ((cm < soko.getC()) && rm == soko.getR())
        {
            leftKeyButton();
        }

        invalidate();


        //moves.setText(String.valueOf(movecount));
       // moves.setText(Integer.toString(R.string.Moves_counter));
        // moves.append(String.valueOf(movecount));

    }

    /**
     * Wall Collision
     * This method check if the player collides with the wall.
     * @param obj - a square object to see if we have collision with a wall
     * @return - if the obj is instance of specific class (the wall class).
     */
    public boolean wallCollision(SokoSquare obj)
    {
        return obj instanceof Wall;
    }

    /**
     * Marked Blank collision
     * This method check if the player have a collision with a marked blank
     * @param obj - a square object to see if we have collision with a marked Blank
     * @param cs  - the number of the column in the map[][] with a sign.
     * 			  - the sign is + if the soko move in positiv x direction
     * 			  - the sign is - if the soko move in negativ x direction
     * @param rsign - the number of the row in the map[][] with a sign.
     * 			 	 - the sign is + if the soko move in positiv y direction
     * 			 	 - the sign is - if the soko move in negativ y direction
     * @return - if the obj is instance of specific class (marked Blank class).
     */
    public boolean blankMCollision(SokoSquare obj,int cs,int rsign)
    {
        if(map[soko.getR()-rsign][soko.getC()-cs] instanceof Blank &&((Blank) obj).getMarked())
        {

            if(!blankmark)
            {
                //obj.setImg(bnm.getImg());
                obj.setImg(getResources(),R.drawable.blank); //bnm
                ((Blank)map[obj.getR()][obj.getC()]).setMarked(false);
            }
            SokoSquare tempPlayer = map[obj.getR()+rsign][obj.getC()+cs];
            map[obj.getR()+rsign][obj.getC()+cs] = obj;  //move blank*/
            map[obj.getR()][obj.getC()] = tempPlayer;
            soko.translate(obj.getR(), obj.getC());
            map[soko.getR()][soko.getC()].translate(soko.getR(), soko.getC()); //move playe
            map[soko.getR()+rsign][soko.getC()+cs].translate(soko.getR()+rsign, (soko.getC()+cs) );  //move blank
            blankmark = true;
            movecount++;
            //sokosound.playSound("snow_walk");

            return true;
        }
        return false;
    }

    /**
     * Blank collision
     * This method check if the player have a collision with a blank (Not marked)
     * @param obj - a square object to see if we have collision with a Blank
     * @param cs  - the number of the column in the map[][] with a sign.
     * 			  - the sign is + if the soko move in positive x direction
     * 			  - the sign is - if the soko move in negative x direction
     * @param tsign - the number of the row in the map[][] with a sign.
     * 			 	 - the sign is + if the soko move in positive y direction
     * 			 	 - the sign is - if the soko move in negative y direction
     * @return - if the obj is instance of specific class (Blank class).
     */
    public boolean blankCollision(SokoSquare obj,int cs, int tsign)
    {
        if(map[soko.getR()-tsign][soko.getC()-cs] instanceof Blank && !((Blank) obj).getMarked())
        {

            if(blankmark){
                //obj.setImg(bm.getImg());
                obj.setImg(getResources(),R.drawable.blankmarked); //bm
                ((Blank)obj).setMarked(true);
                blankmark = false;
            }
            SokoSquare tempPlayer = map[obj.getR()+tsign][obj.getC()+cs];
            map[obj.getR()+tsign][obj.getC()+cs] = obj;  //move blank*/
            map[obj.getR()][obj.getC()] = tempPlayer;
            soko.translate(obj.getR(), obj.getC());
            map[soko.getR()][soko.getC()].translate(soko.getR(), soko.getC()); //move playe
            map[soko.getR()+tsign][soko.getC()+cs].translate(soko.getR()+tsign, (soko.getC()+cs) );  //move blank
            movecount++;
            //sokosound.playSound("snow_walk");
            return true;
        }
        return false;
    }

    /**
     *  Crate collision
     * This method check if the player have a collision with a Crate
     * @param obj - a square object to see if we have collision with a Crate
     * @param cs  - the number of the column in the map[][] with a sign.
     * 			  - the sign is + if the soko move in positive x direction
     * 			  - the sign is - if the soko move in negative x direction
     * @param tsign - the number of the row in the map[][] with a sign.
     * 			 	 - the sign is + if the soko move in positive y direction
     * 			 	 - the sign is - if the soko move in negative y direction
     * @return - if the obj is instance of specific class (Crate class).
     */
    public boolean crateCollision(SokoSquare obj,int cs,int tsign)
    {
        if(obj instanceof Crate)
        {
            if(map[soko.getR()-2*tsign][soko.getC()-2*cs] instanceof Wall || map[soko.getR()-2*tsign][soko.getC()-2*cs] instanceof Crate){
                return false;
            }
            boolean markCrateOnMove = ((Blank)map[soko.getR()-2*tsign][soko.getC()-2*cs]).getMarked();
            if(blankmark){
                map[obj.getR()-tsign][obj.getC()-cs].setImg(getResources(),R.drawable.blankmarked);   //img = blankmarked
                ((Blank)map[obj.getR()-tsign][obj.getC()-cs]).setMarked(true);
            }
            else{
                map[obj.getR()-tsign][obj.getC()-cs].setImg(getResources(),R.drawable.blank);  //blanknotmarked
                ((Blank)map[obj.getR()-tsign][obj.getC()-cs]).setMarked(false);
            }
            blankmark = false;
            SokoSquare tempblank = map[obj.getR()-tsign][obj.getC()-cs];
            if(((Crate)obj).getMarked()){
                blankmark = true;
            }

            map[soko.getR()][soko.getC()] = tempblank;  //move blank*/
            tempblank.translate(soko.getR(), soko.getC());

            map[soko.getR()-2*tsign][soko.getC()-2*cs] = obj;  //move blank*/
            obj.translate(soko.getR()-2*tsign, soko.getC()-2*cs);

            map[soko.getR()-tsign][soko.getC()-cs] = soko;
            soko.translate(soko.getR()-tsign, soko.getC()-cs);

            if(markCrateOnMove){
                ((Crate)map[soko.getR()-tsign][soko.getC()-cs]).setMarked(true);
                ((Crate)map[soko.getR()-tsign][soko.getC()-cs]).setImg(getResources(),R.drawable.cratemarked);  //cm
                //sokosound.playSound("magic");
            }
            else{
                ((Crate)map[soko.getR()-tsign][soko.getC()-cs]).setMarked(false);
                ((Crate)map[soko.getR()-tsign][soko.getC()-cs]).setImg(getResources(),R.drawable.crate); //cnm
                //sokosound.playSound("push");
            }
            /*if(checkWinner()){
                //getTimer().stop();
                sokosound.playSound("magic");
            }*/

            movecount++;

            return true;
        }

        return false;
    }

    /**
     * Right KeyButton
     * Void method - this method Invoke if the player pressed the right key in the keyboard.
     * The method will translate the player to the right of the map (in positive x direction)
     * if its not collide with a two crates beside each other or with a wall.
     */
    protected void rightKeyButton()
    {
        SokoSquare obj =  map[soko.getR()][soko.getC()+1];

        if(wallCollision(obj))
        {
            return;
        }
        if(crateCollision(obj,-1,0))
        {
            return;
        }

        if(blankMCollision(obj,-1,0))
        {
            return;
        }

        if(blankCollision(obj,-1,0))
        {
            return;
        }
    }

    /**
     * Left KeyButton
     * Void method - this method Invoke if the player pressed the left key in the keyboard.
     * The method will translate the player to the left of the map (in negative x direction)
     * if its not collide with a two crates beside each other or with a wall.
     */
    protected void leftKeyButton()
    {
        SokoSquare obj =  map[soko.getR()][soko.getC()-1];

        if(wallCollision(obj))
        {
            return;
        }
        if(crateCollision(obj,1,0))
        {
            return;
        }

        if(blankMCollision(obj,1,0))
        {
            return;
        }

        if(blankCollision(obj,1,0))
        {
            return;
        }
    }

    /**
     * Up KeyButton
     * Void method - this method Invoke if the player pressed the up key in the keyboard.
     * The method will translate the player upward of the map (in negative y direction)
     * if its not collide with a two crates beside each other or with a wall.
     */
    protected void upKeyButton()
    {
        SokoSquare obj =  map[soko.getR()-1][soko.getC()];

        if(wallCollision(obj))
        {
            return;
        }
        if(crateCollision(obj,0,1))
        {
            return;
        }

        if(blankMCollision(obj,0,1))
        {
            return;
        }

        if(blankCollision(obj,0,1))
        {
            return;
        }
    }

    /**
     * Down KeyButton
     * Void method - this method Invoke if the player pressed the down key in the keyboard.
     * The method will translate the player downward of the map (in positive y direction)
     * if its not collide with a two crates beside each other or with a wall.
     */
    protected void downKeyButton()
    {
        SokoSquare obj =  map[soko.getR()+1][soko.getC()];

        if(wallCollision(obj))
        {
            return;
        }
        if(crateCollision(obj,0,-1))
        {
            return;
        }

        if(blankMCollision(obj,0,-1))
        {
            return;
        }

        if(blankCollision(obj,0,-1))
        {
            return;
        }
    }





//trashhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh



        /*protected void touchMove() {
       Log.d(DEBUG_TAG,"newwwwwwwwww test       ");
        /* int delta_x = drag_stop.x - drag_start.x;
        Log.d(DEBUG_TAG,"deltax      "  + drag_stop.x/32 + " - " + drag_start.x/32);
        int delta_y = drag_stop.y - drag_start.y;

        int rm= (int)delta_y;
        Log.d(DEBUG_TAG,"deltaY        "  + rm);
        int cm = (int) delta_x;
        Log.d(DEBUG_TAG,"deltaX        "  + cm);*/


        /*if (Math.abs(delta_x) < 10 && Math.abs(delta_y) < 10)
        {
            Log.d(DEBUG_TAG,"Not enough " + Math.abs(delta_x) );
            return; // not enough of a move
        }*/
        /*if(Math.abs(delta_x) > Math.abs(delta_y)) { // x move
            if(delta_x < 0) {
                Log.d(DEBUG_TAG,"Action was left-");
                doMove(2);  //left
            } else {
                Log.d(DEBUG_TAG,"Action was right-");
                doMove(3);  //right east
            }
        } else { // y move
            if(delta_y < 0) {
                doMove(0); //upp
                Log.d(DEBUG_TAG,"Action was UP-");
            } else {
                Log.d(DEBUG_TAG,"Action was down-");
                doMove(1); //down
            }
        }*/


        /* int rm= (int)drag_start.y/84;
         //rm= rm -(soko.getR()*2);
        Log.d(DEBUG_TAG,"my Test rm " + rm    +  "  soko Row= " + soko.getR());
         int cm = (int) drag_start.x/84;
        //cm= cm -(soko.getR()*2);
        Log.d(DEBUG_TAG,"my Test cm " + cm +     "  soko Colm= " + soko.getC());

        if ((cm > soko.getC()) && rm == soko.getR())
        {
            // downKeyButton();
            Log.d(DEBUG_TAG,"my Test rigt lonly ");

          map[soko.getR()][soko.getC()] =  map[soko.getR()][soko.getC()+1] ;
            map[soko.getR()][soko.getC()].translate(soko.getR(),soko.getC());

            soko.translate(soko.getR(),soko.getC()+1);
            map[soko.getR()][soko.getC()] = soko;
        }

        if ((cm < soko.getC()) && rm == soko.getR())
        {
            // downKeyButton();
            Log.d(DEBUG_TAG,"my Test left lonly " + (cm - soko.getC()) );

            map[soko.getR()][soko.getC()] =  map[soko.getR()][soko.getC()-1] ;
            map[soko.getR()][soko.getC()].translate(soko.getR(),soko.getC());

            soko.translate(soko.getR(),soko.getC()-1);
            map[soko.getR()][soko.getC()] = soko;
        }


        if ((rm > soko.getR()) && cm == soko.getC() )   // every sauare take 3
        {
            // downKeyButton();
            Log.d(DEBUG_TAG,"my Test down lonly " + (cm - soko.getC()) );

           map[soko.getR()][soko.getC()] =  map[soko.getR()+1][soko.getC()] ;
            map[soko.getR()][soko.getC()].translate(soko.getR(),soko.getC());

            soko.translate(soko.getR()+1,soko.getC());
            map[soko.getR()][soko.getC()] = soko;
        }


        if ((rm < soko.getR()) && cm == soko.getC() )   // every sauare take 3
        {
            // downKeyButton();
            Log.d(DEBUG_TAG,"my Test up lonly " + (cm - soko.getC()) );

           map[soko.getR()][soko.getC()] =  map[soko.getR()-1][soko.getC()] ;
            map[soko.getR()][soko.getC()].translate(soko.getR(),soko.getC());

            soko.translate(soko.getR()-1,soko.getC());
            map[soko.getR()][soko.getC()] = soko;
        }


       /* if ((rm > soko.getR()) && cm == soko.getC())
        {
           // downKeyButton();
            Log.d(DEBUG_TAG,"my Test Down");

        }

        if ((rm < soko.getR()) && cm == soko.getC())
        {
            //upKeyButton();
            Log.d(DEBUG_TAG,"my Test up");
        }

        if ((cm > soko.getC()) && rm == soko.getR())
        {
            //rightKeyButton();
            Log.d(DEBUG_TAG,"my Test Right");
        }

        if ((cm < soko.getC()) && rm == soko.getR())
        {
            //leftKeyButton();
            Log.d(DEBUG_TAG,"my Test left");
        }

        invalidate();


    }*/



   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Rect invalid;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.d(DEBUG_TAG,"Action was UP  in onkeydown");
                doMove(0);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                doMove(1);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                doMove(3);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                doMove(2);
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }
*/





}





