package gremlins;
import processing.core.PImage;
import java.util.ArrayList;




public class Fireball{
    protected int x;
    protected int y;
    private PImage sprite_fireball; 




    private int xVel = 0;
    private int yVel = 0;
    protected ArrayList<MyCoord> brick_cords;
    protected ArrayList<MyCoord> stone_cords;
    protected ArrayList<MyCoord> hit_coords;
    protected boolean onoff = true; 
    protected boolean break_onoff = false;

    private PImage sprite0;
    private PImage sprite1;
    private PImage sprite2;
    private PImage sprite3;
    private int count = 0;
    private int hit_x;
    private int hit_y;



    private int speed = 4;
    private boolean hit = false;

    public Fireball(int x, int y, PImage sprite_fireball, PImage sprite0, PImage sprite1, PImage sprite2, PImage sprite3, ArrayList<MyCoord> brick_cords, ArrayList<MyCoord> stone_cords){
        this.x = x;
        this.y = y;
        this.sprite_fireball = sprite_fireball;
        this.sprite0 = sprite0;
        this.sprite1 = sprite1;
        this.sprite2 = sprite2;
        this.sprite3 = sprite3;
        this.brick_cords = brick_cords;
        this.stone_cords = stone_cords;

       
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

    public void stop() {
        this.xVel = 0;
        this.yVel = 0;
    }
    
    public void turnOff(){
        onoff = false;
    }

    public boolean getCondition(){
        return onoff;
    }

    public void reset(){

        this.xVel = 0;
        this.yVel = 0;
    }

    public ArrayList<MyCoord> getListBrick(){
        return brick_cords;
    }

    public ArrayList<MyCoord> getListStone(){
        return stone_cords;
    }

    public boolean collides(int xCoord, int yCoord, ArrayList<MyCoord> po) {
        for (MyCoord co: po){

            if (xCoord > co.getX()-20 && xCoord < co.getX()+20 && yCoord > co.getY()-20 && yCoord < co.getY()+20){
                return true;
            }
        }
        return false;
    }

    // return the index of the coords which has crossed
    public int which_collides(int xCoord, int yCoord, ArrayList<MyCoord> po) {
        for (MyCoord co: po){

            if (xCoord > co.getX()-20 && xCoord < co.getX()+20 && yCoord > co.getY()-20 && yCoord < co.getY()+20){
                return po.indexOf(co);
            }
        }
        return 0;

    }
    
    public void hit(){
        hit = true;
    }
    public boolean getHit(){
        return hit;
    }

    public void finalReset(){
        break_onoff = false;
        onoff = true;
    }



    public void draw(App app) {
        //update every second
        if (break_onoff){
            count += 1;

        }



        //the index of coords in brick_cords in which get hit by fire ball
        int index_hit = which_collides(x+xVel,y+yVel, brick_cords);

        //app.image(sprite_fireball, x, y);
        //get the x and y coords of the tile hit then draw        
        //if the fireball switch is on - draw the fire ball/if its off then nothing is drawn
        if (onoff){
            app.image(sprite_fireball, x, y);
        }

        //if hit stonewall - undraw the fire ball by set the fireball object off
        if (collides(x+xVel,y+yVel, stone_cords)){
            reset();
            hit();
            onoff = false;
        }

        // if hit brickwall - undraw the fireball(by set the fireball object off) and undraw the brick(by remove the brick coords from list)
        if (collides(x+xVel,y+yVel,brick_cords)){
            //on off is false only if the birck is not detroyed
            reset();
            hit_x = brick_cords.get(index_hit).getX();
            hit_y = brick_cords.get(index_hit).getY();
            break_onoff = true;
            brick_cords.remove(index_hit);
            onoff = false;
            hit();
        }

        // animation for brick destroyed
        if (break_onoff){
            // display first image first 4 frames 
            if (count <=4){
                app.image(sprite0, hit_x, hit_y);
            }
            // display second image second 4 frames 
            else if(count >4 && count <=8){
                app.image(sprite1, hit_x, hit_y);
            }
            // display third image third 4 frames 
            else if(count >8 && count <=12){
                app.image(sprite2, hit_x, hit_y);
            }
            // display fourth image fourth 4 frames 
            else if(count >12 && count <16){
                app.image(sprite3,hit_x, hit_y);
            }
            //reset counter set boolean value to false no image drawn
            else if(count == 16){
                count = 0;
                break_onoff = false;
    
            }
            
        }
        
        //update x and y coordinate 
        x += xVel;
        y += yVel;
    }
    
}
