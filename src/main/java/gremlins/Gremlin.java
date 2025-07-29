package gremlins;
import processing.core.PImage;


import java.util.Random;


import java.util.ArrayList;

public class Gremlin{

    private int x;
    private int y;
    
    // four direction - generate 0,1,2,3 initial direction 


    private int xVel = 0;
    private int yVel = 0;
    public int width = 20;


    private PImage sprite;
    protected ArrayList<MyCoord> brick_cords;
    protected ArrayList<MyCoord> stone_cords;
    // cordsofobs.addAll(brick_cords);
    // cordsofobs.addAll(stone_cords);
    protected int direction = 0;
    protected int randomdi = App.randomGenerator.nextInt(4);// 1-3 random num
    private Random random = new Random();



    public Gremlin (int x, int y, PImage sprite, ArrayList<MyCoord> brick_cords, ArrayList<MyCoord> stone_cords){

        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.brick_cords = brick_cords;
        this.stone_cords = stone_cords;



    }
    public void setDirection(int num){
        direction(num);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void stop() {
        this.xVel = 0;
        this.yVel = 0;
    }
    public void direction(int di){

        //up
        if (di == 0){
            xVel = 0;
            yVel = -1;
            this.direction = 0;
        }
        //down 
        else if (di == 1){
            xVel = 0;
            yVel = 1;
            this.direction = 1;
        }
        //left
        else if (di == 2){
            xVel = -1;
            yVel = 0;
            this.direction = 2;
        }
        //right
        else if (di == 3){
            xVel = 1;
            yVel = 0;
            this.direction = 3;
        }
    }


    public void draw(App app){

        // up
        if (xVel == 0 || yVel == -1){
            direction = 0;
            if (collides(x+xVel,y+yVel, brick_cords)){
                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(2);
                }
                else{
                    direction(3);
                }
                //System.out.println(direction);
            }

            if (collides(x+xVel,y+yVel, stone_cords)){
                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(2);
                }
                else{
                    direction(3);
                }
                //System.out.println(direction);
            }

            
            
        }

        //down
        if (xVel == 0 || yVel == 1){
            //System.out.println(x+xVel);

            if (collides(x+xVel,y+yVel, brick_cords)){

                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(2);
                }
                else{
                    direction(3);
                }
            }

            if (collides(x+xVel,y+yVel, stone_cords)){

                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(2);
                }
                else{
                    direction(3);
                }
            }
        }

    
        //left 
        else if (xVel == -1 || yVel == 0){

            if (collides(x+xVel,y+yVel, brick_cords)){
                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(0);
                }
                else{
                    direction(1);
                }
            }

            if (collides(x+xVel,y+yVel, stone_cords)){
                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(0);
                }
                else{
                    direction(1);
                }
            }
        }
        //right
        else if (xVel == 1 || yVel == 0){

            if (collides(x+xVel,y+yVel, brick_cords)){
                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(0);
                }
                else{
                    direction(1);
                }
            }

            if (collides(x+xVel,y+yVel, stone_cords)){
                //left 2, right 3
                boolean a = random.nextBoolean();
                //System.out.println(a);
                if (a == true){
                    direction(0);
                }
                else{
                    direction(1);
                }
            }
        }
        // Handles graphic 
        // for (MyCoord co: cordsself){
        //     app.image(sprite, co.getX(), co.getY());
        // }
        app.image(sprite, x, y);


        if (xVel > 0){

            direction = 0;

        }
        //going left
        else if (xVel < 0){

            direction = 1;

        }

        if (yVel > 0){

            direction = 2;
        }
        // going up
        else if (yVel < 0){
            direction = 3;
        }

        this.y += yVel;
        this.x += xVel;





        


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



    public boolean isin(MyCoord ele, ArrayList<MyCoord> ls,int num){
        // num 0 for finding x, 1 for finding y
        boolean flag = false;
        for (MyCoord co: ls){
            //System.out.println(co.getX());
            if (num == 0){
                if (ele.getX() == co.getX()){
                    flag = true;
                }
            }
            else{
                if (ele.getY() == co.getY()){
                    flag = true;
                }
            }

        }
        return flag;
    }
    
}
