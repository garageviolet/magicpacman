package gremlins;

import processing.core.PImage;



public class Powerup {
    protected boolean onoff = false; 
    private PImage sprite;
    private int counter;
    
    protected int x;
    protected int y;


    public Powerup (int x, int y, PImage sprite){

        this.x = x;
        this.y = y;
        this.sprite = sprite;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public boolean getCondition(){
        return onoff;
    }
    public void turnon(){
        onoff = true;
    }

    public void turnOff(){
        onoff = false;
    }
    public void draw(App app) {

        if (onoff){
            app.image(sprite, x, y);
        }


        if (!onoff){
            counter += 1;
        }

        if (counter == 300){
            turnon();
            counter = 0;// reset the counter when reapwn 
        }


    }
    

}
