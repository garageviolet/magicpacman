package gremlins;
import processing.core.PImage;


public class Stonewall {
    
    private PImage sprite;
    public static final int HEIGHT = 20;
    public static final int WIDTH = 20;

    public Stonewall(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Updates the stonewall every frame.
     */
    public void draw(App app) {
        
        for (MyCoord co: app.stone_cords_ls){
            app.image(sprite, co.getX(), co.getY());
        }
    }
}
