package gremlins;
import processing.core.PImage;

public class Brickwall{

    private PImage sprite;
    public static final int HEIGHT = 20;
    public static final int WIDTH = 20;


    public Brickwall(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Updates the brickwall every frame.
     */
    public void draw(App app) {
        
        for (MyCoord co: app.brick_cords_ls){
            app.image(sprite, co.getX(), co.getY());
        }
    }    
}
