import java.util.Random();

public class Biome {

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

    Stratum getStratum(int size, int width){
        s = new Stratum(size, size);
        float sc = size*size/4.f;
        float a = gen.nextInt(360)/(360*2*3.14f); //angle
        float ca = math.cos(a);
        float sa = math.sin(a);
        float e = (gen.nextInt(100)+100)/200.f; //excentricity
        for (int i=-size/2; i<size/2; ++i){
            for (int j=-size/2; j<size/2; ++j)
                if ((sa*i+ca*j)*(sa*i+ca*j) + (sa*j+ca*i)*(sa*j+ca*i)*e < sc)
                    s.width[i][j] = width; //rotated ellipse
        return s;
    }

    void dropStratum(Stratum s, Material m, Chunk c, int x, int y){
        int a = (x+s.x<c.width)?(x+s.x):(c.width);
        int b = (y+s.y<c.width)?(y+s.y):(c.width);
        for (int i=x; i<a; ++i)
            for (int j=y; j<b; ++j){
                int k = 0;
                while (c.blockArray[i][j][k].m.code != 0)
                    ++k;
                int w = k+s.width[i-x][j-y];
                for (; k<w; c.blockArray[x][y][k++].m = new m){}; //WUT FUCK
            }
    }

    public abstract void fillChunk(Chunk c){
    }

    Temperature getTemperature(){
        return new Temperature(0);
    }
}
