package gremlins;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.event.KeyEvent;
import java.lang.Math;
import java.io.*;
import java.util.*; 


public class App extends PApplet {

    /**
     * The width of the window.
     */
    public static final int WIDTH = 720;

    /**
     * The height of the window.
     */
    public static final int HEIGHT = 720;

    /**
     * Counter for record runtime
     */
    public int count = 0;

    /**
     * The number of frames per second.
     */
    public static final int FPS = 60;

    /**
     * The number of frames per second.
     */
    public static final Random randomGenerator = new Random();

    /**
     * Config path as string for loading game configuration 
     */
    public String configPath;

    /**
     * The left Bound for Player
     */
    public int leftBound = 40;

    /**
     * The right Bound for Player
     */
    public int rightBound = 40;

    /**
     * The bottom Bound for Player
     */
    public int bottomBound = 660;
    public int XReleased = 0;

    /**
     * Deafult lives for Player load from config file
     */
    protected int deafult_lives;

    /**
     * Live lives for player update every time when losing life
     */
    protected int current_life;

    /**
     * Index for loading which config in json array
     */
    public int whichmap = 0;
    
    public boolean win = false;
    private boolean lose_life = false;

    
    /**
     * Boolean value decides whether the game has started
     */
    private boolean game_start = true;

    /**
     * Counter for fireball
     */
    protected int count_fireball = 0;

    /**
     * Counter for gremlin
     */
    private int gremlin_count = 0;

    /**
     * Random integer generator between 1-3 for general use
     */
    protected int randomdi = App.randomGenerator.nextInt(4);

    /**
     * Boolean value decides wizard cool down 
     */
    private boolean cool_down = false;

    /**
     * Counter for wizard fireball cooldown 
     */
    private  int rise_num = 0;
    
    /**
     * ArrayList stores gremlin object 
     */
    private ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>(1);

    /**
     * ArrayList stores power up object 
     */
    private ArrayList<Powerup> powerups = new ArrayList<Powerup>(1);

    /**
     * ArrayList stores Fireball object 
     */
    protected ArrayList<Fireball> fireballs = new ArrayList<Fireball>(1);

    /**
     * ArrayList stores Powerup object 
     */
    protected ArrayList<Powerup> powers = new ArrayList<Powerup>(1);

    /**
     * ArrayList stores Slime object 
     */
    protected ArrayList<Slime> slimes = new ArrayList<Slime>(1);
    
    /**
     * ArrayList stores gremlin cool down value for all levels
     */
    private ArrayList<Double> enemy_cooldownls  = new ArrayList<Double>(1);

    /**
     * ArrayList stores wizard cool down value for all levels
     */
    private ArrayList<Double> wizard_cooldownls  = new ArrayList<Double>(1);

    /**
     * ArrayList stores gremlin initial facing direction
     */
    private ArrayList<Integer> initial_g_direction  = new ArrayList<Integer>(1);

    /**
     * ArrayList stores gremlin initial facing direction
     */
    private boolean collide_powerup = false;

    protected Player player;
    private Exit exit;
    private Stonewall stonewally;
    private Brickwall brickwally;
    private Fireball fireball;
    private Slime slime;
    private MyCoord cords;
    protected ArrayList<MyCoord> brick_cords_ls = new ArrayList<MyCoord>();
    protected ArrayList<MyCoord> stone_cords_ls = new ArrayList<MyCoord>();
    protected ArrayList<MyCoord> power_cords_ls = new ArrayList<MyCoord>();
    protected ArrayList<Object> levelsls= new ArrayList<Object>();//["level1.txt", "level2.txt"] String 

    private int powerup_counter = 0;
    private int powerup_start_counter;// 5 seconds
    private boolean power_start = false;



