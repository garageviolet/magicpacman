package gremlins;
import processing.core.PImage;

public class Exit{
    private int x;
    private int y;
    private PImage sprite;

    public Exit(int x, int y, PImage sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;

    }

    
    public void draw(App app) {
        app.image(sprite, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
