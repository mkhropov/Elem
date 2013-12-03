class Terrain {
    public int x;
    public int y;

    public int low; //low point
    public int high; //high point

    public short[][] profile;

    Terrain(int x, int y, int low, int high){
        this.x = x;
        this.y = y;
        this.low = low;
        this.high = high;
        this.profile = new short[x][y]; //HOPE it's set to zero
    }

    void smooth(int n, int[] xc, int[] yc){
//        for (int i=1; i<x-1; ++i)
//            for (int j=1; j<y-1; ++j){
//                if
    }
}