    public App() {
        this.configPath = "config.json";
        
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        //Load the json file
        JSONObject conf = loadJSONObject(new File(this.configPath));
        

        //ArrayList<Object> wizard_cooldownls  = new ArrayList<Object>();// [0.333, 2.0] double
        //ArrayList<Object> enemy_cooldownls  = new ArrayList<Object>();// [3.0, 1.0] double


        // two object in the file - String[] levels, String lives
        JSONArray levels = conf.getJSONArray("levels");
        deafult_lives = (Integer)conf.get("lives");




        for(int i = 0; i < levels.size(); i++) {
            //get each level infos
            JSONObject level = (JSONObject) levels.get(i);
            // levelsfile - ArrayList with all the map file name("level1.txt, level2.txt")
            String layout = (String) level.get("layout");
            double wizard_cooldown = (double) level.get("wizard_cooldown"); 
            double enemy_cooldown = (double) level.get("enemy_cooldown"); 


            wizard_cooldownls.add(wizard_cooldown);
            enemy_cooldownls.add(enemy_cooldown);
            levelsls.add(layout);

        }
        //levelsfile - arraylist of string with levels filename 
        // level1 - "level1.txt"(or other file name provide by the cofig file)
        String level = levelsls.get(whichmap).toString();



        // map file names - levelsls ["level1.txt", "level2.txt"] String 
        // wizard_cooldownls - [0.333, 2.0] double
        // enemy_cooldownls - [3.0, 1.0] double


        // "layout": "level1.txt",
        // "wizard_cooldown": 0.3333,
        // "enemy_cooldown": 3.0



        String[] lines = loadStrings(level);

        //first time launch the game set current lives to deafult lives
        if (game_start == true){
            current_life = deafult_lives;
            if (powerup_counter >= 300){
                for(Powerup p: powerups){
                    p.turnon();
                }
            }
            game_start = false;
        }


        ArrayList<MyCoord> gremlin_cords_ls = new ArrayList<MyCoord>();
        ArrayList<MyCoord> obs = new ArrayList<MyCoord>();
        MyCoord exit_cords = new MyCoord();
        //this.cords = new MyCoord();

        //get all cor of 'X'
        for (int i = 0 ; i < lines.length; i++) {

            //i - y coords, j - x coords
            //(i,j) - (x,y)
            for (int j = 0 ; j < lines[i].length(); j++){

                // all the stonewall 
                if (lines[i].charAt(j) == 'X'){
                    this.cords = new MyCoord();
                    cords.setX(j*20);
                    cords.setY(i*20);
                    stone_cords_ls.add(cords);
                }

                // all the brickwall
                else if (lines[i].charAt(j) == 'B'){
                    this.cords = new MyCoord();
                    // *20 match with tile 
                    cords.setX(j*20);
                    cords.setY(i*20);
                    // append coords to list
                    brick_cords_ls.add(cords);
                }
                else if (lines[i].charAt(j) == 'E'){
                    this.cords = new MyCoord();
                    // *20 match with tile 
                    exit_cords.setX(j*20);
                    exit_cords.setY(i*20);


                }

                else if (lines[i].charAt(j) == 'P'){
                    this.cords = new MyCoord();
                    // *20 match with tile 
                    cords.setX(j*20);
                    cords.setY(i*20);
                    power_cords_ls.add(cords);
                    //append coords to list
                    powerups.add(new Powerup(cords.getX(), cords.getY(), this.loadImage("src/main/resources/gremlins/power.png")));        
                }

            }
 
        }

        //coords of stonewall and brickwall
        obs.addAll(stone_cords_ls);
        obs.addAll(brick_cords_ls);



        //loop thru lines(level*.txt)
        for (int i = 0 ; i < lines.length; i++) {

            //i - y coords, j - x coords
            //(i,j) - (x,y)
            // loop thru each char for each line in lines 
            for (int j = 0 ; j < lines[i].length(); j++){
                // all gremlins
                if (lines[i].charAt(j) == 'G'){
                    //Gnum += 1;
                    this.cords = new MyCoord();
                    // *20 match with tile 
                    cords.setX(j*20);
                    cords.setY(i*20);
                    // append coords to list
                    gremlin_cords_ls.add(cords);
                    gremlin_count += 1;
                    gremlins.add(new Gremlin(cords.getX(), cords.getY(),this.loadImage("src/main/resources/gremlins/gremlin.png"), brick_cords_ls, stone_cords_ls));
                }

            }
        }

        /**
         * Creates a new player with coordinates (60, 20)
        */
        this.player = new Player(60, 20,this.loadImage("src/main/resources/gremlins/wizard0.png"), this.loadImage("src/main/resources/gremlins/wizard1.png"), this.loadImage("src/main/resources/gremlins/wizard2.png"), this.loadImage("src/main/resources/gremlins/wizard3.png"), current_life, brick_cords_ls, stone_cords_ls);
        
        /**
         * Creates a new Stonewall 
        */
        this.stonewally = new Stonewall(this.loadImage("src/main/resources/gremlins/stonewall.png"));

        /**
         * Creates a new Brickwall 
        */
        this.brickwally = new Brickwall(this.loadImage("src/main/resources/gremlins/brickwall.png"));

        /**
         * Creates a new Powerup 
        */
        // this.powerupy = new Powerup(this.loadImage("src/main/resources/gremlins/power.png"));


        /**
         * Creates a new Exit 
        */
        this.exit = new Exit(exit_cords.getX(), exit_cords.getY(),this.loadImage("src/main/resources/gremlins/doorss.png"));




        // random generate dierction (0,1,2,3) add to the arraylist 
        while(initial_g_direction.size() != gremlin_count){
            initial_g_direction.add(1 + (int) (Math.random() * (3 - 0) + 0));
        }

        //set initial facing direction for each gremlin
        for(Gremlin g: gremlins){
            for(int num: initial_g_direction){
                g.setDirection(num);
            }
        }

    }

