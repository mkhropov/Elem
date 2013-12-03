class TerrainRough extends Terrain {
    TerrainRough(int x, int y, int low, int high){ //low<0, high>0
        Random gen = new Random(x+y+low+high);
        profile = new short[x][y];
        int n = x*y/16 + gen.nextInt(x*y/9 - x*y/16);
        int[] xc = new int[n];
        int[] yc = new int[n];
        int[] d  = new int[n];
        for (int i=0; i<n; ++i){
            xc[i] = gen.newInt(x);
            yc[i] = gen.newInt(y);
            d[i]  = gen.newInt(high-low);
            profile[xc[i]][yc[i]] = low+d[i];
        }
        for (int i=0; i<(high-low)/2; ++i)
            smooth();
    }
}
