package gremlins;

class MyCoord{
    private int X;
    private int Y;
    protected boolean exist = true;

    public MyCoord() {
        this(0,0);
    }        
    public MyCoord(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }
    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
    public boolean get_existence(){
        return exist;
    }
    public void setX(int X) {
        this.X = X;
    }
    public void setY(int Y) {
        this.Y = Y;
    }
    public void destroyed(){
        exist = false;
    }
    
}