    /**
     * Reset game state
    */
    public void reset(){
        power_start = false;
        powerup_start_counter = 0;
        powerups.clear();
        powerup_counter = 0;
        rise_num = 0;
        count = 0;
        slimes.clear();       
        levelsls.clear();
        gremlins.clear();
        brick_cords_ls.clear();
        stone_cords_ls.clear();
        power_cords_ls.clear();
        fireballs.clear();
    }


    
    /** 
     * Random coordiate genrator satisfy gremlin respawn conditon in specs
     */
    public MyCoord co_generate(){
        MyCoord new_g_Coord = new MyCoord();
        int new_x = 0;
        int new_y = 0;

        boolean flag1 = false;
        boolean flag2 = false;

        while (true){
            flag1 = false;
            flag2 = false;

            new_x = randomGenerator.nextInt(680 - 20 + 1) + 20;
            new_y = randomGenerator.nextInt(620 - 20 + 1) + 20;

            new_g_Coord.setX(new_x);
            new_g_Coord.setY(new_y);
            
            for(MyCoord b: brick_cords_ls){

                //new coords overlaop with one of the brick coords
                if (new_g_Coord == b){
                    flag1 = true;
                }

            }

            
            for(MyCoord s: stone_cords_ls){
                //new coords overlaop with one of the stone coords
                if (new_g_Coord == s){
                    flag2 = true;
                }
            }
            if (!flag1 && !flag2 && new_x%20 == 0 && new_y%20 == 03 && dist(new_x,new_y, player.getX(),player.getY()) > 200){
                break;
            }
        }

        return new_g_Coord;   
    }

    
    /** 
     * @param xCoord
     * @param yCoord
     * @param x2Coord
     * @param y2Coord
     * @return boolean
     * Method take two coordinate and decides whether they collides/overlap 
     */
    public boolean collides(int xCoord, int yCoord, int x2Coord, int y2Coord) {

        //20 - sprite size
        //sprite overlap count as collide
        if (xCoord > x2Coord-20 && xCoord < x2Coord+20 && yCoord > y2Coord-20 && yCoord < y2Coord+20){
            return true;
        }

        return false;
    }




