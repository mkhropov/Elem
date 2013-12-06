import java.util.Random;
//import java.util.math;

public abstract class Biome {

    class Stratum {
        public int x;
        public int y;
        public int[][] width;
        Stratum(int x, int y){
            this.x = x;
            this.y = y;
            width = new int[x][y];
        }
    }

    public String name;
    public Random gen;

    Biome(){
        name = "abstract biome";
        gen = new Random();
    }

    public Stratum getStratum(int size, int width){
        Stratum s = new Stratum(size, size);
        double sc = size*size/4.d;
        double a = gen.nextInt(180)/(360*2*3.14d); //angle
        double ca = Math.cos(a);
        double sa = Math.sin(a);
        double e = (gen.nextInt(100)+100)/200.d; //excentricity
        for (int i=-size/2; i<size/2; ++i)
            for (int j=-size/2; j<size/2; ++j)
                if ((sa*i+ca*j)*(sa*i+ca*j) + (sa*j+ca*i)*(sa*j+ca*i)*e < sc)
                    s.width[i][j] = width; //rotated ellipse
        return s;
    }

    public void dropStratum(Stratum s, Material m, Chunk c, int x, int y){
		int i0 = (x<0)?(0):(x);
        int i1 = (x+s.x<c.width)?(x+s.x):(c.width);
		int j0 = (y<0)?(0):(y);
        int j1 = (y+s.y<c.width)?(y+s.y):(c.width);
        for (int i=i0; i<i1; ++i)
            for (int j=j0; j<j1; ++j){
                int k = 0;
                while (c.blockArray[i][j][k].m != null)
                    ++k;
                int w = k+s.width[i-x][j-y];
                for (; k<w; c.blockArray[x][y][k++].m = m){};
            }
    }

    public abstract void fillChunk(Chunk c);

    Temperature getTemperature(){
        return new Temperature(0);
    }
}
