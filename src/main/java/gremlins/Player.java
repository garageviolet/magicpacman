package gremlins;

import java.util.ArrayList;
import processing.core.PImage;

public class Player {
    protected int x;
    protected int y;

    protected int direction = 0;
    protected int lives = 0;

    private int xVel = 0;
    private int yVel = 0;

    private int speed = 2;


    public static final int HEIGHT = 20;
    public static final int WIDTH = 20;

    private PImage sprite0;
    private PImage sprite1;
    private PImage sprite2;
    private PImage sprite3;
    protected ArrayList<MyCoord> brick_cords;
    protected ArrayList<MyCoord> stone_cords;

    //change sprite for differnet direction 
    public Player(int x, int y, PImage sprite0, PImage sprite1, PImage sprite2, PImage sprite3, int lives, ArrayList<MyCoord> brick_cords, ArrayList<MyCoord> stone_cords) {
        this.x = x;
        this.y = y;
        this.sprite0 = sprite0;
        this.sprite1 = sprite1;
        this.sprite2 = sprite2;
        this.sprite3 = sprite3;
        this.brick_cords = brick_cords;
        this.stone_cords = stone_cords;

        

        //dynamic lives
        this.lives = lives;
        
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getxVel() {
        return xVel;
    }

    public int getyVel() {
        return yVel;
    }

    /**
     * Player go left 
     */
    public void left() {
        this.xVel = -speed;
    }

    /**
     * Player go right
     */
    public void right() {
        this.xVel = speed;
    }

    /**
     * Player go up
     */
    public void up() {
        this.yVel = -speed;
    }

    /**
     * Player go down
     */
    public void down() {
        this.yVel = speed; 
    }

    /**
     * Player stops
     */
    public void stop() {
        this.xVel = 0;
        this.yVel = 0;
    }
    
    /**
     * Speed up for power up 
     */    
    public void speedUp(){

        speed = 4;
    }

    /**
     * Set player to normal speed
     */    
    public void normalSpeed(){
        speed = 2;
    }
    

    /**
     * Set player to normal speed
     * @return Whether two coords collides
     */    
    public boolean collides(int xCoord, int yCoord, ArrayList<MyCoord> po) {
        for (MyCoord co: po){

            if (xCoord > co.getX()-20 && xCoord < co.getX()+20 && yCoord > co.getY()-20 && yCoord < co.getY()+20){
                //System.out.println(co.getX());
                //if the tag is true then the wall indeed exist 

                return true;
            }
        }
        //else no collision no wall exist
        return false;
    }

    /**
     * Updates the player every frame.
     */
    public void draw(App app) {
        //real time lives load and draw
        //UI display lives remaining
        //smooth wizard movement fit in tile of 20

        int i = lives;
        while (i > 0){
            //display for ui how many left at bottom left corner
            app.image(sprite1, 50+WIDTH*i, 675);
            i -= 1;
        }
       
        //bounds for each tile of brickwall and stonewall
        // if x %20 != 0 - x + 1
        if (collides(x+xVel,y+yVel, brick_cords)){
            this.stop();
        }
        if (collides(x+xVel,y+yVel, stone_cords)){
            this.stop();
        }
        //bounds for bottom bound
        if (y+yVel > app.bottomBound) {
            this.stop();
        }

        // if player go up load image with up direction 
        //image for different direction of the player
        //direction 0,1,2,3 right,left,down,up
        //going right
        //ONLY FOR IMAGE
        if (xVel > 0){
            app.image(sprite1, x, y);
            direction = 0;

        }
        //going left
        else if (xVel < 0){
            app.image(sprite0, x, y);
            direction = 1;

        }
        //xVel = 0 stationary 
        // left
        if (direction == 0){
            app.image(sprite1, x, y);
            //if not fit in whole tile keep moving
            // and x+1 not in the brick or stone coords
            if (x%20 != 0){
                x += 1;
            }
        }
        else if(direction == 1){
            app.image(sprite0, x, y);
            if (x%20 != 0){
                x -= 1;
            }
        }


        // going down
        if (yVel > 0){
            app.image(sprite2, x, y);
            direction = 2;
        }
        // going up
        else if (yVel < 0){
            direction = 3;
            app.image(sprite3, x, y);
        }

        if (direction == 2){
            app.image(sprite2, x, y);
            //if not fit in whole tile keep moving
            if (y%20 != 0){
                y += 1;
            }

        }
        else if(direction == 3){
            app.image(sprite3, x, y);
            //if not fit in whole tile keep moving
            if (y%20 != 0){
                y -= 1;
            }
        }


        x += xVel;
        y += yVel;

    }

}