    /**
     * Receive key pressed signal from the keyboard.
    */
    @Override
    public void keyPressed(KeyEvent e) {
        //if player lose all lives


        if (this.player.lives == 0){
            //default depends on config file 
            //int lives - default lives from config file
            //this.player.lives - real time lives from player class
            if (key >= 0){
                //for any key pressed initialised lives
                //this.player.lives 
                power_start = false;
                whichmap = 0;
                reset();
                setup();
                current_life = deafult_lives;
                fireball.finalReset();

                
            }
        } 
        
        // if win pressed any key to reset the game
        if(win){
            if (key >= 0){
                // for any key pressed initialised lives
                power_start = false;
                win = false;
                current_life = deafult_lives;
                reset();
                setup();
                fireball.finalReset();
            }
        }

        int key = e.getKeyCode();
        if (key == 32){
            //player shot fire ball 
            //every key pressed add fireball obejct to the list 
            //fireball counting for later remove from list as index

            //if fireball not cooling down add new fireball object to the list
            // when shooting fireball start cooldown 
            if (!cool_down){
                fireballs.add(this.fireball = new Fireball(player.getX(), player.getY(), this.loadImage("src/main/resources/gremlins/fireball.png"),this.loadImage("src/main/resources/gremlins/brickwall_destroyed0.png"),this.loadImage("src/main/resources/gremlins/brickwall_destroyed1.png"),this.loadImage("src/main/resources/gremlins/brickwall_destroyed2.png"),this.loadImage("src/main/resources/gremlins/brickwall_destroyed3.png"),brick_cords_ls, stone_cords_ls));
                cool_down = true;

            }
            
            //sync new fireball object with player direction
            if (player.direction == 0){
                fireball.right();
            }
            else if (player.direction == 1){
                fireball.left();                
            }
            else if (player.direction == 2){
                fireball.down();                
            }
            else if (player.direction == 3){
                fireball.up();                
            }
        }

        //keypressed for wizard movement each direction
        if (key == 37) { //left arrow
            player.left();
            this.player.direction = 1;
        } 
        else if (key == 39) { //right arrow
            player.right();
            this.player.direction = 0;
        }
        else if (key == 38) { //up arrow
            player.up();
            this.player.direction = 3;
        }
        else if (key == 40) { //down arrow
            player.down();
            this.player.direction = 2;
        }
        

    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    @Override
    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();
        if (key == 37 || key == 39) {
            //get coords when realease the key

            player.stop();
        }
        // when release key 
        else if (key == 38 || key == 40) {
            player.stop();
        }
    }


    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {

        background(191, 151, 108);
        fill(255,255,255);



        Double d = enemy_cooldownls.get(whichmap);
        Double dd = wizard_cooldownls.get(whichmap);

        int current_g_cooldown = (Integer) d.intValue();
        Double current_w_cooldown = (Double) dd;

        count += 1;
        rise_num += 1;
        powerup_counter += 1;

        if (this.player.lives == 0){
            textSize(50);
            text("GAME OVER!", WIDTH*0.28f, HEIGHT*0.5f);
        }
        else if (win){
            textSize(50);
            text("YOU WIN!", WIDTH*0.33f, HEIGHT*0.5f);
        }
        else{

            //add new slime object to the list every x seconds spicified in json file
            //get slime frequency for current level

            //draw slimes on screen 
            for(Slime s: slimes){
                s.draw(this);
            }

            //draw gremlins on screen 
            for(Gremlin g: gremlins){
                g.draw(this);
            }

            //draw fireballs on screen 
            for(Fireball f: fireballs){
                f.draw(this);
            }

            //draw powerups on screen 5 seconds start of game(300/60fps = 5 seconds)

            for(Powerup p: powerups){
                p.draw(this);
            }
            

            //every x cooldown seconds gremlins shoot slimes
            if (count == current_g_cooldown*60){
                //initialize counter
                count = 0;
                for(Gremlin g: gremlins){

                    //add slime object every x seconds
                    slimes.add(this.slime = new Slime(g.getX(), g.getY(), this.loadImage("src/main/resources/gremlins/slime.png"),brick_cords_ls, stone_cords_ls));
                    
                    //sync slime direction with gremlin
                    if (g.direction == 0){
                        slime.right();
                    }
                    else if (g.direction == 1){
                        slime.left();                
                    }
                    else if (g.direction == 2){
                        slime.down();                
                    }
                    else if (g.direction == 3){
                        slime.up();                
                    }
                }


            }



            for (Gremlin g : gremlins) {

                //every x seconds add slime object to the list 
                //if player and gremin colldies player loses 1 life
                if (collides(player.getX(), player.getY(), g.getX(), g.getY())){
                    lose_life = true;
                }

                //if fireball and gremin colldies gremlin reset
                for (Fireball f : fireballs) {
                    //if fireball still exist and encounter a gremlin
                    if (f.getCondition() == true){

                        if(collides(f.getX(), f.getY(), g.getX(), g.getY())){
                            MyCoord a = co_generate();
                            g.setX(a.getX());
                            g.setY(a.getY());
                            f.turnOff();
                        }
                    }
                    for (Slime s : slimes) {
                        if(s.getCondition() == true){
                            if(collides(f.getX(), f.getY(), s.getX(), s.getY())){
                                f.turnOff();
                                s.turnOff();
                            }
                        }
                    }
                }
            }

            //if player and slime colldies player loses 1 life
            for (Slime s : slimes) {
                if(collides(player.getX(), player.getY(), s.getX(), s.getY())){
                    if (s.getCondition() == true){
                        lose_life = true;
                    }
                }
            }

            //lose life conditon and reset game to inital state
            if (lose_life){

                reset();
                setup();
                lose_life = false;
                this.player.lives -= 1;
                current_life -=1;
            }

            //if player and power up collides speed up player
            //turn the power up off and start counting for respawn 



            for (Powerup p : powerups) {
                if(collides(player.getX(), player.getY(), p.getX(), p.getY()) && p.getCondition()){
                    p.turnOff();
                    power_start = true;
                    powerup_start_counter = 600;// set and reset reamining time for power up
                    player.speedUp();
                    // power_index = power_cords_ls.indexOf(p);
                    
                }

            }

            if (power_start){
                powerup_start_counter -= 1;
                if (powerup_start_counter == 0){
                    player.normalSpeed();
                    power_start = false;
                }
            }


            //if player and power up collides undraw power up from screen 
            if(power_cords_ls.size() > 0 && collide_powerup){
                //power_cords_ls.remove(power_index);
                collide_powerup = false;
            }

            //player reach exit on map
            if (collides(player.getX(), player.getY(), exit.getX(), exit.getY())){
                //change to next map
                whichmap += 1;
                //reset count
                count = 0;
                //winning condition - reached exit of the final map
                if (whichmap == levelsls.size()){
                    win = true;
                    //rest map counting
                    whichmap = 0;
                }
                reset();
                setup();

            }



            //draw each object on screen
            this.player.draw(this);
            this.stonewally.draw(this);
            this.brickwally.draw(this);
            this.exit.draw(this);



            
            
            fill(255,255,255);
            textSize(20);

            //display UI on screen 
            int display_map_num = whichmap+1;
            
            //remaining lives text
            text("Lives: ", WIDTH*0.01f, HEIGHT*0.959f);
            
            //Levels
            text("Level: " + display_map_num + "/"+ levelsls.size(), WIDTH*0.25f, HEIGHT*0.959f);
            
            //power up remaining draw on screen 
            if (power_start == true){
                text("SPEED UP remaining " + powerup_start_counter/60 + " s", WIDTH*0.6f, HEIGHT*0.95f);
            }

            //reset counter for wizard cooldown 
            double actual_cool_down = Math.ceil(current_w_cooldown*60);



            Double actual_cooldown_length = new Double(100/actual_cool_down);
            float new_yes = actual_cooldown_length.floatValue();
            //20, 120
            // when rise num == 20 cooldown end 

            if (rise_num == actual_cool_down){
                rise_num = 0;
                cool_down = false;
            }
            
            //draw wizard cooldown bar on screen 
            // Integer cool_down_int = (int) (current_w_cooldown*15);


            // cool_down_int
            // current_w_cooldown - 0.33333, 2
            //rise_num * x == 100


            if(cool_down){
                if (rise_num <= actual_cool_down){

                    rect(600, 690, 100, 5);
                    fill(0);
                    rect(600, 690, rise_num*new_yes, 5);
                }
            }
        }

        
    }

    /** 
     * @param args
     */
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
