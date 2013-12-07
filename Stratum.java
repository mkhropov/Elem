import java.util.Random;

public class Stratum {
    public int x;
    public int y;
    public int[][] width;

    Stratum(int x, int y, int width){
        this.x = x;
        this.y = y;
        this.width = new int[x][y];
        
        Random gen = new Random(x);

        double scx = x*x/4.d;
        double scy = y*y/4.d;
        double a = gen.nextInt(180)/(360*2*3.14d); //angle
        double ca = Math.cos(a);
        double sa = Math.sin(a);
        // ellipse will be cut when rotated =|
        // and scalings are off
        // whatever
        for (int i=-x/2; i<x/2; ++i)
            for (int j=-y/2; j<y/2; ++j)
                if (((sa*i+ca*j)*(sa*i+ca*j)/scx + 
                     (sa*j+ca*i)*(sa*j+ca*i)/scy ) < 1.d)
                    this.width[i+x/2][j+y/2] = width;
    }

    public void drop(Material m, Chunk c, int x, int y){
		int i0 = (x<0)?(0):(x);
        int i1 = (x+this.x<c.width)?(x+this.x):(c.width);
		int j0 = (y<0)?(0):(y);
        int j1 = (y+this.y<c.width)?(y+this.y):(c.width);
        for (int i=i0; i<i1; ++i)
            for (int j=j0; j<j1; ++j){
                int k = 0;
                while (c.blockArray[i][j][k].m != null)
                    ++k;
                int w = k+this.width[i-x][j-y];
                for (; k<w; c.blockArray[x][y][k++].m = m){};
            }
    }
}
