package gremlins;
import processing.core.PImage;
import java.util.ArrayList;

public class Slime {
    protected int x;
    protected int y;
    private int xVel = 0;
    private int yVel = 0;
    private PImage sprite_slime; 

    private int speed = 4;
    protected boolean onoff = true; 
    protected int direction;

    protected ArrayList<MyCoord> brick_cords;
    protected ArrayList<MyCoord> stone_cords;

    public Slime(int x, int y, PImage sprite_slime,ArrayList<MyCoord> brick_cords, ArrayList<MyCoord> stone_cords){
        this.x = x;
        this.y = y;
        this.sprite_slime = sprite_slime;
        this.brick_cords = brick_cords;
        this.stone_cords = stone_cords;

       
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void left() {
        this.xVel = -speed;
    }

    public void right() {
        this.xVel = speed;
    }

    public void up() {
        this.yVel = -speed;
    }

    public void down() {
        this.yVel = speed; 
    }

    public void reset(){
        this.xVel = 0;
        this.yVel = 0;
    }
    public boolean getCondition(){
        return onoff;
    }
    public void turnOff(){
        onoff = false;
    }



    public boolean collides(int xCoord, int yCoord, ArrayList<MyCoord> po) {
        for (MyCoord co: po){

            if (xCoord > co.getX()-20 && xCoord < co.getX()+20 && yCoord > co.getY()-20 && yCoord < co.getY()+20){
                //System.out.println(co.getX());
                return true;
            }
        }
        return false;
    }

    public void draw(App app) {
        //get the x and y coords of the tile hit then draw
        //if the fireball switch is on - draw the fire ball/if its off then nothing is drawn
        if (onoff){
            app.image(sprite_slime, x, y);
        }

        //if hit stonewall - undraw the fire ball by set the fireball object off
        if (collides(x+xVel,y+yVel, stone_cords)){
            reset();
            onoff = false;
        }
        
        // if hit brickwall - undraw the fireball(by set the fireball object off) and undraw the brick(by remove the brick coords from list)
        if (collides(x+xVel,y+yVel,brick_cords)){
            //on off is false only if the birck is not detroyed
            reset();
            onoff = false;
        
        }
        x += xVel;
        y += yVel;
    }
    
}
